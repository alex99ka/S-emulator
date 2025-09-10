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
import java.util.List;

public class OpJumpEqualVariable extends AbstractOpBasic {

    Variable comparableVariable;
    Label jEConstantLabel;

    public OpJumpEqualVariable(Variable variable, Label jEConstantLabel, Variable comparableVariable) {
        super(OpData.JUMP_EQUAL_VARIABLE, variable);
        this.jEConstantLabel = jEConstantLabel;
        this.comparableVariable = comparableVariable;
    }
    public OpJumpEqualVariable(Variable variable, Label jEConstantLabel, Variable comparableVariable, String creatorRep) {
        super(OpData.JUMP_EQUAL_VARIABLE, variable, FixedLabel.EMPTY, creatorRep);
        this.jEConstantLabel = jEConstantLabel;
        this.comparableVariable = comparableVariable;
    }

    public OpJumpEqualVariable(Variable variable, Label label, Label jEConstantLabel, Variable comparableVariable) {
        super(OpData.JUMP_EQUAL_VARIABLE, variable, label);
        this.jEConstantLabel = jEConstantLabel;
        this.comparableVariable = comparableVariable;
    }
    public OpJumpEqualVariable(Variable variable, Label label, Label jEConstantLabel, Variable comparableVariable, String creatorRep) {
        super(OpData.JUMP_EQUAL_VARIABLE, variable, label, creatorRep);
        this.jEConstantLabel = jEConstantLabel;
        this.comparableVariable = comparableVariable;
    }

    @Override
    public Label execute(SProgram program) {
        program.increaseCycleCounter(getCycles());
        return program.getVariableValue(getVariable()).equals(program.getVariableValue(comparableVariable)) ? jEConstantLabel : FixedLabel.EMPTY;
    }

    //implementation of deep clone
    @Override
    public Op myClone() {
        Op op = new OpJumpEqualVariable(getVariable().myClone(), getLabel().myClone(),
                jEConstantLabel.myClone(), comparableVariable.myClone());
        op.setExpandIndex(getMyExpandIndex());
        return op;
    }

    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        List<Op> ops = new ArrayList<>();
        Label lStart    = program.newUniqueLabel();
        Label lCheckZ2  = program.newUniqueLabel();
        Label lNotEqual = program.newUniqueLabel();
        Variable z1 = program.newWorkVar();
        Variable z2 = program.newWorkVar();
        Label targetLabel = jEConstantLabel;
        Variable v    = this.getVariable();
        Variable vTag = this.comparableVariable;
        switch (extensionLevel) {
            case 0: {
                return List.of(this);
            }
            case 1: {
                Op a1 = new OpAssignment(z1, getLabel(), v,repToChild(program));
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY)) {
                    program.addLabel(getLabel(), a1);
                }
                Op a2 = new OpAssignment(z2, vTag,repToChild(program));

                Op anchorStart = new OpNeutral(z2,lStart,repToChild(program));
                program.addLabel(lStart, anchorStart);

                Op jz1 = new OpJumpZero(z1, lCheckZ2,repToChild(program));
                Op jz2 = new OpJumpZero(z2, lNotEqual,repToChild(program));

                Op d1 = new OpDecrease(z1,repToChild(program));
                Op d2 = new OpDecrease(z2,repToChild(program));

                Op goStart = new OpGoToLabel(program.newWorkVar(), lStart,repToChild(program)); //loop until someone is 0

                Op anchorCheck = new OpNeutral(v,lCheckZ2,repToChild(program));
                program.addLabel(lCheckZ2, anchorCheck);

                Op jzEqual = new OpJumpZero(z2, targetLabel,repToChild(program));

                Op anchorNotEq = new OpNeutral(v,lNotEqual,repToChild(program));
                program.addLabel(lNotEqual, anchorNotEq);

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
                break;

            }

            default: {
                Op a1 = new OpAssignment(z1, getLabel(), v,repToChild(program));
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY)) {
                    program.addLabel(getLabel(), a1);
                }
                List<Op> assign1 = a1.expand(extensionLevel-1, program);

                Op a2 = new OpAssignment(z2, vTag,repToChild(program));
                List<Op> assign2 = a2.expand(extensionLevel-1, program);

                Op anchorStart = new OpNeutral(v,lStart, repToChild(program));
                program.addLabel(lStart, anchorStart);

                Op jz1 = new OpJumpZero(z1, lCheckZ2,repToChild(program));
                List<Op> jz1ext = jz1.expand(extensionLevel-1, program);

                Op jz2 = new OpJumpZero(z2, lNotEqual,repToChild(program));
                List<Op> jz2ext = jz2.expand(1, program);

                Op d1 = new OpDecrease(z1,repToChild(program));
                Op d2 = new OpDecrease(z2,repToChild(program));

                Op goStart = new OpGoToLabel(program.newWorkVar(), lStart,repToChild(program));
                List<Op> goExt = goStart.expand(extensionLevel-1, program);

                Op anchorCheck = new OpNeutral(v,lCheckZ2,repToChild(program));
                program.addLabel(lCheckZ2, anchorCheck);

                Op jzEqual = new OpJumpZero(z2, targetLabel,repToChild(program));
                List<Op> jzEqExt = jzEqual.expand(1, program);

                Op anchorNotEq = new OpNeutral(z2,lNotEqual,repToChild(program));
                program.addLabel(lNotEqual, anchorNotEq);

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
                break;
            }
        }
        return ops;
    }



    @Override
    public String getRepresentation()
    {
        return String.format("if %s = %s GOTO %s", getVariable().getRepresentation(), comparableVariable.getRepresentation(), jEConstantLabel.getLabelRepresentation());
    }
    @Override
    public String getUniqRepresentation()
    {
        String lbl;
        if (getLabel() == null || getLabel().equals(FixedLabel.EMPTY))
            lbl = "";
        else
            lbl = " [" + getLabel().getLabelRepresentation() + "]";
        return String.format("if %s = %s GOTO %s", getVariable().getRepresentation(), comparableVariable.getRepresentation(), jEConstantLabel.getLabelRepresentation()) + getFatherRep() + lbl;
    }
}
