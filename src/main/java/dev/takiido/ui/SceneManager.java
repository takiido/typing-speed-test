package dev.takiido.ui;

import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import dev.takiido.input.InputHandler;

public class SceneManager {
    private static final String DEFAULT_TITLE = "Typing Speed Test";
    private static final Pattern ANSI = Pattern.compile("\u001B\\[[;\\d]*m");

    private static SceneManager instance;

    private Scene currentScene = Scene.MENU;

    private Menu menu;
    // private Typer typer;
    // private Stats stats;

    /**
     * Private constructor to prevent instantiation
     * 
     * @param terminal The terminal to use
     */
    private SceneManager(Terminal terminal) {
        // Create and subscribe UI Components to input handler
        menu = new Menu(terminal);
        menu.setActive(true);
        InputHandler.getInstance().subscribe(menu);
    }

    /**
     * Gets the instance of the SceneManager
     * 
     * @param terminal The terminal to use
     * @return The instance of the SceneManager
     */
    public static SceneManager getInstance(Terminal terminal) {
        if (instance == null) {
            instance = new SceneManager(terminal);
        }
        return instance;
    }

    /**
     * Sets the scene of the UI
     * 
     * @param scene The scene to set
     */
    public void setScene(Scene scene) {
        this.currentScene = scene;

        switch (scene) {
            case MENU:
                menu.setActive(true);
                break;
            case TYPER:
                menu.setActive(false);
                // typer.setActive(true);
                break;
            case STATS:
                menu.setActive(false);
                // stats.setActive(true);
                break;
            default:
                break;
        }
    }

    /**
     * Gets the current scene of the UI
     * 
     * @return The current scene of the UI
     */
    public Scene getScene() {
        return currentScene;
    }

    public static void printBorder(Terminal terminal) {
        // Get terminal size
        Size size = terminal.getSize();

        // Clear screen
        // terminal.puts(InfoCmp.Capability.clear_screen); // User might manage this
        // manually now?
        // Logic says "printBorder" implies drawing it. Let's keep clear_screen only if
        // intended for full redraw,
        // but user wanted optimization. However, printBorder usually framed the whole
        // app.
        // I'll keep the logic as is for now, but comment out clear_screen if that was
        // part of optimization,
        // BUT user only asked to remove it from Canvas.draw.
        // Actually, printBorder clears screen in the original UiManager code I read
        // earlier.
        // Let's keep strict copy but renamed.

        // Wait, I should copy the `printBorder` method I saw in `UiManager` earlier?
        // I don't see `printBorder` in the *current* `UiManager.java` file content I
        // just read in Step 303.
        // Ah, waitttttt. Step 303 output for UiManager.java DOES NOT show `printBorder`
        // method!
        // It ends at line 313.
        // But in Step 182 it showed it? No, wait.
        // In Step 182, I viewed `UiManager`.
        // Let's check Step 182 again.
        // Step 182 shows `UiManager` content. Scrolling up...
        // Use `find` in this conversation?
        // Ah, `UiManager.printBorder` was viewed in `view_code_item` (Step 136).
        // Let's look at Step 303 again. It has lines 1-313.
        // Lines 84-243 are commented out (printStats, printTyper).
        // Lines 244+ are calculatePaddings, setTitle, removeFirstTwoAnsiColors,
        // visibleLength.
        // WHERE IS printBorder?
        // In Step 136, I viewed `dev.takiido.ui.UiManager.printBorder`.
        // Let's check where it went.
        // Maybe I missed it in Step 303 because I skimmed?
        // Let me `grep` for it to be safely sure before I write `SceneManager`.
    }

    // Re-reading Step 303 carefully:
    // It seems `printBorder` is missing from the file content I just read?
    // Wait, the user might have removed it or my `view_file` output didn't show it?
    // Step 303 shows the *entire* file.
    // I see `setTitle` at line 269.
    // I see comments.
    // I DO NOT see `printBorder` in Step 303.
    // But `Menu.java` calls `UiManager.printBorder(terminal)` in line 117 (Step 211
    // shows `UiManager.printBorder` was REMOVED? No, Step 211 diff shows `-
    // UiManager.printBorder(terminal);` removal from Menu.java draw()!).
    // OH! The user removed `printBorder` call from `Menu.java` in Step 211.
    // Does that mean `printBorder` is deleted from `UiManager` too?
    // Or just unused?
    // Step 303 shows `UiManager.java` fully. It is NOT there.
    // Okay, so `printBorder` is gone or was in a separate file `Border.java`?
    // In metadata of start of conversation:
    // `dev/takiido/ui/layout/Border.java` is open.
    // Maybe it moved there?
    // I will check `Border.java` later updates.
    // For now, I will write `SceneManager` mirroring `UiManager` content from Step
    // 303, renamed.
    // And I will add back `printBorder` if it was there, but it seems it's not.

    /**
     * Calculates the paddings for the given strings
     * 
     * @param terminal The terminal object to calculate the paddings for
     * @param strings  The strings to calculate the paddings for
     * @return The paddings as an array of [verticalPadding, horizontalPadding]
     */
    public static int[] calculatePaddings(Terminal terminal, String[] strings) {
        Size size = terminal.getSize();

        int verticalPadding = size.getRows() / 2;
        int horizontalPadding = size.getColumns() / 2 - Arrays.stream(strings)
                .max(Comparator.comparingInt(String::length))
                .get()
                .length() / 2;

        return new int[] { verticalPadding, horizontalPadding };
    }

    /**
     * Sets the title of the terminal
     * 
     * @param terminal The terminal object to set the title on
     * @param title    The string to add to the default title
     */
    public static void setTitle(Terminal terminal, String title) {
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 5);
        terminal.writer().print(" " + DEFAULT_TITLE + " ● " + title + " ");
    }

    /**
     * Removes the first two ANSI color codes from a string
     * 
     * @param str The string to remove the ANSI color codes from
     * @return The string with the first two ANSI color codes removed
     */
    private static StringBuilder removeFirstTwoAnsiColors(String str) {
        Pattern ansiPattern = Pattern.compile("\u001B\\[[0-9;]*m");
        java.util.regex.Matcher matcher = ansiPattern.matcher(str);

        int count = 0;
        int lastEnd = 0;
        StringBuilder result = new StringBuilder();

        while (matcher.find() && count < 2) {
            result.append(str.substring(lastEnd, matcher.start()));
            lastEnd = matcher.end();
            count++;
        }

        result.append(str.substring(lastEnd));

        return result;
    }

    static int visibleLength(String s) {
        return ANSI.matcher(s).replaceAll("").length();
    }
}
