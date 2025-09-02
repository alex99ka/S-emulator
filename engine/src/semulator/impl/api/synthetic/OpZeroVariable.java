package semulator.impl.api.synthetic;

import semulator.impl.api.basic.OpDecrease;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OpZeroVariable extends AbstractOpBasic  {
    public OpZeroVariable( Variable variable) {
        this(variable, FixedLabel.EMPTY);
    }

    public OpZeroVariable(Variable variable, Label label) {
        super(OpData.ZERO_VARIABLE, variable, label);
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
                Op jnz = new OpJumpNotZero(getVariable(), skip);
                if (getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(), jnz);
                }
                ops.add(jnz);
                // תווית לולאה – מוציאים 1 בכל חזרה
                Label loop = program.newUniqueLabel();
                Op dec = new OpDecrease(getVariable(), loop);
                program.addLabel(loop, dec);
                // חזרה עד האפס
                Op back = new OpJumpNotZero(getVariable(), loop);
                // תווית עצירה – מגיעים לכאן אם skip
                Op anchor = new OpNeutral(getVariable(), skip);
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
