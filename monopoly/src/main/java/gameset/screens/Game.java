package gameset.screens;

import gameset.functionality.Player;
import gameset.functionality.Property;
import gameutils.Ansi;

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
        // border top
        display.append("-".repeat(204));
        // TODO: display board - need to factor in size of screen too, do this later (assumes size of 204x55 for now)
        // Draw top row
        for (int i = 20; i <= 30; i++) {
            display.append(drawProperty(game.getBoard().getProperty(i), 204));
        }
        display.append(System.lineSeparator());
        // Draw middle rows
        // Draw bottom row
        // border bottom
        display.append("-".repeat(204));

        display.append(System.lineSeparator());
        display.append("TODO: List commands (which update state to different screen - passing game - and eventually going back here)");
        System.out.print(display);
    }

    @Override
    protected void handleInput() {

    }

    private String drawProperty(Property property, int screenWidth) {
        int maxWidth = screenWidth / 10;
        StringBuilder display = new StringBuilder();
        // TODO: there is a bug in the centerString method causing property to go to next line. Fix bug
        display.append(property.displayPropertyName(maxWidth));
        // TODO: add players on property here when implemented
        return display.toString();
    }

    private String centerString(String str, int maxSize) {
        String noColorStr = Ansi.stripAnsi(str);
        int spacer = maxSize - noColorStr.length();
        int leading = spacer / 2;
        int trailing = spacer - leading;
        return " ".repeat(leading) + str + " ".repeat(trailing);
    }
}
