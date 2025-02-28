package gameset.functionality;

import gameutils.Ansi;

import java.util.Random;
import java.util.Scanner;


public class Dice {

    private boolean doubles = false;
    private int dice1;
    private int dice2;
    private int rollTotal;

    public boolean isDoubles() {
        return doubles;
    }

    /**
     * Gets random values for the dice, calculates the total and checks for doubles.
     */
    private void getDiceValues(){
        Random rand = new Random();
        this.dice1 = rand.nextInt(6) + 1;
        this.dice2 = rand.nextInt(6) + 1;
        this.rollTotal = dice1 + dice2;
        this.doubles = dice1 == dice2;
    }

    public int getRollTotal() {
        return rollTotal;
    }

    public void setRollTotal(int rollTotal) {
        this.rollTotal = rollTotal;
    }

    /**
     * Prints the result of the roll.
     */
    @Override
    public String toString() {
        return "----------------" +
                System.lineSeparator() +
                "Dice 1 is: " + this.dice1 +
                System.lineSeparator() +
                "Dice 2 is: " + this.dice2 +
                System.lineSeparator() +
                "Roll is: " + this.rollTotal +
                System.lineSeparator() +
                "----------------";
    }

    /**
     * Prompts the user to roll.
     *
     * @param player player object
     * @param rollButton Scanner for the roll button
     */
    private void playerRollPrompt(Player player, Scanner rollButton) {
        System.out.println("Player: " + player.getColor() + player.getName() +
                Ansi.ANSI_RESET + Ansi.ANSI_YELLOW + "\nPress R to roll. " + Ansi.ANSI_RESET);

        rollInputVerifier(rollButton);  // Wait for valid input
        getDiceValues();
    }

    /**
     * Checks the input and retries until the user presses R.
     *
     * @param rollButton scanner for the roll button
     */
    private void  rollInputVerifier(Scanner rollButton) {
        String rollInput = rollButton.next();
        while (!rollInput.equalsIgnoreCase("R")) {
            System.out.println("Please press 'R' to roll");
            rollInput = rollButton.next();
        }
    }

    /**
     * Call to roll the dice.
     */
    public int rollDice(Player player) {
        playerRollPrompt(player, new Scanner(System.in));
        String output = toString();
        System.out.println(output);
        return this.rollTotal;
    }
}
