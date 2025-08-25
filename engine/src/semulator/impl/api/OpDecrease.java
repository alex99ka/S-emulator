package semulator.impl.api;

import semulator.variable.Variable;

public class OpDecrease extends AbstractOpBasic {
    public OpDecrease(Variable variable) {
        super(OpData.DECREASE, variable);
    }
}
