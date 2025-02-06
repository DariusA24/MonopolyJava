package gameset.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a Chance card.
 * A Chance card can instruct players to move, pay money, collect money,
 * or perform other actions based on the game rules.
 */
@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
@JsonPropertyOrder({
        "cardText",
        "action",
        "isSpecial",
        "targetLocation",
        "moneyValue"
})
public class ChanceCard { // TODO: should make this extends OR implement a card interface to utilize generics
    /** The description or instruction on the card. */
    @JsonProperty("cardText")
    private String cardText;
    /** The type of action the card performs (e.g., "Move", "Pay", "Collect"). */
    @JsonProperty("action")
    private Actions action;
    /** Indicates if this is a special card that stays in Player inventory (e.g., "Get Out of Jail Free"). */
    @JsonProperty("isSpecial")
    private boolean isSpecial;
    /** The target location for movement, if applicable. */
    @JsonProperty("targetLocation")
    private String targetLocation; // TODO: turn to Property Object
    /** The amount of money to pay or collect, if applicable. */
    @JsonProperty("moneyValue")
    private int moneyValue;

    public ChanceCard() {}
}
