package semulator.impl.api.synthetic;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

public class OpJumpZero extends AbstractOpBasic {
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
    public Label execute(SProgram program) {
        return program.getVariableValue(getVariable()) == 0L ? JZlabel: FixedLabel.EMPTY;
    }
}
