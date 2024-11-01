package gameset.functionality;

import gameutils.Ansi;

public class Property {

    private final String name;

    private String type;
    private String color;
    private final int price;
    private int rent;
    private String owner;

    //TODO: Add hotels
    //private Map<String, Integer> hotelList = new HashMap<String, Integer>();

    public Property(String name, String type,  int price, int rent, String color){
        this.name = name;
        this.type = type;
        this.price = price;
        this.color = color;
        this.rent = rent;
        this.owner = "";
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

    //TODO - don't pass in property object
    public void displayProperty(Property property){
        System.out.println("----------------");
        System.out.println("Property: " + displayPropertyName(property));
        System.out.println("Price: " + property.price);
        System.out.println("Rent: " + property.rent);
        System.out.println("Owned: " + property.owner);
        System.out.println("----------------");
    }

    //TODO - don't pass in property object
    public String displayPropertyName(Property property){
        return (getPropertyAnsiColor(property) + property.getName() + Ansi.ANSI_RESET);
    }

    //TODO - don't pass in property object

    public String getPropertyAnsiColor(Property property){
        Ansi ansi = new Ansi();
        return ansi.propertyToAnsiColor(property.color);
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
