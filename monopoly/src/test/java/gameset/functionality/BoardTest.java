package gameset.functionality;


import gameutils.ResourceParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


import java.io.IOException;
import java.util.ArrayList;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Mock
    private Player playerMock;
    @Mock
    ArrayList<Property> properties = new ArrayList<>();

    private Board board;

    @BeforeEach
    public void setUp() throws IOException {
        playerMock = new Player("test1", 0, "blue");
        ArrayList<Player> mockedPlayer = new ArrayList<>();
        // Load the property data from the JSON file
        ResourceParser propertyParser = new ResourceParser("/models/propertyDataTest.json");
        properties = propertyParser.parseJsonToArrayList(Property.class);
        // Instantiate the Board with the mocked parsers
        board = new Board(mockedPlayer);
    }

    @Test
    public void testGetGameBoard() {
        // Test that the board's game board is initialized correctly
        ArrayList<Property> gameBoard = board.getGameBoard();
        assertNotNull(gameBoard);
        assertEquals(40, gameBoard.size());
    }

    @Test
    public void testGetPropertiesColorCount() {
        // Test color count
        long count = board.getPropertiesColorCount("blue");
        assertEquals(2, count);

        // Test for a color that doesn't exist
        count = board.getPropertiesColorCount("magenta");
        assertEquals(0, count);
    }

    @Test
    public void testPassedGo() {
        // Capture system output
        System.setOut(new java.io.PrintStream(new java.io.ByteArrayOutputStream()));
        board.passedGo(playerMock);
        // Validate player money was updated
        Assertions.assertEquals(200, playerMock.getMoney());
    }

    @Test
    public void testGetPropertyPositionGo() {
        OptionalInt position = board.getPropertyPosition("Go");
        assertTrue(position.isPresent());
        assertEquals(0, position.getAsInt());
    }

    @Test
    public void testGetPropertyPositionFreeParking() {
        OptionalInt position = board.getPropertyPosition("Free Parking");
        assertTrue(position.isPresent());
        assertEquals(20, position.getAsInt());
    }

    @Test
    public void testGetPropertyPositionUnknownProperty() {
        OptionalInt position = board.getPropertyPosition("Unknown Property");
        assertFalse(position.isPresent());
    }
}
