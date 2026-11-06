/**
 * Practical 03 - Part A - Program 2: Duplicate card check (driver).
 * Adds cards to an array one at a time and reports the first repeat.
 */
public class Driver {

    public static void main(String[] args) {
        Card[] incoming = {
            new Card("Ace", "Spades"), new Card("Queen", "Hearts"),
            new Card("10", "Clubs"), new Card("Ace", "Spades"),
            new Card("Queen", "Hearts")
        };
        Card[] hand = new Card[incoming.length];
        int size = 0;

        for (Card card : incoming) {
            boolean duplicate = false;
            for (int i = 0; i < size; i++) {
                if (hand[i].equals(card)) {
                    duplicate = true;
                    break;
                }
            }
            if (duplicate) {
                System.out.println("Duplicate found: " + card);
                break;
            }
            hand[size++] = card;
            System.out.println("Added: " + card);
        }
        System.out.println("Cards in hand: " + size);
    }
}
