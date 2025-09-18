package gui.MainController;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import semulator.impl.api.skeleton.AbstractOpBasic;
import semulator.impl.api.skeleton.Op;
import semulator.impl.api.skeleton.OpData;
import semulator.impl.api.skeleton.OpType;
import semulator.input.XmlTranslator.Factory;
import semulator.program.SProgram;
import semulator.statistics.Statistics;
import semulator.variable.Variable;
import semulator.variable.VariableType;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class MainController {

    private SProgram program;
    private SProgram programcopy;
    private final SimpleIntegerProperty currdegree = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty maxdegree = new SimpleIntegerProperty(0);
    private String currentFilePath ="";
    private boolean loadedFromFile;
    private Statistics stats;
    private Map<Integer, TextField> inputFields = new HashMap<>();

    @FXML
    private Button collapse;

    @FXML
    private Label currdeg;

    @FXML
    private Button debug;

    @FXML
    private Button expand;

    @FXML
    private TextField fileroute;

    @FXML
    private Button functionselector;

    @FXML
    private Button highlightselection;

    @FXML
    private TableView<Op> instructionstable;

    @FXML
    private TableColumn<Op, Integer> instructiontablecycles;

    @FXML
    private TableColumn<Op, String> instructiontableinstr;

    @FXML
    private TableColumn<Op, String> instructiontablelabel;

    @FXML
    private TableColumn<Op, Integer> instructiontablenumber;

    @FXML
    private TableColumn<Op, String> instructiontabletype;

    @FXML
    private Button load;

    @FXML
    private Button loadSavedProgram;

    @FXML
    private Label maxDeg;

    @FXML
    private TableView<Statistics> programhistorytable;

    @FXML
    private TableColumn<Statistics, Integer> historytablecycles;

    @FXML
    private TableColumn<Statistics, Integer> historytabledegree;

    @FXML
    private TableColumn<Statistics, String> historytableinput;

    @FXML
    private TableColumn<Statistics, Integer> historytablenumber;

    @FXML
    private TableColumn<Statistics, Integer> historytableresult;

    @FXML
    private TableView<String> commandshistory;

    @FXML
    private TableColumn<Op, Integer> commandstablecycles;

    @FXML
    private TableColumn<Op, String> commandstableinstr;

    @FXML
    private TableColumn<Op, String> commandstablelabel;

    @FXML
    private TableColumn<Op, Integer> commandstablenumber;

    @FXML
    private TableColumn<Op, String> commandstabletype;


    @FXML
    private Label programinputs;

    @FXML
    private Label programinputs1;

    @FXML
    private VBox programinputsvbox;

    @FXML
    private VBox programvarsvbox;

    @FXML
    private Button rerun;

    @FXML
    private Button resume;

    @FXML
    private Button run;

    @FXML
    private Button saveprogram;

    @FXML
    private Button stop;

    @FXML
    void collapseProgram(ActionEvent event) {
        if(currdegree.get() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Collapse Program Error");
            alert.setHeaderText("Cannot collapse further");
            alert.showAndWait();
            return;
        }
        currdegree.setValue(currdegree.getValue()-1);
        programcopy= program.myClone();
        programcopy.expandProgram(currdegree.getValue());
        instructionstable.setItems(getOps(programcopy));
        commandshistory.getItems().clear();
    }

    @FXML
    void debugProgram(ActionEvent event) { //TODO:implement

    }

    @FXML
    void expandProgram(ActionEvent event) {
        if(currdegree.get() == maxdegree.get()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Expand Program Error");
            alert.setHeaderText("Cannot expand further");
            alert.showAndWait();
            return;
        }
        currdegree.setValue(currdegree.getValue()+1);
        programcopy.expandProgram(currdegree.getValue());
        instructionstable.setItems(getOps(programcopy));
        commandshistory.getItems().clear();

    }

    @FXML
    void highlightSelection(ActionEvent event) {

    }

    @FXML
    void loadProgram(ActionEvent event) {
        Factory factory = new Factory();
        if(fileroute.getText().equals(currentFilePath)) {
            return;
        }
        String filePath = fileroute.getText();
        if(!filePath.endsWith(".xml")) {
            System.out.println("please provide a valid XML file path");
            return;
        }
        File file = new File(filePath);
        try {
            program = factory.loadProgramFromXml(file);
            programcopy = program.myClone();
            programcopy.expandProgram(currdegree.getValue());
            currentFilePath=fileroute.getText();
            loadedFromFile=false;
            currdegree.set(0);
            maxdegree.set(program.getProgramDegree());

        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load Program Error");
            alert.setHeaderText("Could not load the program");
            alert.setContentText(e.getMessage()); // or a custom message
            alert.showAndWait();
        }
        System.out.println("Program loaded successfully.");
        stats.reset();




    }

    @FXML
    public void initialize() {
        noLoadedProgram();
        setInstructionsTableAddFormat(commandstablelabel, commandstablenumber, commandstablecycles, commandstabletype, commandstableinstr);
        setInstructionsTableAddFormat(instructiontablelabel, instructiontablenumber, instructiontablecycles, instructiontabletype, instructiontableinstr);
        setProgramHistoryAddFormat(historytablenumber, historytabledegree, historytablecycles, historytableinput, historytableresult);
        setCommandshistoryListener();
    }

//    @FXML
//    void loadSavedProgram(ActionEvent event) {
//        if(loadedFromFile) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Load Program from file");
//            alert.setHeaderText("Program already loaded from file");
//            alert.showAndWait();
//            return;
//        }
//        try {
//            ProgramState toLoad = ProgramState.loadProgramState();
//            program = toLoad.getOrigin();
//            programcopy = toLoad.getCopy();
//
//            commandshistory.getItems().clear();
//            programhistorytable.getItems().clear();
//            programLoaded();
//
//            instructionstable.getItems().clear();
//            instructionstable.setItems(getOps(programcopy));
//            loadedFromFile=true;
//
//            currdegree.set(program.getProgramDegree()-programcopy.getProgramDegree());
//            maxdegree.set(program.getProgramDegree());
//
//
//            setInputVariables(program);
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Load Program from file");
//            alert.setHeaderText(String.format("Program %s with degree %d/%d loaded successfully.",program.getName(),currdegree.getValue(),maxdegree.getValue()));
//            alert.showAndWait();
//            currentFilePath="";
//            fileroute.setText("");
//
//        } catch (Exception e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Load Program from file Error");
//            alert.setHeaderText("Could not load the program");
//            alert.setContentText(e.getMessage()); // or a custom message
//            alert.showAndWait();
//        }
//
//    }

    @FXML
    void resumeProgram(ActionEvent event) {

    }

    @FXML
    void runProgram(ActionEvent event) {
        Collection<Variable>initialInputs;
        try {
            initialInputs = loadVariablesToProgramCopy();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Run Program Error");
            alert.setHeaderText("Could not load input variables");
            alert.setContentText(e.getMessage()); // or a custom message
            alert.showAndWait();
            return;
        }
        programcopy.execute();
        Statistics stats=new Statistics(currdegree.getValue(),initialInputs,programcopy.get(),programcopy.getVars().clone());
        stats.appendStatistics();
        programhistorytable.getItems().add(stats);
        showVariables();
    }

    @FXML
    void saveProgram(ActionEvent event) {
        if(program==null || programcopy==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Program Error");
            alert.setHeaderText("No program loaded");
            alert.showAndWait();
            return;
        }
        ProgramState state=new ProgramState(program,programcopy);
        try {
            state.saveProgramState();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Program");
            alert.setHeaderText(String.format("Program %s with degree %d/%d saved successfully.",program.getName(),currdegree.getValue(),maxdegree.getValue()));
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Program Error");
            alert.setHeaderText("Could not save the program");
            alert.setContentText(e.getMessage()); // or a custom message
            alert.showAndWait();
        }
    }

    @FXML
    void selectFunction(ActionEvent event) {

    }

    @FXML
    void stopdebug(ActionEvent event) {

    }

    public ObservableList<Op> getOps(SProgram program) {
        ObservableList<Op> instructions = FXCollections.observableArrayList();
        if (program == null) return instructions;
        instructions.addAll(program.getOps());
        return instructions;
    }

    private void setInstructionsTableAddFormat(TableColumn<Op, String> label, TableColumn<Op, Integer> number, TableColumn<Op, Integer> cycles, TableColumn<Op, String> type, TableColumn<Op, String> instr) {
        // Define how each column gets its value
        label.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLabel().getLabelRepresentation())
        );

        number.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getMyExpandIndex()).asObject()
        );

        cycles.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCycles()).asObject()
        );

        type.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getType().equals(OpType.BASIC.getType()) ? "B" : "S")
        );

        instr.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRepresentation())
        );

    }

    private void setProgramHistoryAddFormat(TableColumn<Statistics, Integer> number, TableColumn<Statistics, Integer> degree, TableColumn<Statistics, Integer> cycles, TableColumn<Statistics, String> input, TableColumn<Statistics, Integer> result) {
        // Define how each column gets its value

        number.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getRunNumber()).asObject()
        );

        degree.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getRunDegree()).asObject()
        );

        cycles.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCycles()).asObject()
        );

        input.setCellValueFactory(cellData -> {
                    StringBuilder sb = new StringBuilder();
                    programcopy.getAllVars().forEach((var) -> {;
                        sb.append(var.getRepresentation()).append("=").append(programcopy.getVariableValue(var)).append(",");
                    });
                    if(!sb.isEmpty()) sb.deleteCharAt(sb.length()-1);
                    return new SimpleStringProperty(sb.toString());
                }
        );
        result.setCellValueFactory(ignored ->
                new SimpleIntegerProperty(programcopy.getVariableValue(Variable.RESULT)).asObject()
        );


    }

    public void setCommandshistoryListener() {
        instructionstable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            commandshistory.getItems().clear();
            ArrayList<String> history = new ArrayList<>();
            Op prev = newSel;
            if (prev == null) return; // No selection
          // a loop that parses the string result from reptochild and seperates it to string by the seperator "<<<"
            String[] dinesty = prev.getRepresentation().split(Pattern.quote("<<<"));
            for (String papa: dinesty) {
                papa.trim();
                if(!papa.isEmpty())
                    history.add(papa);
            }
            history.add(prev.getRepresentation());
            commandshistory.getItems().addAll(history);
        });
    }
    public void noLoadedProgram(){
        instructionstable.setPlaceholder(new Label("No program loaded"));
        commandshistory.setPlaceholder(new Label("No instruction selected"));
        programhistorytable.setPlaceholder(new Label("No program loaded"));
        instructionstable.getItems().clear();
        commandshistory.getItems().clear();
        programhistorytable.getItems().clear();
        inputFields.clear();
        loadedFromFile=false;
        program=null;
        programcopy=null;
        expand.setVisible(false);
        collapse.setVisible(false);
        run.setVisible(false);
        debug.setVisible(false);
        stop.setVisible(false);
        resume.setVisible(false);
        rerun.setVisible(false);
        saveprogram.setVisible(false);
        functionselector.setVisible(false);
        highlightselection.setVisible(false);
        currdeg.textProperty().unbind();
        maxDeg.textProperty().unbind();
        currdeg.setText("0");
        maxDeg.setText("0");
    }
    public void programLoaded()
    {
        expand.setVisible(true);
        collapse.setVisible(true);
        run.setVisible(true);
        debug.setVisible(true);
        stop.setVisible(true);
        resume.setVisible(true);
        rerun.setVisible(true);
        saveprogram.setVisible(true);
        functionselector.setVisible(true);
        highlightselection.setVisible(true);
        currdeg.textProperty().bind(currdegree.asString());
        maxDeg.textProperty().bind(maxdegree.asString());
        instructionstable.setItems(getOps(program));
        commandshistory.getItems().clear();
        programhistorytable.getItems().clear();
        inputFields.clear();
        programvarsvbox.getChildren().clear();
        stats.reset();
    }
    public void setInputVariables(SProgram program) {
        programinputsvbox.getChildren().clear();
        if(program==null) return;

        Collection<Variable> inputs = program.getInputVars();
        if(inputs.isEmpty()) {
            programinputs.setText("No input variables");
            return;
        }

        boolean fill=false;
        Label previousLabel = null;
        TextField previousTextField = null;

        for (Variable variable : program.getInputVars()){
            Integer varPos = variable.getIndex();
            Label label = new Label(variable.getRepresentation());
            TextField textField = new TextField();
            textField.setMaxSize(60, 60);

            textField.textProperty().addListener((obs, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });

            inputFields.put(varPos, textField);
            if(!fill) {
                fill=true;
                previousLabel = label;
                previousTextField = textField;
            }
            else {
                HBox hBox = new HBox(10,previousLabel,previousTextField, label,textField); // 10 is spacing between label and field
                hBox.paddingProperty().set(new javafx.geometry.Insets(5, 5, 5, 5));
                programinputsvbox.getChildren().add(hBox);
                fill=false;
            }
        }
        if(fill) {
            HBox hBox = new HBox(10, previousLabel, previousTextField); // 10 is spacing between label and field
            hBox.paddingProperty().set(new javafx.geometry.Insets(5, 5, 5, 5));
            programinputsvbox.getChildren().add(hBox);
        }
    }

