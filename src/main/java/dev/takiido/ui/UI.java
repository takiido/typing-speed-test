package dev.takiido.ui;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;

public class UI {
    static final String RESET = "\u001B[0m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String RED_BACKGROUND = "\u001B[41m";
    static final String YELLOW_BACKGROUND = "\u001B[43m";
    static final String GREEN_BACKGROUND = "\u001B[42m";

    static final Pattern ANSI = Pattern.compile("\u001B\\[[;\\d]*m");

    private static final String DEFAULT_TITLE = "Typing Speed Test";

    public static int menu(Terminal terminal, NonBlockingReader reader) throws IOException {
        int selected = 1;
        printMenu(terminal, selected);

        int ch;
        while ((ch = reader.read()) != -1) {
            if (ch == 10 || ch == 13) {
                return selected;
            }
            if (ch == 27) { // ESC
                int next1 = reader.read(); // should be '['
                int next2 = reader.read(); // actual arrow code

                if (next1 == '[') {
                    switch (next2) {
                        case 'A': // UP
                            selected--;
                            break;
                        case 'B': // DOWN
                            selected++;
                            break;
                        case 'C': // RIGHT
                            // if you want right navigation
                            break;
                        case 'D': // LEFT
                            // if you want left navigation
                            break;
                    }
                }
            } else if (ch == '1') {
                selected = 1;
            } else if (ch == '2') {
                selected = 2;
            } else if (ch == '3') {
                selected = 3;
            }

            // wrap-around
            if (selected < 1)
                selected = 3;
            if (selected > 3)
                selected = 1;

            printMenu(terminal, selected);
        }
        return -1;
    }

    /**
     * Draws a border around the terminal
     * 
     * @param terminal The terminal to draw the border on
     * @param title    The title to display on the top of the border
     */
    public static void printBorder(Terminal terminal) {
        // Get terminal size
        Size size = terminal.getSize();

        // Clear screen
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.writer().flush();

        // Top border
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
        terminal.writer().print(YELLOW + "╔" + "═".repeat(size.getColumns() - 2) + "╗");

        // Vertical borders
        for (int i = 1; i < size.getRows() - 1; i++) {
            terminal.puts(InfoCmp.Capability.cursor_address, i, 0);
            terminal.writer().print(YELLOW + "║");

            terminal.puts(InfoCmp.Capability.cursor_address, i, size.getColumns() - 1);
            terminal.writer().print(YELLOW + "║");
        }

        // Bottom border
        terminal.puts(InfoCmp.Capability.cursor_address, size.getRows() - 1, 0);
        terminal.writer().print(YELLOW + "╚" + "═".repeat(size.getColumns() - 2) + "╝");

        // Write title to terminal
        setTitle(terminal, DEFAULT_TITLE);

        // Reset color and cursor position
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
        terminal.writer().print(RESET);

        terminal.writer().flush();
    }

    /**
     * Draws a main menu with
     * 
     * @param terminal The terminal to draw the menu on
     * @param selected The currently selected option
     */
    private static void printMenu(Terminal terminal, int selected) {
        String[] menu = {
                "1. Start training",
                "2. Start test",
                "3. Exit"
        };

        printBorder(terminal);

        setTitle(terminal, "Menu");

        // Calculate padding
        int[] paddings = calculatePaddings(terminal, menu);

        // Draw menu
        terminal.puts(InfoCmp.Capability.cursor_address, paddings[0] - 2, paddings[1]);
        if (selected == 1) {
            terminal.writer().print(RESET + YELLOW_BACKGROUND + menu[0] + RESET);
        } else {
            terminal.writer().print(RESET + menu[0]);
        }

        terminal.puts(InfoCmp.Capability.cursor_address, paddings[0], paddings[1]);
        if (selected == 2) {
            terminal.writer().print(RESET + YELLOW_BACKGROUND + menu[1] + RESET);
        } else {
            terminal.writer().print(RESET + menu[1]);
        }

        terminal.puts(InfoCmp.Capability.cursor_address, paddings[0] + 2, paddings[1]);
        if (selected == 3) {
            terminal.writer().print(RESET + YELLOW_BACKGROUND + menu[2] + RESET);
        } else {
            terminal.writer().print(RESET + menu[2]);
        }

        // Reset color and cursor position
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
        terminal.writer().print(RESET);

        terminal.writer().flush();
    }

    /**
     * Draws the stats screen
     * 
     * @param terminal     The terminal to draw the stats on
     * @param reader       The reader to read input from
     * @param typedCount   The number of characters typed
     * @param correctCount The number of characters typed correctly
     * @param elapsedTime  The elapsed time in milliseconds
     * @throws IOException If an I/O error occurs
     */
    public static void printStats(Terminal terminal, NonBlockingReader reader, int typedCount, int correctCount,
            long elapsedTime) throws IOException {

        // Calculate accuracy
        double accuracy = typedCount == 0 ? 0 : (double) correctCount / typedCount * 100;

        String[] stats = {
                "Accuracy: " + String.format("%.2f", accuracy) + " %",
                "Time: " + String.format("%.2f", elapsedTime / 1000.0) + " seconds"
        };

        // Calculate padding
        int[] paddings = calculatePaddings(terminal, stats);

        // Clear screen
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.writer().flush();

        setTitle(terminal, "Stats");

        // Print stats
        terminal.puts(InfoCmp.Capability.cursor_address, paddings[0] - 1, paddings[1]);
        terminal.writer().print(stats[0]);
        terminal.puts(InfoCmp.Capability.cursor_address, paddings[0], paddings[1]);
        terminal.writer().print(stats[1]);
        terminal.puts(InfoCmp.Capability.cursor_address, paddings[0] + 1, paddings[1]);
        terminal.writer().print("Press any key to continue...\n");
        terminal.writer().flush();

        // Wait for a single key press to exit
        reader.read();
    }

    /**
     * Prints the typing screen
     * 
     * @param terminal The terminal to print on
     * @param reader   The reader to read input from
     * @param text     The text to type
     * @param timeLeft The time left in milliseconds
     * @param training Whether the user is training or testing
     * @throws IOException If an I/O error occurs
     */
    public static void printTyper(Terminal terminal, NonBlockingReader reader, String text, int timeLeft,
            boolean training) throws IOException {
        UI.printBorder(terminal);

        int verticalPadding = terminal.getSize().getRows() / 2;

        StringBuilder typedHistory = new StringBuilder();
        StringBuilder coloredHistory = new StringBuilder();
        StringBuilder printedString = new StringBuilder();

        int correctCount = 0;
        int typedCount = 0;

        // Calculate initial display width
        int displayWidth = terminal.getSize().getColumns() / 2 - 2;
        String stringToDisplay = text.substring(0, Math.min(displayWidth, text.length()));
        int initialStringLength = stringToDisplay.length();

        // Start time for WPM calculation
        long startTime = System.currentTimeMillis();

        terminal.puts(InfoCmp.Capability.cursor_address, verticalPadding, terminal.getSize().getColumns() / 2);
        terminal.writer().print(RESET + stringToDisplay);
        terminal.writer().flush();

        int textIndex = initialStringLength;

        // Continue until all text has been typed
        while (printedString.length() < text.length()) {
            terminal.puts(InfoCmp.Capability.cursor_address, verticalPadding, terminal.getSize().getColumns() / 2);
            terminal.writer().flush();

            char ch = (char) reader.read();
            char expected = stringToDisplay.charAt(0);

            typedCount++;
            typedHistory.append(ch);

            // Add colored character to history
            if (ch == expected) {
                correctCount++;
                if (expected == ' ') {
                    coloredHistory.append(GREEN_BACKGROUND).append(expected).append(RESET);
                } else {
                    coloredHistory.append(GREEN).append(expected).append(RESET);
                }
            } else {
                if (expected == ' ') {
                    coloredHistory.append(RED_BACKGROUND).append(expected).append(RESET);
                } else {
                    coloredHistory.append(RED).append(expected).append(RESET);
                }
            }

            // Trim colored history if it exceeds half screen width
            if (typedHistory.length() > terminal.getSize().getColumns() / 2 - 1) {
                coloredHistory = removeFirstTwoAnsiColors(coloredHistory.toString());
                coloredHistory.delete(0, 1);
            }

            printedString.append(expected);
            stringToDisplay = stringToDisplay.substring(1);

            // Add next character to display if available
            if (textIndex < text.length()) {
                stringToDisplay += text.charAt(textIndex);
                textIndex++;
            }

            // Calculate display position
            int step = visibleLength(coloredHistory.toString());
            int startCol = terminal.getSize().getColumns() / 2;

            // Display colored history + remaining text
            terminal.puts(InfoCmp.Capability.cursor_address, verticalPadding, startCol - step);
            terminal.writer().print(coloredHistory.toString() + RESET + stringToDisplay);
            terminal.writer().flush();

            if (!training && timeLeft > 0) {
                long elapsed = System.currentTimeMillis() - startTime;
                if (elapsed >= timeLeft) {
                    break;
                }
            }
        }

        // Calculate elapsed time
        long elapsedTime = System.currentTimeMillis() - startTime;

        // After completion, show stats
        printStats(terminal, reader, typedCount, correctCount, elapsedTime);
    }

    /**
     * Calculates the paddings for the given strings
     * 
     * @param terminal The terminal object to calculate the paddings for
     * @param strings  The strings to calculate the paddings for
     * @return The paddings as an array of [verticalPadding, horizontalPadding]
     */
    private static int[] calculatePaddings(Terminal terminal, String[] strings) {
        Size size = terminal.getSize();

        int verticalPadding = size.getRows() / 2;
        int horizontalPadding = size.getColumns() / 2 - Arrays.stream(strings)
                .max(Comparator.comparingInt(String::length))
                .get()
                .length() / 2;

        return new int[] { verticalPadding, horizontalPadding };
    }

    /**
     * Sets the title of the terminal
     * 
     * @param terminal The terminal object to set the title on
     * @param title    The string to add to the default title
     */
    private static void setTitle(Terminal terminal, String title) {
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 5);
        terminal.writer().print(DEFAULT_TITLE + " ● " + title);
    }

    /**
     * Removes the first two ANSI color codes from a string
     * 
     * @param str The string to remove the ANSI color codes from
     * @return The string with the first two ANSI color codes removed
     */
    private static StringBuilder removeFirstTwoAnsiColors(String str) {
        // ANSI escape sequence pattern: \u001B followed by [ and then characters until
        // 'm'
        Pattern ansiPattern = Pattern.compile("\u001B\\[[0-9;]*m");
        java.util.regex.Matcher matcher = ansiPattern.matcher(str);

        int count = 0;
        int lastEnd = 0;
        StringBuilder result = new StringBuilder();

        while (matcher.find() && count < 2) {
            // Append text before this ANSI code
            result.append(str.substring(lastEnd, matcher.start()));
            lastEnd = matcher.end();
            count++;
        }

        // Append the rest of the string after the first two ANSI codes
        result.append(str.substring(lastEnd));

        return result;
    }

    /**
     * Returns the visible length of a string by removing ANSI escape sequences
     * 
     * @param s The string to get the visible length of
     * @return The visible length of the string
     */
    static int visibleLength(String s) {
        return ANSI.matcher(s).replaceAll("").length();
    }

}