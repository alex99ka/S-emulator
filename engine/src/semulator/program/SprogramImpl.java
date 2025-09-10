package semulator.program;

import semulator.execution.ExecutionContext;
import semulator.execution.ExecutionContextImpl;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.*;

public class SprogramImpl implements SProgram {
    private final String name;
    private  List<Op> opList;
    private int cycles;
    private int opListIndex;
    private List<Variable> inputVars;
    private ExecutionContextImpl context;
    private Set<Variable> variables;
    private LinkedHashSet <Label> labelsHashSet;
    private int expandIndex;


    public SprogramImpl(String name) {
        this.name = name;
        opList = new ArrayList<>();
        opListIndex = 0;
        context = new ExecutionContextImpl();
        cycles = 0;
        variables = new HashSet<>();
        labelsHashSet = new LinkedHashSet<>();
        inputVars = new ArrayList<>();
        expandIndex = 0;
    }

    public void setAllVars(Set<Variable> inputVars)
    {
        variables.addAll( inputVars);
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
        context.createSnap(this, input);
    }

    public void setInputVars(List<Variable> vars) {
        this.inputVars = vars;
    }
    public void setInputVars(Set<Variable> vars) {
        this.inputVars = new ArrayList<>(vars);
    }
    public int getOpsIndex()
    {
        return opListIndex;
    }
    public int getOpsIndex(Op currentOp)
    {
        int index;
        if (currentOp==null)
            throw(new IllegalArgumentException("the op is null"));
        else if (opList.stream().anyMatch(op -> op.getRepresentation().equals(currentOp.getRepresentation())))
            index = opList.indexOf(opList.stream()
                    .filter(op -> op.getRepresentation().equals(currentOp.getRepresentation()))
                    .findFirst().get());
        else
            throw(new IllegalArgumentException("the op is not in the program"));
        return index;
    }
    @Override
    public Variable GetNextVar(int i) {
        return inputVars.get(i);
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
        return opList.get(opListIndex++);
    }

    @Override
    public void ChangeOpIndex(Op currentOp) {
      if (currentOp==null)
            throw(new IllegalArgumentException("the op is null"));
      else if (opList.stream().anyMatch(op -> op.getUniqRepresentation().equals(currentOp.getUniqRepresentation())))
          opListIndex = opList.indexOf(opList.stream()
                  .filter(op -> op.getUniqRepresentation().equals(currentOp.getUniqRepresentation()))
                  .findFirst().get());
      else
          throw(new IllegalArgumentException("the op is not in the program"));
      opListIndex++; //!!!!!!!!!!! god damn it
    }
    @Override
    public Long getVariableValue(Variable variable) {
        return context.getVariableValue(variable);
    }
    @Override
    public void AddSnap(ArrayList<Variable> vars, ArrayList<Long> vals) {context.addSnap(vars, vals);}

    @Override
    public Op getOpByLabel(Label label) {
        return context.getLabelMap().get(label);
    }

    // deep clone
    @Override
    public SProgram myClone() {
        SProgram newProgram = new SprogramImpl(this.name);
        for (Op op : this.opList) {
            newProgram.addOp( op.myClone()); // Assuming Op is immutable or properly cloned
        }
        newProgram.setInputVars(new ArrayList<>(this.inputVars));
        newProgram.setAllVars(new HashSet<>(this.variables));
        newProgram.setContext(context);
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
        Variable tmp = context.newWorkVar();
        variables.add(tmp);
        return tmp;
    }

    public void print()
    {
        int i;
        ArrayList<String> res = new ArrayList<>();
        ArrayList <String> stringInputVars = new ArrayList<>();
        AbstractOpBasic currentOp;

        boolean exitLabel=false;
        for (i=0; i<opList.size(); i++)
        {
            currentOp = (AbstractOpBasic) opList.get(i);
            // if type is synthetic put S in type else put B
            res.add(currentOp.repToChild(this).replaceFirst("<<<", ""));
        }
         for (Variable v : this.inputVars) {
                stringInputVars.add(v.getRepresentation());
         }

         System.out.println("Program: "+name+"\n");
         System.out.println("Program Variables: \n");
         System.out.println(String.join(" ", stringInputVars)+"\n");
         System.out.println("Program Labels: \n");
         for (Label l : context.getLabelMap().keySet()) {
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

    public void expandProgram(int degree)
    {
        List<Op> expandedList = new  ArrayList<>();
        for (Op op: opList) {
            expandedList.addAll(op.expand(degree,this));
        }
        opList = expandedList;
    }
   public Map<Variable, Long> getCurrSnap()
    {
        return context.getCurrSnap();
    }
    public List<Long> getInputFromUser() {
    System.out.println("please enter " + inputVars.size() + " inputs as numbers, separated by commas");

    Scanner scanner = new Scanner(System.in);
    String line = scanner.nextLine();

    List<Long> numbers = new ArrayList<>();

    String[] tokens = line.split(",");

    for (String token : tokens) {
        try {
            long num = Long.parseLong(token.trim());
            numbers.add(num);
        } catch (NumberFormatException e) {//ignore invalid input}
        }
    }
        return numbers;
    }

    @Override
    public void setContext(ExecutionContext context) {
        this.context = new ExecutionContextImpl(context);
    }

    @Override
    public int getExapndIndex() {
        return expandIndex++;
    }
}

