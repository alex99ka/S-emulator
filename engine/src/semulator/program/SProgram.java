package semulator.program;

import semulator.execution.ExecutionContext;
import semulator.impl.api.skeleton.Op;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.*;

public interface SProgram
{
    String getName();
    void addLabelSet(LinkedHashSet<Label> labels);


    void addOp(Op instruction);

    List<Op> getOps();

    boolean validate();

    int calculateMaxDegree();

    int calculateCycles();
    void increaseCycleCounter(int cycles);
    Label newUniqueLabel();
    Variable GetNextVar(int j);
    void createFirstSnap(List<Long> input);
    int getAmountOfVars();
    Long getVariableValue(Variable var);
    void AddSnap(ArrayList<Variable> vars, ArrayList<Long> vals);
    Op getOpByLabel(Label label);
    Op getNextOp();
    void ChangeOpIndex(Op currentOp);
    int getOpsIndex();
    int getOpsIndex(Op currentOp); // overload for external use for father rep
    void setInputVars(List<Variable> vars);
    void setInputVars(Set<Variable> vars) ;
    void setAllVars(Set<Variable> inputVars);
    Set<Variable> getAllVars();
    void addLabel(Label label, Op op);
    SProgram myClone();
    int getProgramDegree();
    void expandProgram(int degree);
     Variable newWorkVar();
    void print();
     Map<Variable, Long> getCurrSnap();
     List<Long> getInputFromUser();
     int getExapndIndex();
     void setContext(ExecutionContext context);
}
