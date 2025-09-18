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


    private ArrayList<Map<Variable,Integer>> snapshots; // to turn off the comment
    private Map<Variable, Integer> currSnap;
    private Map<Label, Op> labelMap;

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
        currSnap = new HashMap<>(context.getCurrSnap());
        labelMap = new HashMap<>(context.getLabelMap());
        labelindex = getLabelindex();
        workVarIndex = getWorkVarIndex();

    }
    public Map<Variable, Integer> getCurrSnap()
    {
        return Map.copyOf(currSnap);
    }


    @Override
    public Map<Label, Op> getLabelMap() {
        return labelMap;
    }

    public ExecutionContextImpl() {
        snapshots = new ArrayList<>(); // move to another function that will handle inputs from the user
        labelMap = new HashMap<>();
        currSnap = new HashMap<>();
    }

    @Override
    public List<Map< Variable, Integer>> getSnapshots() {
        return snapshots;
    }
   public void createSnap(SProgram program, List<Integer> input) {

        Map<Variable, Integer> snap = new HashMap<>();
        Variable tmp;

        for (int i = 0; i < program.getAmountOfVars(); i++) { //fills all the input var with the input and the rest with 0
            tmp = program.GetNextVar(i);
            if (i < input.size())
                snap.put(tmp, input.get(i));
            else
                snap.put( tmp ,0);
        }

        for(Variable v : program.getAllVars()) // make sure all vars are in the snap and if not add them with value 0
        {
            snap.computeIfAbsent(v, k -> 0);
        }
       snap.put(Variable.RESULT, 0); //add the result var
       Map <Variable,Integer> first = Map.copyOf(snap);  // making an immutable copy
       snapshots.add(first);
       currSnap = snap;
    }
    public Integer getVariableValue(Variable v) {return currSnap.get(v);}

    @Override
    public void addSnap(ArrayList<Variable> vars, ArrayList<Integer> vals) {
        if (vars.size() != vals.size()) {
            throw new IllegalArgumentException("vars and vals must have the same length");
        }

       for (int i  = 0; i < vals.size(); i++) {
           currSnap.put(vars.get(i),vals.get(i)); // the current snapshot
        }
        Map<Variable,Integer> first = Map.copyOf(currSnap);  // making an immutable copy
        snapshots.add(first);
    }


    @Override
    public Label newUniqueLabel() {
        while (labelMap.containsKey(new LabelImpl(labelindex++))) {//ignore and just raise the index
             } //empty beacuse the ++ is needed
        return new LabelImpl(labelindex-1);
    }

    @Override
    public Variable newWorkVar() {
        Variable tmp;
        while (currSnap.containsKey(new VariableImpl(VariableType.WORK,workVarIndex++)));
        tmp =  new VariableImpl(VariableType.WORK,workVarIndex++);
        currSnap.put(tmp,0);
        return tmp;
    }

    @Override
    public void addOpWithNewLabel(Op op ) {
        labelMap.put(newUniqueLabel(),op);
    }
}
