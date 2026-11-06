import java.util.Objects;

/**
 * Practical 03 - Part A - Program 2: Duplicate card check.
 * A playing card compared by rank and suit.
 */
public class Card {

    private String rank;
    private String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String toString() {
        return rank + " of " + suit;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        Card other = (Card) o;
        return rank.equals(other.rank) && suit.equals(other.suit);
    }

    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}
