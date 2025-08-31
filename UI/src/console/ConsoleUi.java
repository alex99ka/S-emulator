package console;
// import the program package from the engine module

import semulator.program.*;
//import engine.Statistics;
import semulator.input.XmlTranslator.Factory;
import semulator.variable.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Scanner;


public class ConsoleUI {
    public ConsoleUI() {
        SProgram program=null;
        SProgram programCopy = null;
        Factory factory = new Factory();
        System.out.println("Welcome to S-Emulator IDE");
        boolean exit = false;
        File file;

        while (!exit) {
            Menu();
            int choice;
            try {
                choice = new Scanner(System.in).nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Enter XML file path: ");
                        String filePath = new BufferedReader(new InputStreamReader(System.in)).readLine();
                        file = new File(filePath);
                        program = factory.loadProgramFromXml(file);
                        programCopy = program.myClone();
                        System.out.println("Program loaded successfully.");
                        //Statistics.reset();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    if(program==null){
                        System.out.println("No program loaded. Please load a program first.");
                        break;
                    }
                    program.print();
                    break;
                case 3:
                    if(program==null){
                        System.out.println("No program loaded. Please load a program first.");
                        break;
                    }
                    int expansionsAvailable = program.getProgramDegree();
                    int expansionsRequested=-1;
                    do {
                        try {
                            System.out.println("Enter expansion degree, Max is: " + expansionsAvailable);
                            expansionsRequested = new Scanner(System.in).nextInt();
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please enter a number.");
                            expansionsRequested=-1;
                        }
                    } while(expansionsRequested <0 || expansionsRequested > expansionsAvailable);
                    int diffDegrees=program.getProgramDegree()-programCopy.getProgramDegree();

                    if(expansionsRequested != diffDegrees){ // If the degree has changed, re-deploy the program
                        programCopy= program.clone();
                        programCopy.deployToDegree(expansionsRequested);
                    }
                    System.out.println("After Expansion: ");
                    System.out.println(programCopy);
                    break;
                case 4:
                    if(program==null){
                        System.out.println("No program loaded. Please load a program first.");
                        break;
                    }
                    int degree = program.getProgramDegree();
                    System.out.println("Available Degrees: " + degree);
                    int selectedDegree=-1;
                    do {
                        try {
                            System.out.println("Select Expansion Degree: ");
                            selectedDegree = new Scanner(System.in).nextInt();
                        }catch (Exception e) {
                            System.out.println("Invalid input. Please enter a number.");
                            continue;
                        }
                    } while(selectedDegree < 0  || selectedDegree > degree);

                    diffDegrees=program.getProgramDegree()-programCopy.getProgramDegree();
                    if(selectedDegree != diffDegrees){ // If the degree has changed, re-deploy the program
                        programCopy= program.myClone();
                        programCopy.deployToDegree(selectedDegree);
                    }
                    Collection<Variable>initVars= programCopy.setUserInput();
                    int y=programCopy.execute().getValue();
                    int cycles= programCopy.getCycleCount();
                    try {
                        new Statistics(selectedDegree, initVars, cycles, y).appendStatistics();
                    }catch (Exception e) {
                        System.out.println("Error writing statistics: " + e.getMessage());
                    }
                    finally {
                        System.out.println("After Execution: ");
                        System.out.println(programCopy);
                        System.out.println(programCopy.getVars());
                        System.out.println("Total Cycles: " + cycles);
                    }
                    break;
                case 5:
                    try {
                        Collection<Statistics> programStats = Statistics.loadStatisticsIndividually();
                        for(Statistics stats: programStats){
                            System.out.println(stats);
                        }
                    } catch (Exception e) {
                        System.out.println("Error loading statistics: " + e.getMessage());
                    }
                    break;
                case 6:
                    exit = true;
                    System.out.println("Exiting S-Emulator IDE. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        new ConsoleUI();
    }

    public static void Menu() {

        System.out.println("1. Load Program from XML");
        System.out.println("2. Display Program");
        System.out.println("3. Expand Program");
        System.out.println("4. Run Program");
        System.out.println("5. Show History and Statistics");
        System.out.println("6. Exit");
    }
}