//package gameset.screens;
//
//import gameutils.Ansi;
//
//import java.util.Scanner;
//
//public class GameScreen {
//    public String turnMenu(Scanner scanner) {
//        System.out.println("****************");
//        System.out.println("1. End Turn");
//        System.out.println("2. View Properties");
//        System.out.println("3. Trade Center");
//        System.out.println("****************");
//        System.out.println("Enter the number choice: ");
//        String inputChoice = scanner.next();
//        while (!inputChoice.equals("1") && !inputChoice.equals("2") && !inputChoice.equals("3")){
//            System.out.println("Enter a valid choice");
//            inputChoice = scanner.next();
//        }
//        return inputChoice;
//    }
//
//    public int jailScreen(Scanner scanner, boolean hasGetOutOfJailCard) {
//        int validChoices = 2;
//        String menu = Ansi.ANSI_RED + "You are in jail" + System.lineSeparator() + Ansi.ANSI_RESET +
//                "1. Roll a double to leave jail" + System.lineSeparator() +
//                "2. Pay $50 to leave jail" + System.lineSeparator();
//
//        if (hasGetOutOfJailCard) {
//            menu += "3. Use get out of jail free card" + System.lineSeparator();
//            validChoices += 1;
//        }
//
//        System.out.println(menu);
//
//        String inputChoice = scanner.next();
//        int choice;
//        while (true) {
//            try {
//                choice = Integer.parseInt(inputChoice);
//                if (choice >= 1 && choice <= validChoices) {
//                    break;
//                }
//                throw new NumberFormatException("Enter a valid choice");
//            } catch (NumberFormatException e) {
//                System.out.println("Enter a valid choice");
//                inputChoice = scanner.next();
//            }
//        }
//        return choice;
//    }
//}
