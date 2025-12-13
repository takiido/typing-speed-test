package dev.takiido.input;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

public class InputHandler {
    // Singleton instance
    private static InputHandler instance;

    // NonBlockingReader instance
    private NonBlockingReader reader;

    private InputHandler(Terminal terminal) throws IOException {
        reader = terminal.reader();
    }

    public static InputHandler getInstance(Terminal terminal) throws IOException {
        if (instance == null) {
            instance = new InputHandler(terminal);
        }
        return instance;
    }

    /**
     * Handles input from the terminal
     * 
     * @throws IOException
     */
    public void handleInput() throws IOException {
        int first = reader.read();
        InputActions result = null;

        switch (first) {
            case 27: // ESC - potential arrow key sequence
                int second = reader.read();
                if (second == 91) { // '['
                    int third = reader.read();
                    switch (third) {
                        case 65:
                            result = InputActions.Up;
                            break;
                        case 66:
                            result = InputActions.Down;
                            break;
                        case 67:
                            result = InputActions.Right;
                            break;
                        case 68:
                            result = InputActions.Left;
                            break;
                    }
                }
                break;
            case 13:
                result = InputActions.Enter;
                break;
            case 127:
                result = InputActions.Backspace;
                break;
            case 32:
                result = InputActions.Space;
                break;
            default:
                if (first >= 32 && first < 127) { // Printable ASCII
                    result = InputActions.Character;
                }
                break;
        }

        if (result != null) {
            if (result == InputActions.Character) {
                System.out.println((char) first);
            }
            printInputResult(result);
        }
    }

    private void printInputResult(InputActions result) {
        System.out.println(result);
    }

}
