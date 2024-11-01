package gameset.functionality;

import gameset.screens.Start;

import java.io.FileNotFoundException;

import static java.lang.System.exit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Start start = new Start();
        Game game = new Game();

        String choice = start.MenuScreen();
        if(choice.equals("1")) {
            int playerAmount = start.getPlayerAmountScreen();
            game.gameLoop(playerAmount);
        }
        else exit(0);
    }
}