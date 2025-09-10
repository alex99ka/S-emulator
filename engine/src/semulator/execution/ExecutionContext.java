package semulator.execution;
import semulator.impl.api.skeleton.Op;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ExecutionContext {

    Long getVariableValue(Variable v);
    Map<Label, Op> getLabelMap();
    void addSnap(ArrayList<Variable> vars, ArrayList<Long> vals);
    Map<Variable, Long> getCurrSnap();
    List<Map<Variable, Long>> getSnapshots();




}