package semulator.execution;

import semulator.variable.Variable;
import java.util.Map;

public interface ProgramExecuter {
    Long run(Long... input);
    Map<Variable, Long> variableState();
}
