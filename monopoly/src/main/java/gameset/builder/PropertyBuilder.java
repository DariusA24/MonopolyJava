package gameset.builder;

import gameset.functionality.Property;

import java.util.ArrayList;

public class PropertyBuilder {
    public final String name;
    public final String type;
    public int price = 0;  // Default value
    public String owner = "";
    public String color = "";  // Default value
    public int rent = 0;  // Default value
    public ArrayList<Integer> buildingPrices = new ArrayList<>();  // Default value
    public int mortgage = 0;  // Default value

    // Constructor for mandatory fields
    public PropertyBuilder(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // Setter methods (with chaining)
    public PropertyBuilder price(int price) {
        this.price = price;
        return this;
    }

    public PropertyBuilder color(String color) {
        this.color = color;
        return this;
    }

    public PropertyBuilder rent(int rent) {
        this.rent = rent;
        return this;
    }

    public PropertyBuilder buildingPrices(ArrayList<Integer> buildingPrices) {
        this.buildingPrices = buildingPrices;
        return this;
    }

    public PropertyBuilder mortgage(int mortgage) {
        this.mortgage = mortgage;
        return this;
    }

    public PropertyBuilder owner(String owner) {
        this.owner = owner;
        return this;
    }

    // Method to create the Property object
    public Property build() {
        return new Property(this); // Calls the Property constructor
    }
}
