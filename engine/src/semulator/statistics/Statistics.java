package semulator.statistics;
import java.util.ArrayList;
import java.util.List;

public class Statistics {

    int runNumber;
    int runDegree;
    List<Integer> userInputs;
    long res;
    int cycles;

    public int getRunNumber() {
        return runNumber;
    }

    public int getRunDegree() {
        return runDegree;
    }

    public List<Integer> getUserInputs() {
        return userInputs;
    }

    public long getRes() {
        return res;
    }

    public int getCycles() {
        return cycles;
    }


    public Statistics() {
        userInputs = new ArrayList<Integer>();
        runNumber = 0;
        runDegree = 0;
        res = 0;
        cycles = 0;
    }
    public void incRun() {runNumber++;}
    public void setRunDegree(int runDegree) {
        this.runDegree = runDegree;
    }

    public void setRes(long res) {
        this.res = res;
    }
    public void setCycles(int cycles) {
        this.cycles = cycles;
    }
    // deep copy
    public void addUserInput(List<Integer> inputs) {userInputs.addAll(inputs);}
    public void printStats() {
        System.out.println("Run Number: " + runNumber);
        System.out.println("Run Degree: " + runDegree);
        System.out.println("User Inputs: " + userInputs);
        System.out.println("Result: " + res);
        System.out.println("Cycles: " + cycles);
    }
    public void reset()
    {
        userInputs.clear();
        runNumber = 0;
        runDegree = 0;
        res = 0;
        cycles = 0;
    }
}
