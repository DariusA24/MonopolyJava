package gameset.functionality;

import gameset.cards.ChanceCard;
import gameset.cards.CommunityChestCard;
import gameutils.Ansi;
import gameutils.ResourceParser;
import java.io.*;
import java.util.ArrayList;

public class Board {
    private final ArrayList<Property> gameBoard;
    private Deck<ChanceCard> chanceCards;
    private Deck<CommunityChestCard> communityChestCards;

    public Board() throws IOException {
        ResourceParser propertyParser = new ResourceParser("/models/propertyData.json");
        ResourceParser chanceCardParser = new ResourceParser("/models/chanceData.json");
        ResourceParser communityChestCardParser = new ResourceParser("/models/communityData.json");
        this.gameBoard = propertyParser.parseJsonToArrayList(Property.class);
        this.chanceCards = new Deck<>(chanceCardParser.parseJsonToArrayList(ChanceCard.class));
        this.communityChestCards = new Deck<>(communityChestCardParser.parseJsonToArrayList(CommunityChestCard.class));
    }

    public ArrayList<Property> getGameBoard() {
        return gameBoard;
    }

    public void listBoard() {
        new gameset.screens.Board().printBoard();
    }

    public void passedGo(Player player) {
        player.setMoney(200);
        System.out.println("Player: ");
        player.displayColoredName();
        System.out.println(" Passed Go, Collect: " + Ansi.ANSI_GREEN + " $200 " + Ansi.ANSI_RESET);
    }

    public void updatedBoardLocation(int previousLocation, int newLocation, String playerColoredName) {
        gameset.screens.Board boardScreen = new gameset.screens.Board();
        boardScreen.updatePlayerPositionOnBoard(
                this.gameBoard.get(previousLocation).getName(),
                this.gameBoard.get(newLocation).getName(),
                playerColoredName
        );
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
        int maxLen = size/11 - 1;
        char corner = '+';
        String dash = "-";
        char edge = '|';
        StringBuilder boardString = new StringBuilder();

        boardString.append(makeBorder(size, corner, dash));

        int row = 0;
        while (row <= 10){
            int propertyStartIdx = 20 - row;
            if (propertyStartIdx == 20) {
                for (int i = 0; i <= 10; i++) {
                    if (i == 10) {
                        boardString.append(surroundWithEdge(getProperty(propertyStartIdx + i).displayPropertyName(maxLen + 1), edge, false));
                    } else {
                        boardString.append(surroundWithEdge(getProperty(propertyStartIdx + i).displayPropertyName(maxLen), edge, true));
                    }
                }
                boardString.append(System.lineSeparator());
                // TODO: add players here, comma separated if multiple
                for (int i = 0; i <= 10; i++) {
                    if (i == 10) {
                        boardString.append(surroundWithEdge(centerString(" ", maxLen+1), edge, false));
                    } else {
                        boardString.append(surroundWithEdge(centerString(" ", maxLen), edge, true));
                    }
                }
                boardString.append(System.lineSeparator());
            } else if (propertyStartIdx == 10) {
                // Go to 0
                for (int i = 0; i <= 10; i++) {
                    if (i == 10) {
                        boardString.append(surroundWithEdge(getProperty(0).displayPropertyName(maxLen + 1), edge, false));
                    } else {
                        boardString.append(surroundWithEdge(getProperty(propertyStartIdx - i).displayPropertyName(maxLen), edge, true));
                    }
                }
                boardString.append(System.lineSeparator());
                // TODO: add players here, comma separated if multiple
                for (int i = 0; i <= 10; i++) {
                    if (i == 10) {
                        boardString.append(surroundWithEdge(centerString(" ", maxLen+1), edge, false));
                    } else {
                        boardString.append(surroundWithEdge(centerString(" ", maxLen), edge, true));
                    }
                }
                boardString.append(System.lineSeparator());
            } else {
                // TODO: fix bug off by 3 spaces??
                boardString.append(surroundWithEdge(getProperty(propertyStartIdx).displayPropertyName(maxLen), edge, false));
                boardString.append(" ".repeat(size - (maxLen+1) * 2 - 1));
                boardString.append(surroundWithEdge(getProperty(propertyStartIdx + (row + 5) * 2).displayPropertyName(maxLen+1), edge, false));
                boardString.append(System.lineSeparator());
                // TODO: add players here, comma separated if multiple
                boardString.append(surroundWithEdge(centerString(" ", maxLen), edge, false));
                boardString.append(" ".repeat(size - (maxLen+1) * 2 - 1));
                boardString.append(surroundWithEdge(centerString(" ", maxLen+1), edge, false));
                boardString.append(System.lineSeparator());
            }
            row++;
        }
        // Board
        // L0: 20 - 30 (free parking - go to jail)
        // L1: 19 & 31 + 12
        // L2: 18 & 32 + 14
        // L3: 17 & 33 + 16
        // L4: 16 & 34
        // L5: 15 & 35
        // L6: 14 & 36
        // L7: 13 & 37
        // L8: 12 & 38
        // L9: 11 & 39 (st charles & Boardwalk)
        // L10: 10 - 0 (GO -> JAIL)

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

    static String centerString(String str, int maxSize) {
        int spacer = maxSize - str.length();
        int leading = spacer / 2;
        int trailing = spacer - leading;
        return " ".repeat(leading) + str + " ".repeat(trailing);
    }
}
