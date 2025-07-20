//package gameset.functionality;
//
//import gameset.cards.Card;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Class representing a deck of cards.
// *
// * Includes methods for shuffling, drawing, and adding cards to top or bottom of the deck.
// */
//public class Deck<T extends Card> {
//    ArrayList<T> deck;
//
//    /**
//     * Creates a new deck of cards.
//     *
//     * @param cards the cards to add to the deck
//     */
//    public Deck(List<T> cards) {
//        this.deck = (ArrayList<T>) cards;
//    }
//
//    /**
//     * Randomizes the order of the cards in the deck.
//     */
//    public void shuffle() {
//        Collections.shuffle(this.deck);
//    }
//
//    /**
//     * Removes and returns the card at the top of the deck.
//     *
//     * @return the card at the top of the deck
//     */
//    public T draw() {
//        return this.deck.removeFirst();
//    }
//
//    /**
//     * Adds a card to the bottom of the deck.
//     *
//     * @param card the card to add
//     */
//    public void addToBottom(T card) {
//        this.deck.add(card);
//    }
//
//    /**
//     * Adds a card to the top of the deck.
//     *
//     * @param card the card to add
//     */
//    public void addToTop(T card) {
//        this.deck.addFirst(card);
//    }
//
//    /**
//     * Returns the card at the top of the deck without removing it.
//     *
//     * @return the card at the top of the deck
//     */
//    public T peek() {
//        return this.deck.getFirst();
//    }
//
//    /**
//     * Returns the card at the specified index without removing it.
//     *
//     * @param index the index of the card to return
//     * @return the card at the specified index
//     */
//    public T peek(int index) {
//        return this.deck.get(index);
//    }
//
//    /**
//     * Removes a card from the deck.
//     *
//     * @param card the card to remove
//     * @return true if the card was found and removed, false otherwise
//     */
//    public boolean removeCard(T card) {
//        return this.deck.remove(card);
//    }
//
//    /**
//     * Returns the number of cards in the deck.
//     *
//     * @return the number of cards in the deck
//     */
//    public int size() {
//        return this.deck.size();
//    }
//
//    /**
//     * Returns the underlying deck of cards. Utilize for testing.
//     *
//     * @return the deck of cards
//     */
//    public ArrayList<T> getDeck() {
//        return this.deck;
//    }
//}
