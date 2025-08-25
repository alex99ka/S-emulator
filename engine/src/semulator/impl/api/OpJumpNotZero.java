package semulator.impl.api;


import semulator.variable.Variable;

public class OpJumpNotZero extends AbstractOpBasic {
    public OpJumpNotZero(Variable variable) {
        super(OpData.JUMP_NOT_ZERO,variable);
    }
}
