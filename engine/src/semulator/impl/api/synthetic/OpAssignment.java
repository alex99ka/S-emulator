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

    public OpAssignment(Variable variable, Label label,  Variable outSideVar) {
        super(OpData.ASSIGNMENT, variable, label);
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
    public OpAssignment myClone() {
        return new OpAssignment(getVariable().myClone(), getLabel().myClone(), outSideVar.myClone());
    }
    @Override
    public String getRepresentation() {
        return String.format("%s ← %s", getVariable().getRepresentation(), outSideVar.getRepresentation());
    }
    @Override
    public List<Op> expand(int extensionLevel,SProgram program) {
        List<Op> myInstructions = new ArrayList<>();

        switch (extensionLevel) {
            case 0:
                return List.of(this);
            case 1: {
                Label label1 = program.newUniqueLabel();
                Label label2 = program.newUniqueLabel();
                Label label3 = program.newUniqueLabel();
                Variable v = this.getVariable();
                Variable vTag = outSideVar;
                Variable z1 = program.newWorkVar();
                Op instr2 = new OpZeroVariable(v,getLabel());
                if(getLabel() != null && getLabel() != FixedLabel.EMPTY){
                    program.addLabel(getLabel(), instr2);
                }
                Op instr3 = new OpJumpNotZero(vTag,label1);
                Op instr4 = new OpGoToLabel(vTag, label3);

                Op instr5 = new OpDecrease( vTag,label1);
                program.addLabel(label1, instr5 );
                Op instr6 = new OpIncrease(z1);
                Op instr7 = new OpJumpNotZero(vTag,label1);
                Op instr8 = new OpDecrease(z1,label2);
                program.addLabel(label2, instr8 );
                Op instr9 = new OpIncrease(v);
                Op instr10 = new OpIncrease(vTag);
                Op instr11 = new OpJumpNotZero(z1,label2);
                Op instr12 = new OpNeutral(v, label3);
                program.addLabel(label3, instr12 );

                myInstructions.add(instr2);
                myInstructions.add(instr3);
                myInstructions.add(instr4);
                myInstructions.add(instr5);
                myInstructions.add(instr6);
                myInstructions.add(instr7);
                myInstructions.add(instr8);
                myInstructions.add(instr9);
                myInstructions.add(instr10);
                myInstructions.add(instr11);
                myInstructions.add(instr12);
                return myInstructions;
            }
            default: {
                Label label1 = program.newUniqueLabel();
                Label label2 = program.newUniqueLabel();
                Label label3 = program.newUniqueLabel();
                Variable v = this.getVariable();
                Variable vTag = outSideVar;
                Variable z1 = program.newWorkVar();
                //Op instr1 = new OpNeutral(v, getLabel());
                Op instr2 = new OpZeroVariable(v,getLabel());
                if(getLabel() != null && getLabel() != FixedLabel.EMPTY){
                    program.addLabel(getLabel(), instr2);
                }
                List<Op> zeroExtend = instr2.expand(1, program);

                Op instr3 = new OpJumpNotZero(vTag,label1);
                Op instr4 = new OpGoToLabel(vTag,label3);
                List<Op> gotoExtend = instr4.expand(1, program);

                Op instr5 = new OpDecrease(vTag,label1);
                program.addLabel(label1, instr5 );
                Op instr6 = new OpIncrease(z1);
                Op instr7 = new OpJumpNotZero(vTag,label1 );
                Op instr8 = new OpDecrease(z1,label2 );
                program.addLabel(label2, instr8 );
                Op instr9 = new OpIncrease(v);
                Op instr10 = new OpIncrease(vTag);
                Op instr11 = new OpJumpNotZero(z1,label2);
                Op instr12 = new OpNeutral(v, label3);
                program.addLabel(label3, instr12 );

                myInstructions.addAll(zeroExtend);

                myInstructions.add(instr3);

                myInstructions.addAll(gotoExtend);

                myInstructions.add(instr5);
                myInstructions.add(instr6);
                myInstructions.add(instr7);
                myInstructions.add(instr8);
                myInstructions.add(instr9);
                myInstructions.add(instr10);
                myInstructions.add(instr11);
                myInstructions.add(instr12);
                return myInstructions;
            }
        }
    }


}
