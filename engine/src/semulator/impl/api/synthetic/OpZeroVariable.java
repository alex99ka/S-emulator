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
        this( variable, label,"");
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
    public Op myClone() {

        Op op =  new OpZeroVariable(getVariable().myClone(), getLabel().myClone());
        op.setExpandIndex(getMyExpandIndex());
        return op;
    }

    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        switch (extensionLevel) {
            case 0 : {
                return List.of(this);
            }
            default : {
                // as long as  (var != 0) -> var-- loop
                List<Op> ops = new ArrayList<>();

                final Variable v = getVariable();

                final Label lBody = program.newUniqueLabel();
                final Label lEnd  = program.newUniqueLabel();

                // אם v != 0 → קפוץ לגוף; אחרת נפילה ל-NEXT (סיום)
                Op jnz =new OpJumpNotZero(v, getLabel(),lBody,repToChild(program));
                ops.add(jnz);
                if(getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(), jnz);
                }
                Variable dummy = program.newWorkVar();
                OpIncrease inc = new OpIncrease(dummy,repToChild(program));
                ops.add(inc); // פעולה ניטרלית לצורך ספירת מחזורים
                Op jnzToEnd = new OpJumpNotZero(dummy, lEnd,repToChild(program));
                ops.add(jnzToEnd);
                // גוף הלולאה
                Op dec = new OpDecrease(v,lBody,repToChild(program));
                program.addLabel(lBody, dec);
                ops.add(dec);
                ops.add(new OpJumpNotZero(v, lBody,repToChild(program)));

                Op endAnchor = new OpNeutral(v, lEnd,repToChild(program));
                program.addLabel(lEnd, endAnchor);
                ops.add(endAnchor);

                return ops;
            }
        }
    }


    @Override
    public String getRepresentation() {
        return String.format("%s ← 0", getVariable().getRepresentation()) ;
    }
    @Override
    public String getUniqRepresentation() {
        String lbl;
        if (getLabel() == null || getLabel().equals(FixedLabel.EMPTY))
            lbl = "";
        else
            lbl = " [" + getLabel().getLabelRepresentation() + "]";
        return String.format("%s ← 0", getVariable().getRepresentation()) + getFatherRep() +lbl;
    }
}
