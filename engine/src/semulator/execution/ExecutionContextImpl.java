package semulator.execution;

import semulator.program.SprogramImpl;
import semulator.variable.Variable;
import semulator.variable.VariableImpl;
import semulator.variable.VariableType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionContextImpl implements ExecutionContext {


    private ArrayList<Map<Variable, Long>> snapshots = null; // to turn off the comment
    public Map<Variable, Long> CurrSnap;


    public ExecutionContextImpl(SprogramImpl program, Long... input) {
        snapshots = new ArrayList<>(); // move to another function that will handle inputs from the user
        CreateSnap(input);// probably I can get the work var from the program but not sure...
    }

    public List getSnapshots() {
        return snapshots;
    }
   private void CreateSnap(Long... input) {
        int cnt = 0;
        Map<Variable, Long> snap = new HashMap<>();
        for (Long i : input) {
            snap.put(new VariableImpl(VariableType.INPUT,++cnt),i);
        }
       snap.put(Variable.RESULT, 0L); //
       var first = Map.copyOf(snap);  // making an immutable copy
       snapshots.add(first);
       CurrSnap = snap;
    }
    public Long getVariableValue() {return CurrSnap.get(Variable.RESULT);}

    public void AddSnap(Map<Variable, Long> m) {snapshots.add(m);}


}
