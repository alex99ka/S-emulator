package semulator.impl.api.basic;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class OpIncrease extends AbstractOpBasic {

    public OpIncrease(Variable variable) {
        this( variable,"");
    }
    public OpIncrease(Variable variable, String creatorRep) {
        this( variable,FixedLabel.EMPTY,creatorRep);
    }
    public OpIncrease(Variable variable, Label label) {
        this( variable, label,"");
    }
    public OpIncrease(Variable variable, Label label,String creatorRep) {
        super(OpData.INCREASE,variable,label  ,creatorRep);
    }

    @Override
    public Label execute(SProgram program) {

        Integer variableValue = program.getVariableValue(getVariable());
        variableValue++;
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Integer> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(variableValue);

        program.AddSnap(vars,vals);
        program.increaseCycleCounter(getCycles());

        return FixedLabel.EMPTY;
    }
    public List<Op> expand(int ignoredExtensionLevel, SProgram ignoredProgram)
    {
        return List.of(this);
    }
    //implementation of deep clone
    @Override
    public Op myClone() {
        Op op = new OpIncrease(getVariable().myClone(), getLabel().myClone());
        op.setExpandIndex(getMyExpandIndex());
        return op;
    }
    @Override
    public String getRepresentation() {
        return String.format("%s ← %s + 1", getVariable().getRepresentation(), getVariable().getRepresentation()) ;
    }
    @Override
    public String getUniqRepresentation() {
        String lbl;
        if (getLabel() == null || getLabel().equals(FixedLabel.EMPTY))
            lbl = "";
        else
            lbl = " [" + getLabel().getLabelRepresentation() + "]";
        return String.format("%s ← %s + 1", getVariable().getRepresentation(), getVariable().getRepresentation()) + getFatherRep() + lbl;

    }

}
