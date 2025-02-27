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
    public int displayPropertyScreen(Player player, Scanner scanner) {
        int propertyBuildingPurchased = 0;
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
                    Property property = player.getProperties().get(number - 1);
                    System.out.println("Property #" + number + ": " + property.displayPropertyName());
                    if (property.getNumHouses() != 5) {
                        System.out.println("You currently own: " + property.getNumHouses() + " houses");
                        System.out.println("Press B to purchase next building for: $" + property.getBuildingPrice());
                        while (true) {
                            input = scanner.nextLine();
                            if (input.equals("B") || input.equals("b")) {
                                propertyBuildingPurchased = number;
                            }
                            break;
                        }
                    } else if (property.getNumHouses() == 5) {
                        System.out.println("You currently own a hotel on this property");
                    }
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");

            }
        }
        System.out.println("******************");
        return propertyBuildingPurchased;
    }
}
