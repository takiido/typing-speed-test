package dev.takiido.ui.layout;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import dev.takiido.ui.utils.AnsiColor;

public class Border {
    private static final String TOP_LEFT = "╔";
    private static final String TOP_RIGHT = "╗";
    private static final String BOTTOM_LEFT = "╚";
    private static final String BOTTOM_RIGHT = "╝";
    private static final String TOP = "═";
    private static final String BOTTOM = "═";
    private static final String LEFT = "║";
    private static final String RIGHT = "║";
    private static final String DEFAULT_COLOR = AnsiColor.Yellow;

    private String color;
    private String topLeft;
    private String topRight;
    private String bottomLeft;
    private String bottomRight;
    private String top;
    private String bottom;
    private String left;
    private String right;

    /**
     * Creates a border with default color and characters
     */
    public Border() {
        this(DEFAULT_COLOR);
    }

    /**
     * Creates a border with the specified color
     * 
     * @param color The color to use for the border
     */
    public Border(String color) {
        this.color = color;
        this.topLeft = TOP_LEFT;
        this.topRight = TOP_RIGHT;
        this.bottomLeft = BOTTOM_LEFT;
        this.bottomRight = BOTTOM_RIGHT;
        this.top = TOP;
        this.bottom = BOTTOM;
        this.left = LEFT;
        this.right = RIGHT;
    }

    /**
     * Creates a border with the specified color and characters
     * 
     * @param color       The color to use for the border
     * @param topLeft     The top left corner of the border
     * @param topRight    The top right corner of the border
     * @param bottomLeft  The bottom left corner of the border
     * @param bottomRight The bottom right corner of the border
     * @param top         The top border
     * @param bottom      The bottom border
     * @param left        The left border
     * @param right       The right border
     */
    public Border(
            String color,
            String topLeft,
            String topRight,
            String bottomLeft,
            String bottomRight,
            String top,
            String bottom,
            String left,
            String right) {
        this.color = color;
        if (topLeft.length() > 1 ||
                topRight.length() > 1 ||
                bottomLeft.length() > 1 ||
                bottomRight.length() > 1 ||
                top.length() > 1 ||
                bottom.length() > 1 ||
                left.length() > 1 ||
                right.length() > 1) {
            this.topLeft = TOP_LEFT;
            this.topRight = TOP_RIGHT;
            this.bottomLeft = BOTTOM_LEFT;
            this.bottomRight = BOTTOM_RIGHT;
            this.top = TOP;
            this.bottom = BOTTOM;
            this.left = LEFT;
            this.right = RIGHT;
        } else {
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
            this.top = top;
            this.bottom = bottom;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Draws a border around the terminal with custom color characters
     * 
     * @param canvas The canvas to draw the border on
     */
    public void draw(Window window) {
        // Get canvas size
        int width = window.getWidth();
        int height = window.getHeight();

        Terminal terminal = window.getTerminal();

        // Top border
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
        terminal.writer()
                .print(color + topLeft + top.repeat(width - 2) + topRight + AnsiColor.RESET);

        // Vertical borders
        for (int i = 1; i < height - 1; i++) {
            terminal.puts(InfoCmp.Capability.cursor_address, i, 0);
            terminal.writer().print(color + left + AnsiColor.RESET);

            terminal.puts(InfoCmp.Capability.cursor_address, i, width - 1);
            terminal.writer().print(color + right + AnsiColor.RESET);
        }

        // Bottom border
        terminal.puts(InfoCmp.Capability.cursor_address, height - 1, 0);
        terminal.writer()
                .print(color + bottomLeft + bottom.repeat(width - 2) + bottomRight + AnsiColor.RESET);

        // Reset color and cursor position
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
        terminal.writer().flush();
    }
}
