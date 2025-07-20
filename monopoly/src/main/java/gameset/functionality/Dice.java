package gameset.functionality;

import java.util.Random;

public class Dice {
    private boolean doubles = false;
    private int dice1;
    private int dice2;
    private int rollTotal;

    public boolean isDoubles() {
        return doubles;
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
     * Roll the dice.
     */
    public int rollDice() {
        Random rand = new Random();
        this.dice1 = rand.nextInt(6) + 1;
        this.dice2 = rand.nextInt(6) + 1;
        this.rollTotal = dice1 + dice2;
        this.doubles = dice1 == dice2;
        return rollTotal;
    }
}
