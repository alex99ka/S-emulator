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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OpJumpEqualConstant extends AbstractOpBasic {
    Label JEConstantLabel;
    Long constant;
    public OpJumpEqualConstant( Variable variable, Label JEConstantLabel, Long constantValue) {
        super(OpData.JUMP_EQUAL_CONSTANT, variable);
        this.JEConstantLabel = JEConstantLabel;
        this.constant = constantValue;
    }

    public OpJumpEqualConstant( Variable variable, Label label, Label JEConstantLabel, Long constantValue) {
        super(OpData.JUMP_EQUAL_CONSTANT, variable, label);
        this.JEConstantLabel = JEConstantLabel;
        this.constant = constantValue;
    }

    @Override
    public Label execute(SProgram program) {
        program.increaseCycleCounter(getCycles());
        return program.getVariableValue(getVariable()).equals(constant) ? JEConstantLabel : FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public OpJumpEqualConstant myClone() {
        return new OpJumpEqualConstant(getVariable().myClone(), getLabel().myClone(), JEConstantLabel.myClone(), constant);
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
                Label target   = JEConstantLabel;
                Label notEqLbl = program.newUniqueLabel();

                // z1 = v
                Op a1 = new OpAssignment(z1, getLabel(), v);
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY)) {
                    program.addLabel(getLabel(), a1);
                }
                ops.add(a1);

                // check if equal
                for (long i = 0; i < k; i++) {
                    ops.add(new OpJumpZero(z1, notEqLbl)); //
                    ops.add(new OpDecrease(z1));
                }
                // z1 > k?
                ops.add(new OpJumpNotZero(z1, notEqLbl));
                ops.add(new OpGoToLabel(z1, target)); // great success for me

                // not equal anchor
                Op end = new OpNeutral(v, notEqLbl);
                program.addLabel(notEqLbl, end);
                ops.add(end);

            }

            case 2: {
                Variable z1 = program.newWorkVar();
                Variable v     = getVariable();
                long k         = constant;
                Label target   = JEConstantLabel;
                Label notEqLbl = program.newUniqueLabel();

                Op a1 = new OpAssignment(z1, getLabel(), v);
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY)) {
                    program.addLabel(getLabel(), a1);
                }
                ops.addAll(a1.expand(1, program));

                for (long i = 0; i < k; i++) {
                    Op jz = new OpJumpZero(z1, notEqLbl);
                    ops.addAll(jz.expand(1, program));
                    ops.add(new OpDecrease(z1));
                }

                Op jnz = new OpJumpNotZero(z1, notEqLbl);
                ops.add(jnz);

                Op go = new OpGoToLabel(program.newWorkVar(), target);
                ops.addAll(go.expand(1, program));

                Op end = new OpNeutral(v, notEqLbl);
                program.addLabel(notEqLbl, end);
                ops.add(end);

            }

            default: {
                Variable z1    = program.newWorkVar();
                Variable v     = getVariable();
                long k  = constant;
                Label target   = this.JEConstantLabel;
                Label notEqLbl = program.newUniqueLabel();

                Op a1 = new OpAssignment(z1, getLabel(), v);
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY)) {
                    program.addLabel(getLabel(), a1);
                }
                ops.addAll(a1.expand(extensionLevel - 1, program));

                for (long i = 0; i < k; i++) {
                    Op jz = new OpJumpZero(z1, notEqLbl);
                    ops.addAll(jz.expand(extensionLevel - 1, program));
                    ops.add(new OpDecrease(z1));
                }

                Op jnz = new OpJumpNotZero(z1, notEqLbl);
                ops.add(jnz);

                Op go = new OpGoToLabel(program.newWorkVar(), target);
                ops.addAll(go.expand(extensionLevel - 1, program));

                Op end = new OpNeutral(v, notEqLbl);
                program.addLabel(notEqLbl, end);
                ops.add(end);

                return ops;
            }
        }
    }


    @Override
    public String getRepresentation() {
        return String.format("if %s = %d GOTO %s", getVariable().getRepresentation(), constant, JEConstantLabel.getLabelRepresentation());
    }
}
