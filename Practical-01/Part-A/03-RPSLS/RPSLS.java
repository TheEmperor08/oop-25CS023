import java.util.Random;
import java.util.Scanner;

/**
 * Practical 01 - Part A - Program 3 (For Advanced Learners):
 * Rock-Paper-Scissors-Lizard-Spock, 5 rounds against the computer.
 */
public class RPSLS {

    enum Move { ROCK, PAPER, SCISSORS, LIZARD, SPOCK }

    /** Returns true when move a beats move b. */
    static boolean beats(Move a, Move b) {
        return switch (a) {
            // Scissors cut Paper and decapitate Lizard
            case SCISSORS -> b == Move.PAPER || b == Move.LIZARD;
            case PAPER -> b == Move.ROCK || b == Move.SPOCK;         // covers Rock, disproves Spock
            // Rock crushes Lizard and crushes Scissors
            case ROCK -> b == Move.LIZARD || b == Move.SCISSORS;
            case LIZARD -> b == Move.SPOCK || b == Move.PAPER;       // poisons Spock, eats Paper
            case SPOCK -> b == Move.SCISSORS || b == Move.ROCK;      // smashes Scissors, vaporizes Rock
        };
    }

    /** 1 if a beats b, -1 if b beats a, 0 for a tie. */
    static int winner(Move a, Move b) {
        if (a == b) {
            return 0;
        }
        return beats(a, b) ? 1 : -1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int playerScore = 0;
        int computerScore = 0;
        Move[] moves = Move.values();

        for (int round = 1; round <= 5; ) {
            System.out.print("Round " + round
                    + " - your move (ROCK/PAPER/SCISSORS/LIZARD/SPOCK): ");
            if (!scanner.hasNext()) {
                return;
            }
            Move player;
            try {
                player = Move.valueOf(scanner.next().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid move, try again.");
                continue;
            }
            Move computer = moves[random.nextInt(moves.length)];
            int result = winner(player, computer);
            String outcome = switch (result) {
                case 1 -> "You win this round";
                case -1 -> "Computer wins this round";
                default -> "Tie";
            };
            if (result == 1) {
                playerScore++;
            } else if (result == -1) {
                computerScore++;
            }
            System.out.println("You: " + player + " | Computer: " + computer + " -> " + outcome);
            round++;
        }

        String summary;
        if (playerScore > computerScore) {
            summary = "You win " + playerScore + "-" + computerScore;
        } else if (computerScore > playerScore) {
            summary = "Computer wins " + computerScore + "-" + playerScore;
        } else {
            summary = "It's a draw " + playerScore + "-" + computerScore;
        }
        System.out.println(summary);
        scanner.close();
    }
}
