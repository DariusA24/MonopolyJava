package gameset.screens;

abstract class AbstractScreen {
    private final int fps = 30;
    private volatile boolean finished = false;

    protected void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

    public final void render() {
        disableLineBuffering();
        // Ensure restore on exit
        Runtime.getRuntime().addShutdownHook(new Thread(AbstractScreen::restoreTerminal));

        long frameDelay = 1000 / fps;
        long lastTime = System.currentTimeMillis();
        handleInput();
        while (!finished) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= frameDelay) {
                lastTime = currentTime;
                clearAll();
                draw();
            }
        }
        clearAll();
        // In case it's still running after finished = true
        restoreTerminal();
    }

    /**
     * Subclasses implement this to provide what is actually rendered.
     */
    protected abstract void draw();

    protected abstract void handleInput();

    private void clearAll() {
        System.out.print("\033[H"); // Move cursor to top
        System.out.print("\033[2J"); // Clear screen
        System.out.flush();
    };

    /// Note this only works on Linux and MacOS
    private static void disableLineBuffering() {
        try {
            new ProcessBuilder("sh", "-c", "stty -icanon -echo < /dev/tty").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.err.println("Could not disable line buffering: " + e.getMessage());
        }
    }

    /// Note this only works on Linux and MacOS
    private static void restoreTerminal() {
        try {
            new ProcessBuilder("sh", "-c", "stty sane < /dev/tty").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.err.println("Could not restore terminal settings: " + e.getMessage());
        }
    }
}
