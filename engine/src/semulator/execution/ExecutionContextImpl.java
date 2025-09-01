package semulator.execution;
import semulator.impl.api.skeleton.Op;
import semulator.label.FixedLabel;
import semulator.label.Label;
import semulator.label.LabelImpl;
import semulator.program.SProgram;
import semulator.variable.Variable;
import semulator.variable.VariableImpl;
import semulator.variable.VariableType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionContextImpl implements ExecutionContext, ExpandContext {


    private ArrayList<Map<Variable, Long>> snapshots = null; // to turn off the comment
    public Map<Variable, Long> CurrSnap;
    public Map<Label, Op> labelMap;

    public int getLabelindex() {
        return labelindex;
    }

    private int labelindex = 1;

    public int getWorkVarIndex() {
        return workVarIndex;
    }

    private int workVarIndex = 1;



    public void setLabelMap(SProgram program) {
        labelMap = new HashMap<>();
        for (var op : program.getOps()) {
            if (op.getLabel() != FixedLabel.EMPTY) // we don't want to add empty labels to the map
                labelMap.put(op.getLabel(), op);
        }
    }
    //create a deep copy constructor
    public ExecutionContextImpl(ExecutionContext context) {
        //create a deep copy constructor
        snapshots = new ArrayList<>(context.getSnapshots());
        CurrSnap = new HashMap<>(context.getCurrSnap());
        labelMap = new HashMap<>(context.getLabelMap());
        labelindex = getLabelindex();
        workVarIndex = getWorkVarIndex();

    }
    public Map<Variable, Long> getCurrSnap()
    {
        return Map.copyOf(CurrSnap);
    }


    @Override
    public Map<Label, Op> getLabelMap() {
        return labelMap;
    }

    public ExecutionContextImpl() {
        snapshots = new ArrayList<>(); // move to another function that will handle inputs from the user
        labelMap = new HashMap<>();
        CurrSnap = new HashMap<>();
    }


    public List getSnapshots() {
        return snapshots;
    }
   public void CreateSnap(SProgram program,List<Long> input) {

        Map<Variable, Long> snap = new HashMap<>();
        Variable tmp;

        for (int i = 0; i < program.getAmountOfVars(); i++) { //fills all the input var with the input and the rest with 0
            tmp = program.GetNextVar(i);
            if (i < input.size())
                snap.put(tmp, input.get(i));
            else
                snap.put( tmp ,0L);
        }

        for(Variable v : program.getAllVars()) // make sure all vars are in the snap and if not add them with value 0
        {
            if(!snap.containsKey(v))
                snap.put(v,0L);
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


    @Override
    public Label newUniqueLabel() {
        while (labelMap.containsKey(new LabelImpl(labelindex++))) {}
        return new LabelImpl(labelindex-1);
    }

    @Override
    public Variable newWorkVar() {
        Variable tmp;
        while (CurrSnap.containsKey(new VariableImpl(VariableType.WORK,workVarIndex++)));
        tmp =  new VariableImpl(VariableType.WORK,workVarIndex++);
        CurrSnap.put(tmp,0L);
        return tmp;
    }

    @Override
    public void AddOpWithNewLabel(Op op ) {
        labelMap.put(newUniqueLabel(),op);
    }
}
