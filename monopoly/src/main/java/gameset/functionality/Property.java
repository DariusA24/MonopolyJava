package gameset.functionality;

import gameset.builder.PropertyBuilder;
import gameutils.Ansi;

import java.util.ArrayList;

public class Property {

    private final String name;
    private final String type;
    private final String color;
    private final int price;
    private final int mortgage;
    private int rent;
    private String owner;
    private int numHouses;
    private boolean hasHotel;
    private final ArrayList<Integer> buildingPrices;

    // Constants
    private static final int MAX_HOUSES = 4;

    // Private constructor that is called by the builder
    public Property(PropertyBuilder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.price = builder.price;
        this.color = builder.color;
        this.owner = builder.owner;
        this.rent = builder.rent;
        this.buildingPrices = builder.buildingPrices;
        this.mortgage = builder.mortgage;
    }

    public String getName(){
        return this.name;
    }
    public int getPrice(){
        return this.price;
    }
    public int getRent(){
        return this.rent;
    }
    public String getOwner() {return this.owner; }
    public String getType() {return this.type; }
    public String getColor() {return this.color; }
    public int getMortgage() { return this.mortgage; }
    public int getNumHouses() { return this.numHouses; }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    //TODO - don't pass in property object
    public void displayProperty(Property property){
        System.out.println("----------------");
        System.out.println("Property: " + displayPropertyName());
        System.out.println("Price: " + property.price);
        System.out.println("Rent: " + property.rent);
        System.out.println("Owned: " + property.owner);
        System.out.println("----------------");
    }

    //TODO - don't pass in property object
    public String displayPropertyName(){
        return (getPropertyAnsiColor() + this.getName() + Ansi.ANSI_RESET);
    }

    public void displayPropertyInfo() {
        int houseCount = 1;
        System.out.println("---------------");
        System.out.println("Name: " + getName());
        System.out.println("Type: " + getType());
        System.out.println("Color: " + getColor());
        System.out.println("Price: " + getPrice());
        System.out.println("Mortgage: " + getMortgage());
        System.out.println("Rent: " + getRent());
        for (Integer buildingPrice : buildingPrices) {
            if (houseCount < MAX_HOUSES){
                System.out.println("Rent with " + houseCount + " house: " + buildingPrice);
            }
            else {
                System.out.println("Rent with hotel: " + buildingPrice);
            }
            houseCount++;
        }
        System.out.println("---------------");

    }

    //TODO - don't pass in property object
    public String getPropertyAnsiColor(){
        Ansi ansi = new Ansi();
        return ansi.propertyToAnsiColor(color);
    }

    public int getBuildingPrice() {
        return switch (color) {
            case "brown", "cyan" -> 50;
            case "pink", "orange" -> 100;
            case "red", "yellow" -> 150;
            case "green", "blue" -> 200;
            default -> 0;
        };
    }

    public void buildHouse() {
        if (numHouses < 4 && !hasHotel) {
            numHouses++;
            rent = buildingPrices.get(numHouses);
            System.out.println("Purchased a house for: " + getPropertyAnsiColor() + name + Ansi.ANSI_RESET);
        } else if (numHouses == 4 && !hasHotel) {
            numHouses++;
            buildHotel();// Upgrade to hotel when 4 houses are built
        }
        else {
            System.out.println("Unable to purchase house");
        }
    }

    private void buildHotel() {
        if(numHouses == 4 && !hasHotel) {
            hasHotel = true;
            rent = buildingPrices.get(numHouses + 1);
            System.out.println("Purchased a hotel for: " + getPropertyAnsiColor() + name + Ansi.ANSI_RESET);
        }
        else {
            System.out.println("Unable to purchase hotel");
        }

    }

    public void demolishHouse() {
        if (numHouses > 0) {
            numHouses--;
            rent = buildingPrices.get(numHouses);
        }
    }

    public void demolishHotel() {
        if(hasHotel) {
            hasHotel = false;
            rent = buildingPrices.get(numHouses);
        }
    }

    public int railRoadRent(Player player) {
        int railRoadCount = player.railRoadCounter;
        return switch (railRoadCount) {
            case 1 -> 25;
            case 2 -> 50;
            case 3 -> 100;
            case 4 -> 200;
            default -> 0;
        };
    }

}
