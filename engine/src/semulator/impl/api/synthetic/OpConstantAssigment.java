package semulator.impl.api.synthetic;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

import java.util.ArrayList;

public class OpConstantAssigment extends AbstractOpBasic {
    Long constant;
    public OpConstantAssigment( Variable variable, Long constant) {
        super(OpData.CONSTANT_ASSIGNMENT, variable);
        this.constant = constant;
    }

    public OpConstantAssigment(Variable variable, Label label,  Long constant) {
        super(OpData.CONSTANT_ASSIGNMENT, variable, label);
        this.constant = constant;
    }

    @Override
    public Label execute(SProgram program) {
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Long> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(constant);
        program.AddSnap(vars,vals);
        program.increaseCycleCounter(getCycles());

        return FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public OpConstantAssigment myClone() {
        return new OpConstantAssigment(getVariable().myClone(), getLabel().myClone(), constant);
    }
    @Override
    public String getRepresentation() {
        return String.format("%s ← %d", getVariable().getRepresentation(), constant);
    }
}
