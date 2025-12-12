package dev.takiido.ui;

public enum TerminalColors {
    RESET("\u001B[0m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    RED_BACKGROUND("\u001B[41m"),
    YELLOW_BACKGROUND("\u001B[43m"),
    GREEN_BACKGROUND("\u001B[42m");

    private final String code;

    TerminalColors(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
