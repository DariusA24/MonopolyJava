package gameset.screens;

import gameset.functionality.Player;
import gameutils.Ansi;
import java.util.Scanner;

public class GameScreen {
    public String turnMenu(Scanner scanner) {
        System.out.println("****************");
        System.out.println("1. End Turn");
        System.out.println("2. View Properties");
        System.out.println("3. Trade Center");
        System.out.println("****************");
        System.out.println("Enter the number choice: ");
        String inputChoice = scanner.next();
        while (!inputChoice.equals("1") && !inputChoice.equals("2") && !inputChoice.equals("3")){
            System.out.println("Enter a valid choice");
            inputChoice = scanner.next();
        }
        return inputChoice;
    }

    public int jailScreen(Scanner scanner, boolean hasGetOutOfJailCard) {
        int validChoices = 2;
        String menu = Ansi.ANSI_RED + "You are in jail" + System.lineSeparator() + Ansi.ANSI_RESET +
                "1. Roll a double to leave jail" + System.lineSeparator() +
                "2. Pay $50 to leave jail" + System.lineSeparator();

        if (hasGetOutOfJailCard) {
            menu += "3. Use get out of jail free card" + System.lineSeparator();
            validChoices += 1;
        }

        System.out.println(menu);

        String inputChoice = scanner.next();
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(inputChoice);
                if (choice >= 1 && choice <= validChoices) {
                    break;
                }
                throw new NumberFormatException("Enter a valid choice");
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid choice");
                inputChoice = scanner.next();
            }
        }
        return choice;
    }

    public Player tradeScreen(Scanner scanner, Player[] tradablePlayers) {
        // Display players to trade with
        int playerCount = 1;
        for (Player tradablePlayer : tradablePlayers) {
            System.out.println(playerCount++ + "Trade with " + tradablePlayer.getName() + " - cash: " + tradablePlayer.getMoney() + " - property count: " + tradablePlayer.getProperties().size());
        }
        // Prompt for player to select player to trade with
        System.out.println("Enter the number choice: ");
        try {
            return tradablePlayers[scanner.nextInt()];
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Enter a valid choice: ");
            return tradeScreen(scanner, tradablePlayers);
        }
    }

    private enum TradeState {
        Negotiate,
        Accept,
        Decline,
        Counter
    };

    public void tradeExchangeScreen(Scanner scanner, Player player, Player tradingWith) {
        TradeState state = TradeState.Negotiate;
        while (state != TradeState.Accept && state != TradeState.Decline) {
            state = switch (state) {
                case Negotiate -> negotiateScreen(scanner, player, tradingWith);
                case Counter -> counterScreen(scanner, player, tradingWith);
                default -> state;
            };
        }
    }

    private TradeState negotiateScreen(Scanner s, Player player, Player tradingWith) {
        // TODO: make boarders dynamic based on content
        // title
        System.out.println("=== TRADE NEGOTIATION ===");
        // player info
        System.out.println("Initiator: " + player.getName() + "   " + "Other Player: " + tradingWith.getName());
        System.out.println("--------------------------------");
        // TODO: make this a dynamic function as well that redraws based on offer state
        System.out.println("Offering:   Offering:");
        System.out.println("--------------------------------");
        System.out.println("Commands:");
        System.out.println("Arrows - navigate between properties (on cash use left/right to change amount)");
        System.out.println("Enter - add or remove selected item from offer");
        System.out.println("s - send the trade offer");
        System.out.println("q - cancel the trade");
        return TradeState.Counter;
    };

    private TradeState counterScreen(Scanner s, Player player, Player tradingWith) {
        // TODO: make boarders dynamic based on content
        // title
        System.out.println("=== TRADE Proposal ===");
        // player info
        System.out.println("Initiator: " + player.getName() + "   " + "Recipient: " + tradingWith.getName());
        System.out.println("--------------------------------");
        // TODO: make this a dynamic function as well that redraws based on offer state
        System.out.println("Offering:   Offering:");
        System.out.println("--------------------------------");
        System.out.println("Commands:");
        System.out.println("Arrows - navigate between properties (on cash use left/right to change amount)");
        System.out.println("Enter - add or remove selected item from offer");
        System.out.println("s - send the trade offer");
        System.out.println("a - accept the trade");
        System.out.println("q - decline the trade");
        return TradeState.Counter;
    };
}
