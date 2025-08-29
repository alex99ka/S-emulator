package semulator.input.XmlTranslator;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import semulator.impl.api.basic.OpIncrease;
import semulator.impl.api.skeleton.Op;
import semulator.input.gen.XOp;
import semulator.input.gen.XOpArguments;
import semulator.input.gen.XProgram;
import semulator.label.Label;
import semulator.label.LabelImpl;
import semulator.program.SProgram;
import semulator.program.SprogramImpl;
import semulator.impl.api.basic.*;
import semulator.impl.api.synthetic.*;
import semulator.variable.Variable;
import semulator.variable.VariableImpl;
import semulator.variable.VariableType;

import java.io.File;
import java.util.*;

public class Factory
{
    XProgram xProgram;
    Set<String> definedLabels;
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
            xProgram = (XProgram) unmarshaller.unmarshal(xmlFile);
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
                definedLabels.add(inst.getLabel());
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
                    if (!definedLabels.contains(argValue)) {
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

        // Convert each SInstruction to an Op object and add to program
        for (XOp inst : sInstructions) {
            String cmdType = inst.getType();    // "basic" or "synthetic"
            String cmdName = inst.getName();    // e.g., INCREASE, ZERO_VARIABLE, etc.
            String varName = inst.getVariable();  // e.g., "x1", "y", "z2"
            String labelName = inst.getLabel();   // may be null
            int varIndex;

            Label lbl = null;
            // check if the labelname has the format of first char is the letter "L" and the rest are digits
            if (labelName != null && !labelName.isEmpty()) {
                if (!labelName.matches("L\\d+")) {
                    throw new IllegalArgumentException("Invalid label format: " + labelName
                            + " (expected format: L followed by digits, e.g., L1, L2)");
                }
            lbl = new LabelImpl(Integer.parseInt(labelName.substring(1))); // remove the "L" prefix
            }

            // Add the main variable to the set of variables
            if (varName == null || varName.isEmpty())
                throw new IllegalArgumentException("Instruction missing variable: " + cmdName);
            varIndex = Integer.parseInt(varName.substring(1)); // extract index after first char
            VariableType vType = varName.equals("y") ? VariableType.RESULT :
                    (varName.startsWith("x") ? VariableType.INPUT : VariableType.WORK);
            Variable curVar = new VariableImpl(vType, varIndex);


            allVars.add(curVar);  // track this variable for initialization

            Op op;  // will point to a new instruction object
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
                    String targetLabel = getArgumentValue(inst, "JNZLabel");
                    if (targetLabel != null && !targetLabel.isEmpty()) {
                        if (!labelName.matches("L\\d+")) {
                            throw new IllegalArgumentException("Invalid label format: " + targetLabel + "for JNZ target label"
                                    + " (expected format: L followed by digits, e.g., L1, L2)");
                        }
                    }
                    op = new OpJumpNotZero(curVar,new LabelImpl(Integer.parseInt(targetLabel.substring(1))),lbl);
                    break;
                }
                case "JUMP_ZERO": {
                    String targetLabel = getArgumentValue(inst, "JZLabel");
                    if (targetLabel != null && !targetLabel.isEmpty()) {
                        if (!labelName.matches("L\\d+")) {
                            throw new IllegalArgumentException("Invalid label format: " + targetLabel + "for JZ target label"
                                    + " (expected format: L followed by digits, e.g., L1, L2)");
                        }
                    }
                    op = new OpJumpZero(curVar, lbl, new LabelImpl(Integer.parseInt(targetLabel.substring(1))));
                    break;
                }
                case "GOTO_LABEL": {
                    String targetLabel = getArgumentValue(inst, "gotoLabel");
                    if (targetLabel != null && !targetLabel.isEmpty()) {
                        if (!labelName.matches("L\\d+")) {
                            throw new IllegalArgumentException("Invalid label format: " + targetLabel + "for GO_TO_LABEL target label"
                                    + " (expected format: L followed by digits, e.g., L1, L2)");
                        }
                    }
                    op = new OpGoToLabel(curVar, lbl, new LabelImpl(Integer.parseInt(targetLabel.substring(1))));
                    break;
                }
                case "ASSIGNMENT": {
                    // Assignment: copies one variable's value to another
                    String srcVarName = getArgumentValue(inst, "assignedVariable");
                    Variable srcVar = new VariableImpl(srcVarName.equals("y") ? VariableType.RESULT :
                            (srcVarName.startsWith("x") ? VariableType.INPUT : VariableType.WORK), Integer.parseInt(srcVarName.substring(1)))
                    ;
                    allVars.add(srcVar);  // source variable also involved
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
                    String targetLabel = getArgumentValue(inst, "JEConstantLabel");
                    String constValStr = getArgumentValue(inst, "constantValue");
                    Long constVal;
                    try {
                        constVal = Long.parseLong(constValStr);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid constant value: " + constValStr
                                + " (expected a valid integer)");
                    }
                    if (targetLabel != null && !targetLabel.isEmpty()) {
                        if (!labelName.matches("L\\d+")) {
                            throw new IllegalArgumentException("Invalid label format: " + targetLabel + "for JE_CONSTANT target label"
                                    + " (expected format: L followed by digits, e.g., L1, L2)");
                        }
                        op = new OpJumpEqualConstant(curVar, lbl, new LabelImpl(Integer.parseInt(targetLabel.substring(1))), constVal);
                        break;
                    }
                }
                    case "JUMP_EQUAL_VARIABLE": {
                        String targetLabel = getArgumentValue(inst, "JEVariableLabel");
                        String otherVarName = getArgumentValue(inst, "variableName");
                        Variable otherVar = new VariableImpl(otherVarName.equals("y") ? VariableType.RESULT :
                                (otherVarName.startsWith("x") ? VariableType.INPUT : VariableType.WORK), Integer.parseInt(otherVarName.substring(1)))
                                ;
                        allVars.add(otherVar);  // second variable used in comparison
                        if (targetLabel != null && !targetLabel.isEmpty()) {
                            if (!labelName.matches("L\\d+")) {
                                throw new IllegalArgumentException("Invalid label format: " + targetLabel + "for JE_VARIABLE target label"
                                        + " (expected format: L followed by digits, e.g., L1, L2)");
                            }
                        }
                        op = new OpJumpEqualVariable(curVar, lbl, new LabelImpl(Integer.parseInt(targetLabel.substring(1))), otherVar);
                        break;
                    }
                    case "ZERO_VARIABLE":
                        op = new OpZeroVariable(curVar, lbl);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown instruction name: " + cmdName);
                }
                if (lbl != null) {
                    program.addLabel(lbl, op);
                }

                program.getOps().add(op);  // add the constructed operation to the program's list
            }


            // Also ensure the special result variable "y" exists and is initialized to 0
            allVars.add(Variable.RESULT);// add a result var
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



