package semulator.impl.api.basic;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
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
    public Label execute(SProgram program) {

        Long variableValue = program.getVariableValue(getVariable());
        variableValue++;
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Long> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(variableValue);

        program.AddSnap(vars,vals);
        program.increaseCycleCounter(getCycles());

        return FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public OpIncrease myClone() {
        return new OpIncrease(getVariable().myClone(), getLabel().myClone());
    }
    @Override
    public String getRepresentation() {
        return String.format("%s ← %s + 1", getVariable().getRepresentation(), getVariable().getRepresentation());
    }
}
