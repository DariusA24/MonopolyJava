package gameset.screens;

import gameset.functionality.Player;
import gameset.functionality.Property;


import java.util.Scanner;

public class BuildingScreen {
    /**
     * Screen which allows players to choose a property that they would like to build buildings on.
     *
     * <p>This method displays a screen and allows input from the user on the property
     * that they would like to build buildings on.
     *
     * @param player the player object
     * @param scanner which allows input
     */
    public int displayPropertySelectScreen(Player player, Scanner scanner) {
        int propertySelected = 0;
        System.out.println("******************");
        System.out.println("Select property number you would like to view more detail on OR F to exit");
        boolean flag = false;
        while (!flag) {
            String input = scanner.nextLine();
            if (input.equals("F") || input.equals("f")) {
                flag = true;
                continue;
            }
            try {
                int number = Integer.parseInt(input);
                if (number >= 1 && number <= player.getProperties().size()) {
                    propertySelected = number;
                    flag = true;
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
        System.out.println("******************");
        return propertySelected;
    }

    public int displayBuildingOptionScreen(Player player, Scanner scanner) {
        System.out.println("1. Purchase Building");
        System.out.println("2. Sell Building");
        System.out.println("3. Exit");
        boolean flag = false;
        while (!flag) {
            String input = scanner.nextLine();
            if (input.equals("3")) {
                flag = true;
                return 3;
            }
            try {
                int number = Integer.parseInt(input);
                if (number == 1 || number == 2) {
                    return number;
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
        System.out.println("******************");
        return 3;
    }
}
