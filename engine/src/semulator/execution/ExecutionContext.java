package semulator.execution;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.List;
import java.util.Map;

public interface ExecutionContext {

    Long getVariableValue(Variable v);
    public List getSnapshots();
    Map<Label, Op> getLabelMap();

}