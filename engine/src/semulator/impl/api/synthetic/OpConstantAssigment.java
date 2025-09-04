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
import semulator.label.LabelImpl;
import semulator.program.SProgram;
import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpConstantAssigment extends AbstractOpBasic  {
    Long constant;
    public OpConstantAssigment( Variable variable, Long constant) {
        super(OpData.CONSTANT_ASSIGNMENT, variable);
        this.constant = constant;
    }
    public OpConstantAssigment( Variable variable, Long constant, String creatorRep) {
        super(OpData.CONSTANT_ASSIGNMENT, variable, FixedLabel.EMPTY, creatorRep);
        this.constant = constant;
    }

    public OpConstantAssigment(Variable variable, Label label,  Long constant) {
        super(OpData.CONSTANT_ASSIGNMENT, variable, label);
        this.constant = constant;
    }
    public OpConstantAssigment( Variable variable, Label label,  Long constant, String creatorRep) {
        super(OpData.CONSTANT_ASSIGNMENT, variable, label, creatorRep);
        this.constant = constant;
    }

    @Override
    public Label execute(SProgram program) {
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Long> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(constant);
        program.AddSnap(vars,vals);
        program.increaseCycleCounter(getCycles());

        return FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public Op myClone() {
        Op op = new OpConstantAssigment(getVariable().myClone(), getLabel().myClone(), constant);
        op.setExpandIndex(getMyExpandIndex());
        return op;

    }
    @Override
    public String getRepresentation() {
        return String.format("%s ← %d", getVariable().getRepresentation(), constant);
    }

    @Override
    public String getUniqRepresentation() {
        return String.format("%s ← %d", getVariable().getRepresentation(), constant)+ getFather();

    }

    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        List<Op> ops = new ArrayList<>();


        switch (extensionLevel) {
            case 0: {
                return List.of(this);
            }
            case 1: {
                Variable v = getVariable();
                long K = constant;  // הקבוע
                Variable z1 = program.newWorkVar();
                Label loop = program.newUniqueLabel();
                Label after = program.newUniqueLabel();

                Op zeroV = new OpZeroVariable(v, getLabel(),repToChild(program));
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY)) {
                    program.addLabel(getLabel(), zeroV);
                }
                ops.add(zeroV);

                for (long i = 0; i < K; i++) {
                    ops.add(new OpIncrease(z1,repToChild(program)));
                }

                Op jz = new OpJumpZero(z1, after,repToChild(program));
                ops.add(jz);
                // z1->v loop
                Op decZ1 = new OpDecrease(z1, loop,repToChild(program));
                program.addLabel(loop, decZ1);
                ops.add(decZ1);

                ops.add(new OpIncrease(v,repToChild(program)));
                ops.add(new OpJumpNotZero(z1, loop,repToChild(program)));
                //end of loop
                Op end = new OpNeutral(v, after,repToChild(program));
                program.addLabel(after, end);
                ops.add(end);

                return ops;
            }

            default: { // 2 or higher
                Variable v = getVariable();
                long K = constant;
                Variable z1  = program.newWorkVar();
                Label loop = program.newUniqueLabel();
                Label after = program.newUniqueLabel();

                Op zeroV = new OpZeroVariable(v, getLabel(),repToChild(program));
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY)) {
                    program.addLabel(getLabel(), zeroV);
                }
                ops.addAll(zeroV.expand(extensionLevel - 1, program));

                for (long i = 0; i < K; i++) {
                    ops.add(new OpIncrease(z1,repToChild(program)));
                }

                Op jz = new OpJumpZero(z1, after,repToChild(program));
                ops.addAll(jz.expand(extensionLevel - 1, program));

                Op decZ1 = new OpDecrease(z1, loop,repToChild(program));
                program.addLabel(loop, decZ1);
                ops.add(decZ1);

                ops.add(new OpIncrease(v,repToChild(program)));
                ops.add(new OpJumpNotZero(z1, loop,repToChild(program))); // בסיסי

                Op end = new OpNeutral(v, after,repToChild(program));
                program.addLabel(after, end);
                ops.add(end);

                return ops;
            }
        }
    }


}

