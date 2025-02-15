package gameset.cards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gameset.functionality.Game;
import gameset.functionality.Player;
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
        return "----------------" +
                System.lineSeparator() +
                "Community Chest Card" +
                System.lineSeparator() +
                this.description +
                System.lineSeparator() +
                "----------------";
    }

    // TODO: GH issues #29, #30, #31 (Implement community chest card effects)
    @Override
    public void applyEffect(Player p, Game g) {
        switch (this.action) {
            default -> {
                System.out.println("TODO: implement community chest card effects");
            }
        }
    }
}
