package semulator.program;

import semulator.execution.ExecutionContextImpl;
import semulator.impl.api.skeleton.Op;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.*;

public class SprogramImpl implements SProgram {
    private final String name;
    private final List<Op> opList;
    private int OpListindex;
    List<Variable> inputVars;
    ExecutionContextImpl context;
    Set<Variable> variables;


    public SprogramImpl(String name) {
        this.name = name;
        opList = new ArrayList<>();
        OpListindex = 0;
        context = new ExecutionContextImpl();
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
        return 0;
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
}

