package dev.takiido.ui.widgets;

import dev.takiido.ui.TerminalColors;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

public class Box extends Container {
    public Box(int layer) {
        super(layer);
    }

    @Override
    public void draw(Terminal terminal) {
        if (!isActive)
            return;

        // Optionally clear the box area or draw a background
        // For now, just reset colors and move cursor
        terminal.puts(InfoCmp.Capability.cursor_address, this.y, this.x);
        terminal.writer().print(TerminalColors.RESET.getCode());
        terminal.writer().flush();

        // Delegate to Container to draw children with alignment logic
        drawChildren(terminal);
    }
}
