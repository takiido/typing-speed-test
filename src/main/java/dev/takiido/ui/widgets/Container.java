package dev.takiido.ui.widgets;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all widgets that can contain child widgets.
 */
public abstract class Container extends Widget {
    /**
     * Vertical child alignment enum.
     */
    public enum VerticalAlignment {
        TOP,
        CENTER,
        BOTTOM;
    }

    /**
     * Horizontal child alignment enum.
     */
    public enum HorizontalAlignment {
        LEFT,
        CENTER,
        RIGHT;
    }

    protected List<Widget> children; // List of child widgets
    protected VerticalAlignment verticalAlignment = VerticalAlignment.CENTER;
    protected HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;

    /**
     * Constructor for Container widget.
     * 
     * @param layer The layer of the container
     */
    public Container(int layer) {
        super(layer);
        this.children = new ArrayList<>();
    }

    /**
     * Draws the children of the container.
     * 
     * @param terminal The terminal to draw the children on
     */
    protected void drawChildren(org.jline.terminal.Terminal terminal) {
        if (children == null || children.isEmpty())
            return;

        // Calculate total height needed for column layout
        // Assuming column layout for now as per previous Canvas implementation
        // precedent
        int totalHeight = 0;
        for (Widget child : children) {
            totalHeight += child.getHeight();
        }

        // Determine starting Y based on VerticalAlignment
        int currentY = this.y;
        switch (verticalAlignment) {
            case TOP:
                currentY = this.y;
                break;
            case CENTER:
                currentY = this.y + (this.height - totalHeight) / 2;
                break;
            case BOTTOM:
                currentY = (this.y + this.height) - totalHeight;
                break;
        }

        for (Widget child : children) {
            // Determine X based on HorizontalAlignment per child
            int currentX = this.x;
            switch (horizontalAlignment) {
                case LEFT:
                    currentX = this.x;
                    break;
                case CENTER:
                    currentX = this.x + (this.width - child.getWidth()) / 2;
                    break;
                case RIGHT:
                    currentX = (this.x + this.width) - child.getWidth();
                    break;
            }

            // Update child position
            child.setX(currentX);
            child.setY(currentY);

            // Draw
            child.draw(terminal);

            // Advance Y for next child in column
            currentY += child.getHeight();
        }
    }

    /**
     * Adds a child widget to the container.
     * 
     * @param widget The child widget to add
     */
    public void addChild(Widget widget) {
        children.add(widget);
    }

    /**
     * Removes a child widget from the container.
     * 
     * @param widget The child widget to remove
     */
    public void removeChild(Widget widget) {
        children.remove(widget);
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }
}