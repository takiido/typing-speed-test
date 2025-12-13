package dev.takiido.ui.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jline.terminal.Terminal;

import dev.takiido.ui.widgets.Widget;

public class Canvas {
    private boolean hasBorder;
    private int width;
    private int height;
    private Terminal terminal;
    private Border border;

    private List<Widget> widgets;

    /**
     * Constructor, creates a canvas without border
     */
    public Canvas(Terminal terminal) {
        this(terminal, false);
    }

    /**
     * Constructor, creates a canvas with or without a border
     * 
     * @param hasBorder
     */
    public Canvas(Terminal terminal, boolean hasBorder) {
        this(terminal, hasBorder, new Border());
    }

    /**
     * Constructor, creates a canvas with custom border
     * 
     * @param border
     */
    public Canvas(Terminal terminal, boolean hasBorder, Border border) {
        this.width = terminal.getSize().getColumns();
        this.height = terminal.getSize().getRows();
        this.hasBorder = hasBorder;
        this.terminal = terminal;
        this.border = border;
        this.widgets = new ArrayList<>();
    }

    /**
     * Returns the width of the canvas
     * 
     * @return The width of the canvas
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the canvas
     * 
     * @return The height of the canvas
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the size of the canvas
     * 
     * @return The size of the canvas
     */
    public int[] getSize() {
        return new int[] { width, height };
    }

    /**
     * Called when the canvas is resized
     * 
     * @param width  The new width of the canvas
     * @param height The new height of the canvas
     */
    public void onResize(int width, int height) {
        this.width = width;
        this.height = height;

        for (Widget widget : widgets) {
            widget.updateSize(width, height);
        }
    }

    /**
     * Returns the terminal used by the canvas
     * 
     * @return The terminal used by the canvas
     */
    public Terminal getTerminal() {
        return terminal;
    }

    /**
     * Adds a widget to the canvas
     * 
     * @param widget The widget to add
     */
    public void addWidget(Widget widget) {
        widgets.add(widget);
    }

    /**
     * Removes a widget from the canvas
     * 
     * @param widget The widget to remove
     */
    public void removeWidget(Widget widget) {
        widgets.remove(widget);
    }

    /**
     * Draws the canvas
     */
    public void draw() {
        if (hasBorder) {
            border.draw(this);
        }
        // Group widgets by layer
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
