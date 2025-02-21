package gameset.cards;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gameset.functionality.Game;
import gameset.functionality.Player;
import gameutils.Ansi;
import java.util.ArrayList;
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
                System.out.println("Player " + p.getColor() + p.getName() + Ansi.ANSI_RESET + " paid $" + (newBalance - startingBalance));
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
