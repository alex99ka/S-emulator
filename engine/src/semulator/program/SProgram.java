package semulator.program;

import semulator.impl.api.skeleton.Op;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface SProgram {
    String getName();

    void addOp(Op instruction);

    List<Op> getOps();

    boolean validate();

    int calculateMaxDegree();

    int calculateCycles();

    Variable GetNextVar(int j);
    public void createFirstSnap(List<Long> input);
    int getAmountOfVars();
    Long getVariableValue(Variable var);
    void AddSnap(ArrayList<Variable> vars, ArrayList<Long> vals);
    Op getOpByLabel(Label label);
    Op getNextOp();
    void ChangeOpIndex(Op currentOp);
    int getOpsIndex();
    void setInputVars(List<Variable> vars);
    void setInputVars(Set<Variable> inputVars);
    Set<Variable> getAllVars();
    void addLabel(Label label, Op op);
}
