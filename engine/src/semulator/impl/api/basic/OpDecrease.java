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

public class OpDecrease extends AbstractOpBasic {
    @Override
    public Label execute(SProgram program) {
        Integer variableValue = program.getVariableValue(getVariable());
        variableValue = Math.max(0, variableValue - 1);
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Integer> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(variableValue);

        program.AddSnap(vars,vals);
        program.increaseCycleCounter(getCycles());

        return FixedLabel.EMPTY;
    }


    public OpDecrease(Variable variable) {
        this( variable,"");
    }
    public OpDecrease(Variable variable, String creatorRep) {
        this( variable,FixedLabel.EMPTY,creatorRep);
    }
    public OpDecrease(Variable variable, Label label) {
        this( variable, label,"");
    }
    public OpDecrease(Variable variable, Label label,String creatorRep) {
        super(OpData.DECREASE,variable,label  ,creatorRep);
    }
    //implementation of deep clone
    @Override
    public Op myClone() {

        Op op = new OpDecrease(getVariable().myClone(),getLabel().myClone());
        op.setExpandIndex(getMyExpandIndex());
        return op;
    }

    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        return List.of(this);
    }

    @Override
    public String getRepresentation() {
        return String.format("%s ← %s - 1", getVariable().getRepresentation(), getVariable().getRepresentation()) ;
    }
    @Override
    public String getUniqRepresentation() {
        String lbl;
        if (getLabel() == null || getLabel().equals(FixedLabel.EMPTY))
            lbl = "";
        else
            lbl = " [" + getLabel().getLabelRepresentation() + "]";
        return String.format("%s ← %s - 1", getVariable().getRepresentation(), getVariable().getRepresentation()) + getFatherRep() + lbl;
    }



    public List<Op> expand(int ignoredExtensionLevel, SProgram ignoredProgram, Variable ignoredPapa)
    {
        return List.of(this);
    }

}
