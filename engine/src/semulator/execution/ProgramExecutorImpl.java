package semulator.execution;

import semulator.impl.api.skeleton.Op;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;

import java.util.*;

public class ProgramExecutorImpl implements ProgramExecutor {
    SProgram program;
    public ProgramExecutorImpl(SProgram program) {
        this.program = program;
    }
     // the 4th command!


    public long run(List<Long> inputs) {
        program.createFirstSnap(inputs);  // enter the vals from the user to the input vars

        Op current = program.getNextOp();
        while (current != null) {
            Label next = current.execute(program);

            if (next == FixedLabel.EXIT) {
                break;
            } else if (next == FixedLabel.EMPTY || next == null) {
                current = program.getNextOp();
            } else {
                // קפיצה לתווית:
                Op target = program.getOpByLabel(next);
                if (target == null) {
                    throw new IllegalStateException(
                            "Jump to undefined label: " + next.getLabelRepresentation());
                }
                program.ChangeOpIndex(target);   // מציב את האינדקס על היעד
                current = target;
            }
            // stop at the end of the opList
            if (program.getOpsIndex() >= program.getOps().size()) {
                break;
            }
        }

        program.print();
        System.out.println("the result of the program you ran is: " + program.getVariableValue(Variable.RESULT));
        TreeMap<Variable, Long> treeMap = new TreeMap<>(
                Comparator.comparing(Variable::getRepresentation)
        );
        treeMap.putAll(program.getCurrSnap());
        for (Map.Entry<Variable, Long> entry : treeMap.entrySet()) {
            if (!entry.getKey().equals(Variable.RESULT)) {
                System.out.println(entry.getKey().getRepresentation() + " = " + entry.getValue());
            }
        }
        System.out.println("the program ran for: " + program.calculateCycles());



        // 6) מחזירים את y
        return program.getVariableValue(Variable.RESULT);
    }

    public void AddSnap(ArrayList<Variable> vars, ArrayList<Long> vals) {program.AddSnap(vars, vals);}




}
