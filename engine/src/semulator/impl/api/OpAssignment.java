package semulator.impl.api;

import semulator.execution.ExecutionContextImpl;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

import java.util.ArrayList;

public class OpAssignment extends AbstractOpBasic {
    Variable outSideVar;
    public OpAssignment( Variable variable, Variable outSideVar) {
        super(OpData.ASSIGNMENT, variable);
        this.outSideVar = outSideVar;
    }

    public OpAssignment(Variable variable, Label label,  Variable outSideVar) {
        super(OpData.ASSIGNMENT, variable, label);
        this.outSideVar = outSideVar;
    }

    @Override
    public Label execute(ExecutionContextImpl executable) {
        Long variableValue = executable.getVariableValue(outSideVar);
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Long> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(variableValue);
        executable.AddSnap(vars,vals);

        return FixedLabel.EMPTY;
    }
}
