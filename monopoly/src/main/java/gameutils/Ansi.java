package gameutils;

public class Ansi {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String[] ColorList = {ANSI_CYAN, ANSI_PURPLE, ANSI_RED, ANSI_BLUE, ANSI_WHITE};

    public static String propertyToAnsiColor(String color) {
        return switch (color) {
            case "red" -> ANSI_RED;
            case "blue" -> ANSI_BLUE;
            case "yellow" -> ANSI_YELLOW;
            case "green" -> ANSI_GREEN;
            case "pink" -> ANSI_PURPLE;
            case "cyan" -> ANSI_CYAN;
            default -> ANSI_WHITE;
        };
    }

    public static String stripAnsi(String s) {
        return s.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    public static String wrapAnsi(String s, String color) {
        String coloredStr = "";
        coloredStr += propertyToAnsiColor(color);
        coloredStr += s;
        coloredStr += ANSI_RESET;
        return coloredStr;
    }
}
