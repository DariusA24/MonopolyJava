package gameset.functionality;

import gameset.cards.Action;
import gameset.cards.Card;
import gameset.cards.ChanceCard;
import gameset.cards.CommunityChestCard;
import gameutils.Ansi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private String name;
    private int playerMoney;
    private final String color;
    private int location;
    private final List<Property> properties = new ArrayList<Property>();
    private final Map<String, Boolean> colorSets = new HashMap<String, Boolean>();
    private boolean inJail;
    private boolean isBankrupt;
    private int doubleRollCounter;
    public int railRoadCounter;
    // Stores player cards, like get out of jail free
    private final List<Card> inventory = new ArrayList<>();

    public Player(String name, int money, String color) throws IOException {
        this.name = name;
        this.playerMoney = money;
        this.color = color;
        this.location = 0;
        this.inJail = false;
        this.isBankrupt = money < 0;
    }

    public String getName() {
        return (this.color + this.name + Ansi.ANSI_RESET);
    }

    public void displayColoredName() {
        System.out.println(getName());
    }

    public int getMoney() {
        return playerMoney;
    }

    public String getColor() {
        return color;
    }

    public int getLocation() {
        return location;
    }

    public boolean getJailStatus() {
        return inJail;
    }

    public int getDoubleRollCounter() {
        return doubleRollCounter;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setMoney(int newMoney) {
        this.playerMoney = newMoney;
        this.isBankrupt = playerMoney < 0;
    }

    public void setDoubleRollCounter(boolean rolledDouble) {
        if (rolledDouble) {
            this.doubleRollCounter++;
        } else {
            this.doubleRollCounter = 0;
        }
    }

    /**
     * Adds a property to the player property list.
     *
     * <p>This method is responsible for adding a property to the players list.
     * When adding the property it also calls colorSets.puts which adds the color and
     * creates a boolean determining whether the player has monopoly for that colorset.
     *
     * @param property the property that would be added
     * @param board the board object
     */
    public void addProperty(Property property, Board board) throws IOException {
        this.properties.add(property);
        property.setOwner(getNameNoColor());
        colorSets.put(property.getColor(), hasMonopoly(property, board));
    }

    /**
     * Checks if player can purchase buildings for the property.
     *
     * <p>This checks the players colorsets. If they own the color in the colorset,
     * it will then check the value to the color. If it is true, then that means
     * that they have monopoly.
     *
     * @param color the property that would be added
     */
    public boolean canPurchaseBuilding(String color){
        if(colorSets.containsKey(color)){
            return colorSets.get(color);
        }
        return false;
    }

    private long getPropertiesByColor(String color, Board board) throws IOException {
        return board.getPropertiesColorCount(color);
    }

    /**
     * Checks if player has monopoly.
     *
     * <p>Method checks the players properties and filters on the color.
     * It will then get the total amount of colors that the property has
     * and how many the player owns. If it is the same, then it will set
     * hasMonopoly to true.
     *
     * @param property the property that would be added
     * @param board the property that would be added
     */
    private boolean hasMonopoly(Property property, Board board) throws IOException {
        String color = property.getColor();

        long ownedColorProperties = properties.stream()
                .filter(p -> p.getColor().equals(color))
                .count();

        long totalColorProperties = getPropertiesByColor(color, board);

        return ownedColorProperties == totalColorProperties;
    }

    public void addMoney(int money) {
        this.playerMoney += money;
        this.isBankrupt = playerMoney < 0;
    }

    public void goToJail() {
        this.inJail = true;
    }

    public void leaveJail() {
        this.inJail = false;
    }

    /**
     * Updates the position of a player directly on a board (think go directly to jail, do not pass go etc.)
     * This is utilized by Chance/Community cards in certain scenarios. All other methods should use advancePosition
     * which takes into account passing go.
     * <p>
     * This method has a side effect of updating the board.
     *
     * @param newPosition the new position of the player
     * @param board the board
    */
    public void updatePosition(int newPosition, Board board) {
        this.location = newPosition;
        board.setPlayerPosition(this);
    }

    /**
     * Advances the player position on the board.
     * <p>
     * This method has a side effect of updating the board.
     *
     * @param newPosition the new position of the player
     * @param board the board
     */
    public void advancePosition(int newPosition, Board board) {
        if (this.location > newPosition || newPosition == 0) {
            // I must passed Go to get to new position
            board.passedGo(this);
        }
        this.updatePosition(newPosition, board);
    }

    public boolean checkBalance(int cost) {
        if (playerMoney - cost < 0) {
            System.out.println("Not enough money");
            System.out.println("This cost: " + cost + " your balance is: " + playerMoney);
            return false;
        }
        return true;
    }

    public void displayProperties(Game game) {
        int propertyCounter = 1;
        System.out.println("----------------------");
        System.out.println("Your properties are: ");
        for (Property property : properties) {
            System.out.println(propertyCounter + ". " + property.displayPropertyName());
            System.out.println(" - Rent: " + property.getRent(game.getDice().getRollTotal()) + "\n");
            propertyCounter++;
        }
        System.out.println("----------------------");
    }

    public boolean isBankrupt() {
        return this.isBankrupt;
    }

    private void purchasedRailroad() {
        railRoadCounter += 1;
    }

    private void lostRailroad() {
        railRoadCounter -= 1;
    }

    public String getNameNoColor() {
        return name;
    }

    public List<Card> getInventory() {
        return inventory;
    }

    public void addToInventory(Card c) {
        inventory.add(c);
    }

    public void removeFromInventory(Action action, Board b) {
        if (action == Action.GetOutOfJailCard) {
            for (int i = 0; i < inventory.size(); i++) {
                Card c = inventory.get(i);
                if (c.getAction() == Action.GetOutOfJailCard) {
                    inventory.remove(i);
                    if (c instanceof ChanceCard) {
                        b.getChanceCards().addToBottom((ChanceCard) c);
                    }
                    if (c instanceof CommunityChestCard) {
                        b.getCommunityChestCards().addToBottom((CommunityChestCard) c);
                    }
                    break;
                }
            }
            // TODO: once card is played, It should be re-added to the deck
        }
    }

    public boolean hasGetOutOfJailCard() {
        return inventory.stream().anyMatch(c -> c.getAction() == Action.GetOutOfJailCard);
    }
}
