package dev.takiido.ui;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;

import dev.takiido.input.InputActions;
import dev.takiido.input.InputListenerInterface;

public class Menu implements InputListenerInterface {
    private static final String title = "Menu";
    private static final String[] menuOptions = {
            "1. Start training",
            "2. Start test",
            "3. Exit"
    };

    private boolean isActive = true;
    private int selected = 1;
    private Terminal terminal;

    public Menu(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void onInput(InputActions action) throws IOException {
        System.out.println("Menu: " + action);
        switch (action) {
            case Enter:
                handleMenuSelection();
                break;

            case Up:
                handleMenuNavigation(-1);
                break;

            case Down:
                handleMenuNavigation(1);
                break;

            default:
                break;
        }
    }

    @Override
    public void onInput(InputActions action, char c) {
        System.out.println("Menu: " + action + " " + c);
    }

    @Override
    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void open(Terminal terminal, NonBlockingReader reader) throws IOException {
        printMenu(terminal, selected);

        int ch;
        while ((ch = reader.read()) != -1) {
            if (ch == 10 || ch == 13) {
                break;
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
    }

    /**
     * Draws a main menu with
     * 
     * @param terminal The terminal to draw the menu on
     * @param selected The currently selected option
     */
    private static void printMenu(Terminal terminal, int selected) {
        UiManager.printBorder(terminal);

        UiManager.setTitle(terminal, title);

        // Calculate padding
        int[] paddings = UiManager.calculatePaddings(terminal, menuOptions);

        // Draw menu
        terminal.puts(InfoCmp.Capability.cursor_address, paddings[0] - 2, paddings[1]);
        if (selected == 1) {
            terminal.writer().print(TerminalColors.RESET.getCode() + TerminalColors.YELLOW_BACKGROUND.getCode()
                    + menuOptions[0] + TerminalColors.RESET.getCode());
        } else {
            terminal.writer().print(TerminalColors.RESET.getCode() + menuOptions[0]);
        }

        terminal.puts(InfoCmp.Capability.cursor_address, paddings[0], paddings[1]);
        if (selected == 2) {
            terminal.writer().print(TerminalColors.RESET.getCode() + TerminalColors.YELLOW_BACKGROUND.getCode()
                    + menuOptions[1] + TerminalColors.RESET.getCode());
        } else {
            terminal.writer().print(TerminalColors.RESET.getCode() + menuOptions[1]);
        }

        terminal.puts(InfoCmp.Capability.cursor_address, paddings[0] + 2, paddings[1]);
        if (selected == 3) {
            terminal.writer().print(TerminalColors.RESET.getCode() + TerminalColors.YELLOW_BACKGROUND.getCode()
                    + menuOptions[2] + TerminalColors.RESET.getCode());
        } else {
            terminal.writer().print(TerminalColors.RESET.getCode() + menuOptions[2]);
        }

        // TerminalColors.RESET color and cursor position
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
        terminal.writer().print(TerminalColors.RESET.getCode());

        terminal.writer().flush();
    }

    private void handleMenuSelection() throws IOException {
        switch (selected) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }

    private void handleMenuNavigation(int direction) throws IOException {
        selected += direction;
        if (selected < 1)
            selected = 3;
        if (selected > 3)
            selected = 1;
        printMenu(terminal, selected);
    }
}
