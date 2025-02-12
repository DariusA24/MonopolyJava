package gameset.functionality;

import gameset.cards.ChanceCard;
import gameset.cards.CommunityChestCard;
import gameutils.Ansi;
import gameutils.ResourceParser;
import java.io.*;
import java.util.ArrayList;

public class Board {
    private final ArrayList<Property> gameBoard;
    // TODO: GH issue #26 (Card decks - explore Collections.shuffle method or create Deck class)
    private ArrayList<ChanceCard> chanceCards;
    private ArrayList<CommunityChestCard> communityChestCards;

    public Board() throws IOException {
        ResourceParser propertyParser = new ResourceParser("/models/propertyData.json");
        ResourceParser chanceCardParser = new ResourceParser("/models/chanceData.json");
        ResourceParser communityChestCardParser = new ResourceParser("/models/communityData.json");
        this.gameBoard = propertyParser.parseJsonToArrayList(Property.class);
        this.chanceCards = chanceCardParser.parseJsonToArrayList(ChanceCard.class);
        this.communityChestCards = communityChestCardParser.parseJsonToArrayList(CommunityChestCard.class);
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
}
