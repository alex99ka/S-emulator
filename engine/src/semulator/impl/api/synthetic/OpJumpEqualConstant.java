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

public class OpJumpEqualConstant extends AbstractOpBasic {
    Label JEConstantLabel;
    Long constant;
    public OpJumpEqualConstant( Variable variable, Label JEConstantLabel, Long constantValue) {
        super(OpData.JUMP_EQUAL_CONSTANT, variable);
        this.JEConstantLabel = JEConstantLabel;
        this.constant = constantValue;
    }

    public OpJumpEqualConstant( Variable variable, Label label, Label JEConstantLabel, Long constantValue) {
        super(OpData.JUMP_EQUAL_CONSTANT, variable, label);
        this.JEConstantLabel = JEConstantLabel;
        this.constant = constantValue;
    }

    @Override
    public Label execute(SProgram program) {
        program.increaseCycleCounter(getCycles());
        return program.getVariableValue(getVariable()).equals(constant) ? JEConstantLabel : FixedLabel.EMPTY;
    }
    //implementation of deep clone
    @Override
    public OpJumpEqualConstant myClone() {
        return new OpJumpEqualConstant(getVariable().myClone(), getLabel().myClone(), JEConstantLabel.myClone(), constant);
    }

    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        List<Op> myInstructions = new ArrayList<>();

        switch (extensionLevel) {
            case 0:
                return List.of(this);
            case 1: {
                Variable z1 = program.newWorkVar();
                Variable v = getVariable();
                long k = constant;
                Label target = this.JEConstantLabel;
                Label label1 = program.newUniqueLabel();


                Op instr1 = new OpAssignment(z1, getLabel(), v);
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY))
                    program.addLabel(getLabel(), instr1);
                myInstructions.add(instr1);

                for (int i = 0; i < k; i++) {
                    Op jz = new OpJumpZero(z1, label1);
                    Op dec = new OpDecrease(z1);
                    myInstructions.add(jz);
                    myInstructions.add(dec);
                }

                Op instr4 = new OpJumpNotZero(z1, label1);
                Op instr5 = new OpGoToLabel(v, target);
                Op instr6 = new OpNeutral(v, label1);
                program.addLabel(label1, instr6);
                myInstructions.add(instr4);
                myInstructions.add(instr5);
                myInstructions.add(instr6);

                return myInstructions;

            }
            case 2: {

                Variable z1 = program.newWorkVar();
                Variable v = getVariable();
                long k = constant;
                Label target = JEConstantLabel;
                Label label1 = program.newUniqueLabel();


                Op instr1 = new OpAssignment(z1, getLabel(), v);
                List<Op> assExte1 = instr1.expand(1, program);
                myInstructions.addAll(assExte1);

                for (int i = 0; i < k; i++) {
                    Op instr2 = new OpJumpZero(z1, target);
                    List<Op> re = instr2.expand(1, program);
                    myInstructions.addAll(re);

                    Op dec = new OpDecrease(z1);
                    myInstructions.add(dec);
                }


                Op instr4 = new OpJumpNotZero(z1, label1);
                Op instr5 = new OpGoToLabel(v, target);
                List<Op> gotoExte1 = instr5.expand(1, program);

                Op instr6 = new OpNeutral(v, label1);
                myInstructions.add(instr4);
                myInstructions.addAll(gotoExte1);
                myInstructions.add(instr6);

                return myInstructions;
            }
            default: {
                Variable z1 = program.newWorkVar();
                Variable v = getVariable();
                long k = constant;
                Label target = this.JEConstantLabel;
                Label label1 = program.newUniqueLabel();


                Op instr1 = new OpAssignment(z1, getLabel(), v);
                List<Op> assExte1 = instr1.expand(2, program);
                myInstructions.addAll(assExte1);

                for (int i = 0; i < k; i++) {
                    Op instr2 = new OpJumpZero(z1, target);
                    List<Op> re = instr2.expand(2, program);
                    myInstructions.addAll(re);

                    Op dec = new OpDecrease(z1);
                    myInstructions.add(dec);
                }

                Op instr4 = new OpJumpNotZero(z1, label1);
                Op instr5 = new OpGoToLabel(v, target);
                List<Op> gotoExte1 = instr5.expand(1, program);

                Op instr6 = new OpNeutral(v, label1);
                myInstructions.add(instr4);
                myInstructions.addAll(gotoExte1);
                myInstructions.add(instr6);
                return myInstructions;
            }
        }
    }

    @Override
    public String getRepresentation() {
        return String.format("if %s = %d GOTO %s", getVariable().getRepresentation(), constant, JEConstantLabel.getLabelRepresentation());
    }
}
