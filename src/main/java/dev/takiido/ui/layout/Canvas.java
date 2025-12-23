package dev.takiido.ui.layout;

import java.util.ArrayList;
import java.util.List;

import org.jline.terminal.Terminal;
import org.jline.terminal.Terminal.Signal;
import org.jline.terminal.Terminal.SignalHandler;
import org.jline.utils.InfoCmp;

public class Canvas {
    private int width;
    private int height;
    private Terminal terminal;

    private List<Window> windows;

    /**
     * Constructor, creates a canvas
     * 
     * @param terminal
     */
    public Canvas(Terminal terminal) {
        this.width = terminal.getSize().getColumns();
        this.height = terminal.getSize().getRows();
        this.terminal = terminal;
        this.windows = new ArrayList<>();

        // Register signal handler for terminal resize
        terminal.handle(Signal.WINCH, new SignalHandler() {
            @Override
            public void handle(Signal signal) {
                int newWidth = terminal.getSize().getColumns();
                int newHeight = terminal.getSize().getRows();
                onResize(newWidth, newHeight);
                draw(true);
            }
        });
    }

    /**
     * Returns the width of the canvas
     * 
     * @return The width of the canvas
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the canvas
     * 
     * @return The height of the canvas
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the size of the canvas
     * 
     * @return The size of the canvas
     */
    public int[] getSize() {
        return new int[] { width, height };
    }

    /**
     * Called when the canvas is resized
     * 
     * @param width  The new width of the canvas
     * @param height The new height of the canvas
     */
    public void onResize(int width, int height) {
        this.width = width;
        this.height = height;

        for (Window window : windows) {
            window.updateSize(width, height);
        }
    }

    /**
     * Returns the terminal used by the canvas
     * 
     * @return The terminal used by the canvas
     */
    public Terminal getTerminal() {
        return terminal;
    }

    /**
     * Adds a widget to the canvas
     * 
     * @param widget The widget to add
     */
    public void addWindow(Window window) {
        windows.add(window);
    }

    /**
     * Removes a widget from the canvas
     * 
     * @param widget The widget to remove
     */
    public void removeWindow(Window window) {
        windows.remove(window);
    }

    /**
     * Draws the canvas
     */
    public void draw() {
        this.draw(true);
    }

    /**
     * Draws the canvas
     * 
     * @param clearScreen Whether to clear the screen before drawing
     */
    private void draw(boolean clearScreen) {
        // Clear screen
        if (clearScreen) {
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.flush();
        }
    }
}
