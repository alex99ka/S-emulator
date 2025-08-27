package semulator.impl.api;

import semulator.execution.ExecutionContextImpl;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

public class OpJumpEqualConstant extends AbstractOpBasic{
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
    public Label execute(ExecutionContextImpl executable) {
        return executable.getVariableValue(getVariable()).equals(constant) ? JEConstantLabel : FixedLabel.EMPTY;
    }
}
