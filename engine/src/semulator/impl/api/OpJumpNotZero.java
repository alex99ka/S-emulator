package semulator.impl.api;


import semulator.execution.ExecutionContextImpl;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

public class OpJumpNotZero extends AbstractOpBasic {
    public OpJumpNotZero(Variable variable, Label jnzLabel) {
        this(variable, jnzLabel, FixedLabel.EMPTY);
    }

    public OpJumpNotZero(Variable variable, Label jnzLabel, Label label) {
        super(OpData.JUMP_NOT_ZERO,variable, label );
        this.jnzLabel = jnzLabel;
    }

    private final Label jnzLabel;


    @Override
    public Label execute(ExecutionContextImpl executable) {
        long variableValue = executable.getVariableValue(getVariable());
        if (variableValue != 0) {
            return jnzLabel;
        }
        return FixedLabel.EMPTY;
    }
}
