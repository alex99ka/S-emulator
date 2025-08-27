package semulator.impl.api;

import semulator.execution.ExecutionContextImpl;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.ArrayList;

public class OpConstantAssigment extends AbstractOpBasic{
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
    public Label execute(ExecutionContextImpl executable) {
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Long> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(constant);
        executable.AddSnap(vars,vals);
        return FixedLabel.EMPTY;
    }
}
