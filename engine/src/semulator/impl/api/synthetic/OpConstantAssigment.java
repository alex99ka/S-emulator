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

    public OpConstantAssigment(Variable variable, Label label,  Long constant) {
        super(OpData.CONSTANT_ASSIGNMENT, variable, label);
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
    public OpConstantAssigment myClone() {
        return new OpConstantAssigment(getVariable().myClone(), getLabel().myClone(), constant);
    }
    @Override
    public String getRepresentation() {
        return String.format("%s ← %d", getVariable().getRepresentation(), constant);
    }

    @Override
    public List<Op> expand(int extensionLevel, SProgram program) {
        List<Op> myInstructions = new ArrayList<>();

        switch (extensionLevel) {
            case 0:
                return List.of(this);
            case 1: {
                Variable v = getVariable();
                Variable z1 = program.newWorkVar();
                program.AddSnap(new ArrayList<>(Arrays.asList(z1)), new ArrayList<>(Arrays.asList(constant)));
                Label loopLabel = program.newUniqueLabel();
                Label afterLoopLabel = program.newUniqueLabel();
                Op instr1 = new OpZeroVariable(v, getLabel());
                if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY))
                    program.addLabel(getLabel(), instr1);
                Op instr2 = new OpJumpZero(z1, afterLoopLabel);
                Op instr3 = new OpIncrease(v, loopLabel);
                program.addLabel(loopLabel, instr3);
                Op instr4 = new OpDecrease(z1);
                Op instr5 = new OpJumpNotZero(z1, loopLabel);
                Op instr6 = new OpNeutral(v, afterLoopLabel);
                program.addLabel(afterLoopLabel, instr6);
                myInstructions.addAll(Arrays.asList(instr1, instr2, instr3, instr4, instr5, instr6));
                break;
                }

                default: {
                    Variable v = getVariable();
                    Variable z1 = program.newWorkVar();
                    Label loopLabel = program.newUniqueLabel();
                    Label afterLoopLabel = program.newUniqueLabel();
                    List<Op> zeroVariableExpanded = new OpZeroVariable(v, getLabel()).expand(extensionLevel - 1, program);
                    Op instr1 = new OpNeutral(v, getLabel());
                    if (getLabel() != null && !getLabel().equals(FixedLabel.EMPTY))
                        program.addLabel(getLabel(), instr1);
                    List<Op> JumpZeroExpanded = (ArrayList<Op>) new OpJumpZero(z1, afterLoopLabel).expand
                    (extensionLevel - 1, program);
                    Op instr3 = new OpIncrease(v, loopLabel);
                    program.addLabel(loopLabel, instr3);
                    Op instr4 = new OpDecrease(z1);
                    Op instr5 = new OpJumpNotZero(z1, loopLabel);
                    Op instr6 = new OpNeutral(v, afterLoopLabel);
                    program.addLabel(afterLoopLabel, instr6);
                    myInstructions.add(instr1);
                    myInstructions.addAll(zeroVariableExpanded);
                    myInstructions.addAll(JumpZeroExpanded);
                    myInstructions.add(instr3);
                    myInstructions.add(instr4);
                    myInstructions.add(instr5);
                    myInstructions.add(instr6);
                    break;
                }
            }
            return myInstructions;
            }

}

