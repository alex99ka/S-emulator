package semulator.impl.api;

import semulator.execution.ExecutionContext;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

public class OpDecrease extends AbstractOpBasic {
    @Override
    public Label execute(ExecutionContext context) {
        long variableValue = context.getVariableValue(getVariable());
        variableValue = Math.max(0, variableValue - 1);
        context.updateVariable(getVariable(), variableValue);

        return FixedLabel.EMPTY;
    }

    public OpDecrease(Variable variable) {
        super(OpData.DECREASE, variable);

    }
}
