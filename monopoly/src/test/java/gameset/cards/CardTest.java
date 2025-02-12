package gameset.cards;

import gameset.functionality.Game;
import gameset.functionality.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashMap;
import java.util.Map;

public class CardTest {
    @Test
    public void testCardConstructor() {
        Action action = Action.Advance;
        String description = "Test description";
        Map<String, Object> params = new HashMap<>();

        Card card = new Card(action, description, params) {
            @Override
            public void applyEffect(Player p, Game g) {
                // Do nothing
            }
        };

        assertEquals(action, card.action);
        assertEquals(description, card.description);
        assertEquals(params, card.params);
    }
    // TODO: GH issue #28 - test displaying card information
}