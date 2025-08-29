package semulator.impl.api.synthetic;

import semulator.execution.ExecutionContextImpl;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.ArrayList;

public class OpZeroVariable extends AbstractOpBasic {
    public OpZeroVariable(OpData opData, Variable variable) {
        this(opData, variable, FixedLabel.EMPTY);
    }

    public OpZeroVariable(OpData opData, Variable variable, Label label) {
        super(opData, variable, label);
    }

    @Override
    public Label execute(ExecutionContextImpl executable)
    {
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Long> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(0L);
        executable.AddSnap(vars,vals);
        return FixedLabel.EMPTY;
    }
}
