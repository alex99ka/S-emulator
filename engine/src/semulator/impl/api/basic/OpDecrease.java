package semulator.impl.api.basic;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;
import java.util.ArrayList;

public class OpDecrease extends AbstractOpBasic {
    @Override
    public Label execute(SProgram program) {
        Long variableValue = program.getVariableValue(getVariable());
        variableValue = Math.max(0, variableValue - 1);
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Long> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(variableValue);

        program.AddSnap(vars,vals);

        return FixedLabel.EMPTY;
    }

    public OpDecrease(Variable variable) {
        super(OpData.DECREASE, variable);
    }

    public OpDecrease(Variable variable, Label label) {
        super(OpData.DECREASE, variable, label);
    }
}
