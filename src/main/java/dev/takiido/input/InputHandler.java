package dev.takiido.input;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

public class InputHandler {
    // Singleton instance
    private static InputHandler instance;

    private NonBlockingReader reader; // NonBlockingReader instance
    private final List<InputListenerInterface> listeners = new CopyOnWriteArrayList<>(); // List of listeners

    private InputHandler(Terminal terminal) throws IOException {
        reader = terminal.reader();
    }

    public static InputHandler getInstance(Terminal terminal) throws IOException {
        if (instance == null) {
            instance = new InputHandler(terminal);
        }
        return instance;
    }

    public static InputHandler getInstance() {
        return instance;
    }

    public void subscribe(InputListenerInterface listener) {
        listeners.add(listener);
    }

    public void unsubscribe(InputListenerInterface listener) {
        listeners.remove(listener);
    }

    public void notifyListeners(InputActions action) {
        for (InputListenerInterface listener : listeners) {
            if (listener.isActive()) {
                listener.onInput(action);
            }
        }
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
            notifyListeners(result);
        }
    }

    private void printInputResult(InputActions result) {
        System.out.println(result);
    }

}
