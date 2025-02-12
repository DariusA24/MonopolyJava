package gameset.functionality;

import gameutils.Ansi;

import java.util.Random;
import java.util.Scanner;


public class Dice {

    private boolean doubles = false;

    public boolean isDoubles() {
        return doubles;
    }

    /**
     * Gets random values for the dice, calculates the total and checks for doubles.
     */
    private int getDiceValues(){
        Random rand = new Random();
        int dice1 = rand.nextInt(6) + 1;
        int dice2 = rand.nextInt(6) + 1;
        int rollTotal = dice1 + dice2;
        this.doubles = dice1 == dice2;
        printDiceResults(dice1, dice2, rollTotal);
        return rollTotal;
    }
    /**
     * Prints the result of the roll.
     *
     * @param dice1 result of the first dice
     * @param dice2 result of the second dice
     * @param rollTotal total of the first and second dice added together
     */
    private void printDiceResults(int dice1, int dice2, int rollTotal) {
        System.out.println("************");
        System.out.println("Dice 1 is: " + dice1);
        System.out.println("Dice 2 is: " + dice2);
        System.out.println("Roll is: " + rollTotal);
        System.out.println("************");
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
        return getDiceValues();
    }
}
