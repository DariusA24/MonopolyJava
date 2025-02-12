package gameset.functionality;
import gameutils.Ansi;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "type",
        "price",
        "rent",
        "color"
})
public class Property {

    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("rent")
    private Integer rent;
    @JsonProperty("color")
    private String color;
    private String owner = "";

    //TODO: Add hotels
    //private Map<String, Integer> hotelList = new HashMap<String, Integer>();

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

    @JsonProperty("rent")
    public Integer getRent() {
        return rent;
    }

    @JsonProperty("color")
    public String getColor() {
        return color;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "----------------" +
                System.lineSeparator() +
                "Property: " + displayPropertyName() +
                System.lineSeparator() +
                "Price: " + this.price +
                System.lineSeparator() +
                "Rent: " + this.rent +
                System.lineSeparator() +
                "Owned: " + this.owner +
                System.lineSeparator() +
                "----------------";
    }

    public String displayPropertyName(){
        Ansi ansi = new Ansi();
        return (ansi.propertyToAnsiColor(this.color) + this.name + Ansi.ANSI_RESET);
    }
}
