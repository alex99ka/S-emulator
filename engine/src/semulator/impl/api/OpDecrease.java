package semulator.impl.api;

import semulator.execution.ExecutionContextImpl;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class OpDecrease extends AbstractOpBasic {
    @Override
    public Label execute(ExecutionContextImpl executable) {
        Long variableValue = executable.getVariableValue(getVariable());
        variableValue = Math.max(0, variableValue - 1);
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Long> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(variableValue);

        executable.AddSnap(vars,vals);

        return FixedLabel.EMPTY;
    }

    public OpDecrease(Variable variable) {
        super(OpData.DECREASE, variable);

    }
}
