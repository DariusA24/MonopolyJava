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

    private Integer numHouses = 0;
    private boolean hasHotel;
    private Integer rent;
    private boolean isMortgaged;


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

    public Integer getRent(int previousRoll) {
        if (type.equalsIgnoreCase("utility")) {
            // TODO: known bug here, if player has monopoly, then rent is 10x not 4x
            return 4 * previousRoll;
        } else if (type.equalsIgnoreCase("railroad")) {
            // TODO: known bug here, if player has more than 1 railroad, then rent is different
            return 25;
        } else {
            return rentWithBuildingsList.get(numHouses);
        }
    }

    public String printRent() {
        return switch (type.toLowerCase()) {
            case "railroad" ->
                // TODO: known bug here, if player has more than 1, then rent is different
                    "25";
            case "utility" ->
                // TODO: known bug here, if player has monopoly, then rent is 10x not 4x
                    "4 times amount shown on dice";
            default -> rentWithBuildingsList.get(numHouses).toString();
        };
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
                "Rent: " + printRent() +
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

    /**
     * Upgrades the property to a hotel.
     *
     * <p>This method is responsible for upgrading a property to a hotel.
     * Once the property is upgraded, we have to adjust the house counters by
     * removing four houses.
     *
     * @param board the board object
     */
    private void upgradeToHotelAdjuster(Board board) {
        for (int i = 0; i < MAX_HOUSES; i++) {
            board.adjustBuildingCounters("house", false);
        }
    }

    /**
     * Removes a hotel from the board.
     *
     * <p>This method is responsible for removing a hotel from a property.
     * In order to remove a hotel from the property, there has to be enough houses on the board.
     *
     * @param board the board object
     */
    private boolean removeHotelAdjuster(Board board) {
        int HOUSE_AMOUNT = 32;
        if (HOUSE_AMOUNT - board.getHouseCount() < MAX_HOUSES) {
           System.out.println("Not enough houses to remove hotel.");
           return false;
        }
        board.adjustBuildingCounters("hotel", false);
        for (int i = 0; i < MAX_HOUSES; i++) {
            board.adjustBuildingCounters("house", true);
        }
        return true;
    }

    /**
     * Builds a building on the property
     *
     * <p>This method is responsible for building a building on a property.
     * When building we also adjust the counters on the board.
     *
     * @param board the board object
     */
    public boolean buildBuilding(Board board) {
        if (hasHotel) {
            System.out.println("Unable to purchase anymore buildings on this property.");
            return false;
        } else if (numHouses == MAX_HOUSES) {
            if (board.adjustBuildingCounters("hotel", true)) {
                addHotel();
                upgradeToHotelAdjuster(board);
                return true;
            }
        } else {
            if (board.adjustBuildingCounters("house", true)) {
                addHouse();
                return true;
            }
        }
        return false;
    }
    /**
     * Removes a building from the board
     *
     * <p>This method is responsible for removing a building from the property.
     *
     * @param board the board object
     */
    public void removeBuilding(Board board) {
        if (hasHotel && removeHotelAdjuster(board)) {
           hasHotel = false;
        }
        else if (numHouses > 0) {
            numHouses--;
            board.adjustBuildingCounters("house", false);
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

    public boolean hasHotel() {
        return hasHotel;
    }
}
