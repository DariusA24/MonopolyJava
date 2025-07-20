package gameset.screens;

import java.io.IOException;

public class SetupGame extends AbstractScreen {
    private String currentInput = "";
    private SetupGameStates currentState;
    private int playerIndex = 0;
    // Variables to be passed to game constructor
    private int numberOfPlayers;
    private String[] playerNames;

    private enum SetupGameStates {
        NUMBER_OF_PLAYERS,
        NUMBER_OF_PLAYERS_INVALID,
        PLAYER_NAMES
    }

    public SetupGame() {
        this.currentState = SetupGameStates.NUMBER_OF_PLAYERS;
    }

    @Override
    protected void draw() {
        switch (currentState) {
            case NUMBER_OF_PLAYERS -> {
                System.out.println("Enter the number of players (2-4):");
                System.out.print(currentInput);
            }
            case NUMBER_OF_PLAYERS_INVALID -> {
                System.out.println("Invalid number of players. Please enter a number between 2 and 4 followed by enter:");
                System.out.print(currentInput);
            }
            case PLAYER_NAMES -> {
                // TODO: want this one by one as loop until all names are entered
                System.out.println("Enter the names of the players:");
                System.out.print("Enter the name of player " + (playerIndex + 1) + ": " + currentInput);
            }
        }
    }

    @Override
    protected void handleInput() {
        while (!isFinished()) {
            try {
                int ch = System.in.read();
                switch (ch) {
                    // If enter is pressed, need to evaluate the current state
                    case 10 -> {
                        switch (currentState) {
                            case NUMBER_OF_PLAYERS, NUMBER_OF_PLAYERS_INVALID -> {
                                try {
                                    int numberOfPlayers = Integer.parseInt(currentInput);
                                    if (numberOfPlayers >= 2 && numberOfPlayers <= 4) {
                                        this.numberOfPlayers = numberOfPlayers;
                                        this.playerNames = new String[numberOfPlayers];
                                        currentState = SetupGameStates.PLAYER_NAMES;
                                        currentInput = "";
                                    } else {
                                        currentState = SetupGameStates.NUMBER_OF_PLAYERS_INVALID;
                                    }
                                } catch (NumberFormatException e) {
                                    currentState = SetupGameStates.NUMBER_OF_PLAYERS_INVALID;
                                }
                            }
                            case PLAYER_NAMES -> {
                                playerNames[playerIndex++] = currentInput;
                                currentInput = "";
                                if (playerIndex == numberOfPlayers) {
                                    // TODO: create a game
                                    // Game game = new Game(numberOfPlayers, playerNames);
                                    this.setFinished(true);
                                    // TODO: set to the Game screen and pass the game
                                    // this.setNextScreen(() -> new MainMenu());
                                }
                            }
                        }
                    }
                    // If delete is pressed, remove the last character
                    case 127 -> {
                        if (!currentInput.isEmpty()) {
                            currentInput = currentInput.substring(0, currentInput.length() - 1);
                        }
                    }
                    // If any other key is pressed, add it to the current input
                    default -> currentInput += (char) ch;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

