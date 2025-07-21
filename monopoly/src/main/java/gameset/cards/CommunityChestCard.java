package gameset.cards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

/**
 * Represents a Community Chest card.
 * A Community Chest card can instruct players to move, pay money, collect money,
 * or perform other actions based on the game rules.
 */
@JsonPropertyOrder({
        "description",
        "action",
        "params",
})
public class CommunityChestCard extends Card {
    public CommunityChestCard() {}

    public CommunityChestCard(Action action, String description, Map<String, Object> params) {
        super(action, description, params);
    }

    @Override
    public String toString() {
        return "Community Chest" +
                System.lineSeparator() +
                this.description;
    }
}
