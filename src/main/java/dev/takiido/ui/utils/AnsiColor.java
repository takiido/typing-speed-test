package dev.takiido.ui.utils;

/**
 * Utility class for ANSI color codes
 */
public class AnsiColor {
    /**
     * Returns an ANSI color code for Black foreground color.
     * 
     * @return The ANSI color code
     */
    public static final String Black = "\u001B[30m";

    /**
     * Returns an ANSI color code for Red foreground color.
     * 
     * @return The ANSI color code
     */
    public static final String Red = "\u001B[31m";

    /**
     * Returns an ANSI color code for Green foreground color.
     * 
     * @return The ANSI color code
     */
    public static final String Green = "\u001B[32m";

    /**
     * Returns an ANSI color code for Yellow foreground color.
     * 
     * @return The ANSI color code
     */
    public static final String Yellow = "\u001B[33m";

    /**
     * Returns an ANSI color code for Blue foreground color.
     * 
     * @return The ANSI color code
     */
    public static final String Blue = "\u001B[34m";

    /**
     * Returns an ANSI color code for Purple foreground color.
     * 
     * @return The ANSI color code
     */
    public static final String Purple = "\u001B[35m";

    /**
     * Returns an ANSI color code for Cyan foreground color.
     * 
     * @return The ANSI color code
     */
    public static final String Cyan = "\u001B[36m";

    /**
     * Returns an ANSI color code for White foreground color.
     * 
     * @return The ANSI color code
     */
    public static final String White = "\u001B[37m";

    /**
     * Returns an ANSI color code for Black background color.
     * 
     * @return The ANSI color code
     */
    public static final String BlackBg = "\u001B[40m";

    /**
     * Returns an ANSI color code for Red background color.
     * 
     * @return The ANSI color code
     */
    public static final String RedBg = "\u001B[41m";

    /**
     * Returns an ANSI color code for Green background color.
     * 
     * @return The ANSI color code
     */
    public static final String GreenBg = "\u001B[42m";

    /**
     * Returns an ANSI color code for Yellow background color.
     * 
     * @return The ANSI color code
     */
    public static final String YellowBg = "\u001B[43m";

    /**
     * Returns an ANSI color code for Blue background color.
     * 
     * @return The ANSI color code
     */
    public static final String BlueBg = "\u001B[44m";
    /**
     * Returns an ANSI color code for Purple background color.
     * 
     * @return The ANSI color code
     */
    public static final String PurpleBg = "\u001B[45m";

    /**
     * Returns an ANSI color code for Cyan background color.
     * 
     * @return The ANSI color code
     */
    public static final String CyanBg = "\u001B[46m";

    /**
     * Returns an ANSI color code for White background color.
     * 
     * @return The ANSI color code
     */
    public static final String WhiteBg = "\u001B[47m";

    /**
     * Returns an ANSI color code for resetting the color.
     * 
     * @return The ANSI color code
     */
    public static final String RESET = "\u001b[0m";

    /**
     * Returns an ANSI color code for a foreground color specified by a hex
     * value.
     * 
     * @param hex The hex value of the color (e.g. "7f11e0")
     * @throws IllegalArgumentException if the hex color code is invalid
     * @return The ANSI color code
     */
    public static String fgHex(String hex) {
        hex = formatHex(hex);

        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return "\u001b[38;2;" + r + ";" + g + ";" + b + "m";
    }

    /**
     * Returns an ANSI color code for a background color specified by a hex
     * value.
     * 
     * @param hex The hex value of the color (e.g. "7f11e0")
     * @throws IllegalArgumentException if the hex color code is invalid
     * @return The ANSI color code
     */
    public static String bgHex(String hex) {
        hex = formatHex(hex);

        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return "\u001b[48;2;" + r + ";" + g + ";" + b + "m";
    }

    /**
     * Formats a hex color code to a valid hex color code.
     * 
     * @param hex The hex color code to format
     * @throws IllegalArgumentException if the hex color code is invalid
     * @return The formatted hex color code
     */
    private static String formatHex(String hex) {
        if (hex.length() > 7) {
            throw new IllegalArgumentException("Invalid hex color code: " + hex);
        }

        if (hex.length() == 7) {
            hex = hex.substring(1);
        }

        return hex;
    }
}
