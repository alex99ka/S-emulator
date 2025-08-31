package semulator.impl.api.basic;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

public class OpNeutral extends AbstractOpBasic {
    public OpNeutral(Variable variable) {
        super(OpData.NEUTRAL,variable);
    }

    public OpNeutral(Variable variable, Label label) {
        super(OpData.NEUTRAL, variable, label);
    }

    @Override
    public Label execute(SProgram program) {
        return FixedLabel.EMPTY;
    }
}