//    public Collection<Variable> loadVariablesToProgramCopy() throws Exception {
//        Collection<Variable> variables = new ArrayList<>();
//        if(programcopy==null) return variables;
//        for (Map.Entry<Integer, TextField> entry : inputFields.entrySet()) {
//            Integer varPos = entry.getKey();
//            TextField textField = entry.getValue();
//            String text = textField.getText();
//            if (text == null) {
//                throw new Exception("Input variable at position " + varPos + " is empty.");
//            }
//
//            if(text.isEmpty()) {
//                text = "0";
//            }
//
//            try {
//                int value = Integer.parseInt(text);
//                Variable toUpdate = programcopy.getVars().getInput().get(varPos);
//                toUpdate.setValue(value);
//                variables.add(Variable.createDummyVar(toUpdate.getType(), toUpdate.getPosition(), toUpdate.getValue()));
//            } catch (NumberFormatException e) {
//                throw new Exception("Input variable at position " + varPos + " is not a valid integer.");
//            }
//        }
//        return variables;
//    }
//    public void showVariables()
//    {
//        HBox hBox = new HBox(10);// 10 is spacing between label and field
//        VBox inputVars = new VBox();
//        javafx.geometry.Insets insets = new javafx.geometry.Insets(5, 5, 5, 5);
//        inputVars.paddingProperty().set(insets);
//
//        for(Variable var:programcopy.getVars().getInput().values()) {
//            Label label = new Label(var.toString()+" = "+var.getValue());
//            inputVars.getChildren().add(label);
//        }
//        VBox workVars = new VBox();
//        workVars.paddingProperty().set(insets);
//
//        for (Variable var : programcopy.getVars().getEnvvars().values()) {
//            Label label = new Label(var.toString()+" = "+var.getValue());
//            workVars.getChildren().add(label);
//        }
//
//        Label yLabel= new Label("y = "+programcopy.getVars().getY().getValue());
//        yLabel.paddingProperty().set(insets);
//
//        Label cyclesLabel= new Label("Cycles = "+programcopy.getCycleCount());
//        cyclesLabel.paddingProperty().set(insets);
//
//        hBox.getChildren().addAll(inputVars,workVars,yLabel,cyclesLabel);
//        programvarsvbox.getChildren().clear();
//        programvarsvbox.getChildren().add(hBox);
//    }


}