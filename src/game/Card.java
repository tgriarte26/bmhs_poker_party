package game;

public class Card {

    private int suit; // The suit of the card (0-3 representing ♠, ♣, ♦, ♠ respectively).
    private int value; // The value of the card (2-14 representing 2-10, J, Q, K, A respectively).

    /**
     * Constructor for a Card object.
     * @param suit The suit of the card (0-3 representing ♠, ♣, ♦, ♠ respectively).
     * @param value The value of the card (2-14 representing 2-10, J, Q, K, A respectively).
     */
    public Card(int suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    /**
     * Get the suit of the card.
     * @return The suit of the card.
     */
    public int getSuit() {
        return suit;
    }

    /**
     * Get the value of the card.
     * @return The value of the card.
     */
    public int getValue() {
        return value;
    }

    /**
     * Convert the card to a string representation.
     * @return A string representation of the card, e.g., "10♠" for the 10 of spades.
     */
    public String toString() {
        String[] suits = {"♥", "♣", "♦", "♠"};
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        return "["+ values[this.value-2] + suits[this.suit] + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Card otherCard = (Card) obj;
        return this.suit == otherCard.getSuit() && this.value == otherCard.getValue();
    }
}
