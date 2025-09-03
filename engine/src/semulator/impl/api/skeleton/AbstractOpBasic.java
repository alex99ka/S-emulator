package semulator.impl.api.skeleton;
import semulator.program.SProgram;
import semulator.variable.Variable;
import semulator.label.*;

public abstract class AbstractOpBasic implements Op {

    private final OpData opData;
    private final Label label;
    private final Variable variable;
    private final String father;

    public String getFather() {
        return father;
    }

    @Override
    public String repToChild(SProgram program) {
        String rep;
        String lbl;
        if (label == null)
            lbl = "";
        else
            lbl = label.equals(FixedLabel.EMPTY) ? label.getLabelRepresentation():""  ;
        StringBuilder sb = new StringBuilder();
        sb.append(getFatherRep());
        int index = getFatherRep() == null ? program.getOpsIndex(this) :program.getExapndIndex();
        sb.append("<<<");
        sb.append(String.format("#%d (S)[%5s] %S (%d)  ",index ,lbl,getRepresentation(),getCycles()));
        rep = sb.toString();
        return rep;
    }

    //Ctors
    protected AbstractOpBasic(OpData opData, Variable variable) { //allow to create without label
        this(opData, variable, FixedLabel.EMPTY,null);
    }
    protected AbstractOpBasic(OpData opData, Variable variable, String creatorRep) { //allow to create without label
        this(opData, variable, FixedLabel.EMPTY,creatorRep);
    }
    protected AbstractOpBasic(OpData opData, Variable variable, Label label)
    {
        this(opData, variable, label, null);
    }


    protected AbstractOpBasic(OpData opData, Variable variable, Label label, String creatorRep) {
        this.opData = opData;
        this.label = label;
        this.variable = variable;
        this.father = creatorRep == null ? "" : creatorRep;
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
    @Override
     public String getFatherRep() {
        return father;
    }

}
