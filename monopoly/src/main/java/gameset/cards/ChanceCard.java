package gameset.cards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gameset.functionality.Game;
import gameset.functionality.Player;

/**
 * Represents a Chance card.
 * A Chance card can instruct players to move, pay money, collect money,
 * or perform other actions based on the game rules.
 */
//@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@JsonPropertyOrder({
        "description",
        "action",
        "params",
})
public class ChanceCard extends Card {
    public ChanceCard() {}

    @Override
    public void applyEffect(Player p, Game g) {
        switch (this.action) {
            default -> {
                System.out.println("TODO: implement chance card action");
            }
        }
    }
}
