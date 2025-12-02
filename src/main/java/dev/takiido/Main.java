package dev.takiido;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;

import dev.takiido.filereader.FileReader;

public class Main {
    public static void main(String[] args) throws Exception {

        clearTerminal();

        // Load text from file
        FileReader fileReader = new FileReader();
        String text = fileReader.readFile("ExampleText.txt");

        // Print the text the user must type
        System.out.println(text);
        System.out.println();

        // Setup terminal in raw mode
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .jna(true)
                .build();

        terminal.enterRawMode();
        NonBlockingReader reader = terminal.reader();

        int index = 0; // user typing position
        int length = text.length();

        System.out.println("Start typing (ESC to exit):");

        int ch;

        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);

        while ((ch = reader.read()) != 27) { // ESC exits

            if (index >= length) {
                break;
            }

            char expected = text.charAt(index);
            char typed = (char) ch;
            terminal.flush();

            terminal.puts(InfoCmp.Capability.cursor_address, 0, index + 1);

            if (Character.toLowerCase(expected) == Character.toLowerCase(typed)) {
                System.out.print("\u001B[32m" + typed + "\u001B[0m"); // green good
                index++;
            } else {
                System.out.print("\u001B[31m" + typed + "\u001B[0m"); // red wrong
            }
        }

        clearTerminal();

        terminal.close();
    }

    private static void clearTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
