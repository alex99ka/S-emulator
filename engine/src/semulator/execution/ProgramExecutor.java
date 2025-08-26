package semulator.execution;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.Map;


public interface ProgramExecutor {
    long run(Long... input);
    Map<Variable, Long> variableState();

}
