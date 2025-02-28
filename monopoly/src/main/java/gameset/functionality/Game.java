package gameset.functionality;

import gameset.screens.BuildingScreen;
import gameset.cards.ChanceCard;
import gameset.cards.CommunityChestCard;
import gameset.screens.GameScreen;
import gameutils.Ansi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Game {
    private ArrayList<Player> playerList = new ArrayList<Player>();
    private final Dice dice = new Dice();
    private Scanner userInput = new Scanner(System.in);
    private boolean rollAgain = false;
    private GameScreen gameScreen = new GameScreen();
    private BuildingScreen buildingScreen = new BuildingScreen();
    private GameInitializer gameInitializer = new GameInitializer();
    private Board board;

    public Dice getDice() {
        return dice;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    /**
     * This method will be used to list the players stats. Having this as a
     * placeholder method to test.
     */
    private void listPlayers() {
        System.out.println("-----------------------");
        System.out.println("Players are: ");
        int playerCount = 1;
        for (Player player : playerList) {
            System.out.println("Player #" + playerCount + ": " + player.getColor() + player.getName() + Ansi.ANSI_RESET
                    + Ansi.ANSI_GREEN + " Cash: " + Ansi.ANSI_RESET + player.getMoney());
            playerCount++;
        }
        System.out.println("-----------------------");
    }

    /**
     * Checks if player is able to purchase a building on the property.
     *
     * <p>This method checks a players properties to see if they are able to purchase
     * a property. First it checks if the player owns all the colorsets, then it checks the players' money.
     *
     * @param player the player object
     * @param property object which the building will be added to
     */
    private void purchaseBuildings(Property property, Player player) {
        if (player.canPurchaseBuilding(property.getColor())) {
            int cost = property.getBuildingPrice();
            if (player.getMoney() - cost >= 0) {
                player.setMoney(player.getMoney() - cost);
                property.buildBuilding();
                System.out.println("You purchased your " + property.getNumHouses() + " houses of " + property.displayPropertyName());
            }
            else {
                System.out.println("Unable to purchase building due to insufficient funds");
            }
        }
        else {
            System.out.println("Unable to purchase building due to you not having all colorset for: " + property.getColor());
        }
    }

    /**
     * @param property
     * @param player
     */
    private void purchaseProperty(Property property, Player player) throws IOException {
        System.out.println("Your balance is: " + Ansi.ANSI_GREEN + player.getMoney() + Ansi.ANSI_RESET);
        System.out.println("Press P to purchase: ");
        System.out.println("Press F to skip: ");
        System.out.println("******************");
        boolean flag = false;
        while (!flag) {
            String input = userInput.next();
            if (input.equals("P") || input.equals("p")) {
                if (player.checkBalance(property.getPrice())) {
                    player.addProperty(property, board);
                    player.setMoney(player.getMoney() - property.getPrice());
                    System.out.println("Purchasing: " + property.displayPropertyName());
                    System.out.println("Player balance is: " + Ansi.ANSI_GREEN + player.getMoney() + Ansi.ANSI_RESET);
                }
                flag = true;


            } else if (input.equals("F") || input.equals("f")) {
                System.out.println("Property not purchased");
                flag = true;
            } else {
                System.out.println("Please enter a valid input");
            }
            System.out.println("******************");
        }

    }

    /**
     * @param property
     * @param player
     */
    private void viewProperty(Property property, Player player) throws IOException {
        if (property.getOwner().isEmpty()) {
            System.out.println("Property is not owned");
            System.out.println("Press E to view details about the property");
            String input = userInput.next();
            if (input.equals("E") || input.equals("e")) {
                System.out.println(property);
                purchaseProperty(property, player);

            }
        } else {
            int rentDue = property.getRent(this.dice.getRollTotal());
            System.out.println("Property is owned by: " + property.getOwner());
            System.out.println("Rent is: " + rentDue);
            System.out.println("Transaction details: " + player.getMoney() + " - " + rentDue);
            player.setMoney(player.getMoney() - rentDue);

        }
    }

    /**
     *
     * @param landingSpot
     * @param player
     */
    private void handlePlayerLanding(int landingSpot, Player player) throws IOException {
        Property property = board.getProperty(landingSpot);
        System.out.println(player.getColor() + player.getName() + Ansi.ANSI_RESET + " Landed on " + property.displayPropertyName());
        System.out.println("---------------");
        String propertyType = property.getType();
        switch (propertyType) {
            case "utility", "property", "railroad" -> viewProperty(property, player);
            case "tax" -> System.out.println("Landed on tax");
            case "card" -> {
                switch (property.getName().toLowerCase()) {
                    case "chance" -> {
                        ChanceCard c = board.getChanceCards().draw();
                        System.out.println(c);
                        c.applyEffect(player, this);
                    }
                    case "community chest" -> {
                        CommunityChestCard c = board.getCommunityChestCards().draw();
                        System.out.println(c);
                        c.applyEffect(player, this);
                    }
                }
            }
            default -> System.out.println("Landed on a space");
        }
        System.out.println("---------------");
    }

    private boolean handleDoubleRoll(Player player) {
        player.setDoubleRollCounter(rollAgain);
        return player.getDoubleRollCounter() == 3;
    }

    private void handlePlayerInJail(Player player) {
        String choice = gameScreen.jailScreen(userInput);
        boolean jailChoiceFlag = true;
        while (jailChoiceFlag)
            if (choice.equals("1")) {
                dice.rollDice(player);
                if (dice.isDoubles()) {
                    player.displayColoredName();
                    System.out.println("Rolled doubles and escaped jail!");
                } else {
                    player.displayColoredName();
                    System.out.println("Did not roll doubles and is still in jail.");
                }
                jailChoiceFlag = false;
            } else {
                if (player.getMoney() - 50 < 0) {
                    player.displayColoredName();
                    System.out.println("Current balance: ");
                    System.out.println(player.getMoney());
                    System.out.println("Not enough funds to get out of jail. Please roll.");
                    choice = "1";
                } else {
                    player.displayColoredName();
                    System.out.println("New balance: ");
                    player.addMoney(-50);
                    System.out.println(player.getMoney());
                    jailChoiceFlag = false;
                }
            }
    }

    /**
     * @param player
     * @return where the player landed on the board
     */
    private int getLandingSpot(Player player) {
        int roll = dice.rollDice(player);
        rollAgain = dice.isDoubles();
        int landingSpot = player.getLocation() + roll;
        if (handleDoubleRoll(player)) {
            System.out.println("Rolled doubles three times - go to jail: ");
            player.goToJail();
            return 10;
        }
        System.out.println("Player location: " + player.getLocation());
        System.out.println("Roll: " + roll);

        if (landingSpot > 39) {
            landingSpot = landingSpot - 39;
            board.passedGo(player);
        } else if (landingSpot == 10) {
            System.out.println("Landed on JAIL: ");
            player.goToJail();
        }

        System.out.println("Landing spot: " + landingSpot);
        return landingSpot;
    }


    /**
     *
     * @param player
     */
    private void playerTurn(Player player) throws IOException {
        // TODO: We can add check if bankrupt here to handle mortgages first
        // Then we can fall into the if/else logic
        if (player.getJailStatus()) {
            handlePlayerInJail(player);
        } else {
            int landingSpot = getLandingSpot(player);
            player.updatePosition(landingSpot, board);
            System.out.println(board);
            handlePlayerLanding(landingSpot, player);
        }
        String choice = gameScreen.turnMenu(userInput);
        while (!choice.equals("1")) {
            if (choice.equals("2")) {
                player.displayProperties(this);
                if (!player.getProperties().isEmpty()) {
                    int purchasedBuilding = buildingScreen.displayPropertyScreen(player, userInput);
                    if (purchasedBuilding != 0) {
                        Property property = player.getProperties().get(purchasedBuilding - 1);
                        purchaseBuildings(property, player);
                    }
                }
            }
            if (choice.equals("3")) {
                System.out.println("Choice not yet made");
            }
            choice = gameScreen.turnMenu(userInput);
        }
        System.out.println("Turn ending.");
        System.out.println("**************");
    }

    /**
     * @param playerAmount
     * @throws FileNotFoundException
     */
    public void gameLoop(int playerAmount) throws IOException {
        boolean playGame = true;
        int turnTrack = 0;
        playerList = gameInitializer.setPlayers(playerAmount);
        listPlayers();
        this.setBoard(new Board(playerList));
        System.out.println(board);
        System.out.println(Ansi.ANSI_BLUE + "********** GAME IS STARTING **********" + Ansi.ANSI_RESET);

        //Main game loop
        while (playGame) {
            Player player = playerList.get(turnTrack);
            System.out.println(Ansi.ANSI_WHITE + "**********" + player.getName().toUpperCase() + " TURN" + "**********" + Ansi.ANSI_WHITE);
            playerTurn(player);

            while (rollAgain) {
                System.out.println(player.getColor() + player.getName() + Ansi.ANSI_RESET + " Rolled Doubles. It is your turn again.");
                playerTurn(player);
            }
            if (playerList.size() <= 1) {
                playGame = false;
            } else if (turnTrack == playerList.size() - 1) {
                turnTrack = 0;
            } else turnTrack++;
        }
    }
}
