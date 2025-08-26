package semulator.impl.api;

import semulator.execution.ExecutionContext;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

public class OpIncrease extends AbstractOpBasic {
    public OpIncrease(Variable variable) {
        super(OpData.INCREASE,variable);
    }

    @Override
    public Label execute(ExecutionContext context) {

        long variableValue = context.getVariableValue(getVariable());
        variableValue++;
        context.updateVariable(getVariable(), variableValue);

        return FixedLabel.EMPTY;
    }
}
