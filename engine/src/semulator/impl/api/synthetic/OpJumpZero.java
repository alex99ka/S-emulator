package semulator.impl.api.synthetic;
import semulator.impl.api.basic.OpJumpNotZero;
import semulator.impl.api.basic.OpNeutral;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OpJumpZero extends AbstractOpBasic  {
    Label JZlabel;
    public OpJumpZero( Variable variable, Label JZlabel) {
        super(OpData.JUMP_ZERO, variable);
        this.JZlabel = JZlabel;
    }

    public OpJumpZero( Variable variable, Label label, Label JZlabel) {
        super(OpData.JUMP_ZERO, variable, label);
        this.JZlabel = JZlabel;
    }

    @Override
    public Label execute(SProgram program) {
        program.increaseCycleCounter(getCycles());
        return program.getVariableValue(getVariable()) == 0L ? JZlabel: FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public OpJumpZero myClone() {
        return new OpJumpZero(getVariable().myClone(), getLabel().myClone(), JZlabel.myClone());
    }

    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        List<Op> ops = new ArrayList<>();

        switch (extensionLevel) {
            case 0: {
                return List.of(this);
            }
            case 1: {
                Label skip = program.newUniqueLabel();

                Op jnz = new OpJumpNotZero(getVariable(), skip, getLabel());
                if (getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(), jnz);
                }

                Variable dummy = program.newWorkVar();
                Op go = new OpGoToLabel(dummy, JZlabel);

                Op anchor = new OpNeutral(getVariable(), skip);
                program.addLabel(skip, anchor);

                ops.add(jnz);
                ops.add(go);
                ops.add(anchor);
            }

            default: {
                Label skip = program.newUniqueLabel();

                Op jnz = new OpJumpNotZero(getVariable(), skip, getLabel());
                if (getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(), jnz);
                }
                ops.add(jnz);

                Variable dummy = program.newWorkVar();
                Op go = new OpGoToLabel(dummy, JZlabel);
                ops.addAll(go.expand(1, program));

                Op anchor = new OpNeutral(getVariable(), skip);
                program.addLabel(skip, anchor);
                ops.add(anchor);

            }
                return ops;
        }
    }


    @Override
    public String getRepresentation() {
        return String.format("if %s = 0 GOTO %s", getVariable().getRepresentation(), JZlabel.getLabelRepresentation());
    }
}
