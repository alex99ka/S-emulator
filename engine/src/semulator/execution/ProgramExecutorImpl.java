package semulator.execution;
import semulator.impl.api.Op;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

import java.util.Map;


public class ProgramExecutorImpl implements ProgramExecutor{
    private final SProgram program;

    public ProgramExecutorImpl(SProgram program) {
        this.program = program;
    }

    @Override
    public long run(Long... input) {

        ExecutionContext context = null; // create the context with inputs.

        Op currentInstruction = program.getOps().getFirst();
        Label nextLabel;
        do {
            nextLabel = currentInstruction.execute(context);

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
}
