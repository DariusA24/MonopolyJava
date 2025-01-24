package screens;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Game");
        Parent initialRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("StartingMenu.fxml")));
        stage.setScene(new Scene(initialRoot, 300, 200));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}