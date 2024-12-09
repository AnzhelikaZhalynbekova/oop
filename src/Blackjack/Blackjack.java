package Blackjack;

import java.util.Scanner;

public class Blackjack {

    private static final int HEARTS = 0;
    private static final int DIAMONDS = 1;
    private static final int SPADES = 2;
    private static final int CLUBS = 3;

    private static final int JACK = 11;
    private static final int QUEEN = 12;
    private static final int KING = 13;
    private static final int ACE = 14;

    private static final int STARTING_BANKROLL = 100;

    private final Scanner scanner = new Scanner(System.in);

    private String getPlayerMove() {
        while (true) {
            System.out.print("Enter move (hit/stand): ");
            String move = scanner.nextLine().toLowerCase();

            if (move.equals("hit") || move.equals("stand")) {
                return move;
            }
            System.out.println("Please try again.");
        }
    }

    private void dealerTurn(Hand dealer, Deck deck) {
        while (true) {
            System.out.println("Dealer's hand");
            System.out.println(dealer);

            int value = dealer.getValue();
            System.out.println("Dealer's hand has value " + value);

            System.out.print("Press Enter to continue...");
            scanner.nextLine();

            if (value < 17) {
                System.out.println("Dealer hits");
                Card c = deck.deal();
                dealer.addCard(c);

                System.out.println("Dealer card was " + c);

                if (dealer.busted()) {
                    System.out.println("Dealer busted!");
                    break;
                }
            } else {
                System.out.println("Dealer stands.");
                break;
            }
        }
    }

    private boolean playerTurn(Hand player, Deck deck) {
        while (true) {
            String move = getPlayerMove();

            if (move.equals("hit")) {
                Card c = deck.deal();
                System.out.println("Your card was: " + c);
                player.addCard(c);
                System.out.println("Player's hand");
                System.out.println(player);

                if (player.busted()) {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    private boolean playerWins(Hand player, Hand dealer) {
        if (player.busted()) {
            return false;
        }

        if (dealer.busted()) {
            return true;
        }

        return player.getValue() > dealer.getValue();
    }

    private boolean push(Hand player, Hand dealer) {
        return player.getValue() == dealer.getValue();
    }

    private double findWinner(Hand dealer, Hand player, int bet) {
        if (playerWins(player, dealer)) {
            System.out.println("Player wins!");

            if (player.hasBlackjack()) {
                return 1.5 * bet;
            }

            return bet;
        } else if (push(player, dealer)) {
            System.out.println("You push");
            return 0;
        } else {
            System.out.println("Dealer wins");
            return -bet;
        }
    }

    private double playRound(double bankroll) {
        System.out.print("What is your bet? ");
        int bet = Integer.parseInt(scanner.nextLine());

        Deck deck = new Deck();
        deck.shuffle();

        Hand player = new Hand();
        Hand dealer = new Hand();

        player.addCard(deck.deal());
        player.addCard(deck.deal());
        dealer.addCard(deck.deal());
        dealer.addCard(deck.deal());

        System.out.println("Player's Hand:");
        System.out.println(player);

        System.out.println("Dealer's Hand:");
        dealer.printDealerHand();

        boolean playerBusted = playerTurn(player, deck);

        if (playerBusted) {
            System.out.println("You busted :(");
        }

        System.out.print("Press Enter for dealer's turn...");
        scanner.nextLine();
        dealerTurn(dealer, deck);

        double bankrollChange = findWinner(dealer, player, bet);
        bankroll += bankrollChange;

        System.out.println("New bankroll: " + bankroll);

        return bankroll;
    }

    public void run() {
        double bankroll = STARTING_BANKROLL;
        System.out.println("Starting bankroll: " + bankroll);

        while (true) {
            bankroll = playRound(bankroll);

            System.out.print("Would you like to play again? (Y/N): ");
            String playAgain = scanner.nextLine();
            if (playAgain.equalsIgnoreCase("N")) {
                break;
            }
        }

        System.out.println("Thanks for playing!");
    }

    public static void main(String[] args) {
        Blackjack game = new Blackjack();
        game.run();
    }
}
