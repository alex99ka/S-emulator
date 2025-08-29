package semulator.execution;

import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.impl.api.skeleton.OpType;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.program.SProgram;
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
    public Map<Label, Op> labelMap;

    public void setLabelMap(SProgram program) {
        labelMap = new HashMap<>();
        for (var op : program.getOps()) {
            if (op.getLabel() != FixedLabel.EMPTY) // we don't want to add empty labels to the map
                labelMap.put(op.getLabel(), op);
        }
    }


    @Override
    public Map<Label, Op> getLabelMap() {
        return labelMap;
    }


    public ExecutionContextImpl(SProgram program, List<Long> input) {
        snapshots = new ArrayList<>(); // move to another function that will handle inputs from the user
        CreateSnap(program, input);// probably I can get the work var from the program but not sure...
        labelMap = new HashMap<>();
    }


    public List getSnapshots() {
        return snapshots;
    }
   private void CreateSnap(SProgram program,List<Long> input) {

        Map<Variable, Long> snap = new HashMap<>();
        Variable tmp;

        for (int i = 0; i < program.getAmountOfVars(); i++) { //fills all the var with the input and the rest with 0
            tmp = program.GetNextVar(i);
            if (i < input.size())
                snap.put(tmp, input.get(i));
            else
                snap.put( tmp ,0L);
        }
       snap.put(Variable.RESULT, 0L); //add the result var
       var first = Map.copyOf(snap);  // making an immutable copy
       snapshots.add(first);
       CurrSnap = snap;
    }
    public Long getVariableValue(Variable v) {return CurrSnap.get(v);}

    public void AddSnap(ArrayList<Variable> vars, ArrayList<Long> vals) {
        if (vars.size() != vals.size()) {
            throw new IllegalArgumentException("vars and vals must have the same length");
        }

       for (int i  = 0; i < vals.size(); i++) {
           CurrSnap.put(vars.get(i),vals.get(i)); // the current snapshot
        }
        var first = Map.copyOf(CurrSnap);  // making an immutable copy
        snapshots.add(first);
    }


}
