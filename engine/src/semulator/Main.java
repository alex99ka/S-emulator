package semulator;

import semulator.execution.ProgramExecutor;
import semulator.execution.ProgramExecutorImpl;
import semulator.impl.api.basic.OpDecrease;
import semulator.impl.api.basic.OpIncrease;
import semulator.impl.api.basic.OpJumpNotZero;
import semulator.impl.api.basic.OpNeutral;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.input.XmlTranslator.Factory;
import semulator.input.operation;
import semulator.label.FixedLabel;
import semulator.label.LabelImpl;
import semulator.program.SProgram;
import semulator.program.SprogramImpl;
import semulator.variable.Variable;
import semulator.variable.VariableImpl;
import semulator.variable.VariableType;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {


        Long result;
//
//        List<Long> input = new ArrayList<>();
//        input.add(3L);
//        input.add(6L);
//        input.add(2L);


//        SProgram p = new SprogramImpl("test");
        operation op = new operation(); // should create ctor the does
//        op.debugging();

        SProgram p = new SprogramImpl("test");
        // here should be the part to read a xml file and create the program with the path C:\Users\User\Downloads\example 1 s- emulator
        // and the file name is minus.xml
        File file;
        try{
            file = new File("C:\\Users\\User\\Downloads\\example 1 s- emulator\\minus.xml");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            file = null;
        }
        Factory f = new Factory();
       try {
           p = f.loadProgramFromXml(file);
       }
         catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        ProgramExecutor programExecutor = new ProgramExecutorImpl(p);
        try {
            result = programExecutor.run(op.getUserInput()); // here should be the output of scanner !!!
        } catch (Exception e)
        {
            result = null;
        }
    }
}
