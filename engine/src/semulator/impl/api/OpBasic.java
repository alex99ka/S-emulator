package semulator.impl.api;
import semulator.interfaces.basicInsturction;
import semulator.impl.api.OpData;

public abstract class OpBasic implements basicInsturction {

   private final OpData opData;

    protected OpBasic(OpData opData) {
        this.opData = opData;
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
