package gameset.functionality;

import gameutils.Ansi;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private int playerMoney;

    private final String color;

    private int location;
    private final List<Property> properties = new ArrayList<Property>();

    private boolean inJail;

    private int doubleRollCounter;
    public int railRoadCounter;
    

    public Player(String name, int money, String color){
        this.name = name;
        this.playerMoney = money;
        this.color = color;
        this.location = 0;
        this.inJail = false;
    }

    public String getName(){
        return (this.color + this.name + Ansi.ANSI_RESET);
    }

    public void displayColoredName() {
        System.out.println(getName());
    }

    public int getMoney(){
        return playerMoney;
    }

    public String getColor() {return color; }

    public int getLocation() {return location; }

    public boolean getJailStatus() { return inJail; }

    public int getDoubleRollCounter() {return doubleRollCounter; }

    public List<Property> getProperties(){
        return properties;
    }

    public void setMoney(int newMoney){
        this.playerMoney = newMoney;
    }

    public void setDoubleRollCounter(boolean rolledDouble){
        if(rolledDouble){
            this.doubleRollCounter++;
        }
        else {
            this.doubleRollCounter = 0;
        }
    }

    public void addProperty(Property property){
        this.properties.add(property);
    }

    public void addMoney(int money){
        this.playerMoney += money;
    }

    public void goToJail(){
        this.inJail = true;
    }

    public void leaveJail() {this.inJail = false; }
    
    public void updatePosition(int newPosition){
        this.location = newPosition;
    }

    public boolean checkBalance(int cost){
        if(playerMoney - cost < 0){
            System.out.println("Not enough money");
            System.out.println("This cost: " + cost + " your balance is: " + playerMoney);
            return false;
        }
        return true;
    }

    public void displayPropertes(){
        System.out.println("----------------------");
        System.out.println("Your properties are: ");
        for(Property property : properties){
            System.out.println(property.displayPropertyName(property));
            System.out.println(" - Rent: " + property.getRent() + "\n");
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






}
