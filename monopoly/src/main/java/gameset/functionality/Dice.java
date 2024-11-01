package gameset.functionality;

import gameutils.Ansi;

import java.util.Random;
import java.util.Scanner;


public class Dice {

    public boolean doubles = false;
    private int getDiceValues(){
        Random rand = new Random();
        int dice1 = rand.nextInt(6) + 1;
        int dice2 = rand.nextInt(6) + 1;
        int rollTotal = dice1 + dice2;

        if(dice1 == dice2){
            this.doubles = true;
        }
        else {
            this.doubles = false;
        }

        System.out.println("************");
        System.out.println("Dice 1 is: " + dice1);
        System.out.println("Dice 2 is: " + dice2);
        System.out.println("Roll is: " + rollTotal);
        System.out.println("************");
        return rollTotal;
    }

    public int rollDice(Player player){
        Scanner rollButton = new Scanner(System.in);
        System.out.println("Player: " + player.getColor() + player.getName() +
                Ansi.ANSI_RESET + Ansi.ANSI_YELLOW + "\nPress R to roll. " + Ansi.ANSI_RESET);

        String rollInput = rollButton.next();
        while(!rollInput.equals("R") && !rollInput.equals("r")){
            System.out.println("Please press 'R' to roll");
            rollInput = rollButton.next();
        }
        return getDiceValues();
    }
}
