package dev.takiido.input;

/**
 * Interface for input listeners
 */
public interface InputListenerInterface {
    /**
     * Checks if the listener is active
     * 
     * @return true if the listener is active, false otherwise
     */
    boolean isActive();

    /**
     * Sets the active state of the listener
     * 
     * @param active true to set the listener as active, false otherwise
     */
    void setActive(boolean active);

    /**
     * Handles input events
     * 
     * @param action The input action
     */
    void onInput(InputActions action);

    /**
     * Handles input events with a character
     * 
     * @param action The input action
     * @param c      The character
     */
    void onInput(InputActions action, char c);
}
