package gameset.functionality;

import gameset.cards.Action;
import gameset.cards.Card;
import gameset.cards.ChanceCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    @Test
    public void testDeckConstructorEmpty() {
        Deck deck = new Deck(new ArrayList<>());
        assertEquals(0, deck.size());
    }

    @Test
    public void testDeckConstructorWithCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new ChanceCard(Action.Advance, "Test card 1", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 2", new HashMap<>()));
        Deck deck = new Deck(cards);
        assertEquals(2, deck.size());
    }

    // NOTE: it is possible for this test to fail given that shuffle is random.
    // There is a chance that the order of the cards will be the same.
    @Test
    public void testShuffle() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new ChanceCard(Action.Advance, "Test card 1", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 2", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 3", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 4", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 5", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 6", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 7", new HashMap<>()));
        Deck deck = new Deck(cards);
        ArrayList<Card> initialOrder = new ArrayList<>(deck.getDeck());
        deck.shuffle();
        ArrayList<Card> shuffledOrder = new ArrayList<>(deck.getDeck());
        assertNotEquals(initialOrder, shuffledOrder);
    }

    @Test
    public void testDraw() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new ChanceCard(Action.Advance, "Test card 1", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 2", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 3", new HashMap<>()));
        Deck deck = new Deck(cards);
        Card card1 = deck.draw();
        assertEquals(new ChanceCard(Action.Advance, "Test card 1", new HashMap<>()), card1);
        assertEquals(2, deck.size());
    }

    @Test
    public void testPeek() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new ChanceCard(Action.Advance, "Test card 1", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 2", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 3", new HashMap<>()));
        Deck deck = new Deck(cards);
        Card card1 = deck.peek();
        assertEquals(new ChanceCard(Action.Advance, "Test card 1", new HashMap<>()), card1);
        assertEquals(3, deck.size());
    }

    @Test
    public void testPeekIndex() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new ChanceCard(Action.Advance, "Test card 1", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 2", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 3", new HashMap<>()));
        Deck deck = new Deck(cards);
        Card card1 = deck.peek(1);
        assertEquals(new ChanceCard(Action.DirectMove, "Test card 2", new HashMap<>()), card1);
        assertEquals(3, deck.size());
    }

    @Test
    public void testAddToBottom() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new ChanceCard(Action.Advance, "Test card 1", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 2", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 3", new HashMap<>()));
        Deck deck = new Deck(cards);
        deck.addToBottom(new ChanceCard(Action.DirectMove, "Test card 4", new HashMap<>()));
        assertEquals(4, deck.size());
        assertEquals(new ChanceCard(Action.DirectMove, "Test card 4", new HashMap<>()), deck.getDeck().get(3));
    }

    @Test
    public void testAddToTop() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new ChanceCard(Action.Advance, "Test card 1", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 2", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 3", new HashMap<>()));
        Deck deck = new Deck(cards);
        deck.addToTop(new ChanceCard(Action.DirectMove, "Test card 4", new HashMap<>()));
        assertEquals(4, deck.size());
        assertEquals(new ChanceCard(Action.DirectMove, "Test card 4", new HashMap<>()), deck.getDeck().getFirst());
    }

    @Test
    public void testRemoveCard() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new ChanceCard(Action.Advance, "Test card 1", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 2", new HashMap<>()));
        cards.add(new ChanceCard(Action.DirectMove, "Test card 3", new HashMap<>()));
        Deck deck = new Deck(cards);
        assertTrue(deck.removeCard(new ChanceCard(Action.DirectMove, "Test card 2", new HashMap<>())));
        assertEquals(2, deck.size());
        assertEquals(new ChanceCard(Action.Advance, "Test card 1", new HashMap<>()), deck.peek());
        assertEquals(new ChanceCard(Action.DirectMove, "Test card 3", new HashMap<>()), deck.peek(1));
    }
}
