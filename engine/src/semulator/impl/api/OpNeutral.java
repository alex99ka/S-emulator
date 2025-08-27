package semulator.impl.api;

import semulator.execution.ExecutionContextImpl;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

public class OpNeutral extends AbstractOpBasic {
    public OpNeutral(Variable variable) {
        super(OpData.NEUTRAL,variable);
    }

    public OpNeutral(Variable variable, Label label) {
        super(OpData.NEUTRAL, variable, label);
    }

    @Override
    public Label execute(ExecutionContextImpl executable) {
        return FixedLabel.EMPTY;
    }
}
