package console;
// import the program package from the engine module

import semulator.execution.ProgramExecutor;
import semulator.execution.ProgramExecutorImpl;
import semulator.program.*;
//import engine.Statistics;
import semulator.input.XmlTranslator.Factory;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import java.util.List;
import java.util.Scanner;


public class ConsoleUI {
    public ConsoleUI() {
        SProgram program=null;
        SProgram programCopy = null;
        Factory factory = new Factory();
        System.out.println("Welcome to S-Emulator IDE");
        boolean exit = false;
        File file;
        String NPL = "No program loaded. Please load a program first.";
        Scanner scanner = new Scanner(System.in);

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
                    int diffDegrees=program.getProgramDegree()-programCopy.getProgramDegree();

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

                    List<Long> initVars= program.getInputFromUser();
                    Long y= executor.run(initVars);
//                case 5:
//                    try {
//                        Collection<Statistics> programStats = Statistics.loadStatisticsIndividually();
//                        for(Statistics stats: programStats){
//                            System.out.println(stats);
//                        }
//                    } catch (Exception e) {
//                        System.out.println("Error loading statistics: " + e.getMessage());
//                    }
//                    break;
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