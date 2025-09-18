package gui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("S-emulator");
        Parent root = javafx.fxml.FXMLLoader.load(
                GUI.class.getResource("/gui/resources/fxml/UserInterface.fxml")
        );
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
