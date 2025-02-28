package gameset.cards;

import gameset.functionality.Board;
import gameset.functionality.Game;
import gameset.functionality.Player;
import gameutils.Ansi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CardTest {
    Map<String, Object> params;
    ArrayList<Player> playerList;
    Game game;

    @BeforeEach public void setUp() throws IOException {
        params = new HashMap<>();
        playerList = new ArrayList<>();
        playerList.add(new Player("Player 1", 0, new Ansi().propertyToAnsiColor("blue")));
        playerList.add(new Player("Player 2", 0, new Ansi().propertyToAnsiColor("red")));
        playerList.add(new Player("Player 3", 0, new Ansi().propertyToAnsiColor("green")));
        game = new Game();
        game.setBoard(new Board(playerList));
        game.setPlayerList(playerList);
    }

    @Test
    public void testCardConstructor() {
        Action action = Action.Advance;
        String description = "Test description";

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
        params.put("value", 150);
        CommunityChestCard card = new CommunityChestCard(Action.MoneyReceive, "Receive $150", params);
        Player p = playerList.getFirst();

        // Test
        card.applyEffect(p, game);
        assertEquals(150, p.getMoney());
    }

    @Test
    public void testApplyEffectMoneyReceiveAll() {
        // Card Setup
        params.put("value", 50);
        CommunityChestCard card = new CommunityChestCard(Action.MoneyReceiveAll, "Receive $50 from each player", params);

        // Setup Players
        playerList.get(0).setMoney(100);
        playerList.get(1).setMoney(200);
        playerList.get(2).setMoney(300);

        // Test
        card.applyEffect(playerList.get(0), game);
        assertEquals(200, playerList.get(0).getMoney());
        assertEquals(150, playerList.get(1).getMoney());
        assertEquals(250, playerList.get(2).getMoney());
    }

    @Test
    public void testApplyEffectMoneyPay() throws IOException {
        // Card Setup
        params.put("value", 100);
        ChanceCard card = new ChanceCard(Action.MoneyPay, "Pay $100 in hospital fees", params);

        // Setup Players
        playerList.getFirst().setMoney(500);

        // Test
        card.applyEffect(playerList.getFirst(), game);
        assertEquals(400, playerList.getFirst().getMoney());
    }

    @Test
    public void testApplyEffectMoneyPayAll() throws IOException {
        // Card Setup
        params.put("value", 50);
        ChanceCard card = new ChanceCard(Action.MoneyPayAll, "Pay $50 to each player", params);

        // Player Setup
        playerList.get(0).setMoney(100);
        playerList.get(1).setMoney(200);
        playerList.get(2).setMoney(300);

        // Test
        card.applyEffect(playerList.get(0), game);
        assertEquals(0, playerList.get(0).getMoney());
        assertEquals(250, playerList.get(1).getMoney());
        assertEquals(350, playerList.get(2).getMoney());
    }

    @Test
    public void testApplyEffectAdvanceToGo() {
        // Card Setup
        params.put("targetLocation", "Go");
        CommunityChestCard card = new CommunityChestCard(Action.Advance, "Advance to Go (Collect $200)", params);

        // Player Setup - on community chest card position
        playerList.getFirst().updatePosition(2, game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves to go
        assertEquals(0, playerList.getFirst().getLocation());
        // 2. player gets $200
        assertEquals(200, playerList.getFirst().getMoney());
    }

    @Test
    public void testApplyEffectAdvanceToPropertyStCharlesPlace() {
        // Card Setup
        params.put("targetLocation", "St. Charles Place");
        ChanceCard card = new ChanceCard(Action.Advance, "Advance to St. Charles Place. If you pass Go, collect $200.", params);

        // Player Setup - on chance card position
        playerList.getFirst().updatePosition(36, game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves to St. Charles Place
        assertEquals(11, playerList.getFirst().getLocation());
        // 2. player gets $200 (because they passed go)
        assertEquals(200, playerList.getFirst().getMoney());
    }

    @Test
    public void testApplyEffectAdvanceToPropertyIllinoisAveNoPassGo() {
        // Card Setup
        params.put("targetLocation", "Illinois Ave.");
        ChanceCard card = new ChanceCard(Action.Advance, "Advance to Illinois Ave. If you pass Go, collect $200.", params);

        // Player Setup - on chance card position
        playerList.getFirst().updatePosition(22, game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves to Illinois Ave
        assertEquals(24, playerList.getFirst().getLocation());
        // 2. ensure player does not receive $200 (because they do not pass go)
        assertEquals(0, playerList.getFirst().getMoney());
    }
}