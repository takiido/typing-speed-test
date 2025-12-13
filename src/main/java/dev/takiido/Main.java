package dev.takiido;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import dev.takiido.filereader.FileReader;
import dev.takiido.input.InputHandler;
import dev.takiido.ui.UiManager;
import dev.takiido.ui.UiState;

public class Main {
    public static void main(String[] args) throws IOException {
        String text = FileReader.readFile("ExampleText.txt");

        // Setup terminal in raw mode
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .jna(true)
                .build();
        terminal.enterRawMode();

        InputHandler inputHandler = InputHandler.getInstance(terminal);
        UiManager uiManager = UiManager.getInstance();
        uiManager.setState(UiState.MENU);

        while (true) {
            inputHandler.handleInput();
        }

        // terminal.puts(InfoCmp.Capability.clear_screen);
        // terminal.writer().flush();
        // terminal.close();
    }
}
