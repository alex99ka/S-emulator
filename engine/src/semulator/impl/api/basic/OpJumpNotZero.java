package semulator.impl.api.basic;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

public class OpJumpNotZero extends AbstractOpBasic {
    private final Label jnzLabel;

    public OpJumpNotZero(Variable variable, Label jnzLabel) {
        this(variable, jnzLabel, FixedLabel.EMPTY);
    }

    public OpJumpNotZero(Variable variable, Label jnzLabel, Label label) {
        super(OpData.JUMP_NOT_ZERO,variable, label );
        this.jnzLabel = jnzLabel;
    }



    @Override
    public Label execute(SProgram program) {
        Long variableValue = program.getVariableValue(getVariable());
        if (variableValue != 0) {
            return jnzLabel;
        }
        return FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public OpJumpNotZero myClone() {
        return new OpJumpNotZero(getVariable().myClone(), jnzLabel.myClone(), getLabel().myClone());
    }
    @Override
    public String getRepresentation()
    {
        return String.format("IF %s!=0 GOTO %s", getVariable().getRepresentation(), jnzLabel.getLabelRepresentation());
    }
}
