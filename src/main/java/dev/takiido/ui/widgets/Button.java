package dev.takiido.ui.widgets;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import dev.takiido.ui.TerminalColors;

public class Button extends Widget {
    private String label;
    private boolean isSelected;

    public Button(String label, int layer) {
        super(layer); // Not a container
        this.label = label;
        this.height = 1; // Default height
        this.width = label.length() + 2; // Default width with padding
    }

    @Override
    public void draw(Terminal terminal) {
        if (!isActive)
            return;

        terminal.puts(InfoCmp.Capability.cursor_address, y, x);

        if (isSelected) {
            terminal.writer().print(TerminalColors.YELLOW_BACKGROUND.getCode() + TerminalColors.RESET.getCode());
        } else {
            terminal.writer().print(TerminalColors.RESET.getCode());
        }

        // Draw label
        terminal.writer().print(" " + label + " "); // Simple padding

        // Reset
        terminal.writer().print(TerminalColors.RESET.getCode());
        terminal.writer().flush();
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        // Trigger a redraw? Ideally Canvas handles the loop, but for now we just set
        // state.
    }

    public boolean isSelected() {
        return isSelected;
    }
}
