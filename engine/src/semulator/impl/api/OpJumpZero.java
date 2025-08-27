package semulator.impl.api;

import semulator.execution.ExecutionContextImpl;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

public class OpJumpZero extends AbstractOpBasic{
    Label JZlabel;
    public OpJumpZero( Variable variable, Label JZlabel) {
        super(OpData.JUMP_ZERO, variable);
        this.JZlabel = JZlabel;
    }

    public OpJumpZero( Variable variable, Label label, Label JZlabel) {
        super(OpData.JUMP_ZERO, variable, label);
        this.JZlabel = JZlabel;
    }

    @Override
    public Label execute(ExecutionContextImpl executable) {
        return executable.getVariableValue(getVariable()) == 0L ? JZlabel: FixedLabel.EMPTY;
    }
}
