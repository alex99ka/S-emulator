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
import java.util.List;

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
        List<Op> myInstructions = new ArrayList<>();

        switch (extensionLevel) {
            case 0:
                return List.of(this);
            default: {
                Label label = program.newUniqueLabel();
                Op instr1 = new OpNeutral(getVariable(), getLabel());
                Op instr2 = new OpDecrease(getVariable(), label);
                Op instr3 = new OpJumpNotZero(getVariable(), label);
                myInstructions.add(instr1);
                myInstructions.add(instr2);
                myInstructions.add(instr3);
                return myInstructions;
            }
        }
    }

    @Override
    public String getRepresentation() {
        return String.format("%s ← 0", getVariable().getRepresentation());
    }
}
