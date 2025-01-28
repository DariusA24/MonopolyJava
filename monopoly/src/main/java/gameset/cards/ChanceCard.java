package gameset.cards;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a Chance card.
 * A Chance card can instruct players to move, pay money, collect money,
 * or perform other actions based on the game rules.
 */
public class ChanceCard { // TODO: should make this extends OR implement a card interface to utilize generics
    /** The description or instruction on the card. */
    private final String cardText;
    /** The type of action the card performs (e.g., "Move", "Pay", "Collect"). */
    private final Actions action;
    /** Indicates if this is a special card that stays in Player inventory (e.g., "Get Out of Jail Free"). */
    private final boolean isSpecial;
    /** The target location for movement, if applicable. */
    private final String targetLocation; // TODO: turn to Property Object
    /** The amount of money to pay or collect, if applicable. */
    private final int moneyValue;

    /**
     * Constructor for Chance Cards of variant like "Head to go and collect $200"
     * @param cardText The description or instruction on the card
     * @param action The type of action the card performs ("Move", "Pay", "Collect", etc.)
     * @param targetLocation The target location for movement, if applicable.
     * @param moneyValue The amount of money to pay or collect, if applicable.
     * @param isSpecial Indicates special card that stays in Player inventory
     */
    public ChanceCard(String cardText, Actions action, String targetLocation, int moneyValue, boolean isSpecial) {
        this.cardText = cardText;
        this.action = action;
        this.targetLocation = targetLocation;
        this.moneyValue = moneyValue;
        this.isSpecial = isSpecial;
    }

    public ChanceCard(Optional<String> cardText, Optional<Actions> action, Optional<String> targetLocation, Optional<Integer> moneyValue, Optional<Boolean> isSpecial) {
        this.cardText = cardText.orElse("Default Card Text"); // Default card text if absent
        this.action = action.orElse(Actions.Default); // Default action if absent
        this.targetLocation = targetLocation.orElse("Default Location"); // Default location if absent
        this.moneyValue = moneyValue.orElse(0); // Default money value if absent
        this.isSpecial = isSpecial.orElse(false);
    }

    public String getCardText() {
        return cardText;
    }

    public Actions getAction() {
        return action;
    }

    public Optional<String> getTargetLocation() {
        if (targetLocation == null || targetLocation.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(targetLocation);
    }

    public int getMoneyValue() {
        return moneyValue;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChanceCard that)) return false;
        return Objects.equals(cardText, that.cardText);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cardText);
    }
}
