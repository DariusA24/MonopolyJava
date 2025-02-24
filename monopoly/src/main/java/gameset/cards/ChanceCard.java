package gameset.cards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

/**
 * Represents a Chance card.
 * A Chance card can instruct players to move, pay money, collect money,
 * or perform other actions based on the game rules.
 */
@JsonPropertyOrder({
        "description",
        "action",
        "params",
})
public class ChanceCard extends Card {
    public ChanceCard() {}

    public ChanceCard(Action action, String description, Map<String, Object> params) {
        super(action, description, params);
    }

    @Override
    public String toString() {
        return "Chance" +
                System.lineSeparator() +
                this.description;
    }
}
