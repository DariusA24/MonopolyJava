package gameutils;

public class Ansi {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String[] ColorList = {ANSI_CYAN, ANSI_PURPLE, ANSI_RED, ANSI_BLUE, ANSI_WHITE};

    public String propertyToAnsiColor(String color){
        if(color.equals("red")){
            return ANSI_RED;
        }
        if(color.equals("blue")){
            return ANSI_BLUE;
        }
        if(color.equals("yellow")){
            return ANSI_YELLOW;
        }
        if(color.equals("green")){
            return ANSI_GREEN;
        }
        if(color.equals("pink")){
            return ANSI_PURPLE;
        }
        if(color.equals("cyan")){
            return ANSI_CYAN;
        }
       else return ANSI_WHITE;
    }


}
