package semulator.execution;
import semulator.program.SProgram;

import java.util.List;

public interface ProgramExecutor {
    long run( List<Long> inputs);
}
