package semulator.impl.api;

import semulator.execution.ExecutionContextImpl;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

public class OpIncrease extends AbstractOpBasic {
    public OpIncrease(Variable variable) {
        super(OpData.INCREASE,variable);
    }

    @Override
    public Label execute(ExecutionContextImpl executable) {

        long variableValue = executable.getVariableValue(getVariable());
        variableValue++;
        executable.AddSnap(variableValue);

        return FixedLabel.EMPTY;
    }
}
