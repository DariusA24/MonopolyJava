package gameset.screens;

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

    public String jailScreen(Scanner scanner) {

        System.out.println(Ansi.ANSI_RED + "You are in jail \n" + Ansi.ANSI_RESET +
                "1. Roll a double to leave jail \n" +
                "2. Pay $50 to leave jail. \n");
        String inputChoice = scanner.next();
        while (!inputChoice.equals("1") && !inputChoice.equals("2")){
            System.out.println("Enter a valid choice");
            inputChoice = scanner.next();
        }
        return inputChoice;
    }
}
