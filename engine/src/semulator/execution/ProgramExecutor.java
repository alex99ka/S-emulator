package semulator.execution;

import semulator.variable.Variable;

import java.util.List;
import java.util.Map;

public interface ProgramExecutor {
    Long run(List<Long> input);
    Map<Variable, Long> variableState();
}
