package semulator.statistics;

import semulator.variable.Variable;

import java.util.ArrayList;
import java.util.List;

public class Statistics {

    int runNumber;
    int runDegree;
    List<Long> userInputs;
    long res;
    int cycles;

    public Statistics() {
        userInputs = new ArrayList<>();
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
    public void addUserInput(List<Long> inputs) { // deep copy
        for (Long input : inputs) {
            userInputs.add(input);
        }
    }
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
