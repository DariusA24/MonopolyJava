package gameset.functionality;


import gameutils.Ansi;
import gameutils.LoadProperties;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Board {
    public ArrayList<Property> gameBoard = new ArrayList<Property>();
    gameset.screens.Board boardScreen = new gameset.screens.Board();

    private void listBoard() {
      boardScreen.printBoard();
    }

    public void createBoard() throws FileNotFoundException {
        LoadProperties loadProperties = new LoadProperties();
        gameBoard = loadProperties.LoadPropertyList("src/main/java/models/propertyData.json");
        listBoard();
    }

    public void passedGo(Player player){
        player.setMoney(200);
        System.out.println("Player: ");
        player.displayColoredName();
        System.out.println(" Passed Go, Collect: " + Ansi.ANSI_GREEN + " $200 " + Ansi.ANSI_RESET);
    }

    public void updatedBoardLocation(int previousLocation, int newLocation, String playerColoredName){
       boardScreen.updatePlayerPositionOnBoard(gameBoard.get(previousLocation).getName(), gameBoard.get(newLocation).getName(), playerColoredName);
    }



}
