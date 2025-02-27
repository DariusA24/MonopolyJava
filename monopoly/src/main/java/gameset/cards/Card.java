package gameset.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import gameset.functionality.Game;
import gameset.functionality.Player;
import gameutils.Ansi;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Card card)) return false;
        return action == card.action && Objects.equals(description, card.description) && Objects.equals(params, card.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, description, params);
    }


    // TODO: GH issues #30, #31 (Implement chance card effects)
    /**
     * Applies the effect of this card to the player and the game.
     *
     * <p>This method is responsible for implementing the effects of the different
     * chance/community cards. The effects are applied by modifying the player's money,
     * location on the board, or status in jail by editing the game state as necessary.
     *
     * @param p the player to apply the effect to
     * @param g the game to apply the effect to
     */
    public void applyEffect(Player p, Game g) {
        switch (this.action) {
            case Advance -> {
                System.out.println("TODO: implement chance card - Advance");
            }
            case AdvanceConditional -> {
                System.out.println("TODO: implement chance card - AdvanceConditional");
            }
            case DirectMove -> {
                System.out.println("TODO: implement chance card - DirectMove");
            }
            case MoneyReceive -> {
                int amount = (int) this.params.get("value");
                p.addMoney(amount);
                System.out.println("Player " + p.getColor() + p.getName() + Ansi.ANSI_RESET + " received $" + amount);
            }
            case MoneyReceiveAll -> {
                int amount = (int) this.params.get("value");
                int startingBalance = p.getMoney();
                ArrayList<Player> players = g.getPlayerList();
                for (Player otherPlayer : players) {
                    if (!otherPlayer.equals(p)) {
                        otherPlayer.addMoney(-amount);
                        p.addMoney(amount);
                    }
                }
                int newBalance = p.getMoney();
                System.out.println("Player " + p.getColor() + p.getName() + Ansi.ANSI_RESET + " received $" + (newBalance - startingBalance));
            }
            case MoneyPay -> {
                int amount = (int) this.params.get("value");
                p.addMoney(-amount);
                System.out.println("Player " + p.getColor() + p.getName() + Ansi.ANSI_RESET + " paid $" + amount);
            }
            case MoneyPayAll -> {
                int amount = (int) this.params.get("value");
                int startingBalance = p.getMoney();
                ArrayList<Player> players = g.getPlayerList();
                for (Player otherPlayer : players) {
                    if (!otherPlayer.equals(p)) {
                        p.addMoney(-amount);
                        otherPlayer.addMoney(amount);
                    }
                }
                int newBalance = p.getMoney();
                System.out.println("Player " + p.getColor() + p.getName() + Ansi.ANSI_RESET + " paid $" + Math.abs(newBalance - startingBalance));
            }
            case GetOutOfJailCard -> {
                System.out.println("TODO: implement chance card - GetOutOfJailCard");
            }
            case OwnedPropertyPay -> {
                System.out.println("TODO: implement chance card - OwnedPropertyPay");
            }
        }
    }
}
