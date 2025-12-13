package dev.takiido.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jline.terminal.Terminal;

import dev.takiido.input.InputActions;
import dev.takiido.input.InputListenerInterface;
import dev.takiido.ui.layout.Canvas;
import dev.takiido.ui.widgets.Button;

public class Menu implements InputListenerInterface {
    private static final String title = "Menu";

    private boolean isActive = true;
    private int selected = 0; // 0-indexed for list access
    private Terminal terminal;
    private Canvas canvas;
    private List<Button> options;

    public Menu(Terminal terminal) {
        this.terminal = terminal;
        this.options = new ArrayList<>();
        init();
    }

    private void init() {
        // Initialize Canvas
        // Size will be updated on draw/resize, but initial size is needed?
        // Let's use current terminal size
        canvas = new Canvas(terminal, true);

        // Create Title (Layer 0) - Not selectable
        // For title, we might want a label widget, but Button works for now (just don't
        // select it)
        // Actually, user plan said "Title: Menu (Layer 0)"
        Button titleButton = new Button(title, 0);
        canvas.addWidget(titleButton);

        // Create Options (Layer 1)
        options.add(new Button("Start training", 1));
        options.add(new Button("Start test", 1));
        options.add(new Button("Exit", 1));

        for (Button btn : options) {
            canvas.addWidget(btn);
        }

        updateSelection();
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void onInput(InputActions action) throws IOException {
        if (!isActive)
            return;

        // Resize canvas if needed (simple check)
        if (canvas.getWidth() != terminal.getWidth() || canvas.getHeight() != terminal.getHeight()) {
            canvas.onResize(terminal.getWidth(), terminal.getHeight());
        }

        switch (action) {
            case Enter:
                handleMenuSelection();
                break;

            case Up:
                selected--;
                if (selected < 0)
                    selected = options.size() - 1;
                updateSelection();
                draw();
                break;

            case Down:
                selected++;
                if (selected >= options.size())
                    selected = 0;
                updateSelection();
                draw();
                break;

            default:
                break;
        }
    }

    @Override
    public void onInput(InputActions action, char c) {
        // Handle direct number input if needed, or ignore
    }

    @Override
    public void setActive(boolean active) {
        this.isActive = active;
        if (active) {
            try {
                draw();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSelection() {
        for (int i = 0; i < options.size(); i++) {
            options.get(i).setSelected(i == selected);
        }
    }

    private void draw() throws IOException {
        SceneManager.setTitle(terminal, title);
        canvas.onResize(terminal.getWidth(), terminal.getHeight());
        canvas.draw();
    }

    private void handleMenuSelection() throws IOException {
        // options indices: 0=Training, 1=Test, 2=Exit
        switch (selected) {
            case 0:
                System.out.println("Start training selected");
                break;
            case 1:
                System.out.println("Start test selected");
                break;
            case 2:
                System.out.println("Exit selected");
                System.exit(0);
                break;
            default:
                break;
        }
    }
}
