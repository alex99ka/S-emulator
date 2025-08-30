package semulator.execution;

import semulator.impl.api.skeleton.Op;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.program.SprogramImpl;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProgramExecutorImpl implements ProgramExecutor {
    ExecutionContextImpl context; // THE PROGRAM NEEDS TO HOLD THE CONTEXT
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
            nextLabel = currentOp.execute(context);

            if (nextLabel == FixedLabel.EMPTY) {
                if(program.getOpsIndex() == program.getOps().size()){
                    break;
                }
                currentOp = program.getNextOp();
            } else {
                currentOp = context.getLabelMap().get(nextLabel);
                try {// throw exception if the label does not exist
                    program.ChangeOpIndex(currentOp);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                    throw e;
                }

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
