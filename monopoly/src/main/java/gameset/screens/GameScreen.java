package gameset.screens;

import gameset.functionality.Player;
import gameutils.Ansi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
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
            System.out.println(playerCount++ + ". Trade with " + tradablePlayer.getName() + " - cash: " + tradablePlayer.getMoney() + " - property count: " + tradablePlayer.getProperties().size());
        }
        // Prompt for player to select player to trade with
        System.out.println("Enter the number choice: ");
        try {
            int choice = scanner.nextInt();
            while (choice < 1 || choice > tradablePlayers.length) {
                System.out.println("Enter a valid choice");
                choice = scanner.nextInt();
            }
            return tradablePlayers[choice - 1];
        } catch (InputMismatchException e) {
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
        // TODO: uncomment the loop once counter screen is implemented otherwise it will never end
//        while (state != TradeState.Accept && state != TradeState.Decline) {
            state = switch (state) {
                case Negotiate -> negotiateScreen(scanner, player, tradingWith);
                case Counter -> counterScreen(scanner, player, tradingWith);
                default -> state;
            };
//        }
    }

    private TradeState negotiateScreen(Scanner s, Player player, Player tradingWith) {
        // TODO: make boarders dynamic based on content
        Table table = new Table();
        table.addSection(
                new Section(
                        new TableHeader("=== TRADE NEGOTIATION ===")
                )
        );
        // TODO: for some reason the other player is not aligned with the rest of the table
        table.addSection(
                new Section(
                        new TableHeader("Initiator: " + player.getName(), "Other Player: " + tradingWith.getName())
                )
        );
        // TODO: make curser dynamic [x] or [ ] and also have > or < to indicate selected
        // build table rows
        Section offeringSection = new Section(new TableHeader("Offering:", "Offering:"));
        // Add properties to table
        int mostProperties = Math.max(player.getProperties().size(), tradingWith.getProperties().size());
        for (int i = 0; i < mostProperties; i++) {
            offeringSection.addRow(new TableRow(
                    i < player.getProperties().size() ? player.getProperties().get(i).displayPropertyName() : "",
                    i < tradingWith.getProperties().size() ? tradingWith.getProperties().get(i).displayPropertyName() : ""
            ));
        }
        // TODO: actually want this to be dynamic cash based on offer state Show current selection/max
        offeringSection.addRow(new TableRow("$0/" + player.getMoney(), "$0/" + tradingWith.getMoney()));
        // Add commands to table
        table.addSection(offeringSection);
        Section commandsSection = new Section(new TableHeader("Commands:"));
        commandsSection.addRow(new TableRow("Enter - add or remove selected item from offer"));
        commandsSection.addRow(new TableRow("s - send the trade offer"));
        commandsSection.addRow(new TableRow("q - cancel the trade"));
        table.addSection(commandsSection);
        // Display table
        System.out.println(table.toString());
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

// TODO: move to a separate utility class
interface TableProperties {
    // The largest string in the table should be this length
    int maxLength();
}

class Table implements TableProperties {
    ArrayList<Section> sections;

    Table(ArrayList<Section> sections) {
        this.sections = sections;
    }

    Table() {
        this.sections = new ArrayList<>();
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void removeSection(Section section) {
        this.sections.remove(section);
    }

    public void updateSection(int index, Section section) {
        this.sections.set(index, section);
    }

    public int maxLength() {
        int maxLength = 0;
        for (Section section : sections) {
            if (section.maxLength() > maxLength) {
                maxLength = section.maxLength();
            }
        }
        return maxLength;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Section d : sections) {
            s.append(d.toString(this.maxLength()));
            s.append(System.lineSeparator());
        }
        return s.toString();
    }
}

class Section implements TableProperties {
    TableHeader header;
    ArrayList<TableRow> rows;

    Section(TableHeader header, ArrayList<TableRow> rows) {
        this.header = header;
        this.rows = rows;
    }

    Section(TableHeader header) {
        this.header = header;
        this.rows = new ArrayList<>();
    }

    public void addRow(TableRow r) {
        this.rows.add(r);
    }

    public void removeRow(TableRow r) {
        this.rows.remove(r);
    }

    public void updateRow(int index, TableRow r) {
        this.rows.set(index, r);
    }

    public int maxLength() {
        // Find the longest row
        int maxLength = 0;
        for (TableRow row : rows) {
            if (row.maxLength() > maxLength) {
                maxLength = row.maxLength();
            }
        }
        // Find out if the header is longer
        if (header.maxLength() > maxLength) {
            maxLength = header.maxLength();
        }
        return maxLength;
    }

    public String toString(int spacing) {
        StringBuilder s = new StringBuilder();
        s.append(header.toString(spacing));
        s.append(System.lineSeparator());
        for (TableRow d : rows) {
            s.append(d.toString(spacing));
            s.append(System.lineSeparator());
        }
        return s.toString();
    }
}

interface TableData {
    int getCols();
    String[] getData();

}

class TableHeader implements TableData, TableProperties {
    int cols;
    ArrayList<String> data;

    TableHeader(ArrayList<String> data) {
        this.data = data;
        this.cols = data.size();
    }


    TableHeader(String... strings) {
        this.data = new ArrayList<>(Arrays.asList(strings));
        this.cols = data.size();
    }

    public int getCols() {
        return cols;
    }

    public String[] getData() {
        return data.toArray(new String[0]);
    }

    public int maxLength() {
        int maxLength = 0;
        for (String s : data) {
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
        }
        return maxLength;
    }

    public String toString(int spacing) {
        StringBuilder s = new StringBuilder();
        for (String d : data) {
            s.append(String.format("%-" + spacing + "s", d));
        }
        return s.toString();
    }
}

class TableRow implements TableData, TableProperties {
    int cols;
    ArrayList<String> data;

    TableRow(int cols, ArrayList<String> data) {
        this.cols = cols;
        this.data = data;
    }

    TableRow(String... strings) {
        this.data = new ArrayList<>(Arrays.asList(strings));
        this.cols = data.size();
    }

    public int getCols() {
        return cols;
    }

    public String[] getData() {
        return data.toArray(new String[0]);
    }

    public int maxLength() {
        int maxLength = 0;
        for (String s : data) {
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
        }
        return maxLength;
    }

    public String toString(int spacing) {
        StringBuilder s = new StringBuilder();
        for (String d : data) {
            s.append(String.format("%-" + spacing + "s", d));
        }
        return s.toString();
    }
}