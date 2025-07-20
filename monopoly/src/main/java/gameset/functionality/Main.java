package gameset.functionality;

import gameset.screens.AbstractScreen;
import gameset.screens.Startup;

public class Main {
    public static void main(String[] args) {
        // Start with the initial screen
        AbstractScreen currentScreen = new Startup();

        // Main game loop
        while (currentScreen != null) {
            // Render the current screen and get the next one
            // If it returned null, the game will exit
            currentScreen = currentScreen.render();
        }

        // Ensure the terminal is restored when the game exits
        AbstractScreen.restoreTerminal();
    }
}