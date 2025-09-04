package semulator.impl.api.basic;

import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;
import java.util.List;

public class OpNeutral extends AbstractOpBasic
{
    public OpNeutral(Variable variable) {
        this( variable,FixedLabel.EMPTY,"");
    }

    public OpNeutral(Variable variable, String creatorRep) {
        this( variable, FixedLabel.EXIT, creatorRep );
    }

    public OpNeutral(Variable variable, Label label) {
        this( variable, label,"");
    }

    public OpNeutral( Variable variable, Label label, String creatorRep) {
        super(OpData.NEUTRAL, variable, label, creatorRep);
    }

    public List<Op> expand(int extensionLevel, SProgram program) {
        return List.of(this);
    }

    @Override
    public Label execute(SProgram program) {
        program.increaseCycleCounter(getCycles());
        return FixedLabel.EMPTY;
    }

    //implementation of deep clone
    @Override
    public Op myClone() {
        Op op = new OpNeutral(getVariable().myClone(), getLabel().myClone());
        op.setExpandIndex(getMyExpandIndex());
        return op;
    }
    @Override
    public String getRepresentation()
    {
        return String.format("%s ← %s", getVariable().getRepresentation(), getVariable().getRepresentation());
    }
    @Override
    public String getUniqRepresentation() {
        String lbl;
        if (getLabel() == null || getLabel().equals(FixedLabel.EMPTY))
            lbl = "";
        else
            lbl = " [" + getLabel().getLabelRepresentation() + "]";
        return String.format("%s ← %s", getVariable().getRepresentation(), getVariable().getRepresentation()) + getFather() + " " + lbl;

    }
}

