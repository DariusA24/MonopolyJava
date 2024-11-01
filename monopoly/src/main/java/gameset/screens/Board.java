package gameset.screens;

public class Board {
   String monopolyBoard =  " _______________________________________________________\n" +
            "|                     |                     |                     |\n" +
            "|  GO                 |  Mediterranean Ave. |  Community Chest    |\n" +
            "|_____________________|_____________________|_____________________|\n" +
            "|                     |                     |                     |\n" +
            "|  Baltic Ave.        |  Income Tax          |  Reading Railroad   |\n" +
            "|_____________________|_____________________|_____________________|\n" +
            "|                     |                     |                     |\n" +
            "|  Oriental Ave.      |  Chance              |  Vermont Ave.       |\n" +
            "|_____________________|_____________________|_____________________|\n" +
            "|                     |                     |                     |\n" +
            "|  Connecticut Ave.   |  Jail                |  St. Charles Place  |\n" +
            "|_____________________|_____________________|_____________________|\n" +
            "|                     |                     |                     |\n" +
            "|  Electric Company   |  States Ave.         |  Virginia Ave.      |\n" +
            "|_____________________|_____________________|_____________________|\n" +
            "|                     |                     |                     |\n" +
            "|  Pennsylvania Ave.  |  St. James Place     |  Community Chest    |\n" +
            "|_____________________|_____________________|_____________________|\n" +
            "|                     |                     |                     |\n" +
            "|  Tennessee Ave.     |  New York Ave.       |  Free Parking       |\n" +
            "|_____________________|_____________________|_____________________|\n" +
            "|                     |                     |                     |\n" +
            "|  Kentucky Ave.      |  Indiana Ave.        |  Illinois Ave.      |\n" +
            "|_____________________|_____________________|_____________________|\n" +
            "|                     |                     |                     |\n" +
            "|  B&O Railroad       |  Atlantic Ave.       |  Ventnor Ave.       |\n" +
            "|_____________________|_____________________|_____________________|\n" +
            "|                     |                     |                     |\n" +
            "|  Water Works        |  Marvin Gardens      |  Go to Jail         |\n" +
            "|_____________________|_____________________|_____________________|\n" +
            "|                     |                     |                     |\n" +
            "|  Pacific Ave.       |  North Carolina Ave. |  Community Chest    |\n" +
            "|_____________________|_____________________|_____________________|\n" +
            "|                     |                     |                     |\n" +
            "|  Pennsylvania Ave.  |  Pennsylvania Railroad |  Park Place      |\n" +
            "|_____________________|_____________________|_____________________|\n" +
            "|                     |                     |                     |\n" +
            "|  Luxury Tax         |  Boardwalk           |  Chance             |\n" +
            "|_____________________|_____________________|_____________________|";
    public void printBoard() {
        System.out.println(monopolyBoard);
    }

    public void updatePlayerPositionOnBoard(String previousLocation, String playerLocation, String playerColoredName) {
        // Replace specific positions with desired text
        monopolyBoard = monopolyBoard
                .replace(previousLocation + " " + playerColoredName, previousLocation)
                .replace( playerLocation, playerLocation + " " + playerColoredName);
        printBoard();
    }
}
