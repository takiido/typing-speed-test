package dev.takiido;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import dev.takiido.input.InputHandler;
import dev.takiido.ui.layout.Border;
import dev.takiido.ui.layout.Canvas;
import dev.takiido.ui.layout.Window;
import dev.takiido.ui.utils.AnsiColor;

public class Main {
    public static void main(String[] args) throws IOException {
        // String text = FileReader.readFile("ExampleText.txt");

        // Setup terminal in raw mode
        Terminal terminal = TerminalBuilder.builder()
                .build();
        terminal.enterRawMode();
        terminal.trackMouse(Terminal.MouseTracking.Normal);

        InputHandler inputHandler = InputHandler.getInstance(terminal);
        Border border = new Border(AnsiColor.fgHex("#d97757"));
        Window window = new Window(0, 0, terminal.getSize().getColumns(), terminal.getSize().getRows(), true, border,
                true, true);
        Canvas canvas = new Canvas(terminal);
        canvas.addWindow(window);

        // Box box = new Box(0);
        // box.addChild(new Button("Button 1", 1));
        // box.addChild(new Button("Button 2", 1));
        // box.addChild(new Button("Button 3", 1));
        // canvas.addWidget(box);

        // SceneManager sceneManager = SceneManager.getInstance(terminal);
        // sceneManager.setScene(Scene.MENU);

        // Initial clear and draw
        terminal.puts(org.jline.utils.InfoCmp.Capability.clear_screen);
        canvas.draw();
        terminal.writer().flush();

        while (true) {
            inputHandler.handleInput();
            canvas.draw();
        }
    }
}
