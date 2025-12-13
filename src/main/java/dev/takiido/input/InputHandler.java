package dev.takiido.input;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

/**
 * Handles input from the terminal
 */
public class InputHandler {
    // Singleton instance
    private static InputHandler instance;

    private NonBlockingReader reader; // NonBlockingReader instance
    private final List<InputListenerInterface> listeners = new CopyOnWriteArrayList<>(); // List of listeners

    /**
     * Private constructor to enforce singleton pattern
     * 
     * @param terminal The terminal to use for input
     * @throws IOException
     */
    private InputHandler(Terminal terminal) throws IOException {
        reader = terminal.reader();
    }

    /**
     * Returns the singleton instance of InputHandler or creates a new one if it
     * doesn't exist
     * 
     * @param terminal The terminal to use for input
     * @return The singleton instance of InputHandler
     * @throws IOException
     */
    public static InputHandler getInstance(Terminal terminal) throws IOException {
        if (instance == null) {
            instance = new InputHandler(terminal);
        }
        return instance;
    }

    /**
     * Returns the singleton instance of InputHandler
     * 
     * @return The singleton instance of InputHandler
     */
    public static InputHandler getInstance() {
        return instance;
    }

    /**
     * Subscribes a listener to the input handler
     * 
     * @param listener The listener to subscribe
     */
    public void subscribe(InputListenerInterface listener) {
        listeners.add(listener);
    }

    /**
     * Unsubscribes a listener from the input handler
     * 
     * @param listener The listener to unsubscribe
     */
    public void unsubscribe(InputListenerInterface listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all subscribed listeners of an input action
     * 
     * @param action The input action to notify listeners of
     */
    public void notifyListeners(InputActions action) {
        for (InputListenerInterface listener : listeners) {
            if (listener.isActive()) {
                listener.onInput(action);
            }
        }
    }

    /**
     * Notifies all subscribed listeners of an input action and character
     * 
     * @param action The input action to notify listeners of
     * @param c      The character associated with the input action
     */
    public void notifyListeners(InputActions action, char c) {
        for (InputListenerInterface listener : listeners) {
            if (listener.isActive()) {
                listener.onInput(action, c);
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
            if (result == InputActions.Character) {
                notifyListeners(result, (char) first);
            } else {
                notifyListeners(result);
            }
        }
    }
}
