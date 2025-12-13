package dev.takiido.ui.widgets;

/**
 * Base class for all widgets.
 */
public abstract class Widget {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int layer;
    protected int minWidth;
    protected int minHeight;
    protected boolean isActive = true;

    /**
     * Constructor for Widget.
     * 
     * @param layer The layer of the widget
     */
    public Widget(int layer) {
        this.layer = layer;
    }

    /**
     * Draws the widget.
     * 
     * @param terminal The terminal to draw the widget on
     */
    public abstract void draw(org.jline.terminal.Terminal terminal);

    /**
     * Gets the x position of the widget.
     * 
     * @return The x position of the widget
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x position of the widget.
     * 
     * @param x The x position of the widget
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y position of the widget.
     * 
     * @return The y position of the widget
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y position of the widget.
     * 
     * @param y The y position of the widget
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the width of the widget.
     * 
     * @return The width of the widget
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the widget.
     * 
     * @param width The width of the widget
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the height of the widget.
     * 
     * @return The height of the widget
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the widget.
     * 
     * @param height The height of the widget
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the layer of the widget.
     * 
     * @return The layer of the widget
     */
    public int getLayer() {
        return layer;
    }

    /**
     * Checks if the widget is active.
     * 
     * @return True if the widget is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active state of the widget.
     * 
     * @param active The active state of the widget
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Updates the size of the widget.
     * 
     * @param width  The new width of the widget
     * @param height The new height of the widget
     */
    public void updateSize(int width, int height) {
        // Do nothing by default
    }
}