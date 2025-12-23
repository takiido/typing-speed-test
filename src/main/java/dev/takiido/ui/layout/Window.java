package dev.takiido.ui.layout;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jline.terminal.Terminal;

import dev.takiido.ui.widgets.Widget;

public class Window {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean hasBorder;
    private Border border;
    private boolean isActive;
    private boolean isFloating;
    private Terminal terminal;

    private List<Widget> widgets;

    public Window() {
        this(0, 0, 0, 0, false, new Border(), false, false);
    }

    public Window(int x, int y, int width, int height, boolean hasBorder, Border border, boolean isActive,
            boolean isFloating) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hasBorder = hasBorder;
        this.border = border;
        this.isActive = isActive;
        this.isFloating = isFloating;
    }

    /**
     * Returns the width of the window
     * 
     * @return The width of the window
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the window
     * 
     * @return The height of the window
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the size of the window
     * 
     * @return The size of the window
     */
    public int[] getSize() {
        return new int[] { width, height };
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void updateSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void draw() {
        if (hasBorder) {
            border.draw(this);
        }

        Map<Integer, List<Widget>> layerMap = widgets.stream()
                .collect(Collectors.groupingBy(Widget::getLayer));

        // Iterate through layers in ascending order
        layerMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    List<Widget> layerWidgets = entry.getValue();
                    int layerCount = layerWidgets.size();

                    if (layerCount == 0)
                        return;

                    // Calculate total height of widgets in this layer (simple stacking)
                    // Assuming we want to center the whole block of widgets vertically
                    // But for now let's just stack them starting from middle or top?
                    // "if two widgets are on the same layer they will be drawn in column"
                    // Let's center the column vertically in the screen.

                    int totalHeight = layerWidgets.stream().mapToInt(Widget::getHeight).sum();
                    // Add some spacing? Let's say 1 line spacing
                    int spacing = 1;
                    totalHeight += (layerCount - 1) * spacing;

                    int startY = (height - totalHeight) / 2;
                    if (startY < 0)
                        startY = 0; // Prevent out of bounds

                    int currentY = startY;

                    for (Widget widget : layerWidgets) {
                        // Center horizontally
                        int startX = (width - widget.getWidth()) / 2;
                        if (startX < 0)
                            startX = 0;

                        widget.setX(startX);
                        widget.setY(currentY);
                        widget.draw(terminal);

                        currentY += widget.getHeight() + spacing;
                    }
                });
    }
}
