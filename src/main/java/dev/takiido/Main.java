package dev.takiido;

import java.io.IOException;
import java.util.regex.Pattern;

import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;

import dev.takiido.filereader.FileReader;

public class Main {
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";

    static final String YELLOW_BACKGROUND = "\u001B[43m";
    static final String RED_BACKGROUND = "\u001B[41m";
    static final String GREEN_BACKGROUND = "\u001B[42m";

    static final String RESET = "\u001B[0m";

    static final Pattern ANSI = Pattern.compile("\u001B\\[[;\\d]*m");

    static int visibleLength(String s) {
        return ANSI.matcher(s).replaceAll("").length();
    }

    public static void main(String[] args) throws IOException {
        String text = FileReader.readFile("ExampleText.txt");

        // Setup terminal in raw mode
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .jna(true)
                .build();
        terminal.enterRawMode();

        // Print the text the user must type
        // terminal.writer().print(text);
        // terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
        // terminal.writer().flush();

        NonBlockingReader reader = terminal.reader();

        int timeLeft = 60000;

        switch (menu(terminal, reader)) {
            case 1:
                start(terminal, reader, text, timeLeft, false);
                break;
            case 2:
                start(terminal, reader, text, timeLeft, true);
                break;
            case 3:
                break;
        }

        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.writer().flush();
        terminal.close();
    }

    private static void printStats(Terminal terminal, NonBlockingReader reader, int typed, int correct,
            long elapsedTime)
            throws IOException {
        // Clear screen
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.writer().flush();

        // Calculate accuracy
        double accuracy = typed == 0 ? 0 : (double) correct / typed * 100;

        // Print stats
        terminal.writer().printf("Accuracy: %.2f%%\n", accuracy);
        terminal.writer().printf("Time: %.2f seconds\n", elapsedTime / 1000.0);
        terminal.writer().print("Press any key to continue...\n");
        terminal.writer().flush();

        // Wait for a single key press to exit
        reader.read();
    }

    private static void start(Terminal terminal, NonBlockingReader reader, String text, int timeLeft,
            boolean training) throws IOException {
        drawBorder(terminal);

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

    private static int menu(Terminal terminal, NonBlockingReader reader) throws IOException {
        int selected = 1;
        drawMenu(terminal, selected);

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

            drawMenu(terminal, selected);
        }
        return -1;
    }

    private static void drawMenu(Terminal terminal, int selected) {
        drawBorder(terminal);

        int verticalPadding = terminal.getSize().getRows() / 2;
        int horizontalPadding = terminal.getSize().getColumns() / 2 - 9;

        terminal.puts(InfoCmp.Capability.cursor_address, 0, 5);
        terminal.writer().print(" Menu ");
        terminal.puts(InfoCmp.Capability.cursor_address, verticalPadding - 2, horizontalPadding);

        if (selected == 1) {
            terminal.writer().print(RESET + YELLOW_BACKGROUND + "1. Start training" + RESET);
        } else {
            terminal.writer().print(RESET + "1. Start training");
        }
        terminal.puts(InfoCmp.Capability.cursor_address, verticalPadding, horizontalPadding);
        if (selected == 2) {
            terminal.writer().print(RESET + YELLOW_BACKGROUND + "2. Start test" + RESET);
        } else {
            terminal.writer().print(RESET + "2. Start test");
        }
        terminal.puts(InfoCmp.Capability.cursor_address, verticalPadding + 2, horizontalPadding);
        if (selected == 3) {
            terminal.writer().print(RESET + YELLOW_BACKGROUND + "3. Exit" + RESET);
        } else {
            terminal.writer().print(RESET + "3. Exit");
        }

        terminal.puts(InfoCmp.Capability.cursor_address, terminal.getSize().getRows() - 1, 0);
        terminal.writer().flush();
    }

    private static void drawBorder(Terminal terminal) {
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

        terminal.writer().flush();
    }

    private static void moveText(Terminal terminal, String text, boolean forward) {
        terminal.puts(InfoCmp.Capability.cursor_address, terminal.getSize().getRows() / 2,
                terminal.getSize().getColumns() / 2);
        terminal.writer().print(text);
        terminal.writer().flush();
    }

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
}
