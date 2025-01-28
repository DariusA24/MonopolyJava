package gameset.functionality;

import gameset.cards.Actions;
import gameset.cards.ChanceCard;
import gameutils.Ansi;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class Board {
    private ArrayList<Property> gameBoard;
    private ArrayList<ChanceCard> changeCards;
    // TODO: add field for community cards

    public Board() throws IOException {
        this.gameBoard = loadPropertyFile();
        this.changeCards = loadChangeCardFile();
        // TODO: community cards
        // this.changeCards = loadProperties.LoadPropertyList("src/main/java/models.models/chanceData.json");
    }

    public ArrayList<Property> getGameBoard() {
        return gameBoard;
    }

    public void listBoard() {
        new gameset.screens.Board().printBoard();
    }

    public void passedGo(Player player) {
        player.setMoney(200);
        System.out.println("Player: ");
        player.displayColoredName();
        System.out.println(" Passed Go, Collect: " + Ansi.ANSI_GREEN + " $200 " + Ansi.ANSI_RESET);
    }

    public void updatedBoardLocation(int previousLocation, int newLocation, String playerColoredName) {
        gameset.screens.Board boardScreen = new gameset.screens.Board();
        boardScreen.updatePlayerPositionOnBoard(
                this.gameBoard.get(previousLocation).getName(),
                this.gameBoard.get(newLocation).getName(),
                playerColoredName
        );
    }

    private ArrayList<Property> loadPropertyFile() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/models/propertyData.json")))
        );
        String s = r.lines().reduce("", (prevLines, currLine) -> prevLines + "\n" + currLine);
        r.close();

        ArrayList<Property> list = new ArrayList<>();

        JSONParser parser = new JSONParser();
        // TODO: discuss with Darius, If parse error, then no property data. Can game continue w/o this?
        try {
            JSONArray jsonPropertyList = (JSONArray) parser.parse(s);
            for (Object o : jsonPropertyList) {
                JSONObject property = (JSONObject) o;
                String name = (String) property.get("name");
                String type = (String) property.get("type");
                Long price = (Long) property.get("price");
                Long rent = (Long) property.get("rent");
                String color = (String) property.get("color");
                Property parsedProperty = new Property(name, type, Math.toIntExact(price), Math.toIntExact(rent), color);
                list.add(parsedProperty);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    private ArrayList<ChanceCard> loadChangeCardFile() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/models/chanceData.json")))
        );
        String s = r.lines().reduce("", (prevLines, currLine) -> prevLines + "\n" + currLine);
        r.close();

        ArrayList<ChanceCard> list = new ArrayList<>();

        JSONParser parser = new JSONParser();
        // TODO: discuss with Darius, If parse error, then no change card data. Can game continue w/o this?
        try {
            JSONArray jsonPropertyList = (JSONArray) parser.parse(s);
            for (Object o : jsonPropertyList) {
                JSONObject card = (JSONObject) o;

                Optional<String> cardText = Optional.ofNullable((String) card.get("cardText"));
                Optional<Actions> action = Optional.ofNullable((String) card.get("action"))
                        .map(Actions::valueOf); // Convert string to enum
                Optional<Boolean> isSpecial = Optional.ofNullable((Boolean) card.get("isSpecial"));
                Optional<String> targetLocation = Optional.ofNullable((String) card.get("targetLocation"));
                Optional<Integer> moneyValue = Optional.ofNullable((Long) card.get("moneyValue")).map(Long::intValue);

                list.add(new ChanceCard(
                        cardText,
                        action,
                        targetLocation,
                        moneyValue,
                        isSpecial
                ));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }
}
