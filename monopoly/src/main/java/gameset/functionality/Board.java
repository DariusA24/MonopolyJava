package gameset.functionality;

import gameset.cards.ChanceCard;
import gameset.cards.CommunityChestCard;
import gameutils.Ansi;
import gameutils.ResourceParser;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Board {
    private final HashMap<String, Integer> playerPositions = new HashMap<>();
    private final ArrayList<Property> gameBoard;
    private final Deck<ChanceCard> chanceCards;
    private final Deck<CommunityChestCard> communityChestCards;
    private int house_counter;
    private int hotel_counter;

    public Board(ArrayList<Player> players) throws IOException {
        ResourceParser propertyParser = new ResourceParser("/models/propertyData.json");
        ResourceParser chanceCardParser = new ResourceParser("/models/chanceData.json");
        ResourceParser communityChestCardParser = new ResourceParser("/models/communityData.json");
        this.gameBoard = propertyParser.parseJsonToArrayList(Property.class);
        this.chanceCards = new Deck<>(chanceCardParser.parseJsonToArrayList(ChanceCard.class));
        this.communityChestCards = new Deck<>(communityChestCardParser.parseJsonToArrayList(CommunityChestCard.class));
        this.house_counter = 0;
        this.hotel_counter = 0;
        players.forEach(p -> playerPositions.put(p.getNameNoColor(), 0));
    }

    public ArrayList<Property> getGameBoard() {
        return gameBoard;
    }

    public void passedGo(Player player) {
        player.addMoney(200);
        System.out.println("Player: ");
        player.displayColoredName();
        System.out.println(" Passed Go, Collect: " + Ansi.ANSI_GREEN + " $200 " + Ansi.ANSI_RESET);
    }

    /**
     * Gets the properties within the specified colorset.
     *
     * @param color The color of the property that you want to look at.
     * @return The properties that fit the colorset.
     */
    public ArrayList<Property> getPropertiesByColor(String color) {
        return this.gameBoard.stream().filter(property -> property.getColor().equals(color))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * Gets the amount of properties within the specified colorset.
     *
     * @param color The color of the property that you want to look at.
     * @return The amount of properties that fit the colorset.
     */
    public Long getPropertiesColorCount(String color) {
        return (long) getPropertiesByColor(color).size();
    }

    /**
     * Checks if all the properties have the same amount of houses before allowing player to build a new house.
     *
     * @param propertyToAddBuilding The property that the player wants to build a new house on.
     * @return The list of properties names that must have buildings built on.
     * If empty, then the user can proceed to build on the property specified.
     */
    public ArrayList<String> evenlyBuildingAcrossGroupWithNoMortgage(Property propertyToAddBuilding) {
        int amountOfHousesOnProperty = propertyToAddBuilding.getNumHouses();
        ArrayList<Property> propertiesToTest = getPropertiesByColor(propertyToAddBuilding.getColor());
        ArrayList<String> propertiesNotMeetingFilter = new ArrayList<>();
        for(Property property : propertiesToTest) {
            if ((!property.getName().equals(propertyToAddBuilding.getName()) && (property.getNumHouses() < amountOfHousesOnProperty) || (property.isMortgaged()))) {
                propertiesNotMeetingFilter.add(property.getName());
            }
        }
        return propertiesNotMeetingFilter;
    }

    public Property getProperty(int location) {
        return this.gameBoard.get(location);
    }

    public int getHouseCount() {
        return this.house_counter;
    }

    public int getHotelCount() {
        return this.hotel_counter;
    }

    /**
     * Gets the position of the property with the given name on the board.
     *
     * @param propertyName The name of the property to search for.
     * @return The position of the property, or an empty Optional if it is not found.
     */
    public OptionalInt getPropertyPosition(String propertyName) {
        return this.gameBoard.stream()
                .filter(property -> property.getName().equals(propertyName))
                .mapToInt(gameBoard::indexOf)
                .findFirst();
    }

    /**
     * Returns the index of the next nearest property of a specific type (railroad, utility, property).
     *
     * @param location the starting location
     * @param type the type of property
     * @return The index of the closest property
     */
    public Optional<Integer> getNearestPropertyType(int location, String type) {
        return gameBoard.stream()
                .filter(p -> p.getType().equalsIgnoreCase(type))
                .map(gameBoard::indexOf)
                .sorted()
                // 39 -> 12 (-39), 38 -> 39 (1),
                .map(p -> p - location < 0 ? p - location + 40 : p - location)
                .min(Integer::compare);
    }

    public Deck<ChanceCard> getChanceCards() {
        return chanceCards;
    }

    public Deck<CommunityChestCard> getCommunityChestCards() {
        return communityChestCards;
    }

    /**
     * Returns if the house/hotel counter has been adjusted based on what the event was.
     *
     * @param buildingType the type of building that needs to adjust the counter.
     * @param buying whether buying or selling
     * @return If the counters have been adjusted
     */
    public boolean adjustBuildingCounters(String buildingType, boolean buying) {
        int HOUSE_AMOUNT = 32;
        int HOTEL_AMOUNT = 12;
        if (buildingType.equalsIgnoreCase("house")) {
            if (buying) {
                house_counter++;
                if (house_counter > HOUSE_AMOUNT) {
                    house_counter--;
                    return false;
                }
                return true;
            } else {
                if (house_counter > 0) {
                    house_counter--;
                }
            }
        } else if (buildingType.equalsIgnoreCase("hotel")) {
            if (buying) {
                hotel_counter++;
                if (hotel_counter > HOTEL_AMOUNT) {
                    hotel_counter--;
                    return false;
                }
            } else {
                if (hotel_counter > 0) {
                    hotel_counter--;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        int size = 220;
        int maxLen = size / 11 - 1;
        char corner = '+';
        String dash = "-";
        char edge = '|';
        StringBuilder boardString = new StringBuilder();

        boardString.append(makeBorder(size, corner, dash));

        int row = 0;
        while (row <= 10) {
            int propertyStartIdx = 20 - row;
            if (propertyStartIdx == 20) {
                for (int i = 0; i <= 10; i++) {
                    if (i == 10) {
                        boardString.append(
                                surroundWithEdge(
                                        getProperty(propertyStartIdx + i).displayPropertyName(maxLen + 1),
                                        edge,
                                        false
                                )
                        );
                    } else {
                        boardString.append(
                                surroundWithEdge(
                                        getProperty(propertyStartIdx + i).displayPropertyName(maxLen),
                                        edge,
                                        true
                                )
                        );
                    }
                }
                boardString.append(System.lineSeparator());

                // Add players
                for (int i = 0; i <= 10; i++) {
                    if (i == 10) {
                        boardString.append(
                                surroundWithEdge(
                                        centerString(
                                                printPlayersOnProperty(propertyStartIdx + i, maxLen),
                                                maxLen + 1
                                        ),
                                        edge,
                                        false
                                )
                        );
                    } else {
                        boardString.append(
                                surroundWithEdge(
                                        centerString(
                                                printPlayersOnProperty(propertyStartIdx + i, maxLen),
                                                maxLen
                                        ),
                                        edge,
                                        true
                                )
                        );
                    }
                }
                boardString.append(System.lineSeparator());
            } else if (propertyStartIdx == 10) {
                for (int i = 0; i <= 10; i++) {
                    if (i == 10) {
                        boardString.append(
                                surroundWithEdge(
                                        getProperty(0).displayPropertyName(maxLen + 1),
                                        edge,
                                        false
                                )
                        );
                    } else {
                        boardString.append(
                                surroundWithEdge(
                                        getProperty(propertyStartIdx - i).displayPropertyName(maxLen),
                                        edge,
                                        true
                                )
                        );
                    }
                }
                boardString.append(System.lineSeparator());

                // Add players
                for (int i = 0; i <= 10; i++) {
                    if (i == 10) {
                        boardString.append(
                                surroundWithEdge(
                                        centerString(printPlayersOnProperty(0, maxLen), maxLen + 1),
                                        edge,
                                        false
                                )
                        );
                    } else {
                        boardString.append(
                                surroundWithEdge(
                                        centerString(printPlayersOnProperty(propertyStartIdx - i, maxLen), maxLen),
                                        edge,
                                        true
                                )
                        );
                    }
                }
                boardString.append(System.lineSeparator());
            } else {
                boardString.append(surroundWithEdge(getProperty(propertyStartIdx).displayPropertyName(maxLen), edge, false));
                boardString.append(" ".repeat(size - (maxLen + 1) * 2 - 1));
                boardString.append(surroundWithEdge(getProperty(propertyStartIdx + (row + 5) * 2).displayPropertyName(maxLen + 1), edge, false));
                boardString.append(System.lineSeparator());

                // Add players
                boardString.append(
                        surroundWithEdge(
                                centerString(printPlayersOnProperty(propertyStartIdx, maxLen), maxLen),
                                edge,
                                false
                        )
                );
                boardString.append(" ".repeat(size - (maxLen + 1) * 2 - 1));
                boardString.append(
                        surroundWithEdge(
                                centerString(printPlayersOnProperty(propertyStartIdx + (row + 5) * 2, maxLen), maxLen + 1),
                                edge,
                                false
                        )
                );
                boardString.append(System.lineSeparator());
            }
            row++;
        }
        boardString.append(makeBorder(size, corner, dash));
        return boardString.toString();
    }

    private String makeBorder(int size, char corner, String dash) {
        return corner +
                dash.repeat(size) +
                corner +
                System.lineSeparator();
    }

    private String surroundWithEdge(String str, char edge, boolean skipRight) {
        if (skipRight) {
            return edge + str;
        }
        return edge + str + edge;
    }

    private String printPlayersOnProperty(int propertyIdx, int maxSize) {
        StringBuilder playerString = new StringBuilder();
        for (HashMap.Entry<String, Integer> entry : getPlayerPositions().entrySet()) {
            if (entry.getValue() == propertyIdx) {
                playerString.append(entry.getKey()).append(", ");
            }
        }

        // remove trailing comma
        if (!playerString.isEmpty()) {
            playerString.setLength(playerString.length() - 2);
        }

        if (playerString.length() > maxSize) {
            // Show only first letter of each name (can improve later)
            String[] names = playerString.toString().split(", ");
            for (int i = 0; i < names.length; i++) {
                names[i] = Character.toString(names[i].charAt(0));
            }
            return String.join(", ", names);
        }

        return playerString.toString();
    }

    static String centerString(String str, int maxSize) {
        int spacer = maxSize - str.length();
        int leading = spacer / 2;
        int trailing = spacer - leading;
        return " ".repeat(leading) + str + " ".repeat(trailing);
    }

    public HashMap<String, Integer> getPlayerPositions() {
        return playerPositions;
    }

    public void setPlayerPosition(Player p) {
        this.playerPositions.put(p.getNameNoColor(), p.getLocation());
    }
}