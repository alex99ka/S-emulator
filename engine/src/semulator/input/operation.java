package semulator.input;

import semulator.impl.api.basic.OpDecrease;
import semulator.impl.api.basic.OpIncrease;
import semulator.impl.api.basic.OpJumpNotZero;
import semulator.impl.api.basic.OpNeutral;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.label.FixedLabel;
import semulator.label.LabelImpl;
import semulator.program.SProgram;
import semulator.variable.Variable;
import semulator.variable.VariableImpl;
import semulator.variable.VariableType;

import java.util.ArrayList;
import java.util.List;

public class operation {

    SProgram p;

    public operation(SProgram program) {
        this.p = program;
    }

    public void debugging()
    {
        Variable x1 = new VariableImpl(VariableType.INPUT, 1); // all vars should be 0 initialized
        Variable z1 = new VariableImpl(VariableType.WORK, 1);
        List<Variable> vars= new ArrayList<>();
        vars.add(x1);
        vars.add(z1);
        vars.add(Variable.RESULT);


        LabelImpl l1 = new LabelImpl(1);
        LabelImpl l2 = new LabelImpl(1);
        AbstractOpBasic increase = new OpIncrease(x1, l1);
        AbstractOpBasic decrease = new OpDecrease(z1, l2);
        AbstractOpBasic noop = new OpNeutral(Variable.RESULT);
        AbstractOpBasic jnz = new OpJumpNotZero(x1, l2, FixedLabel.EXIT);


        //p.setVars(vars);
        p.addOp(increase);
        p.addOp(noop);
        p.addOp(decrease);
        p.addOp(increase);
        //p.addOp(jnz);
    }
}
