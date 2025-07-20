package gameset.screens;

import java.io.IOException;

public class LoadGame extends AbstractScreen {
    public LoadGame() {}

    @Override
    protected void draw() {
        System.out.println("The Load Game functionality has not been implemented yet.");
        System.out.println("Press any key to exit.");
    }

    @Override
    protected void handleInput() {
        try {
            int ch = System.in.read();
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
            setFinished(true);
        }
    }
}
