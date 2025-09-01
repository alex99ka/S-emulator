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
        List<Op> myInstructions = new ArrayList<>();

        switch (extensionLevel) {
            case 0:
                return List.of(this);
            case 1: {
                Label label1 = program.newUniqueLabel();

                Op instr1 = new OpJumpNotZero(getVariable(), label1, getLabel());
                if(getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(),instr1);
                }
                Op instr2 = new OpGoToLabel(getVariable(), JZlabel);
                Op instr3 = new OpNeutral(getVariable(), label1);

                myInstructions.add(instr1);
                myInstructions.add(instr2);
                myInstructions.add(instr3);
                return myInstructions;
            }
            default: {
                Label label1 = program.newUniqueLabel();

                Op instr1 = new OpJumpNotZero(getVariable(), label1, getLabel());
                if(getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(),instr1);
                }
                Op instr2 = new OpGoToLabel(getVariable(), JZlabel);
                List<Op> instr = instr2.expand(1, program);

                Op instr3 = new OpNeutral(getVariable(), label1);

                myInstructions.add(instr1);
                myInstructions.addAll(instr);

                myInstructions.add(instr3);

                return myInstructions;
            }
        }
    }

    @Override
    public String getRepresentation() {
        return String.format("if %s = 0 GOTO %s", getVariable().getRepresentation(), JZlabel.getLabelRepresentation());
    }
}
