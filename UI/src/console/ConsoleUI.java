package console;
// import the program package from the engine module

import semulator.execution.ProgramExecutor;
import semulator.execution.ProgramExecutorImpl;
import semulator.program.*;
import semulator.input.XmlTranslator.Factory;
import semulator.statistics.Statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import java.util.List;
import java.util.Scanner;


public class ConsoleUI {
    public ConsoleUI() {
        SProgram program=null;
        SProgram programCopy = null;
        int diffDegrees;
        Factory factory = new Factory();
        System.out.println("Welcome to S-Emulator IDE");
        boolean exit = false;
        File file;
        String NPL = "No program loaded. Please load a program first.";
        Scanner scanner = new Scanner(System.in);
        Statistics stats = new Statistics();

        while (!exit) {
            Menu();
            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); //clean the buffer
                continue;
            }

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Enter XML full file path: ");
                        String filePath = new BufferedReader(new InputStreamReader(System.in)).readLine();
                        if(!filePath.endsWith(".xml")) {
                            System.out.println("please provide a valid XML file path");
                            break;
                        }
                        file = new File(filePath);
                        try {
                            program = factory.loadProgramFromXml(file);
                        }
                        catch (Exception e) {
                            System.out.println("Error loading program: " + e.getMessage());
                            break;
                        }
                        programCopy = program.myClone();
                        System.out.println("Program loaded successfully.");
                        stats.reset();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    if(program==null){
                        System.out.println(NPL);
                        break;
                    }
                    program.print();
                    break;
                case 3:
                    if(program==null){
                        System.out.println(NPL);
                        break;
                    }
                    int expansionsAvailable = program.getProgramDegree();
                    int expansionsRequested=-1;
                    do {
                        try {
                            System.out.println("Enter expansion degree, Max is: " + expansionsAvailable);
                            expansionsRequested = new Scanner(System.in).nextInt();
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please enter a integer (number).");
                            expansionsRequested=-1;
                        }
                    } while(expansionsRequested <0 || expansionsRequested > expansionsAvailable);
                    if (programCopy == null) {
                        System.out.println("Program copy is not initialized. Please load a program first.");
                        break;
                    }
                    diffDegrees=program.getProgramDegree()-programCopy.getProgramDegree();

                    if(expansionsRequested != diffDegrees){ // If the degree has changed, re-deploy the program
                        programCopy= program.myClone();
                        programCopy.expandProgram(expansionsRequested);
                    }
                    System.out.println("After Expansion: ");
                    programCopy.print();
                    break;
                case 4:
                    if(program==null){
                        System.out.println(NPL);
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
                    if (programCopy == null) {
                        System.out.println("Program copy is not initialized. Please load a program first.");
                        break;
                    }
                    diffDegrees=program.getProgramDegree()-programCopy.getProgramDegree();
                    if(selectedDegree != diffDegrees){ // If the degree has changed, re-deploy the program
                        programCopy= program.myClone();
                        programCopy.expandProgram(selectedDegree);
                    }
                    ProgramExecutor executor = new ProgramExecutorImpl(programCopy);

                    List<Integer> initVars= program.getInputFromUser();
                    stats.setRunDegree(selectedDegree);
                    stats.addUserInput(initVars);
                    stats.setRes( executor.run(initVars,stats));
                    stats.incRun();
                    break;
                     case 5:
                         if(program==null){
                                System.out.println(NPL);
                                break;
                         }
                            stats.printStats();
                    break;
                case 6:
                    exit = true;
                    System.out.println("Exiting S-Emulator IDE. Farewell!");
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