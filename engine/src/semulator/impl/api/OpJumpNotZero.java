package semulator.impl.api;


import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.variable.Variable;

public class OpJumpNotZero extends AbstractOpBasic {
    public OpJumpNotZero(Variable variable, Label jnzLabel) {
        this(variable, jnzLabel, FixedLabel.EMPTY);
    }

    public OpJumpNotZero(Variable variable, Label jnzLabel, Label label) {
        super(OpData.JUMP_NOT_ZERO, label,variable );
        this.jnzLabel = jnzLabel;
    }

    private final Label jnzLabel;


    @Override
    public Label execute(ArrayList<Map<Variable, Long>> snapshots) {
        long variableValue = snapshots.getVariableValue(getVariable());

        if (variableValue != 0) {
            return jnzLabel;
        }
        return FixedLabel.EMPTY;
    }
}
