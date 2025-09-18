package semulator.execution;
import semulator.impl.api.skeleton.Op;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ExecutionContext {

    Integer getVariableValue(Variable v);
    Map<Label, Op> getLabelMap();
    void addSnap(ArrayList<Variable> vars, ArrayList<Integer> vals);
    Map<Variable, Integer> getCurrSnap();
    List<Map<@org.jetbrains.annotations.NotNull Variable, @org.jetbrains.annotations.NotNull Integer>> getSnapshots();




}