package semulator.impl.api.synthetic;

import semulator.execution.ExecutionContextImpl;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.Label;
import semulator.variable.Variable;

public class OpGoToLabel extends AbstractOpBasic {
    Label nextLabel;
    public OpGoToLabel(Variable variable, Label nextLabel) {
        super(OpData.GOTO_LABEL,variable);
        this.nextLabel = nextLabel;
    }

    public OpGoToLabel(Variable variable, Label label,Label nextLabel) {
        super(OpData.GOTO_LABEL, variable, label);
        this.nextLabel = nextLabel;
    }

    @Override
    public Label execute(ExecutionContextImpl executable) {
        return nextLabel;
    }
}
