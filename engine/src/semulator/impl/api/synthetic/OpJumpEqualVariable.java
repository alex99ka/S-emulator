package semulator.impl.api.synthetic;
import semulator.impl.api.basic.OpDecrease;
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

public class OpJumpEqualVariable extends AbstractOpBasic {

    Variable comparableVariable;
    Label JEConstantLabel;

    public OpJumpEqualVariable(Variable variable,Label JEConstantLabel, Variable comparableVariable) {
        super(OpData.JUMP_EQUAL_VARIABLE, variable);
        this.JEConstantLabel = JEConstantLabel;
        this.comparableVariable = comparableVariable;
    }

    public OpJumpEqualVariable (Variable variable, Label label, Label JEConstantLabel, Variable comparableVariable) {
        super(OpData.JUMP_EQUAL_VARIABLE, variable, label);
        this.JEConstantLabel = JEConstantLabel;
        this.comparableVariable = comparableVariable;
    }

    @Override
    public Label execute(SProgram program) {
        program.increaseCycleCounter(getCycles());
        return program.getVariableValue(getVariable()).equals( program.getVariableValue(comparableVariable))? JEConstantLabel : FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public OpJumpEqualVariable myClone() {
        return new OpJumpEqualVariable(getVariable().myClone(), getLabel().myClone(),
                JEConstantLabel.myClone(), comparableVariable.myClone());
    }

    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        List<Op> myInstructions = new ArrayList<>();

        switch (extensionLevel) {
            case 0:
                return List.of(this);
            case 1: {
                Label label1 = program.newUniqueLabel();
                Label label2 = program.newUniqueLabel();
                Label label3 = program.newUniqueLabel();
                Variable z1 = program.newWorkVar();
                Variable z2 = program.newWorkVar();

                Label jnzLabel = JEConstantLabel;
                Variable vTag = comparableVariable;
                Variable v = this.getVariable();


                Op instr2 = new OpAssignment(z1, getLabel(),v);
                if(getLabel() != null && !getLabel().equals(FixedLabel.EMPTY))
                    program.addLabel(getLabel(), instr2);
                Op instr3 = new OpAssignment(z2, vTag);
                Op instr4 = new OpJumpZero(z1, label3, label2);
                Op instr5 = new OpJumpZero(z2, label3);
                Op instr6 = new OpDecrease(z1);
                Op instr7 = new OpDecrease(z2);
                Op instr8 = new OpGoToLabel(z1, jnzLabel);
                Op instr9 = new OpJumpZero(z2, label1);
                Op instr10 = new OpNeutral(v);


                myInstructions.add(instr2);
                myInstructions.add(instr3);
                myInstructions.add(instr4);
                myInstructions.add(instr5);
                myInstructions.add(instr6);
                myInstructions.add(instr7);
                myInstructions.add(instr8);
                myInstructions.add(instr9);
                myInstructions.add(instr10);

                return myInstructions;
            }
            case 2: {
                Label label1 = program.newUniqueLabel();
                Label label2 = program.newUniqueLabel();
                Label label3 = program.newUniqueLabel();
                Variable z1 = program.newWorkVar();
                Variable z2 = program.newWorkVar();

                Label jnzLabel = JEConstantLabel;
                Variable vTag = comparableVariable;
                Variable v = this.getVariable();


                Op instr2 = new OpAssignment(z1, getLabel(),v);
                List<Op> assignExt1 = instr2.expand(1, program);

                Op instr3 = new OpAssignment(z2, vTag);
                List<Op> assignExt2 = instr3.expand(1, program);

                Op instr4 = new OpJumpZero(z1, label3, label2);
                List<Op> jumpZerExt1 = instr4.expand(1, program);

                Op instr5 = new OpJumpZero(z2, label3);
                List<Op> jumpZerExt2 = instr5.expand(1, program);

                Op instr6 = new OpDecrease(z1);
                Op instr7 = new OpDecrease(z2);
                Op instr8 = new OpGoToLabel(z1, jnzLabel);
                List<Op> gotoExt1 = instr8.expand(1, program);

                Op instr9 = new OpJumpZero(z2, label1);
                List<Op> jumpZerExt3 = instr9.expand(1, program);

                Op instr10 = new OpNeutral(v);


                myInstructions.addAll(assignExt1);
                myInstructions.addAll(assignExt2);
                myInstructions.addAll(jumpZerExt1);
                myInstructions.addAll(jumpZerExt2);
                myInstructions.add(instr6);
                myInstructions.add(instr7);
                myInstructions.addAll(gotoExt1);
                myInstructions.addAll(jumpZerExt3);
                myInstructions.add(instr10);

                return myInstructions;
            }
            default: {
                Label label1 = program.newUniqueLabel();
                Label label2 = program.newUniqueLabel();
                Label label3 = program.newUniqueLabel();
                Variable z1 = program.newWorkVar();
                Variable z2 = program.newWorkVar();

                Label jnzLabel = JEConstantLabel;
                Variable vTag = comparableVariable;
                Variable v = this.getVariable();


                Op instr2 = new OpAssignment(z1, getLabel(),v);
                List<Op> assignExt1 = instr2.expand(1, program);

                Op instr3 = new OpAssignment(z2, vTag);
                List<Op> assignExt2 = instr3.expand(1, program);

                Op instr4 = new OpJumpZero(z1, label3, label2);
                List<Op> jumpZerExt1 = instr4.expand(1, program);

                Op instr5 = new OpJumpZero(z2, label3);
                List<Op> jumpZerExt2 = instr5.expand(1, program);

                Op instr6 = new OpDecrease(z1);
                Op instr7 = new OpDecrease(z2);
                Op instr8 = new OpGoToLabel(z1, jnzLabel);
                List<Op> gotoExt1 = instr8.expand(1, program);

                Op instr9 = new OpJumpZero(z2, label1);
                List<Op> jumpZerExt3 = instr9.expand(2, program);

                Op instr10 = new OpNeutral(v);

                myInstructions.addAll(assignExt1);
                myInstructions.addAll(assignExt2);
                myInstructions.addAll(jumpZerExt1);
                myInstructions.addAll(jumpZerExt2);
                myInstructions.add(instr6);
                myInstructions.add(instr7);
                myInstructions.addAll(gotoExt1);
                myInstructions.addAll(jumpZerExt3);
                myInstructions.add(instr10);

                return myInstructions;
            }
        }
    }

    @Override
    public String getRepresentation() {
        return String.format("if %s = %s GOTO %s", getVariable().getRepresentation(), comparableVariable.getRepresentation(), JEConstantLabel.getLabelRepresentation());
    }
}
