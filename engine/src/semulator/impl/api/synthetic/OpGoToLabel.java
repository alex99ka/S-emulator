package semulator.impl.api.synthetic;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.OpData;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

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
        return nextLabel;
    }
    //implementation of deep clone
    @Override
    public OpGoToLabel myClone() {
        return new OpGoToLabel(getVariable().myClone(), getLabel().myClone(), nextLabel.myClone());
    }
    @Override
    public String getRepresentation()
    {
        return String.format("GOTO %s", nextLabel.getLabelRepresentation());
    }
}
