package gameutils;

public class Console {
    /// NOTE: For this to work in IntelliJ, you must export the TERM environment variable
    /// to "xterm-256color" (Run -> Edit Configurations -> Environment Variables -> TERM)
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("window")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            // Fallback to some new lines instead
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
