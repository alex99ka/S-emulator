package semulator.impl.api.synthetic;

import semulator.impl.api.basic.OpDecrease;
import semulator.impl.api.basic.OpIncrease;
import semulator.impl.api.basic.OpJumpNotZero;
import semulator.impl.api.basic.OpNeutral;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;
import java.util.ArrayList;
import java.util.List;

public class OpZeroVariable extends AbstractOpBasic  {
    public OpZeroVariable( Variable variable) {
        this(variable, FixedLabel.EMPTY);
    }
    public OpZeroVariable ( Variable variable, String creatorRep) {
        super(OpData.ZERO_VARIABLE, variable, FixedLabel.EMPTY, creatorRep);
    }

    public OpZeroVariable(Variable variable, Label label) {
        super(OpData.ZERO_VARIABLE, variable, label);
    }
    public OpZeroVariable( Variable variable, Label label, String creatorRep) {
        super(OpData.ZERO_VARIABLE, variable, label, creatorRep);
    }

    @Override
    public Label execute(SProgram program)
    {
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Long> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(0L);
        program.AddSnap(vars,vals);
        program.increaseCycleCounter(getCycles());
        return FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public OpZeroVariable myClone() {
        return new OpZeroVariable(getVariable().myClone(), getLabel().myClone());
    }

    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        List<Op> ops = new ArrayList<>();
        switch (extensionLevel) {
            case 0: {
                return List.of(this);
            }

            default: {
                Label skip = program.newUniqueLabel();
                Label loop = program.newUniqueLabel();
                Variable z1 = program.newWorkVar(); //dummy var for skipping if y == 0
                Op inc = new OpIncrease(z1,repToChild(program));
                Op jnz = new OpJumpNotZero(getVariable(), loop,repToChild(program)); //if not zero starts -1 loop else skip
                Op jnzSkip  = new OpJumpNotZero(z1, skip,repToChild(program));
                if (getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(), jnz);
                }
                ops.add(inc);
                ops.add(jnz);
                ops.add(jnzSkip);
                Op dec = new OpDecrease(getVariable(), loop,repToChild(program));
                program.addLabel(loop, dec);
                Op back = new OpJumpNotZero(getVariable(), loop,repToChild(program));
                Op anchor = new OpNeutral(getVariable(), skip,repToChild(program));
                program.addLabel(skip, anchor);
                ops.add(dec);
                ops.add(back);
                ops.add(anchor);
                return ops;
            }
        }
    }


    @Override
    public String getRepresentation() {
        return String.format("%s ← 0", getVariable().getRepresentation());
    }
}
