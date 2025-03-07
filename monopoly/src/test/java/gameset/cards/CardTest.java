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

    @Test
    public void testApplyEffectAdvanceConditionalToUtilityNoPassGo() {
        // Card Setup
        params.put("targetLocation", "Utility");
        params.put("modifier", 10);
        ChanceCard card = new ChanceCard(Action.AdvanceConditional, "Advance token to the nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total 10 times the amount thrown.", params);

        // Player Setup - on chance card position
        playerList.getFirst().updatePosition(22, game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves to Water Works
        assertEquals(28, playerList.getFirst().getLocation());
        // 2. ensure player pay/receive (no pass go + no player owns property)
        assertEquals(0, playerList.getFirst().getMoney());
    }

    @Test
    public void testApplyEffectAdvanceConditionalToUtilityNoPassGoOwnedByOther() throws IOException {
        // Card Setup
        params.put("targetLocation", "Utility");
        params.put("modifier", 10);
        ChanceCard card = new ChanceCard(Action.AdvanceConditional, "Advance token to the nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total 10 times the amount thrown.", params);

        // Game setup - say player rolled a 10
        game.getDice().setRollTotal(10);

        // Player Setup
        playerList.getFirst().setMoney(1000);
        playerList.getLast().setMoney(1000);
        // on chance card position
        playerList.getFirst().updatePosition(22, game.getBoard());
        // Another player owns Water Works
        playerList.getLast().addProperty(game.getBoard().getProperty(28), game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves to Water Works
        assertEquals(28, playerList.getFirst().getLocation());
        // 2. player should pay out 10x the amount thrown (1000 - 10*10)
        assertEquals(900, playerList.getFirst().getMoney());
        // 3. owner should receive out 10x the amount thrown
        assertEquals(1100, playerList.getLast().getMoney());
    }

    @Test
    public void testApplyEffectAdvanceConditionalToUtilityPassGo() {
        // Card Setup
        params.put("targetLocation", "Utility");
        params.put("modifier", 10);
        ChanceCard card = new ChanceCard(Action.AdvanceConditional, "Advance token to the nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total 10 times the amount thrown.", params);

        // Player Setup - on chance card position
        playerList.getFirst().updatePosition(38, game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves to Electric Company
        assertEquals(12, playerList.getFirst().getLocation());
        // 2. ensure player pay/receive (player passed go + no player owns property)
        assertEquals(200, playerList.getFirst().getMoney());
    }

    @Test
    public void testApplyEffectAdvanceConditionalToUtilityPassGoOwnedByOther() throws IOException {
        // Card Setup
        params.put("targetLocation", "Utility");
        params.put("modifier", 10);
        ChanceCard card = new ChanceCard(Action.AdvanceConditional, "Advance token to the nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total 10 times the amount thrown.", params);

        // Game setup - say player rolled a 10
        game.getDice().setRollTotal(10);

        // Player Setup
        playerList.getFirst().setMoney(1000);
        playerList.getLast().setMoney(1000);
        // on chance card position
        playerList.getFirst().updatePosition(37, game.getBoard());
        // Another player owns Water Works
        playerList.getLast().addProperty(game.getBoard().getProperty(12), game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves to Water Works
        assertEquals(12, playerList.getFirst().getLocation());
        // 2. player should pay out 10x the amount thrown (1000 + 200 - 10*10)
        assertEquals(1100, playerList.getFirst().getMoney());
        // 3. owner should receive out 10x the amount thrown
        assertEquals(1100, playerList.getLast().getMoney());
    }

    @Test
    public void testApplyEffectAdvanceConditionalToRailroadNoPassGo() {
        // Card Setup
        params.put("targetLocation", "Railroad");
        params.put("modifier", 2);
        ChanceCard card = new ChanceCard(Action.AdvanceConditional, "Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay owner twice the rent to which they are otherwise entitled.", params);

        // Player Setup - on chance card position
        playerList.getFirst().updatePosition(31, game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves to Short Line
        assertEquals(35, playerList.getFirst().getLocation());
        // 2. ensure player pay/receive (no pass go + no player owns property)
        assertEquals(0, playerList.getFirst().getMoney());
    }

    @Test
    public void testApplyEffectAdvanceConditionalToRailroadNoPassGoOwnedByOther() throws IOException {
        // Card Setup
        params.put("targetLocation", "Railroad");
        params.put("modifier", 2);
        ChanceCard card = new ChanceCard(Action.AdvanceConditional, "Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay owner twice the rent to which they are otherwise entitled.", params);

        // Player Setup
        playerList.getFirst().setMoney(1000);
        playerList.getLast().setMoney(1000);
        // on chance card position
        playerList.getFirst().updatePosition(22, game.getBoard());
        // Another player owns B & O Railroad
        playerList.getLast().addProperty(game.getBoard().getProperty(25), game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves to Water Works
        assertEquals(25, playerList.getFirst().getLocation());
        // 2. player should pay out 2x the rent amount (1000 - 2*25) - player owns 1
        assertEquals(950, playerList.getFirst().getMoney());
        // 3. owner should receive out 10x the amount thrown
        assertEquals(1050, playerList.getLast().getMoney());
    }

    @Test
    public void testApplyEffectAdvanceConditionalToRailroadPassGo() {
        // Card Setup
        params.put("targetLocation", "Railroad");
        params.put("modifier", 2);
        ChanceCard card = new ChanceCard(Action.AdvanceConditional, "Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay owner twice the rent to which they are otherwise entitled.", params);

        // Player Setup - on chance card position
        playerList.getFirst().updatePosition(36, game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves to Reading Railroad
        assertEquals(5, playerList.getFirst().getLocation());
        // 2. ensure player pay/receive (passed go + no player owns property)
        assertEquals(200, playerList.getFirst().getMoney());
    }

    @Test
    public void testApplyEffectAdvanceConditionalToRailroadPassGoOwnedByOther() throws IOException {
        // Card Setup
        params.put("targetLocation", "Railroad");
        params.put("modifier", 2);
        ChanceCard card = new ChanceCard(Action.AdvanceConditional, "Advance to the nearest Railroad. If unowned, you may buy it from the Bank. If owned, pay owner twice the rent to which they are otherwise entitled.", params);

        // Player Setup
        playerList.getFirst().setMoney(1000);
        playerList.getLast().setMoney(1000);
        // on chance card position
        playerList.getFirst().updatePosition(37, game.getBoard());
        // Another player owns Reading Railroad
        playerList.getLast().addProperty(game.getBoard().getProperty(5), game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves to Reading Railroad
        assertEquals(5, playerList.getFirst().getLocation());
        // 2. player should pay out 2x the rent amount (1000 + 200 - 2*25)
        assertEquals(1150, playerList.getFirst().getMoney());
        // 3. owner should receive out 2x the expected rent amount
        assertEquals(1050, playerList.getLast().getMoney());
    }

    @Test
    public void testApplyEffectDirectMoveJailNoPassGo() {
        // Card Setup
        params.put("targetLocation", "Jail");
        params.put("modifier", 0);
        CommunityChestCard card = new CommunityChestCard(Action.DirectMove, "Go to Jail. Go directly to Jail. Do not pass GO, do not collect $200.", params);

        // Player Setup - on community chest card position
        playerList.getFirst().updatePosition(33, game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves to Jail
        assertEquals(10, playerList.getFirst().getLocation());
        // 2. ensure player does not receive $200 (because they do not pass go)
        assertEquals(0, playerList.getFirst().getMoney());
    }

    @Test
    public void testApplyEffectDirectMoveBackThreeSpaces() {
        // Card Setup
        params.put("modifier", -3);
        ChanceCard card = new ChanceCard(Action.DirectMove, "Go Back Three Spaces", params);

        // Player Setup - on chance card position
        playerList.getFirst().updatePosition(22, game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1. ensure player moves back 3 spaces
        assertEquals(19, playerList.getFirst().getLocation());
    }

    @Test
    public void testApplyEffectOwnedPropertyPayNoHousesHotels() {
        // Card Setup
        params.put("housePay", 25);
        params.put("hotelPay", 100);
        ChanceCard card = new ChanceCard(Action.OwnedPropertyPay, "Make general repairs on all your property: For each house pay $25, For each hotel pay $100.", params);

        // Player setup - on chance card position (no properties built on)
        playerList.getFirst().setMoney(1000);
        playerList.getFirst().updatePosition(22, game.getBoard());

        // Test
        card.applyEffect(playerList.getFirst(), game);
        assertEquals(1000, playerList.getFirst().getMoney());
    }

    @Test
    public void testApplyEffectOwnedPropertyPaySomeHousesHotels() throws IOException {
        // Card Setup
        params.put("housePay", 40);
        params.put("hotelPay", 115);
        CommunityChestCard card = new CommunityChestCard(Action.OwnedPropertyPay, "You are assessed for street repairs: Pay $40 per house and $115 per hotel you own.", params);

        // Player setup - on community chest card position
        playerList.getFirst().setMoney(1000);
        playerList.getFirst().updatePosition(2, game.getBoard());
        // Add Brown properties for player 1 and build some houses + hotels
        playerList.getFirst().addProperty(
                game.getBoard().getProperty(1),
                game.getBoard()
        );
        playerList.getFirst().addProperty(
                game.getBoard().getProperty(3),
                game.getBoard()
        );
        // Build 4 houses
        for (int i = 0; i < 4; i++) {
            game.getBoard().getProperty(1).buildBuilding(game.getBoard());
        }

        // Build 1 hotel
        for (int i = 0; i < 5; i++) {
            game.getBoard().getProperty(3).buildBuilding(game.getBoard());
        }


        // Test
        card.applyEffect(playerList.getFirst(), game);
        // 1000 - (40 * 4 + 115)
        assertEquals(725, playerList.getFirst().getMoney());
    }

    @Test
    public void testApplyEffectGetOutOfJailCard() {
        // Card Setup
        ChanceCard card = new ChanceCard(Action.GetOutOfJailCard, "Get out of Jail Free. This card may be kept until needed, or traded/sold.", params);

        // Test
        // Should start with empty inventory
        assertEquals(0, playerList.getFirst().getInventory().size());
        card.applyEffect(playerList.getFirst(), game);
        // Should have inventory of 1
        assertEquals(1, playerList.getFirst().getInventory().size());
        // Should have inventory's first element as GetOutOfJailCard
        assertEquals(card, playerList.getFirst().getInventory().getFirst());
    }
}