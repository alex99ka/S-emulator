package semulator.execution;

import semulator.impl.api.Op;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SprogramImpl;
import semulator.variable.Variable;

import java.util.Map;

public class ProgramExecuterImpl implements ProgramExecuter {
    ExecutionContextImpl context;
    SprogramImpl program;
    public ProgramExecuterImpl(SprogramImpl program) {
        this.program = program;
    }

    public long run(Long... input) {
        this.context = new ExecutionContextImpl(program, input);
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

        return context.getVariableValue();
    }
    public void AddSnap(Long variableValue,Variable... workVariables)
    {
        if (workVariables != null) {
            for (Variable v : workVariables) {
               context.CurrSnap.put(v, variableValue);
            }
        }
        context.CurrSnap.put(semulator.variable.Variable.RESULT, variableValue); // update y
        var frozen = (Map.copyOf(context.CurrSnap));
        context.AddSnap(frozen);
    }


    @Override
    public Map<Variable, Long> variableState() {
        return null;
    }
}
