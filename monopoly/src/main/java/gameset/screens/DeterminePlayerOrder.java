package gameset.screens;

import gameset.functionality.Dice;
import gameset.functionality.Game;
import gameset.functionality.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DeterminePlayerOrder extends AbstractScreen {
    private Game game;
    private final LinkedList<String> playersLeftToRoll;
    private final HashMap<String, Integer> playerRolls = new HashMap<>();
    private ArrayList<Player> sortedPlayerList;
    private State currentState;

    enum State {
        ROLLING,
        SHOWING_RESULTS,
        SHOW_WINNER
    }

    public DeterminePlayerOrder(Game game) {
        this.game = game;
        this.playersLeftToRoll = new LinkedList<>(game.getPlayerList().stream().map(Player::getNameNoColor).toList());
        this.currentState = State.ROLLING;
    }

    @Override
    protected void draw() {
        System.out.println("Let's determine the player order. Roll the highest number to place first.");
        switch (currentState) {
            case ROLLING -> System.out.println(playersLeftToRoll.getFirst() + "... Press any key to roll");
            case SHOWING_RESULTS -> {
                System.out.println("You rolled a " + game.getDice().getRollTotal());
                System.out.println("Press any key to hand over roll to the next player...");
            }
            case SHOW_WINNER -> {
                System.out.println("The player order is:");
                for (int i = 0; i < sortedPlayerList.size(); i++) {
                    System.out.println((i + 1) + ". " + sortedPlayerList.get(i).getName());
                }
                System.out.println("Press any key to continue...");
            }
        }
    }

    @Override
    protected void handleInput() {
        while (!isFinished()) {
            try {
                int ch = System.in.read();

                switch (currentState) {
                    case ROLLING -> {
                        game.getDice().rollDice();
                        playerRolls.put(playersLeftToRoll.getFirst(), game.getDice().getRollTotal());
                        playersLeftToRoll.removeFirst();
                        currentState = State.SHOWING_RESULTS;
                    }
                    case SHOWING_RESULTS -> {
                        if (playersLeftToRoll.isEmpty()) {
                            // Calculate player order
                            LinkedList<String> sortedPlayers = new LinkedList<>(playerRolls.entrySet().stream()
                                    .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                                    .map(Map.Entry::getKey)
                                    .toList());
                            sortedPlayerList = new ArrayList<>(game.getPlayerList().size());
                            for (String name : sortedPlayers) {
                                sortedPlayerList.add(game.getPlayerList().stream()
                                        .filter(player -> player.getNameNoColor().equals(name))
                                        .findFirst()
                                        .orElse(null));
                            }
                            // Update player order in game
                            game.setPlayerOrder(sortedPlayerList);
                            currentState = State.SHOW_WINNER;
                        } else {
                            currentState = State.ROLLING;
                        }
                    }
                    case SHOW_WINNER -> {
                        setNextScreen(() -> new gameset.screens.Game(game));
                        setFinished(true);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading input: " + e.getMessage());
                setFinished(true);
            }
        }
    }
}