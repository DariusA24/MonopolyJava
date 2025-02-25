package gameset.cards;

import gameset.functionality.Game;
import gameset.functionality.Player;
import gameutils.Ansi;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CardTest {
    @Test
    public void testCardConstructor() {
        Action action = Action.Advance;
        String description = "Test description";
        Map<String, Object> params = new HashMap<>();

        Card card = new Card(action, description, params) {
            @Override
            public void applyEffect(Player p, Game g) {
                // Do nothing
            }
        };

        assertEquals(action, card.action);
        assertEquals(description, card.description);
        assertEquals(params, card.params);
    }

    @Test
    public void testApplyEffectMoneyReceive() {
        // Setup
        HashMap<String, Object> params = new HashMap<>();
        params.put("value", 150);
        CommunityChestCard card = new CommunityChestCard(Action.MoneyReceive, "Receive $150", params);
        Player player = new Player("Player 1", 0, new Ansi().propertyToAnsiColor("blue"));
        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(player);
        Game game = new Game();
        game.setPlayerList(playerList);

        // Test
        card.applyEffect(player, game);
        assertEquals(150, player.getMoney());
    }

    @Test
    public void testApplyEffectMoneyReceiveAll() {
        // Card Setup
        HashMap<String, Object> params = new HashMap<>();
        params.put("value", 50);
        CommunityChestCard card = new CommunityChestCard(Action.MoneyReceiveAll, "Receive $50 from each player", params);
        // Players
        Player player1 = new Player("Player 1", 100, new Ansi().propertyToAnsiColor("blue"));
        Player player2 = new Player("Player 2", 200, new Ansi().propertyToAnsiColor("red"));
        Player player3 = new Player("Player 3", 300, new Ansi().propertyToAnsiColor("green"));
        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        Game game = new Game();
        game.setPlayerList(playerList);

        // Test
        card.applyEffect(player1, game);
        assertEquals(200, player1.getMoney());
        assertEquals(150, player2.getMoney());
        assertEquals(250, player3.getMoney());
    }

    @Test
    public void testApplyEffectMoneyPay() {
        // Setup
        HashMap<String, Object> params = new HashMap<>();
        params.put("value", 100);
        ChanceCard card = new ChanceCard(Action.MoneyPay, "Pay $100 in hospital fees", params);
        Player player = new Player("Player 1", 500, new Ansi().propertyToAnsiColor("blue"));
        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(player);
        Game game = new Game();
        game.setPlayerList(playerList);

        // Test
        card.applyEffect(player, game);
        assertEquals(400, player.getMoney());
    }

    @Test
    public void testApplyEffectMoneyPayAll() {
        // Card Setup
        HashMap<String, Object> params = new HashMap<>();
        params.put("value", 50);
        ChanceCard card = new ChanceCard(Action.MoneyPayAll, "Pay $50 to each player", params);
        // Players
        Player player1 = new Player("Player 1", 100, new Ansi().propertyToAnsiColor("blue"));
        Player player2 = new Player("Player 2", 200, new Ansi().propertyToAnsiColor("red"));
        Player player3 = new Player("Player 3", 300, new Ansi().propertyToAnsiColor("green"));
        ArrayList<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        Game game = new Game();
        game.setPlayerList(playerList);

        // Test
        card.applyEffect(player1, game);
        assertEquals(0, player1.getMoney());
        assertEquals(250, player2.getMoney());
        assertEquals(350, player3.getMoney());
    }
}