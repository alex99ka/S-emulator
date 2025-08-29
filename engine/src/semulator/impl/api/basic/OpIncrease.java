package semulator.impl.api.basic;

import semulator.execution.ExecutionContextImpl;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.ArrayList;

public class OpIncrease extends AbstractOpBasic {
    public OpIncrease(Variable variable) {
        super(OpData.INCREASE,variable);
    }

    public OpIncrease(Variable variable, Label label) {
        super(OpData.INCREASE, variable, label);
    }

    @Override
    public Label execute(ExecutionContextImpl executable) {

        long variableValue = executable.getVariableValue(getVariable());
        variableValue++;
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Long> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(variableValue);

        executable.AddSnap(vars,vals);

        return FixedLabel.EMPTY;
    }
}
