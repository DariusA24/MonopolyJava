package gameset.screens;

import java.util.function.Supplier;

public abstract class AbstractScreen {
    private final int fps = 30;
    private volatile boolean finished = false;
    private Supplier<? extends AbstractScreen> nextScreenSupplier = null;

    /**
     * Marks this screen as finished, allowing the screen manager to proceed to the next screen.
     *
     * @param finished Whether the screen is finished
     */
    protected void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

    /**
     * Renders this screen and returns the next screen to display, or null if the application should exit.
     *
     * @return The next screen to display, or null to exit
     */
    public final AbstractScreen render() {
        try {
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
            return getNextScreen();
        } finally {
            // Ensure the terminal is always restored when the screen is done
            restoreTerminal();
        }
    }

    /**
     * Sets the next screen to be displayed after this screen is finished.
     *
     * @param nextScreenSupplier A supplier that creates the next screen instance
     */
    protected final void setNextScreen(Supplier<? extends AbstractScreen> nextScreenSupplier) {
        this.nextScreenSupplier = nextScreenSupplier;
    }

    /**
     * Gets the next screen to be displayed.
     * @return The next screen instance, or null if no next screen is set
     */
    /**
     * Gets the next screen to be displayed.
     *
     * @return The next screen instance, or null if no next screen is set
     */
    public final AbstractScreen getNextScreen() {
        return nextScreenSupplier != null ? nextScreenSupplier.get() : null;
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
    }

    /// Note this only works on Linux and MacOS
    private static void disableLineBuffering() {
        try {
            new ProcessBuilder("sh", "-c", "stty -icanon -echo < /dev/tty").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.err.println("Could not disable line buffering: " + e.getMessage());
        }
    }

    /// Note this only works on Linux and MacOS
    public static void restoreTerminal() {
        try {
            new ProcessBuilder("sh", "-c", "stty sane < /dev/tty").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.err.println("Could not restore terminal settings: " + e.getMessage());
        }
    }
}
