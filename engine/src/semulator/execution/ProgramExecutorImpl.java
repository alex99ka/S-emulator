package semulator.execution;

import semulator.impl.api.skeleton.Op;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
import semulator.statistics.Statistics;
import semulator.variable.Variable;

import java.lang.reflect.Array;
import java.util.*;

public class ProgramExecutorImpl implements ProgramExecutor {
    SProgram program;
    public ProgramExecutorImpl(SProgram program) {
        this.program = program;
    }
     // the 4th command!


    public long run(List<Long> inputs, Statistics stats) {
        program.createFirstSnap(inputs);  // enter the vals from the user to the input vars
        Set<Variable> allVars = new TreeSet<>(Comparator.comparing(Variable::getRepresentation));
        allVars.addAll(program.getAllVars());
        Op current = program.getNextOp();
        while (current != null) {
            Label next = current.execute(program);
            System.out.println(current.repToChild(program));
            for (Variable v: allVars)
                System.out.print("| " + v.getRepresentation() + " = " +program.getVariableValue(v) + " |"); //DEBUG
            System.out.println("\n");

            if (next.equals(FixedLabel.EXIT)) {
                break;
            } else if (next.equals( FixedLabel.EMPTY )) {
                current = program.getNextOp();
            } else {
                Op target = program.getOpByLabel(next);
                if (target == null) {
                    throw new IllegalStateException(
                            "Jump to undefined label: " + next.getLabelRepresentation());
                }
                program.ChangeOpIndex(target);   // מציב את האינדקס על היעד
                current = target;
            }
            // stop at the end of the opList
            if (program.getOpsIndex() > program.getOps().size()) {
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
        System.out.println("the program ran for: " + program.calculateCycles() + "cycles");
        stats.setCycles(program.calculateCycles());
        return program.getVariableValue(Variable.RESULT);
    }

    public void AddSnap(ArrayList<Variable> vars, ArrayList<Long> vals) {program.AddSnap(vars, vals);}




}
