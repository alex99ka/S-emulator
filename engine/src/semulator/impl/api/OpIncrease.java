package semulator.impl.api;

import semulator.variable.Variable;

public class OpIncrease extends AbstractOpBasic {
    public OpIncrease(Variable variable) {
        super(OpData.INCREASE,variable);
    }
}
