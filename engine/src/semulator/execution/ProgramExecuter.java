package semulator.execution;

import semulator.variable.Variable;
import java.util.Map;

public interface ProgramExecuter {
    long run(long... input);
    Map<Variable, Long> variableState();
}
