package dev.takiido;

import java.io.IOException;

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

    static final String RESET = "\u001B[0m";

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

    private static boolean checkInput(int ch, char expected) {
        return expected == (char) ch;
    }

    private static void printStats(Terminal terminal, NonBlockingReader reader, int typed, int correct)
            throws IOException {
        // Clear screen
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.writer().flush();

        // Calculate accuracy
        double accuracy = typed == 0 ? 0 : (double) correct / typed * 100;

        // Print stats
        terminal.writer().printf("Accuracy: %.2f%%\n", accuracy);
        terminal.writer().print("Press any key to continue...\n");
        terminal.writer().flush();

        // Wait for a single key press to exit
        reader.read();
    }

    private static void start(Terminal terminal, NonBlockingReader reader, String text, int timeLeft,
            boolean training) {
        StringBuilder buffer = new StringBuilder();
        int ch;

        int typed = 0;
        int correct = 0;

        // Main loop
        for (;;) {
            // Check if user has finished typing
            if (buffer.length() >= text.length()) {
                break;
            }

            // Read input
            ch = reader.read();

            // ESC key
            if (ch == 27) {
                break;
            }

            // BACKSPACE key
            if (ch == 127) {
                if (buffer.length() > 0) {
                    buffer.deleteCharAt(buffer.length() - 1);

                    // Move cursor left, erase char, move left again
                    terminal.puts(InfoCmp.Capability.cursor_left);
                    terminal.writer().print(text.charAt(buffer.length()));
                    terminal.puts(InfoCmp.Capability.cursor_left);

                    terminal.writer().flush();
                }
                continue;
            }

            // Regular printable char
            if (ch >= 32 && ch <= 126) {
                char c = (char) ch;
                buffer.append(c);
                typed++;

                // Check if the input is correct
                if (checkInput(c, text.charAt(buffer.length() - 1))) {
                    correct++;
                    terminal.writer().print(GREEN);
                } else {
                    terminal.writer().print(RED);
                }

                terminal.writer().print(c);
                terminal.writer().print(RESET);
                terminal.writer().flush();
            }
        }

        printStats(terminal, reader, typed, correct);
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
        terminal.puts(InfoCmp.Capability.clear_screen);
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
}
