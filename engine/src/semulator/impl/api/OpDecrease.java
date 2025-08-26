package semulator.impl.api;

import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.Map;

public class OpDecrease extends AbstractOpBasic {
    @Override
    public Label execute(ArrayList<Map<Variable, Long>> snapshots) {
        long variableValue = snapshots.getVariableValue(getVariable());
        variableValue = Math.max(0, variableValue - 1);
        snapshots.updateVariable(getVariable(), variableValue);

        return FixedLabel.EMPTY;
    }

    public OpDecrease(Variable variable) {
        super(OpData.DECREASE, variable);

    }
}
