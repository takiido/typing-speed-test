package dev.takiido.ui;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;

public class Menu {
    public static int open(Terminal terminal, NonBlockingReader reader) throws IOException {
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

        UI.printBorder(terminal);

        UI.setTitle(terminal, "Menu");

        // Calculate padding
        int[] paddings = UI.calculatePaddings(terminal, menu);

        // Draw menu
        terminal.puts(InfoCmp.Capability.cursor_address, paddings[0] - 2, paddings[1]);
        if (selected == 1) {
            terminal.writer().print(TerminalColors.RESET.getCode() + TerminalColors.YELLOW_BACKGROUND.getCode()
                    + menu[0] + TerminalColors.RESET.getCode());
        } else {
            terminal.writer().print(TerminalColors.RESET.getCode() + menu[0]);
        }

        terminal.puts(InfoCmp.Capability.cursor_address, paddings[0], paddings[1]);
        if (selected == 2) {
            terminal.writer().print(TerminalColors.RESET.getCode() + TerminalColors.YELLOW_BACKGROUND.getCode()
                    + menu[1] + TerminalColors.RESET.getCode());
        } else {
            terminal.writer().print(TerminalColors.RESET.getCode() + menu[1]);
        }

        terminal.puts(InfoCmp.Capability.cursor_address, paddings[0] + 2, paddings[1]);
        if (selected == 3) {
            terminal.writer().print(TerminalColors.RESET.getCode() + TerminalColors.YELLOW_BACKGROUND.getCode()
                    + menu[2] + TerminalColors.RESET.getCode());
        } else {
            terminal.writer().print(TerminalColors.RESET.getCode() + menu[2]);
        }

        // TerminalColors.RESET color and cursor position
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
        terminal.writer().print(TerminalColors.RESET.getCode());

        terminal.writer().flush();
    }
}
