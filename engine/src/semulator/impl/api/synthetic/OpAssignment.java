package semulator.impl.api.synthetic;

import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
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
    public Label execute(SProgram program) {
        Long variableValue = program.getVariableValue(outSideVar);
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Long> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(variableValue);
        program.AddSnap(vars,vals);

        return FixedLabel.EMPTY;
    }
}
