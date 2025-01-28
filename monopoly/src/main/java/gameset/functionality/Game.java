package gameset.functionality;

import gameset.screens.GameScreen;
import gameutils.Ansi;

import java.io.FileNotFoundException;
import java.util.*;

public class Game {
    private ArrayList<Player> playerList = new ArrayList<Player>();
    private final Dice dice = new Dice();
    private Scanner userInput = new Scanner(System.in);
    private boolean rollAgain = false;
    private GameScreen gameScreen = new GameScreen();
    private GameInitializer gameInitializer = new GameInitializer();



    /**
     * This method will be used to list the players stats. Having this as a
     * placeholder method to test.
     */
    private void listPlayers() {
        System.out.println("-----------------------");
        System.out.println("Players are: ");
        int playerCount = 1;
        for(Player player: playerList){
            System.out.println("Player #" + playerCount + ": " +  player.getColor() + player.getName() + Ansi.ANSI_RESET
            + Ansi.ANSI_GREEN +  " Cash: " + Ansi.ANSI_RESET + player.getMoney());
            playerCount++;
        }
        System.out.println("-----------------------");
    }

    /**
     * Purchases the hotel / building if able
     * @param property
     * @param player
     */
    private void purchaseBuildings(Property property, Player player) {
        if (player.canPurchaseProperty(property.getColor())) {
           int cost = property.getBuildingPrice();
           if (cost - player.getMoney() >= 0) {
               player.setMoney(player.getMoney() - cost);
               property.buildHouse();
               System.out.println("You purchased your " + property.getNumHouses() + " houses of " + property.displayPropertyName());
           }
        }
        else {
            System.out.println("Unable to purchase building due to you not having all colorset for: " + property.getColor());
        }
    }

