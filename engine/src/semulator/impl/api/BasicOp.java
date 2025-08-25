package semulator.impl.api;
import semulator.interfaces.basicInsturction;

public abstract class BasicOp implements basicInsturction {
   protected final String name;
   protected final int cycles;

    protected BasicOp(String name, int cycles) {
        this.name = name;
        this.cycles = cycles;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void execute() {
    }

    @Override
    public int getcycles() {
        return 0;
    }

    @Override
    public String toString() {
        return getName();
    }
}
