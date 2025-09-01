package semulator.impl.api.basic;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;
import semulator.variable.VariableImpl;

import java.util.ArrayList;
import java.util.List;

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
        program.increaseCycleCounter(getCycles());

        return FixedLabel.EMPTY;
    }


    public OpDecrease(Variable variable) {
        super(OpData.DECREASE, variable);
    }

    public OpDecrease(Variable variable, Label label) {
        super(OpData.DECREASE, variable, label);
    }
    //implementation of deep clone
    @Override
    public OpDecrease myClone() {
        return new OpDecrease(getVariable().myClone(),getLabel().myClone());
    }

    @Override
    public String getRepresentation() {
        return String.format("%s ← %s + 1", getVariable().getRepresentation(), getVariable().getRepresentation());
    }
    public List<Op> expand(int extensionLevel, SProgram program)
    {
        return List.of(this);
    }

}
