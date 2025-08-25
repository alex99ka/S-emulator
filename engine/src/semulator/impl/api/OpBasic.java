package semulator.impl.api;
import semulator.interfaces.basicInsturction;
import semulator.impl.api.OpData;
import semulator.label.LabeImpl;

public abstract class OpBasic implements basicInsturction {

   private final OpData opData;
   private final LabeImpl label;

    @Override
    public LabeImpl getLabel() {
        return label;
    }

    protected OpBasic(OpData opData,  LabeImpl label ) {
        this.opData = opData;
        this.label = label;
    }

    @Override
    public String getName() {
        return opData.getName();
    }

    @Override
    public void execute() {
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
