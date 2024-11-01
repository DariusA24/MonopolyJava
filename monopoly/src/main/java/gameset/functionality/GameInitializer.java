package gameset.functionality;

import gameutils.Ansi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class GameInitializer {
    private final ArrayList<Player> playerList = new ArrayList<Player>();
    private final Dice dice = new Dice();
    private Scanner userInput = new Scanner(System.in);
    /**
     * This private method will be used to initialize a stack of colors.
     * @return a stack that has the colors
     */
    private Stack<String> CreateColorStack() {
        Stack<String> colorStack = new Stack<String>();
        colorStack.addAll(Arrays.asList(Ansi.ColorList));
        return colorStack;
    }

    /**
     *
     * @param rollScore
     * @param initialPlayerList
     */
    private void fillInPlayerList(int[] rollScore, ArrayList<Player> initialPlayerList) {
        int placedPlayer = 0;
        int highestRoll = 0;
        int highestRollLocation = 0;
        while(placedPlayer < initialPlayerList.size()){
            for(int i = 0; i < rollScore.length; i++){
                if(highestRoll < rollScore[i]){
                    highestRoll = rollScore[i];
                    highestRollLocation = i;
                }
            }
            playerList.add(initialPlayerList.get(highestRollLocation));
            rollScore[highestRollLocation] = 0;
            highestRollLocation = 0;
            highestRoll = 0;
            placedPlayer++;
        }
    }

    /**
     *
     * @param initialPlayerList
     */
    private void setOrderPlayerList(ArrayList<Player> initialPlayerList) {
        int i = 0;
        int[] rollScore = new int[initialPlayerList.size()];
        System.out.println("Rolling to set order");
        while(i < initialPlayerList.size()){
            int roll = dice.rollDice(initialPlayerList.get(i));
            rollScore[i] = roll;
            i++;
        }
        fillInPlayerList(rollScore, initialPlayerList);
    }

    /**
     * This method will set the amount of players that will be playing the game.
     * It will then create the player objects and set them in an array list.
     * @param playerAmount sets the amount of players that will be playing the game
     */
    public ArrayList<Player> setPlayers(int playerAmount) {
        int i = 0;
        Stack<String> colorStack = CreateColorStack();
        ArrayList<Player> initialPlayerList = new ArrayList<Player>();
        System.out.println("-----------------------");
        System.out.println(Ansi.ANSI_YELLOW + "Player Creation" + Ansi.ANSI_RESET);

        while(i < playerAmount){
            System.out.println("Enter Player Name: ");
            String playerName = userInput.next();
            Player player = new Player(playerName, 1500, colorStack.pop());
            initialPlayerList.add(player);
            i++;
        }
        System.out.println("-----------------------");
        setOrderPlayerList(initialPlayerList);
        return playerList;
    }
}
