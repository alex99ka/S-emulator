package semulator.impl.api;
import com.sun.jdi.Value;
import semulator.execution.ExecutionContext;
import semulator.variable.Variable;
import semulator.label.*;

public abstract class AbstractOpBasic implements Op {

   private final OpData opData;
   private final Label label;
   private final Variable variable;


   //Ctors
    protected AbstractOpBasic(OpData opData,Variable variable) { //allow to create without label
       this(opData, FixedLabel.EMPTY,variable);
   }

    @Override
    public Variable getVariable() {
        return variable;
    }

    @Override
    public String getType() {
        return variable.getRepresntation();
    }

    protected AbstractOpBasic(OpData opData, Label label, Variable variable ) {
        this.opData = opData;
        this.label = label;
        this.variable = variable;
    }

    @Override
    public Label getLabel() {
        return label;
    }


    @Override
    public String getName() {
        return opData.getName();
    }

    @Override
    public int getcycles() {
        return opData.getCycles();
    }

    @Override
    public String toString() {
        return getName();
    }
}
