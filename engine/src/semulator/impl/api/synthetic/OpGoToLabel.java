package semulator.impl.api.synthetic;
import semulator.impl.api.basic.OpIncrease;
import semulator.impl.api.basic.OpJumpNotZero;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class OpGoToLabel extends AbstractOpBasic {
    Label nextLabel;
    public OpGoToLabel(Variable variable, Label nextLabel) {
        super(OpData.GOTO_LABEL,variable);
        this.nextLabel = nextLabel;
    }

    public OpGoToLabel(Variable variable, Label label,Label nextLabel) {
        super(OpData.GOTO_LABEL, variable, label);
        this.nextLabel = nextLabel;
    }

    @Override
    public Label execute(SProgram program) {
        program.increaseCycleCounter(getCycles());
        return nextLabel;
    }
    //implementation of deep clone
    @Override
    public OpGoToLabel myClone() {
        return new OpGoToLabel(getVariable().myClone(), getLabel().myClone(), nextLabel.myClone());
    }

    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        List<Op> myInstructions = new ArrayList<>();

        switch (extensionLevel) {
            case 0:
                return List.of(this);
            default: {
                Variable tempVar1 = program.newWorkVar();
                Op instr2 = new OpIncrease(tempVar1, getLabel());
                if (getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(), instr2);
                }
                Op instr3 = new OpJumpNotZero(tempVar1, nextLabel);
                myInstructions.add(instr2);
                myInstructions.add(instr3);
                return myInstructions;
            }
        }
    }

    @Override
    public String getRepresentation()
    {
        return String.format("GOTO %s", nextLabel.getLabelRepresentation());
    }

    public List<Op> expand() {
        List<Op> expanded = new ArrayList<>();
        // 1. Increase dummy variable by 1 (use the synthetic’s own label for this instruction)
        expanded.add(new OpIncrease(getVariable().myClone(), getLabel().myClone()));
        // 2. JumpNotZero to the target label (unconditional jump since var != 0 after increase)
        expanded.add(new OpJumpNotZero(getVariable().myClone(), nextLabel.myClone()));
        return expanded;
    }
}
