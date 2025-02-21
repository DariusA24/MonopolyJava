package gameset.functionality;

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

    private int doubleRollCounter;
    public int railRoadCounter;

    public Player(String name, int money, String color) throws IOException {
        this.name = name;
        this.playerMoney = money;
        this.color = color;
        this.location = 0;
        this.inJail = false;
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
    }

    public void setDoubleRollCounter(boolean rolledDouble) {
        if (rolledDouble) {
            this.doubleRollCounter++;
        } else {
            this.doubleRollCounter = 0;
        }
    }

    public void addProperty(Property property, Board board) {
        this.properties.add(property);
    }

    public boolean canPurchaseBuilding(String color){
        if(colorSets.containsKey(color)){
            return colorSets.get(color);
        }
        return false;
    }

    private long getPropertiesByColor(String color, Board board) throws IOException {
        return board.getPropertiesColorCount(color);
    }

    private boolean hasMonopoly(Property property, Board board) throws IOException {
        String color = property.getColor();

        long ownedColorProperties = properties.stream()
                .filter(p -> p.getColor().equals(color))
                .count();

        long totalColorProperties = (long) getPropertiesByColor(color, board);

        return ownedColorProperties == totalColorProperties;
    }

    public void addMoney(int money) {
        this.playerMoney += money;
    }

    public void goToJail() {
        this.inJail = true;
    }

    public void leaveJail() {
        this.inJail = false;
    }

    public void updatePosition(int newPosition) {
        this.location = newPosition;
    }

    public boolean checkBalance(int cost) {
        if (playerMoney - cost < 0) {
            System.out.println("Not enough money");
            System.out.println("This cost: " + cost + " your balance is: " + playerMoney);
            return false;
        }
        return true;
    }

    public void displayProperties(Board board) {
        int propertyCounter = 1;
        System.out.println("----------------------");
        System.out.println("Your properties are: ");
        for (Property property : properties) {
            System.out.println(propertyCounter + ". " + property.displayPropertyName());
            System.out.println(" - Rent: " + property.getRent() + "\n");
            propertyCounter++;
        }
        System.out.println("----------------------");
    }

    public boolean isBankrupt() {
        return playerMoney < 0;
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
}
