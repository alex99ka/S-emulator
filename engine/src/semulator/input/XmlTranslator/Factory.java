package semulator.input.XmlTranslator;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import semulator.impl.api.basic.OpDecrease;
import semulator.impl.api.basic.OpIncrease;
import semulator.impl.api.basic.OpJumpNotZero;
import semulator.impl.api.basic.OpNeutral;
import semulator.impl.api.skeleton.Op;
import semulator.impl.api.synthetic.*;
import semulator.input.gen.*;
import semulator.label.*;
import semulator.program.*;
import semulator.variable.*;
import java.io.File;
import java.util.*;

public class Factory
{
    XProgram xProgram;
    Set<Label> definedLabels;
    List<XOp> sInstructions;
    SProgram program;


    public SProgram loadProgramFromXml(File xmlFile) throws Exception
    {
        // 1. Basic file validation (exists and .xml extension)
        if (xmlFile == null || !xmlFile.exists() || !xmlFile.isFile()) {
            throw new IllegalArgumentException("XML file not found: " + xmlFile);
        }
        if (!xmlFile.getName().toLowerCase().endsWith(".xml")) {
            throw new IllegalArgumentException("Invalid file type: " + xmlFile.getName()
                    + " (expected .xml)");
        }

        // 2. Unmarshal XML into SProgram object using JAXB
        try {
            JAXBContext context = JAXBContext.newInstance(XProgram.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            xProgram =  (XProgram) unmarshaller.unmarshal(xmlFile);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to parse XML: " + e.getMessage(), e);
        }

        // 3. Program semantic validation: check that all referenced labels exist
        sInstructions = xProgram.getInstructions();
        if (sInstructions == null || sInstructions.isEmpty()) {
            throw new IllegalArgumentException("Invalid program: no instructions defined.");
        }
        // Collect all labels defined in the program
        definedLabels = new HashSet<>();
        for (XOp inst : sInstructions) {
            if (inst.getLabel() != null) {
                if (inst.getLabel().equals( FixedLabel.EXIT.getLabelRepresentation()))
                    definedLabels.add(FixedLabel.EXIT);
                else
                    definedLabels.add(new LabelImpl( inst.getLabel()));
            }
        }
        // Check all instruction arguments for label references that are not defined
        for (XOp inst : sInstructions) {
            ArrayList<XOpArguments> args = (ArrayList<XOpArguments>) inst.getArguments();
            if (args == null) continue; // if there are no arguments, skip
            for (XOpArguments arg : args) {
                String argName = arg.getName();
                String argValue = arg.getValue();
                // If this argument is a jump target label (argument name ends with "Label"):
                if (argName.toLowerCase().endsWith("label")) {
                    if ("EXIT".equalsIgnoreCase(argValue)) {
                        // "EXIT" is considered a special target (program termination), skip existence check
                        continue;
                    }
                    if (!definedLabels.contains(new LabelImpl(argValue)))
                    {
                        // Found a jump to a label that doesn't exist in the program
                        throw new IllegalArgumentException("Invalid program: jump to undefined label \""
                                + argValue + "\" in instruction \""
                                + inst.getName() + "\".");
                    }
                }
            }
        }

        // 4. Build internal Program object
        program = new SprogramImpl(xProgram.getName());


        // Prepare a set to track all variable names used (for initialization)
        Set<Variable> allVars = new HashSet<>();
        List<Variable> inputVars = new ArrayList<>();
        Label lbl;
        String labelRegex = "L\\d+"; // regex pattern for valid labels like L1, L2, etc.

        // Convert each SInstruction to an Op object and add to program
        for (XOp inst : sInstructions) {
            String cmdType = inst.getType();    // "basic" or "synthetic"
            String cmdName = inst.getName();    // e.g., INCREASE, ZERO_VARIABLE, etc.
            String varName = inst.getVariable();  // e.g., "x1", "y", "z2"
            String labelName = inst.getLabel();   // may be null
            int varIndex;

            if (labelName == null || labelName.equals( FixedLabel.EMPTY.getLabelRepresentation() )|| labelName.isEmpty())
                lbl = FixedLabel.EMPTY;
           else  if (labelName.equals( FixedLabel.EXIT.getLabelRepresentation()))
            {
             lbl = FixedLabel.EXIT;
            }
            else
                lbl = new LabelImpl(labelName);

            // Add the main variable to the set of variables
            if (varName == null || varName.isEmpty())
                throw new IllegalArgumentException("Instruction missing variable: " + cmdName);
            if(!varName.equals("y"))
                varIndex = Integer.parseInt(varName.substring(1)); // extract index after first char
            else
                varIndex = 0; // for result variable "y", index is 0
            VariableType vType = varName.equals("y") ? VariableType.RESULT :
                    (varName.startsWith("x") ? VariableType.INPUT : VariableType.WORK);
            Variable curVar = new VariableImpl(vType, varIndex);


            allVars.add(curVar);  // track this variable for initialization
            if(// if it's an input variable, track in inputVars list too
                    vType == VariableType.INPUT && !inputVars.contains(curVar)) {
                inputVars.add(curVar);
            }

            Op op = null;  // will point to a new instruction object
            // Determine which specific Op subclass to instantiate based on the command name
            switch (cmdName) {
                case "INCREASE":
                    op = new OpIncrease(curVar, lbl);
                    break;
                case "DECREASE":
                    op = new OpDecrease(curVar, lbl);
                    break;
                case "NEUTRAL":
                    op = new OpNeutral(curVar, lbl);
                    break;
                case "JUMP_NOT_ZERO": {
                    // JumpNotZero needs the target label to jump to if variable != 0
                    String targetLabelName = getArgumentValue(inst, "JNZLabel");
                    Label targetLabel;
                    if (targetLabelName.equals( FixedLabel.EXIT.getLabelRepresentation())) {
                        targetLabel = FixedLabel.EXIT;
                    } else if (targetLabelName.equals(FixedLabel.EMPTY.getLabelRepresentation())) {
                       targetLabel = FixedLabel.EMPTY;
                    }
                    else
                        targetLabel = new LabelImpl(Integer.parseInt(targetLabelName.substring(1)));

                    op = new OpJumpNotZero(curVar,targetLabel,lbl);
                    break;
                }
                case "JUMP_ZERO": {
                    String targetLabelName = getArgumentValue(inst, "JZLabel");
                    Label targetLabel;
                    if (targetLabelName.equals( FixedLabel.EXIT.getLabelRepresentation())) {
                        targetLabel = FixedLabel.EXIT;
                    } else if (targetLabelName.equals(FixedLabel.EMPTY.getLabelRepresentation())) {
                        targetLabel = FixedLabel.EMPTY;
                    }
                    else
                        targetLabel = new LabelImpl(Integer.parseInt(targetLabelName.substring(1)));
                    op = new OpJumpZero(curVar, lbl, targetLabel);
                    break;
                }
                case "GOTO_LABEL": {
                    String targetLabelName = getArgumentValue(inst, "gotoLabel");
                    Label targetLabel;
                    if (targetLabelName.equals( FixedLabel.EXIT.getLabelRepresentation())) {
                        targetLabel = FixedLabel.EXIT;
                    } else if (targetLabelName.equals(FixedLabel.EMPTY.getLabelRepresentation())) {
                        targetLabel = FixedLabel.EMPTY;
                    }
                    else
                        targetLabel = new LabelImpl(Integer.parseInt(targetLabelName.substring(1)));

                    op = new OpGoToLabel(curVar, lbl, targetLabel);
                    break;
                }
                case "ASSIGNMENT": {
                    // Assignment: copies one variable's value to another
                    String srcVarName = getArgumentValue(inst, "assignedVariable");
                    Variable srcVar = new VariableImpl(srcVarName.equals("y") ? VariableType.RESULT :
                            (srcVarName.startsWith("x") ? VariableType.INPUT : VariableType.WORK), Integer.parseInt(srcVarName.substring(1)))
                    ;
                    allVars.add(srcVar);  // source variable also involved
                    if(// if it's an input variable, track in inputVars list too
                            srcVar.getType() == VariableType.INPUT && !inputVars.contains(curVar)) {
                        inputVars.add(srcVar);
                    }
                    op = new OpAssignment(curVar, lbl, srcVar);
                    break;
                }
                case "CONSTANT_ASSIGNMENT": {
                    String constValStr = getArgumentValue(inst, "constantValue");
                    Long constVal = Long.parseLong(constValStr);
                    op = new OpConstantAssigment(curVar, lbl, constVal);
                    break;
                }
                case "JUMP_EQUAL_CONSTANT": {
                    String targetLabelName = getArgumentValue(inst, "JEConstantLabel");
                    String constValStr = getArgumentValue(inst, "constantValue");
                    long constVal;
                    try {
                        constVal = Long.parseLong(constValStr);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid constant value: " + constValStr
                                + " (expected a valid integer)");
                    }
                    Label targetLabel;
                    if (targetLabelName.equals( FixedLabel.EXIT.getLabelRepresentation())) {
                        targetLabel = FixedLabel.EXIT;
                    } else if (targetLabelName.equals(FixedLabel.EMPTY.getLabelRepresentation())) {
                        targetLabel = FixedLabel.EMPTY;
                    }
                    else
                        targetLabel = new LabelImpl(Integer.parseInt(targetLabelName.substring(1)));
                    op = new OpJumpEqualConstant(curVar, lbl, targetLabel, constVal);

                    break;
                }
                    case "JUMP_EQUAL_VARIABLE": {
                        String targetLabelName = getArgumentValue(inst, "JEVariableLabel");
                        String otherVarName = getArgumentValue(inst, "variableName");
                        Variable otherVar = new VariableImpl(otherVarName.equals("y") ? VariableType.RESULT :
                                (otherVarName.startsWith("x") ? VariableType.INPUT : VariableType.WORK), Integer.parseInt(otherVarName.substring(1)))
                                ;
                        allVars.add(otherVar);  // second variable used in comparison
                        if(// if it's an input variable, track in inputVars list too
                                otherVar.getType() == VariableType.INPUT && !inputVars.contains(curVar)) {
                            inputVars.add(otherVar);
                        }
                        Label targetLabel;
                        if (targetLabelName.equals( FixedLabel.EXIT.getLabelRepresentation())) {
                            targetLabel = FixedLabel.EXIT;
                        } else if (targetLabelName.equals(FixedLabel.EMPTY.getLabelRepresentation())) {
                            targetLabel = FixedLabel.EMPTY;
                        }
                        else
                            targetLabel = new LabelImpl(Integer.parseInt(targetLabelName.substring(1)));
                        op = new OpJumpEqualVariable(curVar, lbl, targetLabel, otherVar);
                        break;
                    }
                    case "ZERO_VARIABLE":
                        op = new OpZeroVariable(curVar, lbl);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown instruction name: " + cmdName);
                }
                if (lbl != FixedLabel.EXIT && lbl != FixedLabel.EMPTY) {
                    program.addLabel(lbl, op);
                }

                program.getOps().add(op);  // add the constructed operation to the program's list
            }
            // Also ensure the special result variable "y" exists and is initialized to 0
            allVars.add(Variable.RESULT);
        //sort input vars by there get representation method
        inputVars.sort(Comparator.comparing(Variable::getRepresntation));
            program.setInputVars(inputVars);
            program.setInputVars(allVars);
            return program;
        }



    private String getArgumentValue(XOp inst, String argName) {
        ArrayList<XOpArguments> args = (ArrayList<XOpArguments>) inst.getArguments();
        if (args == null) {
            throw new IllegalArgumentException("Instruction \"" + inst.getName()
                    + "\" is missing argument: " + argName);
        }
        for (XOpArguments arg : args) {
            if (argName.equals(arg.getName())) {
                return arg.getValue();
            }
        }
        // If not found by name, throw an error
        throw new IllegalArgumentException("Instruction \"" + inst.getName()
                + "\" is missing argument: " + argName);
    }
}



