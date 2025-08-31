package semulator.impl.api.synthetic;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

public class OpJumpEqualVariable extends AbstractOpBasic {

    Variable comparableVariable;
    Label JEConstantLabel;

    public OpJumpEqualVariable(Variable variable,Label JEConstantLabel, Variable comparableVariable) {
        super(OpData.JUMP_EQUAL_VARIABLE, variable);
        this.JEConstantLabel = JEConstantLabel;
        this.comparableVariable = comparableVariable;
    }

    public OpJumpEqualVariable (Variable variable, Label label, Label JEConstantLabel, Variable comparableVariable) {
        super(OpData.JUMP_EQUAL_VARIABLE, variable, label);
        this.JEConstantLabel = JEConstantLabel;
        this.comparableVariable = comparableVariable;
    }

    @Override
    public Label execute(SProgram program) {
        return program.getVariableValue(getVariable()).equals( program.getVariableValue(comparableVariable))? JEConstantLabel : FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public OpJumpEqualVariable myClone() {
        return new OpJumpEqualVariable(getVariable().myClone(), getLabel().myClone(),
                JEConstantLabel.myClone(), comparableVariable.myClone());
    }
    @Override
    public String getRepresentation() {
        return String.format("if %s = %s GOTO %s", getVariable().getRepresentation(), comparableVariable.getRepresentation(), JEConstantLabel.getLabelRepresentation());
    }
}
