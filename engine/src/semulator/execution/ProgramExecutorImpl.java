package semulator.execution;
import semulator.impl.api.Op;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.Map;


public class ProgramExecutorImpl implements ProgramExecutor ,ExecutionContext {
    private final SProgram program;
    private ArrayList< Map<Variable, Long>> snapshots;
    public ProgramExecutorImpl(SProgram program) {
        this.program = program;
        this.snapshots = new ArrayList<>();
    }

    @Override
    public long run(Long... input) {

        ExecutionContext context = null; // create the context with inputs.

        Op currentInstruction = program.getOps().getFirst();
        Label nextLabel;
        do {
            nextLabel = currentInstruction.execute(snapshots);

            if (nextLabel == FixedLabel.EMPTY) {
                // set currentInstruction to the next instruction in line
            } else if (nextLabel != FixedLabel.EXIT) {
                // need to find the instruction at 'nextLabel' and set current instruction to it
            }
        } while (nextLabel != FixedLabel.EXIT);

        return context.getVariableValue(Variable.RESULT);
    }

    @Override
    public Map<Variable, Long> variableState() {
        return Map.of();
    }

    @Override
    public long getVariableValue(Variable v) {
        return 0;
    }

    @Override
    public void updateVariable(Variable v, long value) {

    }
}
