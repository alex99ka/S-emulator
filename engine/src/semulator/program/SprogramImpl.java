package semulator.program;

import semulator.impl.api.Op;

import java.util.ArrayList;
import java.util.List;

public class SprogramImpl implements SProgram{
    private final String name;
    private final List<Op> opList;

    public SprogramImpl(String name) {
    this.name = name;
    opList = new ArrayList<>();
    }
    @Override
    public int calculateCycles() {
        return 0;
    }

    @Override
    public int calculateMaxDegree() {
        return 0;
    }

    @Override //   TO-DO
    public boolean validate() {
        return false;
    }

    @Override
    public List<Op> getOps() {
        return opList;
    }

    @Override
    public void addOp(Op op) {
        opList.add(op);
    }

    @Override
    public String getName() {
        return name;
    }
}
