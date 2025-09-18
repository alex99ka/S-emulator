package GUI;

import javafx.application.Application;

public class GuiUI extends Application {
    @Override
    public void start(javafx.stage.Stage primaryStage) {
        primaryStage.setTitle("S-emulator");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
