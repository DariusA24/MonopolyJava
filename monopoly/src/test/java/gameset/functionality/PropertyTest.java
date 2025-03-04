package gameset.functionality;

import gameutils.ResourceParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyTest {
    Game game;
    @BeforeEach
    public void setUp() {
        game = new Game();
        game.getDice().setRollTotal(6);
    }

    @Test
    public void testPropertyInitialization() throws IOException {
        // Load the property data from the JSON file
        ResourceParser propertyParser = new ResourceParser("/models/propertyDataTest.json");
        ArrayList<Property> properties = propertyParser.parseJsonToArrayList(Property.class);

        // Check if properties are loaded correctly
        assertNotNull(properties);
        assertEquals(2, properties.size());

        Property property = properties.getFirst();

        assertEquals("Park Place", property.getName());
        assertEquals("Property", property.getType());
        assertEquals("blue", property.getColor());
        assertEquals(350, property.getPrice());
        assertEquals(175, property.getMortgage());
        assertEquals(35, property.getRentWithBuildingsList().getFirst());
        assertEquals(35, property.getRent(game.getDice().getRollTotal()));
    }

    @Test
    public void testPropertyRentIncrease() throws IOException {
        // Load the property data from the JSON file
        ResourceParser propertyParser = new ResourceParser("/models/propertyDataTest.json");
        ArrayList<Property> properties = propertyParser.parseJsonToArrayList(Property.class);

        // Check if properties are loaded correctly
        assertNotNull(properties);
        assertEquals(2, properties.size());

        Property property = properties.getFirst();

        //Default Rent
        assertEquals(35, property.getRent(game.getDice().getRollTotal()));

        //Rent with 1 property
        property.buildBuilding();
        assertEquals(175, property.getRent(game.getDice().getRollTotal()));

        //Rent with 2 properties
        property.buildBuilding();
        assertEquals(500, property.getRent(game.getDice().getRollTotal()));

        //Rent with 3 properties
        property.buildBuilding();
        assertEquals(1100, property.getRent(game.getDice().getRollTotal()));

        //Rent with 4 properties
        property.buildBuilding();
        assertEquals(1300, property.getRent(game.getDice().getRollTotal()));

        //Rent with hotel
        property.buildBuilding();
        assertEquals(1500, property.getRent(game.getDice().getRollTotal()));

        //Trying to add another property with hotel
        property.buildBuilding();
        assertEquals(1500, property.getRent(game.getDice().getRollTotal()));
    }

    @Test
    public void testOwnerAssignment() throws IOException {
        // Load the property data from the JSON file
        ResourceParser propertyParser = new ResourceParser("/models/propertyDataTest.json");
        ArrayList<Property> properties = propertyParser.parseJsonToArrayList(Property.class);
        Property property = properties.getFirst();
        property.setOwner("Player1");
        assertEquals("Player1", property.getOwner());
    }

    @Test
    public void testToString() throws IOException {
       // Load the property data from the JSON file
        ResourceParser propertyParser = new ResourceParser("/models/propertyDataTest.json");
        ArrayList<Property> properties = propertyParser.parseJsonToArrayList(Property.class);
        Property property = properties.getFirst();
        property.setOwner("Player1");

        String expectedString = "----------------\n" +
                "Property: " + property.displayPropertyName() + "\n"  +
                "Price: 350\n" +
                "Rent: 35\n" +
                "Owned: Player1\n" +
                "----------------";
        assertEquals(expectedString, property.toString());
    }
}
