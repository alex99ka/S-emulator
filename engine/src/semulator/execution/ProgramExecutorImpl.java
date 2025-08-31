package semulator.execution;

import semulator.impl.api.skeleton.Op;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;
import java.util.ArrayList;
import java.util.List;

public class ProgramExecutorImpl implements ProgramExecutor {
    SProgram program;
    public ProgramExecutorImpl(SProgram program) {
        this.program = program;
    }
     // the 4th command!
    public Long run(List<Long> input) {
        program.createFirstSnap(input);
        Op currentOp = program.getNextOp();
        Label nextLabel;
        do {
            nextLabel = currentOp.execute(program);
            if(nextLabel.equals(FixedLabel.EXIT))
                 break;
            else if (nextLabel.equals(FixedLabel.EMPTY))
                currentOp = program.getNextOp();
             else {
                currentOp = program.getOpByLabel(nextLabel);
                program.ChangeOpIndex(currentOp);
            }


        } while (program.getOpsIndex() < program.getOps().size()-1);

        return program.getVariableValue(Variable.RESULT);
    }

    public void AddSnap(ArrayList<Variable> vars, ArrayList<Long> vals) {program.AddSnap(vars, vals);}




}
