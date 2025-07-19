package gameset.screens;

import java.io.IOException;

public class Startup extends AbstractScreen {
    private final String logo;
    private boolean showBlink = true;
    private long lastBlinkTime = System.currentTimeMillis();

    public Startup() {
        logo = """
                                                      _      \s
                  /\\/\\   ___  _ __   ___  _ __   ___ | |_   _\s
                 /    \\ / _ \\| '_ \\ / _ \\| '_ \\ / _ \\| | | | |
                / /\\/\\ \\ (_) | | | | (_) | |_) | (_) | | |_| |
                \\/    \\/\\___/|_| |_|\\___/| .__/ \\___/|_|\\__, |
                                         |_|            |___/\s
                """;
    }

    @Override
    protected void draw() {
        // Update blink toggle based on time
        long now = System.currentTimeMillis();
        if (now - lastBlinkTime >= 1000) {
            showBlink = !showBlink;
            lastBlinkTime = now;
        }

        // Print logo and maybe blinking message
        System.out.print(logo);
        if (showBlink) {
            System.out.println("Press any key to continue");
            System.out.println("Press q to exit");
        } else {
            // Print empty lines to keep spacing consistent
            System.out.println();
            System.out.println();
        }
    }

    @Override
    protected void handleInput() {
        Thread thread = new Thread(() -> {
            while (true) {
                int ch = 0;
                try {
                    ch = System.in.read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (ch =='q') {
                    System.exit(0);
                } else {
                    setFinished(true);
                }
            }
        });
        thread.start();
    }

    @Override
    public String toString() {
        return "ERROR: To display the Startup screen, call draw()";
    }
}
