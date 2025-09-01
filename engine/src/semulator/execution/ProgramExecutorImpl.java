package semulator.execution;

import semulator.impl.api.skeleton.Op;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.variable.Variable;
import java.util.ArrayList;
import java.util.List;

public class ProgramExecutorImpl implements ProgramExecutor {
    SProgram program;
    public ProgramExecutorImpl(SProgram program) {
        this.program = program;
    }
     // the 4th command!
    public Long run(List<Long> input) {
        program.createFirstSnap(input);
        Op currentOp = program.getNextOp();
        Label nextLabel;
        do {
            nextLabel = currentOp.execute(program);
            if(nextLabel.equals(FixedLabel.EXIT))
                 break;
            else if (nextLabel.equals(FixedLabel.EMPTY))
                currentOp = program.getNextOp();
             else {
                currentOp = program.getOpByLabel(nextLabel);
                program.ChangeOpIndex(currentOp);
            }


        } while (program.getOpsIndex() < program.getOps().size()-1);

        return program.getVariableValue(Variable.RESULT);
    }

    public long run(SProgram originalProgram, int degree, List<Long> inputs) {
        // 2) הרחבה לדרגה שנבחרה
        program.deployToDegree(degree);

        program.createFirstSnap(inputs);

        // 4) ריצה בלולאה
        Op current = program.getNextOp(); // מתחיל מה-op הראשון
        while (current != null) {
            Label next = current.execute(program); // ההוראה תחזיר label לזרימה הבאה

            if (next == FixedLabel.EXIT) {
                break; // סיום תקין
            } else if (next == FixedLabel.EMPTY || next == null) {
                current = program.getNextOp(); // המשך רציף
            } else {
                // קפיצה לתווית:
                Op target = program.getOpByLabel(next.getLabelRepresentation());
                if (target == null) {
                    throw new IllegalStateException(
                            "Jump to undefined label: " + next.getLabelRepresentation());
                }
                program.changeOpIndexTo(target);   // מציב את האינדקס על היעד
                current = target;
            }

            // הגנה – אם עברנו את סוף הרשימה, עצור
            if (program.getOpsIndex() >= program.getOps().size()) {
                break;
            }
        }

        // 5) הדפסות לפי דרישות התרגיל
        System.out.println("After Execution:");
        System.out.println(program); // toString/print של התוכנית לאחר הרחבה
        System.out.println(program.formatFinalVars()); // ייבנה בסעיף 3 למטה
        System.out.println("Total Cycles: " + program.getCycleCount()); // סה״כ cycles

        // 6) מחזירים את y
        return program.getVariableValue(Variable.RESULT);
    }

    public void AddSnap(ArrayList<Variable> vars, ArrayList<Long> vals) {program.AddSnap(vars, vals);}




}
