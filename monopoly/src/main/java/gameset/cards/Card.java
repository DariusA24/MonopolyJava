package gameset.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import gameset.functionality.Board;
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
                    // Retrieve Advance only parameter: targetLocation
                    String targetLocation = (String) this.params.get("targetLocation");
                    Board board = g.getBoard();

                    // 1. Advance player to target location, if player passes go, collect $200
                    board.getPropertyPosition(targetLocation).ifPresent(idx -> p.advancePosition(idx, board));
                }
            }
            case AdvanceConditional -> {
                // Retrieve Advance Conditional's 2 parameters: targetLocation and modifier
                String targetType = (String) this.params.get("targetLocation");
                int multiplier_for_rent = (int) this.params.get("modifier");

                /* Advance Conditional moves the player to the nearest railroad or utility.
                 *  - targetLocation determines the type of property to advance to (railroad or utility)
                 *  - modifier determines the multiplier to apply to the original rent if nearest property is owned
                 *     - 2x for railroad
                 *     - 10x for utility
                 */
                Board board = g.getBoard();

                // 1. Get the nearest railroad or utility from players current board position
                // 2. Move player to the nearest railroad or utility
                board.getNearestPropertyType(p.getLocation(), targetType)
                    .ifPresent(moves_to_next -> p.advancePosition(
                            (p.getLocation() + moves_to_next) % board.getGameBoard().size(), board)
                    );

                // 3. Get the owner of the nearest railroad or utility that current player just advanced to (if any)
                Property property = board.getProperty(p.getLocation());
                String propertyOwner = property.getOwner();

                // 4. If property is owned by a different player, pay proper rent with multiplier
                if (!propertyOwner.isEmpty() && !propertyOwner.equals(p.getNameNoColor())) {
                    int rent = 0;
                    int standardRent = property.getRent(g.getDice().getRollTotal());
                    // When new location is railroad, rent is 2x standard rent
                    if (targetType.equalsIgnoreCase("railroad")) {
                        rent = standardRent * multiplier_for_rent;
                    }

                    // When new location is utility, rent is 10x standard rent (dice rolled)
                    if (targetType.equalsIgnoreCase("utility")) {
                        // TODO: known bug here, if player has monopoly, then rent is 10x not 4x so division is wrong
                        // Divide by 4 for the moment since standardRent assumes 4x dice roll
                        rent = (standardRent / 4) * multiplier_for_rent;
                    }

                    // 5. Transfer money from current player to the owner of the nearest railroad or utility
                    Player otherPlayer = g.getPlayerList().stream()
                            .filter(player -> player.getNameNoColor().equals(propertyOwner))
                            .findFirst()
                            .orElse(null);
                    if (otherPlayer != null) {
                        p.addMoney(-rent);
                        otherPlayer.addMoney(rent);
                    }
                }
            }
            case DirectMove -> {
                Board board = g.getBoard();
                // Retrieve Direct Move's 2 possible parameters: "targetLocation" and "modifier"
                if (this.params.containsKey("targetLocation")) {
                    String targetLocation = (String) this.params.get("targetLocation");
                    // 1. Move player to directly to target location, Does not pass go
                    board.getPropertyPosition(targetLocation).ifPresent(idx -> p.updatePosition(idx, board));
                    // 2. If target location is jail, set player to jail
                    if (targetLocation.equalsIgnoreCase("jail")) {
                        p.goToJail();
                    }
                }

                if (this.params.containsKey("modifier")) {
                    // 1. Move player to directly to target location, Does not pass go
                    int modifier = (int) this.params.get("modifier");
                    // Modifier is relative to current location (for example, move back 3 spaces)
                    int targetLocation = p.getLocation() + modifier;
                    p.updatePosition(targetLocation, board);
                }
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
                // Retrieve Owned Property Pays 2 parameters: "housePay" and "hotelPay"
                int housePay = (int) this.params.get("housePay");
                int hotelPay = (int) this.params.get("hotelPay");

                // 1. Determine the number of houses and hotels owned by the player
                int numHouses = 0;
                int numHotels = 0;
                for (Property property : p.getProperties()) {
                    if (property.hasHotel()) {
                        numHotels++;
                    } else {
                        numHouses += property.getNumHouses();
                    }
                }
                // 2. For each house, pay housePay
                p.addMoney(-(numHouses * housePay));
                // 3. For each hotel, pay hotelPay
                p.addMoney(-(numHotels * hotelPay));
            }
        }
    }
}
