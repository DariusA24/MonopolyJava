package gameset.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import gameset.functionality.Game;
import gameset.functionality.Player;
import java.util.Map;

/**
 * Abstract class representing a card in the game.
 */
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
public abstract class Card {
    @JsonProperty("action")
    Action action;
    @JsonProperty("description")
    String description;
    @JsonProperty("params")
    Map<String, Object> params;

    public Card() {}

    public Card(Action action, String description, Map<String, Object> params) {
        this.action = action;
        this.description = description;
        this.params = params;
    }

    public abstract void applyEffect(Player p, Game g);
}
