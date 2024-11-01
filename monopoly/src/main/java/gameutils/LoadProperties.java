package gameutils;


import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import gameset.functionality.Property;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
                Long price = (Long) property.get("price");
                Long rent = (Long) property.get("rent");
                String color = (String) property.get("color");
                Property parsedProperty = new Property(name, type, Math.toIntExact(price), Math.toIntExact(rent), color);
                PropertyList.add(parsedProperty);
            }
        } catch (IOException | ParseException e){
            e.printStackTrace();
        }


        return PropertyList;
    }

}
