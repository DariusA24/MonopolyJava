package gameset.functionality;

import gameset.cards.ChanceCard;
import gameset.cards.CommunityChestCard;
import gameutils.Ansi;
import gameutils.ResourceParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Board {
    private final HashMap<String, Integer> playerPositions = new HashMap<>();
    private final ArrayList<Property> gameBoard;
    private final Deck<ChanceCard> chanceCards;
    private final Deck<CommunityChestCard> communityChestCards;

    public Board(ArrayList<Player> players) throws IOException {
        ResourceParser propertyParser = new ResourceParser("/models/propertyData.json");
        ResourceParser chanceCardParser = new ResourceParser("/models/chanceData.json");
        ResourceParser communityChestCardParser = new ResourceParser("/models/communityData.json");
        this.gameBoard = propertyParser.parseJsonToArrayList(Property.class);
        this.chanceCards = new Deck<>(chanceCardParser.parseJsonToArrayList(ChanceCard.class));
        this.communityChestCards = new Deck<>(communityChestCardParser.parseJsonToArrayList(CommunityChestCard.class));
        players.forEach(p -> playerPositions.put(p.getNameNoColor(), 0));
    }

    public ArrayList<Property> getGameBoard() {
        return gameBoard;
    }

    public void passedGo(Player player) {
        player.setMoney(200);
        System.out.println("Player: ");
        player.displayColoredName();
        System.out.println(" Passed Go, Collect: " + Ansi.ANSI_GREEN + " $200 " + Ansi.ANSI_RESET);
    }

    public Property getProperty(int location) {
        return this.gameBoard.get(location);
    }

    public Deck<ChanceCard> getChanceCards() {
        return chanceCards;
    }

    public Deck<CommunityChestCard> getCommunityChestCards() {
        return communityChestCards;
    }

    @Override
    public String toString() {
        int size = 220;
        int maxLen = size / 11 - 1;
        char corner = '+';
        String dash = "-";
        char edge = '|';
        StringBuilder boardString = new StringBuilder();

        boardString.append(makeBorder(size, corner, dash));

        int row = 0;
        while (row <= 10) {
            int propertyStartIdx = 20 - row;
            if (propertyStartIdx == 20) {
                for (int i = 0; i <= 10; i++) {
                    if (i == 10) {
                        boardString.append(
                                surroundWithEdge(
                                        getProperty(propertyStartIdx + i).displayPropertyName(maxLen + 1),
                                        edge,
                                        false
                                )
                        );
                    } else {
                        boardString.append(
                                surroundWithEdge(
                                        getProperty(propertyStartIdx + i).displayPropertyName(maxLen),
                                        edge,
                                        true
                                )
                        );
                    }
                }
                boardString.append(System.lineSeparator());

                // Add players
                for (int i = 0; i <= 10; i++) {
                    if (i == 10) {
                        boardString.append(
                                surroundWithEdge(
                                        centerString(
                                                printPlayersOnProperty(propertyStartIdx + i, maxLen),
                                                maxLen + 1
                                        ),
                                        edge,
                                        false
                                )
                        );
                    } else {
                        boardString.append(
                                surroundWithEdge(
                                        centerString(
                                                printPlayersOnProperty(propertyStartIdx + i, maxLen),
                                                maxLen
                                        ),
                                        edge,
                                        true
                                )
                        );
                    }
                }
                boardString.append(System.lineSeparator());
            } else if (propertyStartIdx == 10) {
                for (int i = 0; i <= 10; i++) {
                    if (i == 10) {
                        boardString.append(
                                surroundWithEdge(
                                        getProperty(0).displayPropertyName(maxLen + 1),
                                        edge,
                                        false
                                )
                        );
                    } else {
                        boardString.append(
                                surroundWithEdge(
                                        getProperty(propertyStartIdx - i).displayPropertyName(maxLen),
                                        edge,
                                        true
                                )
                        );
                    }
                }
                boardString.append(System.lineSeparator());

                // Add players
                for (int i = 0; i <= 10; i++) {
                    if (i == 10) {
                        boardString.append(
                                surroundWithEdge(
                                        centerString(printPlayersOnProperty(0, maxLen), maxLen + 1),
                                        edge,
                                        false
                                )
                        );
                    } else {
                        boardString.append(
                                surroundWithEdge(
                                        centerString(printPlayersOnProperty(propertyStartIdx - i, maxLen), maxLen),
                                        edge,
                                        true
                                )
                        );
                    }
                }
                boardString.append(System.lineSeparator());
            } else {
                boardString.append(surroundWithEdge(getProperty(propertyStartIdx).displayPropertyName(maxLen), edge, false));
                boardString.append(" ".repeat(size - (maxLen + 1) * 2 - 1));
                boardString.append(surroundWithEdge(getProperty(propertyStartIdx + (row + 5) * 2).displayPropertyName(maxLen + 1), edge, false));
                boardString.append(System.lineSeparator());

                // Add players
                boardString.append(
                        surroundWithEdge(
                                centerString(printPlayersOnProperty(propertyStartIdx, maxLen), maxLen),
                                edge,
                                false
                        )
                );
                boardString.append(" ".repeat(size - (maxLen + 1) * 2 - 1));
                boardString.append(
                        surroundWithEdge(
                                centerString(printPlayersOnProperty(propertyStartIdx + (row + 5) * 2, maxLen), maxLen + 1),
                                edge,
                                false
                        )
                );
                boardString.append(System.lineSeparator());
            }
            row++;
        }
        boardString.append(makeBorder(size, corner, dash));
        return boardString.toString();
    }

    private String makeBorder(int size, char corner, String dash) {
        return corner +
                dash.repeat(size) +
                corner +
                System.lineSeparator();
    }

    private String surroundWithEdge(String str, char edge, boolean skipRight) {
        if (skipRight) {
            return edge + str;
        }
        return edge + str + edge;
    }

    private String printPlayersOnProperty(int propertyIdx, int maxSize) {
        StringBuilder playerString = new StringBuilder();
        for (HashMap.Entry<String, Integer> entry : getPlayerPositions().entrySet()) {
            if (entry.getValue() == propertyIdx) {
                playerString.append(entry.getKey()).append(", ");
            }
        }

        // remove trailing comma
        if (!playerString.isEmpty()) {
            playerString.setLength(playerString.length() - 2);
        }

        if (playerString.length() > maxSize) {
            // Show only first letter of each name (can improve later)
            String[] names = playerString.toString().split(", ");
            for (int i = 0; i < names.length; i++) {
                names[i] = Character.toString(names[i].charAt(0));
            }
            return String.join(", ", names);
        }

        return playerString.toString();
    }

    static String centerString(String str, int maxSize) {
        int spacer = maxSize - str.length();
        int leading = spacer / 2;
        int trailing = spacer - leading;
        return " ".repeat(leading) + str + " ".repeat(trailing);
    }

    public HashMap<String, Integer> getPlayerPositions() {
        return playerPositions;
    }

    public void setPlayerPosition(Player p) {
        this.playerPositions.put(p.getNameNoColor(), p.getLocation());
    }
}