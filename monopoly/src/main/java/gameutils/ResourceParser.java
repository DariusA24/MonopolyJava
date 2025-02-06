package gameutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class ResourceParser {
    private final String lines;

    /**
     * Constructor for the ResourceParser class.
     * @param resourcePath The path to the resource file. For example "/models/propertyData.json".
     * @throws IOException If the resource file cannot be read.
     */
    public ResourceParser(String resourcePath) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(resourcePath))));
        lines = r.lines().reduce("", (prevLines, currLine) -> prevLines + "\n" + currLine);
        r.close();
    }

    /**
     * Returns the contents of the resource file as a string.
     * @return The contents of the resource file as a string.
     */
    public String getLines() {
        return lines;
    }

    /**
     * Returns the contents of the resource file as an ArrayList of objects.
     * @return The contents of the resource file as an ArrayList of objects.
     * @throws IOException If the resource file cannot be read.
     */
    public <T> ArrayList<T> parseJsonToArrayList(Class<T> clazz) throws IOException {
        byte[] jsonData = this.lines.getBytes();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonData, mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
    }
}
