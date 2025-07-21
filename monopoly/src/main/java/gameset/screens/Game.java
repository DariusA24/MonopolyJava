package gameset.screens;

import gameset.functionality.Player;

public class Game extends AbstractScreen {
    private gameset.functionality.Game game;

    public Game(gameset.functionality.Game game) {
        this.game = game;
    }

    // TODO: left off here - When running the terminal is not cleaning the screen before redrawing
    @Override
    protected void draw() {
        StringBuilder display = new StringBuilder();

        // TODO: list out player names, cash, num properties across top
        for (Player player : game.getPlayerList()) {
            display.append(player.getName() + " - $" + player.getMoney() + ", Property Count: " + player.getProperties().size() + "\t");
        }
        display.append(System.lineSeparator());
        display.append(game.getBoard());
        display.append(System.lineSeparator());
        display.append("TODO: List commands (which update state to different screen - passing game - and eventually going back here)");
        System.out.print(display);
    }

    @Override
    protected void handleInput() {

    }
}
