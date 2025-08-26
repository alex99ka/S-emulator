package semulator.execution;
import semulator.variable.Variable;

import java.util.List;

public interface ExecutionContext {

    Long getVariableValue(Variable v);
    public List getSnapshots();

}