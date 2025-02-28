package gameset.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import gameset.functionality.Game;
import gameset.functionality.Player;
import gameset.functionality.Property;
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


    // TODO: GH issue: #31 (Implement chance card effects)
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
                if (this.params.containsKey("targetLocation")) {
                    String targetLocation = (String) this.params.get("targetLocation");
                    g.getBoard().getPropertyPosition(targetLocation).ifPresent(idx -> p.advancePosition(idx, g.getBoard()));
                }
                System.out.println(g.getBoard());
            }
            case AdvanceConditional -> {
                // contains targetLocation and modifier
                String targetType = (String) this.params.get("targetLocation");
                int multiplier_for_rent = (int) this.params.get("modifier");
                g.getBoard().getNearestPropertyType(p.getLocation(), targetType).ifPresent(idx -> p.advancePosition(idx, g.getBoard()));
                Property property = g.getBoard().getProperty(p.getLocation());
                String propertyOwner = property.getOwner();
                if (!propertyOwner.isEmpty() && !propertyOwner.equals(p.getNameNoColor())) {
                    int rent = property.getRent() * multiplier_for_rent;
                    Player otherPlayer = g.getPlayerList().stream()
                            .filter(player -> player.getNameNoColor().equals(p.getNameNoColor()))
                            .findFirst()
                            .orElse(null);
                    if (otherPlayer != null) {
                        p.addMoney(-rent);
                        otherPlayer.addMoney(rent);
                    }
                }
                System.out.println(g.getBoard());
            }
            case DirectMove -> {
                // 2 possible params: "targetLocation" and "modifier"
                if (this.params.containsKey("targetLocation")) {
                    String targetLocation = (String) this.params.get("targetLocation");
                    g.getBoard().getPropertyPosition(targetLocation).ifPresent(idx -> p.updatePosition(idx, g.getBoard()));
                    if (targetLocation.equals("Jail")) {
                        p.goToJail();
                    }
                }
                if (this.params.containsKey("modifier")) {
                    int modifier = (int) this.params.get("modifier");
                    int targetLocation = p.getLocation() + modifier;
                    p.updatePosition(targetLocation, g.getBoard());
                }
                System.out.println(g.getBoard());
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
