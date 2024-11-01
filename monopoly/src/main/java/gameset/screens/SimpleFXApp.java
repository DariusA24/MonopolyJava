package gameset.screens;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SimpleFXApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create a Button
        Button btn = new Button("Click Me");
        btn.setOnAction(e -> System.out.println("Hello, JavaFX!"));

        // Create a StackPane as the root node
        StackPane root = new StackPane();
        root.getChildren().add(btn);

        // Create a Scene with the root node
        Scene scene = new Scene(root, 300, 250);


        primaryStage.setTitle("Simple JavaFX App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}