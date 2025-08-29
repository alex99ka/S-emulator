package semulator.impl.api.synthetic;

import semulator.execution.ExecutionContextImpl;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
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
    public Label execute(ExecutionContextImpl executable) {
        return executable.getVariableValue(getVariable()).equals( executable.getVariableValue(comparableVariable))? JEConstantLabel : FixedLabel.EMPTY;
    }
}
