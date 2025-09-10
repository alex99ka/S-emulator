package semulator.impl.api.synthetic;

import semulator.impl.api.basic.OpDecrease;
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

public class OpJumpEqualConstant extends AbstractOpBasic {
    Label jEConstantLabel;
    Long constant;
    public OpJumpEqualConstant(Variable variable, Label jEConstantLabel, Long constantValue) {
        super(OpData.JUMP_EQUAL_CONSTANT, variable);
        this.jEConstantLabel = jEConstantLabel;
        this.constant = constantValue;
    }
    public OpJumpEqualConstant(Variable variable, Label jEConstantLabel, Long constantValue, String creatorRep) {
        super(OpData.JUMP_EQUAL_CONSTANT, variable, FixedLabel.EMPTY, creatorRep);
        this.jEConstantLabel = jEConstantLabel;
        this.constant = constantValue;
    }

    public OpJumpEqualConstant(Variable variable, Label label, Label jEConstantLabel, Long constantValue) {
        super(OpData.JUMP_EQUAL_CONSTANT, variable, label);
        this.jEConstantLabel = jEConstantLabel;
        this.constant = constantValue;
    }
    public OpJumpEqualConstant(Variable variable, Label label, Label jEConstantLabel, Long constantValue, String creatorRep) {
        super(OpData.JUMP_EQUAL_CONSTANT, variable, label, creatorRep);
        this.jEConstantLabel = jEConstantLabel;
        this.constant = constantValue;
    }

    @Override
    public Label execute(SProgram program) {
        program.increaseCycleCounter(getCycles());
        return program.getVariableValue(getVariable()).equals(constant) ? jEConstantLabel : FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public Op myClone() {
        Op op = new OpJumpEqualConstant(getVariable().myClone(), getLabel().myClone(), jEConstantLabel.myClone(), constant);
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
                Variable z1 = program.newWorkVar();
                Variable v = getVariable();
                long k = constant;
                Label target   = jEConstantLabel;
                Label notEqLbl = program.newUniqueLabel();

                // z1 = v
                Op a1 = new OpAssignment(z1, getLabel(), v,repToChild(program));
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY)) {
                    program.addLabel(getLabel(), a1);
                }
                ops.add(a1);

                // check if equal
                for (long i = 0; i < k; i++) {
                    ops.add(new OpJumpZero(z1, notEqLbl,repToChild(program))); //
                    ops.add(new OpDecrease(z1,repToChild(program)));
                }
                // z1 > k?
                ops.add(new OpJumpNotZero(z1, notEqLbl,repToChild(program)));
                ops.add(new OpGoToLabel(z1, target,repToChild(program))); // great success for me

                // not equal anchor
                Op end = new OpNeutral(v, notEqLbl,repToChild(program));
                program.addLabel(notEqLbl, end);
                ops.add(end);
                break;

            }

            case 2: {
                Variable z1 = program.newWorkVar();
                Variable v     = getVariable();
                long k         = constant;
                Label target   = jEConstantLabel;
                Label notEqLbl = program.newUniqueLabel();

                Op a1 = new OpAssignment(z1, getLabel(), v,repToChild(program));
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY)) {
                    program.addLabel(getLabel(), a1);
                }
                ops.addAll(a1.expand(1, program));

                for (long i = 0; i < k; i++) {
                    Op jz = new OpJumpZero(z1, notEqLbl,repToChild(program));
                    ops.addAll(jz.expand(1, program));
                    ops.add(new OpDecrease(z1,repToChild(program)));
                }

                Op jnz = new OpJumpNotZero(z1, notEqLbl,repToChild(program));
                ops.add(jnz);

                Op go = new OpGoToLabel(program.newWorkVar(), target,repToChild(program));
                ops.addAll(go.expand(1, program));

                Op end = new OpNeutral(v, notEqLbl,repToChild(program));
                program.addLabel(notEqLbl, end);
                ops.add(end);
                break;
            }

            default: {
                Variable z1    = program.newWorkVar();
                Variable v     = getVariable();
                long k  = constant;
                Label target   = this.jEConstantLabel;
                Label notEqLbl = program.newUniqueLabel();

                Op a1 = new OpAssignment(z1, getLabel(), v,repToChild(program));
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY)) {
                    program.addLabel(getLabel(), a1);
                }
                ops.addAll(a1.expand(extensionLevel - 1, program));

                for (long i = 0; i < k; i++) {
                    Op jz = new OpJumpZero(z1, notEqLbl,repToChild(program));
                    ops.addAll(jz.expand(extensionLevel - 1, program));
                    ops.add(new OpDecrease(z1,repToChild(program)));
                }

                Op jnz = new OpJumpNotZero(z1, notEqLbl,repToChild(program));
                ops.add(jnz);

                Op go = new OpGoToLabel(program.newWorkVar(), target,repToChild(program));
                ops.addAll(go.expand(extensionLevel - 1, program));

                Op end = new OpNeutral(v, notEqLbl,repToChild(program));
                program.addLabel(notEqLbl, end);
                ops.add(end);
                break;
            }
        }
        return ops;
    }


    @Override
    public String getRepresentation() {
        return String.format("if %s = %d GOTO %s", getVariable().getRepresentation(), constant, jEConstantLabel.getLabelRepresentation());
    }
    @Override
    public String getUniqRepresentation() {
        String lbl;
        if (getLabel() == null || getLabel().equals(FixedLabel.EMPTY))
            lbl = "";
        else
            lbl = " [" + getLabel().getLabelRepresentation() + "]";
        return String.format("if %s = %d GOTO %s", getVariable().getRepresentation(), constant, jEConstantLabel.getLabelRepresentation()) + getFatherRep() + lbl;
    }
}
