package semulator.impl.api;

import semulator.execution.ExecutionContextImpl;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.Map;

public class OpDecrease extends AbstractOpBasic {
    @Override
    public Label execute(ExecutionContextImpl executable) {
        Long variableValue = executable.getVariableValue();
        variableValue = Math.max(0, variableValue - 1);
        executable.AddSnap(variableValue);

        return FixedLabel.EMPTY;
    }

    public OpDecrease(Variable variable) {
        super(OpData.DECREASE, variable);

    }
}
