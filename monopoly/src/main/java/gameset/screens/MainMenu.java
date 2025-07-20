package gameset.screens;

import java.io.IOException;

public class MainMenu extends AbstractScreen {
    public MainMenu() {}

    @Override
    protected void draw() {
        System.out.println("1. Start Game");
        System.out.println("2. Continue Game");
        System.out.println("3. Exit");
    }

    @Override
    protected void handleInput() {
        while (!isFinished()) {
            int ch = 0;
            try {
                ch = System.in.read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            switch (ch) {
                case '1':
                    setNextScreen(SetupGame::new);
                    setFinished(true);
                    break;
                case '2':
                    setNextScreen(LoadGame::new);
                    setFinished(true);
                    break;
                case '3':
                    setFinished(true);
                    System.exit(0);
                    break;
            }
        }
    }
}
