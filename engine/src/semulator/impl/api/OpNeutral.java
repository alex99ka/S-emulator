package semulator.impl.api;

import semulator.execution.ExecutionContext;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

public class OpNeutral extends AbstractOpBasic {
    public OpNeutral(Variable variable) {
        super(OpData.NEUTRAL,variable);
    }

    @Override
    public Label execute(ExecutionContext context) {
        return FixedLabel.EMPTY;
    }
}
