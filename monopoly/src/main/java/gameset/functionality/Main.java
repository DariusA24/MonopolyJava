package gameset.functionality;

import gameset.screens.Start;

import java.io.IOException;
import static java.lang.System.exit;
import gameutils.Console;

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        Start start = new Start();
        Game game = new Game();

        String choice = start.MenuScreen();
        if (choice.equals("1")) {
            Console.clearConsole();
            int playerAmount = start.getPlayerAmountScreen();
            game.gameLoop(playerAmount);
        } else exit(0);
    }
}