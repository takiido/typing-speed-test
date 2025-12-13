package dev.takiido.input;

public interface InputListenerInterface {
    boolean isActive();

    void setActive(boolean active);

    void onInput(InputActions action);
}
