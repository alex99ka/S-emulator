package semulator.impl.api;

import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

public class OpIncrease extends AbstractOpBasic {
    public OpIncrease(Variable variable) {
        super(OpData.INCREASE,variable);
    }

    @Override
    public Label execute(ArrayList<Map<Variable, Long>> snapshots) {

        long variableValue = snapshots.getVariableValue(getVariable());
        variableValue++;
        snapshots.updateVariable(getVariable(), variableValue);

        return FixedLabel.EMPTY;
    }
}
