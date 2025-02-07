package gameset.cards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gameset.functionality.Game;
import gameset.functionality.Player;
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

    // TODO: GH issues #29, #30, #31 (Implement chance card effects)
    @Override
    public void applyEffect(Player p, Game g) {
        switch (this.action) {
            default -> {
                System.out.println("TODO: implement chance card effects");
            }
        }
    }
}
