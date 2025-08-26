package semulator.program;

import semulator.impl.api.Op;

import java.util.List;

public interface SProgram {
    String getName();
    void addOp(Op instruction);
    List<Op> getOps();

    boolean validate();
    int calculateMaxDegree();
    int calculateCycles();
}
