package gameutils;


import gameset.builder.PropertyBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import gameset.functionality.Property;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LoadProperties {

    public ArrayList<Property> LoadPropertyList(String file) throws FileNotFoundException {
        ArrayList<Property> PropertyList = new ArrayList<Property>();
        JSONParser parser = new JSONParser();
        System.out.println("In this load file");
        try {
            JSONArray jsonPropertyList = (JSONArray) parser.parse(new FileReader(file));
            for(Object o : jsonPropertyList){
                JSONObject property = (JSONObject) o;
                String name = (String) property.get("name");
                String type = (String) property.get("type");
                Long price = property.containsKey("price") ? (Long) property.get("price") : 0;
                String color = (String) property.get("color");
                Long baseRent = (property.containsKey("baseRent") ? (Long) property.get("baseRent") : 0);
                Long rentWith1House = property.containsKey("rentWith1House") ? (Long) property.get("rentWith1House") : 0;
                Long rentWith2House = property.containsKey("rentWith2Houses") ? (Long) property.get("rentWith2Houses") : 0;
                Long rentWith3House = property.containsKey("rentWith3Houses") ? (Long) property.get("rentWith3Houses") : 0;
                Long rentWith4House = property.containsKey("rentWith4Houses") ? (Long) property.get("rentWith4Houses") : 0;
                Long rentWithHotel = property.containsKey("rentWithHotel") ? (Long) property.get("rentWithHotel") : 0;
                Long mortgage =  (property.containsKey("mortgage") ? (Long) property.get("mortgage") : 0);

                ArrayList<Integer> buildingList = new ArrayList<>(Arrays.asList(Math.toIntExact(rentWith1House), Math.toIntExact(rentWith2House),
                        Math.toIntExact(rentWith3House), Math.toIntExact(rentWith4House), Math.toIntExact(rentWithHotel)));

                PropertyBuilder builder = new PropertyBuilder(name, type);
                builder.color(color);
                builder.rent(Math.toIntExact(baseRent));
                builder.price(Math.toIntExact(price));
                builder.color(color);
                builder.buildingPrices(buildingList);
                builder.mortgage(Math.toIntExact(mortgage));
                PropertyList.add(builder.build());

            }
        } catch (IOException | ParseException e){
            e.printStackTrace();
        }


        return PropertyList;
    }
}
