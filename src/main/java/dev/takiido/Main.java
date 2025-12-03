package dev.takiido;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;

public class Main {
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String RESET = "\u001B[0m";

    public static void main(String[] args) throws Exception {
        String text = "Test";

        // Setup terminal in raw mode
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .jna(true)
                .build();
        terminal.enterRawMode();
        terminal.puts(InfoCmp.Capability.clear_screen);

        // Print the text the user must type
        terminal.writer().print(text);
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
        terminal.writer().flush();

        NonBlockingReader reader = terminal.reader();

        StringBuilder buffer = new StringBuilder();
        int ch;

        // Main loop
        for (;;) {
            // Check if user has finished typing
            if (buffer.length() >= text.length()) {
                break;
            }

            // Read input
            ch = reader.read();

            // ESC key
            if (ch == 27) {
                break;
            }

            // BACKSPACE key
            if (ch == 127) {
                if (buffer.length() > 0) {
                    buffer.deleteCharAt(buffer.length() - 1);

                    // Move cursor left, erase char, move left again
                    terminal.puts(InfoCmp.Capability.cursor_left);
                    terminal.writer().print(text.charAt(buffer.length()));
                    terminal.puts(InfoCmp.Capability.cursor_left);

                    terminal.writer().flush();
                }
                continue;
            }

            // Regular printable char
            if (ch >= 32 && ch <= 126) {
                char c = (char) ch;
                buffer.append(c);

                // Check if the input is correct
                if (checkInput(c, text.charAt(buffer.length() - 1))) {
                    terminal.writer().print(GREEN);
                } else {
                    terminal.writer().print(RED);
                }

                terminal.writer().print(c);
                terminal.writer().flush();
            }
        }

        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.writer().flush();
        terminal.close();
    }

    private static boolean checkInput(int ch, char expected) {
        return Character.toLowerCase(expected) == Character.toLowerCase((char) ch);
    }
}
