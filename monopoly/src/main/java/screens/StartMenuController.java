package screens;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class StartMenuController {
    @FXML
    private void handleStartSubmit() throws IOException {
       GameApplication gameApplication = new GameApplication();
       gameApplication.playerCreation();
    }

    @FXML
    private void handleExitSubmit() throws IOException {
       System.exit(0);
    }
}
