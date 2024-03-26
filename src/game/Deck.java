package game;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards; // The list of cards in the deck.
    private int numDecksUsed;
    /**
     * Constructor for a Deck object.
     * Initializes the deck with a standard 52-card deck, with 4 suits and 13 values each.
     */
    public Deck() {
        this.cards = new ArrayList<>();
        // 0 = heart, 1 = club, 2 = diamond, 3 = spade
        for (int suit = 0; suit <= 3; suit++) {
            // 0-10 as expected, 11 = Jack, 12 = Queen, 13 = King, 14 = Ace
            // Ace *should* be treated as either 1 or 11
            for (int value = 2; value <= 14; value++) {
                cards.add(new Card(suit, value)); // Add a new card to the deck for each suit and value.
            }
        }
        numDecksUsed = 1;
    }

    /**
     * Shuffles the deck of cards.
     * This method uses the Collections.shuffle() method to randomize the order of the cards.
     */
    public void shuffle() {
        Collections.shuffle(cards); // Shuffle the cards in the deck.
    }

    public int getNumDecksUsed() {
        return numDecksUsed;
    }

    /**
     * Retrieves the top card from the deck.
     * If the deck is empty, a new standard 52-card deck is created, shuffled, and the top card is returned.
     * @return The top card from the deck.
     */
    public Card topCard() {
        if (cards.isEmpty()) {
            for (int suit = 0; suit <= 3; suit++) {
                for (int value = 2; value <= 14; value++) {
                    cards.add(new Card(suit, value)); // Add a new card to the deck for each suit and value.
                }
            }
            numDecksUsed++;
            shuffle(); // Shuffle the new deck.
        }
        return cards.remove(cards.size() - 1); // Remove and return the top card from the deck.
    }

    /**
     * Get the current size of the deck.
     * @return The number of cards remaining in the deck.
     */
    public int getDeckSize() {
        return cards.size(); // Return the size of the deck.
    }
}
