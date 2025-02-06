package gameset.functionality;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gameutils.Ansi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class Board {
    private ArrayList<Property> gameBoard;

    public Board() throws IOException {
        this.gameBoard = loadPropertyFile();
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

    private ArrayList<Property> loadPropertyFile() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/models/propertyData.json")))
        );
        String s = r.lines().reduce("", (prevLines, currLine) -> prevLines + "\n" + currLine);
        r.close();

        byte[] jsonData = s.getBytes();
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Property> properties = mapper.readValue(jsonData, new TypeReference<>() {});
        properties.forEach(property -> property.setOwner(""));
        return properties;
    }
}