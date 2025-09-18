package semulator.execution;
import semulator.program.SProgram;
import semulator.statistics.Statistics;

import java.util.List;

public interface ProgramExecutor {
    long run(List<Integer> inputs, Statistics stats );
}
