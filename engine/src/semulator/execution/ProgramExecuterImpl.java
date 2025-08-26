package semulator.execution;

import semulator.impl.api.Op;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SprogramImpl;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.Map;

public class ProgramExecuterImpl implements ProgramExecuter {
    ExecutionContextImpl context;
    SprogramImpl program;
    public ProgramExecuterImpl(SprogramImpl program) {
        this.program = program;
    }
     // the program loop
    public Long run(Long... input) {
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

        return context.getVariableValue(Variable.RESULT);
    }
    public void AddSnap(ArrayList<Variable> vars, ArrayList<Long> vals) {context.AddSnap(vars, vals);}


    @Override
    public Map<Variable, Long> variableState() {
        return null;
    }
}
