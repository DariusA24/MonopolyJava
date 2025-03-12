package gameset.functionality;

import gameutils.ResourceParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Mock
    private Player playerMock;
    @Mock
    ArrayList<Property> properties = new ArrayList<>();
    @Mock
    ArrayList<Player> mockedPlayers = new ArrayList<>();
    private Board board;

    @BeforeEach
    public void SetUp() throws IOException {
        playerMock = new Player("John", 1500, "Red");
        mockedPlayers = new ArrayList<>();
        // Load the property data from the JSON file
        ResourceParser propertyParser = new ResourceParser("/models/propertyDataTest.json");
        properties = propertyParser.parseJsonToArrayList(Property.class);
        // Instantiate the Board with the mocked parsers
        board = new Board(mockedPlayers);
    }

    @Test
    public void testPlayerInitialization() throws IOException {
        assertEquals("John", playerMock.getNameNoColor());
        assertEquals(1500, playerMock.getMoney());
        assertEquals("Red", playerMock.getColor());
        assertEquals(0, playerMock.getLocation());
        assertFalse(playerMock.getJailStatus());
    }

    @Test
    public void testAddMoney() throws IOException {
        playerMock.addMoney(500);

        assertEquals(2000, playerMock.getMoney());
    }

    @Test
    public void testCheckBalance() throws IOException {
        // Should return true since the player has enough money
        assertTrue(playerMock.checkBalance(1000));

        // Should return false since the player doesn't have enough money
        assertFalse(playerMock.checkBalance(2000));
    }

    @Test
    public void testUpdateLocation() throws IOException {
        playerMock.updatePosition(5, board);

        assertEquals(5, playerMock.getLocation());
    }

    @Test
    public void testGoToJail() throws IOException {
        playerMock.goToJail();

        assertTrue(playerMock.getJailStatus());

        playerMock.leaveJail();

        assertFalse(playerMock.getJailStatus());
    }

    @Test
    public void testAddProperty() throws IOException {
        // Load the property data from the JSON file
        ResourceParser propertyParser = new ResourceParser("/models/propertyDataTest.json");
        ArrayList<Property> properties = propertyParser.parseJsonToArrayList(Property.class);
        board = new Board(mockedPlayers);
        playerMock.addProperty(properties.getFirst(), board);  // Pass a mock or real Board if needed

        assertTrue(playerMock.getProperties().contains(properties.getFirst()));
    }

    @Test
    public void testCanPurchaseBuilding_True() throws IOException {
        // Load the property data from the JSON file
        ResourceParser propertyParser = new ResourceParser("/models/propertyDataTest.json");
        ArrayList<Property> properties = propertyParser.parseJsonToArrayList(Property.class);
        board = new Board(mockedPlayers);
        playerMock.addProperty(properties.getFirst(), board);
        playerMock.addProperty(properties.get(1), board);
       assertTrue(playerMock.canPurchaseBuilding(properties.getFirst(), board));
    }

    @Test
    public void testCanPurchaseBuildingFalseNoColorSet() throws IOException {
        // Load the property data from the JSON file
        ResourceParser propertyParser = new ResourceParser("/models/propertyDataTest.json");
        ArrayList<Property> properties = propertyParser.parseJsonToArrayList(Property.class);
        board = new Board(mockedPlayers);
        playerMock.addProperty(properties.getFirst(), board);
        playerMock.addProperty(properties.get(1), board);
        assertFalse(playerMock.canPurchaseBuilding(properties.get(2), board));
    }

    //TODO
    @Test
    public void testCanPurchaseBuilding_False_Not_Even_Across_Properties() throws IOException {
    }

    @Test
    public void testIsBankrupt() throws IOException {
        // Player is not bankrupt initially
        assertFalse(playerMock.isBankrupt());

        // Simulate bankruptcy
        playerMock.setMoney(-100);

        assertTrue(playerMock.isBankrupt());
    }

}
