package semulator.execution;

import semulator.variable.Variable;
import semulator.variable.VariableImpl;
import semulator.variable.VariableType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionContextImpl {

    private final ArrayList<Map<Variable, Long>> snapshots;
    private Map<Variable, Long> CurrSnap;
    public ExecutionContextImpl(Long... input) {
        snapshots = new ArrayList<>();
        CreateSnap(input);
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


    public void AddSnap(long variableValue,Variable... workVariables)
    {
        if (workVariables != null) {
        for (Variable v : workVariables) {
            CurrSnap.put(v, variableValue);
    }
        }
        CurrSnap.put(Variable.RESULT, variableValue); // update y
        var frozen = (Map.copyOf(CurrSnap));
        snapshots.add(frozen);
    }
}
