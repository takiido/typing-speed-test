package dev.takiido;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class Main {
    public static void main(String[] args) throws Exception {
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .jna(true)
                .build();

        terminal.enterRawMode(); // Enter raw mode to read user input without pressing Enter

        NonBlockingReader reader = terminal.reader();

        System.out.println("Press keys (ESC to exit):");

        int ch;

        while ((ch = reader.read()) != 27) { // Esc symbol to close
            System.out.println("Key: " + ch + " (" + (char) ch + ")");
        }

        terminal.close();
    }
}