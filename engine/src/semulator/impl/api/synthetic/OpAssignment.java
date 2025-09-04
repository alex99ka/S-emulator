package semulator.impl.api.synthetic;

import semulator.impl.api.basic.OpDecrease;
import semulator.impl.api.basic.OpIncrease;
import semulator.impl.api.basic.OpJumpNotZero;
import semulator.impl.api.basic.OpNeutral;
import semulator.impl.api.skeleton.*;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.label.LabelImpl;
import semulator.program.SProgram;
import semulator.variable.Variable;
import semulator.variable.VariableImpl;
import semulator.variable.VariableType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpAssignment extends AbstractOpBasic  {
    Variable outSideVar;
    public OpAssignment( Variable variable, Variable outSideVar) {
        super(OpData.ASSIGNMENT, variable);
        this.outSideVar = outSideVar;
    }
    public OpAssignment( Variable variable, Variable outSideVar, String creatorRep) {
        super(OpData.ASSIGNMENT, variable, FixedLabel.EMPTY, creatorRep);
        this.outSideVar = outSideVar;
    }

    public OpAssignment(Variable variable, Label label,  Variable outSideVar) {
        super(OpData.ASSIGNMENT, variable, label);
        this.outSideVar = outSideVar;
    }
    public OpAssignment( Variable variable, Label label,  Variable outSideVar, String creatorRep) {
        super(OpData.ASSIGNMENT, variable, label, creatorRep);
        this.outSideVar = outSideVar;
    }

    @Override
    public Label execute(SProgram program) {
        Long variableValue = program.getVariableValue(outSideVar);
        ArrayList<Variable> vars = new ArrayList<>();
        ArrayList<Long> vals = new ArrayList<>();
        vars.add(getVariable());
        vals.add(variableValue);
        program.AddSnap(vars,vals);
        program.increaseCycleCounter(getCycles());

        return FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public Op myClone() {
        Op op = new OpAssignment(getVariable().myClone(), getLabel().myClone(), outSideVar.myClone());
        op.setExpandIndex(getMyExpandIndex());
        return op;
    }
    @Override
    public String getRepresentation() {
        return String.format("%s ← %s", getVariable().getRepresentation(), outSideVar.getRepresentation()) ;
    }
    @Override
    public String getUniqRepresentation() {
        return String.format("%s ← %s", getVariable().getRepresentation(), outSideVar.getRepresentation()) + getFather();

    }



    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        List<Op> ops = new ArrayList<>();


        switch (extensionLevel) {
            case 0: {
                return List.of(this);
            }
            case 1: {
                Label label1 = program.newUniqueLabel(); //loop vTag ->z1
                Label label2 = program.newUniqueLabel(); // z1 -> v
                Label label3 = program.newUniqueLabel(); // final

                Variable v    = this.getVariable();   //
                Variable vTag = this.outSideVar;      //
                Variable z1   = program.newWorkVar(); // dummy accumulator ()


                Op zeroV = new OpZeroVariable(v, getLabel(),repToChild(program) ); // clean v
                if (getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(), zeroV);
                }

                // 2) אם vTag==0 → דלג לסוף (label3), אחרת היכנס ללולאה הראשונה
                Op jnzVTagEnter = new OpJumpNotZero(vTag, label1,repToChild(program));
                Op gotoEnd = new OpGoToLabel(program.newWorkVar(), label3,repToChild(program));

                // 3) לולאה 1: כל עוד vTag!=0 → vTag--, z1++
                Op decVTag_l1 = new OpDecrease(vTag, label1,repToChild(program));
                program.addLabel(label1, decVTag_l1);
                Op incZ1      = new OpIncrease(z1,repToChild(program));
                Op backL1     = new OpJumpNotZero(vTag, label1,repToChild(program));

                // increase vtag and v together
                Op decZ1_l2 = new OpDecrease(z1, label2,repToChild(program));
                program.addLabel(label2, decZ1_l2);
                Op incV     = new OpIncrease(v,repToChild(program));
                Op incVTag  = new OpIncrease(vTag,repToChild(program));
                Op backL2   = new OpJumpNotZero(z1, label2,repToChild(program));

                Op endAnchor = new OpNeutral(v, label3,repToChild(program));
                program.addLabel(label3, endAnchor);

                ops.add(zeroV);
                ops.add(jnzVTagEnter);
                ops.add(gotoEnd);
                ops.add(decVTag_l1);
                ops.add(incZ1);
                ops.add(backL1);
                ops.add(decZ1_l2);
                ops.add(incV);
                ops.add(incVTag);
                ops.add(backL2);
                ops.add(endAnchor);
                return ops;
            }

            default: { // דרגה 2+
                Label label1 = program.newUniqueLabel(); //loop vTag ->z1
                Label label2 = program.newUniqueLabel(); // z1 -> v
                Label label3 = program.newUniqueLabel(); // final

                Variable v    = this.getVariable();   //
                Variable vTag = this.outSideVar;      //
                Variable z1   = program.newWorkVar(); // dummy accumulator ()


                Op zeroV = new OpZeroVariable(v, getLabel(),repToChild(program) ); // clean v
                if (getLabel() != null && getLabel() != FixedLabel.EMPTY) {
                    program.addLabel(getLabel(), zeroV);
                }
                ops.addAll(zeroV.expand(1, program));

                // 2) אם vTag==0 → דלג לסוף (label3), אחרת היכנס ללולאה הראשונה
                Op jnzVTagEnter = new OpJumpNotZero(vTag, label1,repToChild(program));
                ops.add(jnzVTagEnter);
                Op gotoEnd = new OpGoToLabel(program.newWorkVar(), label3,repToChild(program));
                ops.addAll(gotoEnd.expand(1, program));

                // 3) לולאה 1: כל עוד vTag!=0 → vTag--, z1++
                Op decVTag_l1 = new OpDecrease(vTag, label1,repToChild(program));
                program.addLabel(label1, decVTag_l1);
                ops.add(decVTag_l1);
                Op incZ1      = new OpIncrease(z1,repToChild(program));
                ops.add(incZ1);
                Op backL1     = new OpJumpNotZero(vTag, label1,repToChild(program));
                ops.add(backL1);

                // increase vtag and v together
                Op decZ1_l2 = new OpDecrease(z1, label2,repToChild(program));
                ops.add(decZ1_l2);
                program.addLabel(label2, decZ1_l2);
                Op incV     = new OpIncrease(v,repToChild(program));
                ops.add(incV);
                Op incVTag  = new OpIncrease(vTag,repToChild(program));
                ops.add(incVTag);
                Op backL2   = new OpJumpNotZero(z1, label2,repToChild(program));
                ops.add(backL2);

                Op endAnchor = new OpNeutral(v, label3,repToChild(program));
                ops.add(endAnchor);
                program.addLabel(label3, endAnchor);


                return ops;
            }
        }
    }



}
