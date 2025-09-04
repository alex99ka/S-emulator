package semulator.impl.api.synthetic;
import semulator.impl.api.basic.OpIncrease;
import semulator.impl.api.basic.OpJumpNotZero;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.impl.api.skeleton.OpData;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class OpGoToLabel extends AbstractOpBasic {
    Label nextLabel;
    public OpGoToLabel(Variable variable, Label nextLabel) {
        super(OpData.GOTO_LABEL,variable);
        this.nextLabel = nextLabel;
    }
    public OpGoToLabel(Variable variable, Label nextLabel,String creatorRep) {
        super(OpData.GOTO_LABEL, variable, FixedLabel.EMPTY, creatorRep);
        this.nextLabel = nextLabel;
    }

    public OpGoToLabel(Variable variable, Label label,Label nextLabel) {
        super(OpData.GOTO_LABEL, variable, label);
        this.nextLabel = nextLabel;
    }
    public OpGoToLabel( Variable variable, Label label, Label nextLabel,String creatorRep) {
        super(OpData.GOTO_LABEL, variable, label, creatorRep);
        this.nextLabel = nextLabel;
    }

    @Override
    public Label execute(SProgram program) {
        program.increaseCycleCounter(getCycles());
        return nextLabel;
    }
    //implementation of deep clone
    @Override
    public Op myClone() {
        Op op;
        if(nextLabel.equals(FixedLabel.EXIT))
            op= new OpGoToLabel(getVariable().myClone(), getLabel().myClone(), FixedLabel.EXIT);
        else if(nextLabel.equals(FixedLabel.EMPTY))
            op = new OpGoToLabel(getVariable().myClone(), getLabel().myClone(), FixedLabel.EMPTY);
        else
              op = new OpGoToLabel(getVariable().myClone(), getLabel().myClone(), nextLabel.myClone());
        op.setExpandIndex(getMyExpandIndex());
        return op;

    }

    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        List<Op> ops = new ArrayList<>();

        switch (extensionLevel) {
            case 0: {
                return List.of(this);
            }
            default: {
                Variable tmp = program.newWorkVar();
                Op inc = new OpIncrease(tmp, getLabel(),repToChild(program));
                if (getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(), inc);
                }

                Label target = nextLabel;
                Op jnz = new OpJumpNotZero(tmp, target,repToChild(program));

                ops.add(inc);
                ops.add(jnz);
                return ops;
            }
        }
    }


    @Override
    public String getRepresentation()
    {
        return String.format("GOTO %s", nextLabel.getLabelRepresentation()) ;
    }
    @Override
    public String getUniqRepresentation() {
        return String.format("GOTO %s", nextLabel.getLabelRepresentation()) + getFather();
    }

    public List<Op> expand() {
        List<Op> expanded = new ArrayList<>();
        // 1. Increase dummy variable by 1 (use the synthetic’s own label for this instruction)
        expanded.add(new OpIncrease(getVariable().myClone(), getLabel().myClone()));
        // 2. JumpNotZero to the target label (unconditional jump since var != 0 after increase)
        expanded.add(new OpJumpNotZero(getVariable().myClone(), nextLabel.myClone()));
        return expanded;
    }
}
