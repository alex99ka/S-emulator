package semulator.program;

import semulator.execution.ExecutionContextImpl;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.impl.api.skeleton.OpType;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.*;

public class SprogramImpl implements SProgram {
    private final String name;
    private final List<Op> opList;
    private int cycles;
    private int OpListindex;
    private List<Variable> inputVars;
    private ExecutionContextImpl context;
    private Set<Variable> variables;
    private LinkedHashSet <Label> labelsHashSet;


    public SprogramImpl(String name) {
        this.name = name;
        opList = new ArrayList<>();
        OpListindex = 0;
        context = new ExecutionContextImpl();
        cycles = 0;
        variables = new HashSet<>();
        labelsHashSet = new LinkedHashSet<>();
        inputVars = new ArrayList<>();
    }

    public void setInputVars(Set<Variable> inputVars)
    {
        this.variables = inputVars;
    }
    public Set<Variable> getAllVars()
    {
        return variables;
    }
    public void addLabel(Label label, Op op)
    {
        context.getLabelMap().put(label,op);
    }
    public void createFirstSnap(List<Long> input) {
        context.CreateSnap(this, input);
    }

    public void setInputVars(List<Variable> vars) {
        this.inputVars = vars;
    }
    public int getOpsIndex()
    {
        return OpListindex;
    }
    @Override
    public Variable GetNextVar(int j) {
        return inputVars.get(j);
    }

    @Override
    public int getAmountOfVars() {
        return inputVars.size();
    }

    @Override
    public int calculateCycles() {
        return cycles;
    }

    @Override
    public int calculateMaxDegree() {
        return 0;
    }

    @Override //   TO-DO
    public boolean validate() {
        return false;
    }

    @Override
    public List<Op> getOps() {
        return opList;
    }

    @Override
    public void addOp(Op op) {
        opList.add(op);
    }

    @Override
    public String getName() {
        return name;
    }

    public Op getNextOp() {
        return opList.get(OpListindex++);
    }

    @Override
    public void ChangeOpIndex(Op currentOp) {
      if (currentOp==null)
            throw(new IllegalArgumentException("the op is null"));
      else if(opList.contains(currentOp))
        OpListindex = opList.indexOf(currentOp);
      else
          throw(new IllegalArgumentException("the op is not in the program"));
    }
    @Override
    public Long getVariableValue(Variable var) {
        return context.getVariableValue(var);
    }
    @Override
    public void AddSnap(ArrayList<Variable> vars, ArrayList<Long> vals) {context.AddSnap(vars, vals);}

    @Override
    public Op getOpByLabel(Label label) {
        return context.getLabelMap().get(label);
    }

    // deep clone
    @Override
    public SProgram myClone() {
        SProgram newProgram = new SprogramImpl(this.name);
        for (Op op : this.opList) {
            newProgram.addOp((Op) op.myClone()); // Assuming Op is immutable or properly cloned
        }
        newProgram.setInputVars(new ArrayList<>(this.inputVars));
        newProgram.setInputVars(new HashSet<>(this.variables));
        return newProgram;
    }
    @Override
    public void addLabelSet(LinkedHashSet<Label> labels) {
        this.labelsHashSet = labels;
    }

    public void increaseCycleCounter(int cycles) {this.cycles += cycles;}

    @Override
    public Label newUniqueLabel() {
       return context.newUniqueLabel();
    }
    public Variable newWorkVar()
    {
        return context.newWorkVar();
    }

    public void print()
    {
        int i;
        ArrayList<String> res = new ArrayList<>();
        ArrayList <String> inputVars = new ArrayList<>();
        AbstractOpBasic currentOp;
        String type;
        String label;
        String opRep;
        String cycles;
        boolean exitLabel=false;
        for (i=0; i<opList.size(); i++)
        {
            currentOp = (AbstractOpBasic) opList.get(i);
            // if type is synthetic put S in type else put B
            if(currentOp.getType().equals (OpType.SYNTHETIC.getType()))
                type = "S";
            else
                type = "B";

            label = currentOp.getLabel().getLabelRepresentation();
            opRep= currentOp.getRepresentation();
            cycles= String.valueOf(currentOp.getCycles());
            res.add(String.format("#%d (%s) [%5s] <%s> (%s)", i, type, label, opRep, cycles));
        }
         for (Variable v : this.inputVars) {
                inputVars.add(v.getRepresentation());
         }

         System.out.println("Program: "+name+"\n");
         System.out.println("Program Variables: \n");
         System.out.println(String.join(" ", inputVars)+"\n");
         System.out.println("Program Labels: \n");
         for (Label l : labelsHashSet) {
                if(l.equals(FixedLabel.EXIT))
                    exitLabel=true;
                else
                    System.out.print(l.getLabelRepresentation()+" ");
         }
        if(exitLabel)
            System.out.print(FixedLabel.EXIT.getLabelRepresentation()+"\n");
        System.out.println("\nProgram Instructions: \n");
        System.out.println(String.join("\n", res));
        System.out.println("\n");
    }



    @Override
    public int getProgramDegree() {
        int maxDegree = 0;
        int degree;
        for (Op op : opList) {
            degree = op.getDegree();
            if (degree > maxDegree) {
                maxDegree = degree;
            }
        }
        return maxDegree;
    }

    public void deployToDegree(int degree)
    {
        for(int i=0;i<degree;i++) {
            //deploy();
        }
    }

//    public void deploy()
//    {
//        Set<Label> allprogramlabels = context.labelMap.keySet();
//        for(int i=0;i<instructions.size();i++){
//            AbstractInstruction currentInstruction = instructions.get(i);
//            if(currentInstruction instanceof SyntheticSugar)
//            {
//                AbstractInstruction source;
//                ArrayList<AbstractInstruction> expandedInstructions = ((SyntheticSugar) currentInstruction).expand(this.vars);
//                for(AbstractInstruction instruction:expandedInstructions){
//                    instruction.setPos(++programCounter); // Set position for each expanded instruction
//                }
//                source=instructions.remove(i);
//                expandedInstructions.forEach(instruction->instruction.setSyntheticSource(source));// Set the source for each expanded instruction
//                if(source.getLab() instanceof Label) {
//                    removeFirstLabelCollisions((Label) source.getLab(), expandedInstructions, allprogramlabels);
//                }
//                replaceLabels(expandedInstructions, allprogramlabels); // Replace labels in the expanded instructions if needed
//                instructions.addAll(i, expandedInstructions); // Replace the synthetic sugar with its expanded instructions
//                i+=expandedInstructions.size()-1; // Adjust index to account for the newly added instructions
//            }

        //}
    //}


}