    private void displayPropertyScreen(Player player) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("******************");
        System.out.println("Select property number you would like to view more detail on OR f to exit");
        boolean flag = false;
        while (!flag) {
            String input = scanner.nextLine();
            if (input.equals("F") || input.equals("f")) {
                flag = true;
                continue;
            }
           try {
               int number = Integer.parseInt(input);
               if (number >= 1 && number <= player.getProperties().size()) {
                   Property property = player.getProperties().get(number - 1);
                   System.out.println("Property #" + number + ": " +  property.displayPropertyName());
                   property.displayPropertyInfo();
                   if (property.getNumHouses() != 5){
                       System.out.println("You currently own: " + property.getNumHouses() + " houses");
                       System.out.println("Press B to purchase next building for: $" + property.getBuildingPrice());
                       while(true) {
                           input = scanner.nextLine();
                           if(input.equals("B") || input.equals("b")){
                               purchaseBuildings(property, player);
                           }
                           else {
                               break;
                           }
                       }
                   }
                   else if (property.getNumHouses() == 5){
                       System.out.println("You currently own a hotel on this property");
                   }
                  break;
               }
           }
           catch (NumberFormatException e) {
               System.out.println("Please enter a valid number");

           }
        }
        scanner.close();
        System.out.println("******************");
    }

    /**
     *
     * @param property
     * @param player
     */
    private void purchaseProperty(Property property, Player player) {
        System.out.println("Your balance is: " + Ansi.ANSI_GREEN + player.getMoney() + Ansi.ANSI_RESET);
        System.out.println("Press P to purchase: ");
        System.out.println("Press F to skip: ");
        String input = userInput.next();
        System.out.println("******************");
        boolean flag = false;
        while (!flag){
            if(input.equals("P") || input.equals("p")) {
                if(player.checkBalance(property.getPrice())){
                    player.addProperty(property);
                    player.setMoney(player.getMoney() - property.getPrice());
                    System.out.println("Purchasing: " + property.displayPropertyName());
                    System.out.println("Player balance is: " + Ansi.ANSI_GREEN + player.getMoney() + Ansi.ANSI_RESET);
                    property.setOwner(player.getName());
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
     *
     * @param property
     * @param player
     */
    private void viewProperty(Property property, Player player) {
        if(property.getOwner().isEmpty()){
            System.out.println("Property is not owned");
            System.out.println("Press E to view details about the property");
            String input = userInput.next();
            if(input.equals("E") || input.equals("e")) {
                property.displayProperty(property);
                purchaseProperty(property, player);
            }
        }
        else{
            System.out.println("Property is owned by: " + property.getOwner());
            System.out.println("Rent is: " + property.getRent());
            System.out.println("Transaction details: " + player.getMoney() +  " - " + property.getRent());
            player.setMoney(player.getMoney() - property.getRent());

        }
    }

    /**
     * TODO I would like to have less parameters being passed through this.
     * @param landingSpot
     * @param player
     * @param board
     */
    private void handlePlayerLanding(int landingSpot, Player player, Board board) {
        Property property = board.gameBoard.get(landingSpot);
        System.out.println(player.getColor() + player.getName() + Ansi.ANSI_RESET + " Landed on " + property.displayPropertyName());
        System.out.println("---------------");
        String propertyType = property.getType();
        if (propertyType.equals("property")){
            viewProperty(board.gameBoard.get(landingSpot), player);
        } else if (propertyType.equals("railroad")) {
            System.out.println("Landed on a railroad");

        } else if (propertyType.equals("tax")) {
            System.out.println("Landed on tax");

        } else if (propertyType.equals("card")) {
            System.out.println("Landed on a card");

        }
        else {
            System.out.println("Landed on a space");
        }
        System.out.println("---------------");
    }

    private boolean handleDoubleRoll(Player player){
        player.setDoubleRollCounter(rollAgain);
        return player.getDoubleRollCounter() == 3;
    }

    private void handlePlayerInJail(Player player) {
        String choice = gameScreen.jailScreen();
        boolean jailChoiceFlag = true;
        while(jailChoiceFlag)
            if(choice.equals("1")) {
                dice.rollDice(player);
                 if(dice.doubles) {
                  player.displayColoredName();
                  System.out.println("Rolled doubles and escaped jail!");
                  player.leaveJail();
                  }
                 else {
                 player.displayColoredName();
                  System.out.println("Did not roll doubles and is still in jail.");
                }
                jailChoiceFlag = false;
             }
            else {
                if (player.getMoney() - 50 < 0) {
                  player.displayColoredName();
                  System.out.println("Current balance: ");
                   System.out.println(player.getMoney());
                   System.out.println("Not enough funds to get out of jail. Please roll.");
                   choice = "1";
             }
                 else {
                   player.displayColoredName();
                  System.out.println("New balance: ");
                  player.addMoney(-50);
                  System.out.println(player.getMoney());
                  player.leaveJail();
                  jailChoiceFlag = false;
             }
        }
    }

    /**
     *
     * @param player
     * @return where the player landed on the board
     */
    private int getLandingSpot(Player player, Board board) {
        int roll = dice.rollDice(player);
        rollAgain = dice.doubles;
        int landingSpot = player.getLocation() + roll;
        if(handleDoubleRoll(player)){
            System.out.println("Rolled doubles three times - go to jail: ");
            player.goToJail();
            return 10;
        }
        System.out.println("Player location: " + player.getLocation());
        System.out.println("Roll: " + roll);

        if(landingSpot > 39){
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
     * TODO: Look into not having to pass the entire board into the function
     * @param player
     * @param board
     */
    private void playerTurn(Player player, Board board) {
        if (player.getJailStatus()) {
            handlePlayerInJail(player);
        }
        else {
            int landingSpot = getLandingSpot(player, board);
            board.updatedBoardLocation(player.getLocation(), landingSpot, player.getName());
            player.updatePosition(landingSpot);
            handlePlayerLanding(landingSpot, player, board);
        }
        String choice = gameScreen.turnMenu();
        while(!choice.equals("1")){
            if(choice.equals("2")){
                player.displayProperties();
                if(!player.getProperties().isEmpty()) {
                    displayPropertyScreen(player);
                }
            }
            if(choice.equals("3")){
                System.out.println("Choice not yet made");
            }
           choice = gameScreen.turnMenu();
        }
        System.out.println("Turn ending.");
        System.out.println("**************");
    }

    /**
     *
     * @param playerAmount
     * @throws FileNotFoundException
     */
    public void gameLoop(int playerAmount) throws FileNotFoundException {
        boolean playGame = true;
        int turnTrack = 0;
        Board board = new Board();
        board.createBoard();
        playerList = gameInitializer.setPlayers(playerAmount);
        listPlayers();
        System.out.println(Ansi.ANSI_BLUE + "********** GAME IS STARTING **********" + Ansi.ANSI_RESET);

        //Main game loop
        while(playGame){
            Player player = playerList.get(turnTrack);
            System.out.println(Ansi.ANSI_WHITE + "**********" + player.getName().toUpperCase() + " TURN" + "**********" + Ansi.ANSI_WHITE);
            playerTurn(player, board);

            while(rollAgain){
                System.out.println(player.getColor() + player.getName() + Ansi.ANSI_RESET + " Rolled Doubles. It is your turn again.");
                playerTurn(player, board);
            }
            if(playerList.size() <= 1){
                playGame = false;
            }
            else if(turnTrack == playerList.size() - 1) {
                turnTrack = 0;
            }
            else turnTrack++;
        }
    }


}
