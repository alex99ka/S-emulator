package semulator.impl.api.skeleton;
import semulator.variable.Variable;
import semulator.label.*;

public abstract class AbstractOpBasic implements Op {

    private final OpData opData;
    private final Label label;
    private final Variable variable;



    //Ctors
    protected AbstractOpBasic(OpData opData, Variable variable) { //allow to create without label
        this(opData, variable, FixedLabel.EMPTY);
    }


    public AbstractOpBasic(OpData opData, Variable variable, Label label) {
        this.opData = opData;
        this.label = label;
        this.variable = variable;
    }

    @Override
    public Variable getVariable() {
        return variable;
    }

    @Override
    public String getType() {
        return opData.getType().equals(OpType.BASIC)? "BASIC" : "SYNTHETIC";
    }

    @Override
    public Label getLabel() {
        return label;
    }


    @Override
    public String getName() {
        return opData.getName();
    }

    public int getDegree() {
        return opData.getDegree();
    }

    @Override
    public int getCycles() {
        return opData.getCycles();
    }

    @Override
    public String toString() {
        return getName();
    }

    //implement a deep clone method
    @Override
    public abstract Op myClone();

    @Override
    public String getRepresentation()
    {
     return " ";
    }


}
