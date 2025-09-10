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
import java.util.List;

public class OpJumpZero extends AbstractOpBasic  {
    Label jZLabel;
    public OpJumpZero( Variable variable, Label jZLabel) {
        super(OpData.JUMP_ZERO, variable);
        this.jZLabel = jZLabel;
    }
    public OpJumpZero(Variable variable, Label jZLabel, String creatorRep) {
        super(OpData.JUMP_ZERO, variable, FixedLabel.EMPTY, creatorRep);
        this.jZLabel = jZLabel;
    }

    public OpJumpZero( Variable variable, Label label, Label jZLabel) {
        super(OpData.JUMP_ZERO, variable, label);
        this.jZLabel = jZLabel;
    }
    public OpJumpZero(Variable variable, Label label, Label jZLabel, String creatorRep) {
        super(OpData.JUMP_ZERO, variable, label, creatorRep);
        this.jZLabel = jZLabel;
    }

    @Override
    public Label execute(SProgram program) {
        program.increaseCycleCounter(getCycles());
        return program.getVariableValue(getVariable()) == 0L ? jZLabel : FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public Op myClone() {
        Op op = new OpJumpZero(getVariable().myClone(), getLabel().myClone(), jZLabel.myClone());
        op.setExpandIndex(getMyExpandIndex());
        return op;
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

                Op jnz = new OpJumpNotZero(getVariable(), getLabel(),skip ,repToChild(program));
                if (getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(), jnz);
                }

                Variable dummy = program.newWorkVar();
                Op go = new OpGoToLabel(dummy, jZLabel,repToChild(program));

                Op anchor = new OpNeutral(getVariable(), skip,repToChild(program));
                program.addLabel(skip, anchor);

                ops.add(jnz);
                ops.add(go);
                ops.add(anchor);
                break;
            }

            default: {
                Label skip = program.newUniqueLabel();

                Op jnz = new OpJumpNotZero(getVariable(), getLabel(),skip ,repToChild(program));
                if (getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(), jnz);
                }
                ops.add(jnz);

                Variable dummy = program.newWorkVar();
                Op go = new OpGoToLabel(dummy, jZLabel,repToChild(program));
                ops.addAll(go.expand(1, program));

                Op anchor = new OpNeutral(getVariable(), skip,repToChild(program));
                program.addLabel(skip, anchor);
                ops.add(anchor);
                break;

            }
        }
        return ops;
    }


    @Override
    public String getRepresentation() {
        return String.format("if %s = 0 GOTO %s", getVariable().getRepresentation(), jZLabel.getLabelRepresentation()) ;
    }
    @Override
    public String getUniqRepresentation() {
        String lbl;
        if (getLabel() == null || getLabel().equals(FixedLabel.EMPTY))
            lbl = "";
        else
            lbl = " [" + getLabel().getLabelRepresentation() + "]";
        return String.format("if %s = 0 GOTO %s", getVariable().getRepresentation(), jZLabel.getLabelRepresentation()) + getFatherRep() + lbl;
    }
}
