package dev.takiido;

import java.io.IOException;
import java.util.regex.Pattern;

import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;

import dev.takiido.filereader.FileReader;
import dev.takiido.ui.UI;

public class Main {
    public static void main(String[] args) throws IOException {
        String text = FileReader.readFile("ExampleText.txt");

        // Setup terminal in raw mode
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .jna(true)
                .build();
        terminal.enterRawMode();

        // Print the text the user must type
        // terminal.writer().print(text);
        // terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);
        // terminal.writer().flush();

        NonBlockingReader reader = terminal.reader();

        int timeLeft = 60000;

        switch (UI.menu(terminal, reader)) {
            case 1:
                UI.printTyper(terminal, reader, text, timeLeft, false);
                break;
            case 2:
                UI.printTyper(terminal, reader, text, timeLeft, true);
                break;
            case 3:
                break;
        }

        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.writer().flush();
        terminal.close();
    }

    private static void printStats(Terminal terminal, NonBlockingReader reader, int typed, int correct,
            long elapsedTime)
            throws IOException {
        // Clear screen
        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.writer().flush();

        // Calculate accuracy
        double accuracy = typed == 0 ? 0 : (double) correct / typed * 100;

        // Print stats
        terminal.writer().printf("Accuracy: %.2f%%\n", accuracy);
        terminal.writer().printf("Time: %.2f seconds\n", elapsedTime / 1000.0);
        terminal.writer().print("Press any key to continue...\n");
        terminal.writer().flush();

        // Wait for a single key press to exit
        reader.read();

    }
}
