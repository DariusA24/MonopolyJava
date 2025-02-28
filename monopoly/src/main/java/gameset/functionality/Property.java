package gameset.functionality;

import gameutils.Ansi;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "type",
        "price",
        "rentWithBuildingsList",
        "color",
        "mortgage"
})
public class Property {

    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("rentWithBuildingsList")
    private final ArrayList<Integer> rentWithBuildingsList = new ArrayList<>();
    @JsonProperty("color")
    private String color;
    @JsonProperty("mortgage")
    private Integer mortgage;
    private boolean isMortgaged;

    private Integer numHouses = 0;
    private boolean hasHotel;
    private Integer rent;


    // Constants
    private static final int MAX_HOUSES = 4;
    private String owner = "";

    //Getter
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("price")
    public Integer getPrice() {
        return price;
    }

    @JsonProperty("color")
    public String getColor() {
        return color;
    }

    @JsonProperty("mortgage")
    public Integer getMortgage() {
        return mortgage;
    }

    @JsonProperty("rentWithBuildingsList")
    public ArrayList<Integer> getRentWithBuildingsList() {
        return rentWithBuildingsList;
    }

    public Integer getNumHouses() {
        return numHouses;
    }

    public Integer getRent() {
        return rentWithBuildingsList.get(numHouses);
    }

    public String getOwner() {
        return owner;
    }
    public boolean isMortgaged() {
        return isMortgaged;
    }

    //Setters
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setRent(Integer rent) {
        this.rent = rentWithBuildingsList.get(numHouses);
    }

    public void setMortgaged(boolean mortgaged) {
        this.isMortgaged = mortgaged;
    }

    @Override
    public String toString() {
        return "----------------" +
                System.lineSeparator() +
                "Property: " + displayPropertyName() +
                System.lineSeparator() +
                "Price: " + this.price +
                System.lineSeparator() +
                "Rent: " + getRent() +
                System.lineSeparator() +
                "Owned: " + this.owner +
                System.lineSeparator() +
                "----------------";
    }

    /**
     * Displays the property with the correct property color.
     */
    public String displayPropertyName() {
        Ansi ansi = new Ansi();
        return (ansi.propertyToAnsiColor(this.color) + this.name + Ansi.ANSI_RESET);
    }

    public String displayPropertyName(int maxLength) {
        Ansi ansi = new Ansi();
        if (this.name.length() > maxLength) {
            return (ansi.propertyToAnsiColor(this.color) + this.name.substring(0, maxLength) + Ansi.ANSI_RESET);
        }
        return (ansi.propertyToAnsiColor(this.color) + Board.centerString(this.name, maxLength) + Ansi.ANSI_RESET);
    }

    /**
     * Gets the price of the building depending on the color.
     */
    public int getBuildingPrice() {
        return switch (color) {
            case "brown", "cyan" -> 50;
            case "pink", "orange" -> 100;
            case "red", "yellow" -> 150;
            case "green", "blue" -> 200;
            default -> 0;
        };
    }

    /**
     * Changes the rent based on the number of houses the property has.
     */
    private void changeRent() {
        rent = rentWithBuildingsList.get(numHouses);
    }

    public void buildBuilding() {
        if (hasHotel) {
            System.out.println("Unable to purchase anymore buildings on this property.");
        } else if (numHouses == MAX_HOUSES) {
            addHotel();
        } else {
            addHouse();
        }
    }

    private void addHouse() {
        numHouses++;
        changeRent();
        System.out.println("Purchased a house for: " + displayPropertyName());
    }

    private void addHotel() {
        hasHotel = true;
        numHouses++;
        changeRent();
        System.out.println("Purchased a hotel for: " + displayPropertyName());
    }
}
