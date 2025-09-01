package semulator.impl.api.basic;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

import java.util.List;

public class OpNeutral extends AbstractOpBasic {
    public OpNeutral(Variable variable) {
        super(OpData.NEUTRAL,variable);
    }

    public OpNeutral(Variable variable, Label label) {
        super(OpData.NEUTRAL, variable, label);
    }
    public List<Op> expand(int extensionLevel, SProgram program)
    {
        return List.of(this);
    }
    @Override
    public Label execute(SProgram program) {
        program.increaseCycleCounter(getCycles());
        return FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public OpNeutral myClone() {
        return new OpNeutral(getVariable().myClone(), getLabel().myClone());
    }
    @Override
    public String getRepresentation()
    {
        return String.format("%s ← %s", getVariable().getRepresentation(), getVariable().getRepresentation());
    }
}
