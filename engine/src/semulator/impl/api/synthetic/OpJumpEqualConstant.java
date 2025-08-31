package semulator.impl.api.synthetic;

import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

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
    public String getRepresentation() {
        return String.format("if %s = %d GOTO %s", getVariable().getRepresentation(), constant, JEConstantLabel.getLabelRepresentation());
    }
}
