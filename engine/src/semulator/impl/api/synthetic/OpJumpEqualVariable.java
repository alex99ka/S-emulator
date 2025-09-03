package semulator.impl.api.synthetic;
import semulator.impl.api.basic.OpDecrease;
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

public class OpJumpEqualVariable extends AbstractOpBasic {

    Variable comparableVariable;
    Label JEConstantLabel;

    public OpJumpEqualVariable(Variable variable, Label JEConstantLabel, Variable comparableVariable) {
        super(OpData.JUMP_EQUAL_VARIABLE, variable);
        this.JEConstantLabel = JEConstantLabel;
        this.comparableVariable = comparableVariable;
    }
    public OpJumpEqualVariable(Variable variable, Label JEConstantLabel, Variable comparableVariable, String creatorRep) {
        super(OpData.JUMP_EQUAL_VARIABLE, variable, FixedLabel.EMPTY, creatorRep);
        this.JEConstantLabel = JEConstantLabel;
        this.comparableVariable = comparableVariable;
    }

    public OpJumpEqualVariable(Variable variable, Label label, Label JEConstantLabel, Variable comparableVariable) {
        super(OpData.JUMP_EQUAL_VARIABLE, variable, label);
        this.JEConstantLabel = JEConstantLabel;
        this.comparableVariable = comparableVariable;
    }
    public OpJumpEqualVariable(Variable variable, Label label, Label JEConstantLabel, Variable comparableVariable, String creatorRep) {
        super(OpData.JUMP_EQUAL_VARIABLE, variable, label, creatorRep);
        this.JEConstantLabel = JEConstantLabel;
        this.comparableVariable = comparableVariable;
    }

    @Override
    public Label execute(SProgram program) {
        program.increaseCycleCounter(getCycles());
        return program.getVariableValue(getVariable()).equals(program.getVariableValue(comparableVariable)) ? JEConstantLabel : FixedLabel.EMPTY;
    }

    //implementation of deep clone
    @Override
    public OpJumpEqualVariable myClone() {
        return new OpJumpEqualVariable(getVariable().myClone(), getLabel().myClone(),
                JEConstantLabel.myClone(), comparableVariable.myClone());
    }

    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        List<Op> ops = new ArrayList<>();
        switch (extensionLevel) {
            case 0: {
                return List.of(this);
            }

            case 1: {

                Label Lstart    = program.newUniqueLabel();
                Label LcheckZ2  = program.newUniqueLabel();
                Label LnotEqual = program.newUniqueLabel();
                Variable z1 = program.newWorkVar();
                Variable z2 = program.newWorkVar();

                Label targetLabel = JEConstantLabel;
                Variable v    = this.getVariable();
                Variable vTag = this.comparableVariable;

                Op a1 = new OpAssignment(z1, getLabel(), v,repToChild(program));
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY)) {
                    program.addLabel(getLabel(), a1);
                }
                Op a2 = new OpAssignment(z2, vTag,repToChild(program));

                Op anchorStart = new OpNeutral(v,Lstart,repToChild(program));
                program.addLabel(Lstart, anchorStart);

                Op jz1 = new OpJumpZero(z1, LcheckZ2,repToChild(program));
                Op jz2 = new OpJumpZero(z2, LnotEqual,repToChild(program));

                Op d1 = new OpDecrease(z1,repToChild(program));
                Op d2 = new OpDecrease(z2,repToChild(program));

                Op goStart = new OpGoToLabel(program.newWorkVar(), Lstart,repToChild(program)); //loop untill someone is 0

                Op anchorCheck = new OpNeutral(v,LcheckZ2,repToChild(program));
                program.addLabel(LcheckZ2, anchorCheck);

                Op jzEqual = new OpJumpZero(z2, targetLabel,repToChild(program));

                Op anchorNotEq = new OpNeutral(v,LnotEqual,repToChild(program));
                program.addLabel(LnotEqual, anchorNotEq);

                ops.add(a1);
                ops.add(a2);
                ops.add(anchorStart);
                ops.add(jz1);
                ops.add(jz2);
                ops.add(d1);
                ops.add(d2);
                ops.add(goStart);
                ops.add(anchorCheck);
                ops.add(jzEqual);
                ops.add(anchorNotEq);

            }

            default: {
                Label Lstart    = program.newUniqueLabel();
                Label LcheckZ2  = program.newUniqueLabel();
                Label LnotEqual = program.newUniqueLabel();
                Variable z1 = program.newWorkVar();
                Variable z2 = program.newWorkVar();

                Label targetLabel = JEConstantLabel;
                Variable v    = this.getVariable();
                Variable vTag = this.comparableVariable;

                Op a1 = new OpAssignment(z1, getLabel(), v,repToChild(program));
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY)) {
                    program.addLabel(getLabel(), a1);
                }
                List<Op> assign1 = a1.expand(1, program);

                Op a2 = new OpAssignment(z2, vTag,repToChild(program));
                List<Op> assign2 = a2.expand(1, program);

                Op anchorStart = new OpNeutral(v,repToChild(program));
                program.addLabel(Lstart, anchorStart);

                Op jz1 = new OpJumpZero(z1, LcheckZ2,repToChild(program));
                List<Op> jz1ext = jz1.expand(1, program);

                Op jz2 = new OpJumpZero(z2, LnotEqual,repToChild(program));
                List<Op> jz2ext = jz2.expand(1, program);

                Op d1 = new OpDecrease(z1,repToChild(program));
                Op d2 = new OpDecrease(z2,repToChild(program));

                Op goStart = new OpGoToLabel(program.newWorkVar(), Lstart,repToChild(program));
                List<Op> goExt = goStart.expand(1, program);

                Op anchorCheck = new OpNeutral(v,repToChild(program));
                program.addLabel(LcheckZ2, anchorCheck);

                Op jzEqual = new OpJumpZero(z2, targetLabel,repToChild(program));
                List<Op> jzEqExt = jzEqual.expand(1, program);

                Op anchorNotEq = new OpNeutral(v,repToChild(program));
                program.addLabel(LnotEqual, anchorNotEq);

                ops.addAll(assign1);
                ops.addAll(assign2);
                ops.add(anchorStart);
                ops.addAll(jz1ext);
                ops.addAll(jz2ext);
                ops.add(d1);
                ops.add(d2);
                ops.addAll(goExt);
                ops.add(anchorCheck);
                ops.addAll(jzEqExt);
                ops.add(anchorNotEq);

            }
                return ops;
        }
    }



            @Override
        public String getRepresentation() {
        return String.format("if %s = %s GOTO %s", getVariable().getRepresentation(), comparableVariable.getRepresentation(), JEConstantLabel.getLabelRepresentation());
    }
}
