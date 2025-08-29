package semulator;

import semulator.execution.ProgramExecutor;
import semulator.execution.ProgramExecutorImpl;
import semulator.impl.api.basic.OpDecrease;
import semulator.impl.api.basic.OpIncrease;
import semulator.impl.api.basic.OpJumpNotZero;
import semulator.impl.api.basic.OpNeutral;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.input.operation;
import semulator.label.FixedLabel;
import semulator.label.LabelImpl;
import semulator.program.SProgram;
import semulator.program.SprogramImpl;
import semulator.variable.Variable;
import semulator.variable.VariableImpl;
import semulator.variable.VariableType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        Long result;

        List<Long> input = new ArrayList<>();
        input.add(3L);
        input.add(6L);
        input.add(2L);


        SProgram p = new SprogramImpl("test");
        operation op = new operation(p); // should create ctor the does
        op.debugging();


        ProgramExecutor programExecutor = new ProgramExecutorImpl(p);
        try {
            result = programExecutor.run(input); // here should be the output of scanner !!!
        } catch (Exception e)
        {
            result = null;
        }
    }
}
