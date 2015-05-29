package connect4;

import java.awt.*;
import java.awt.geom.Rectangle2D;

class Message
{
    private final Font font;
    private int x, y;
    private String message;
    private boolean display;
    private boolean fade;
    private Color color;
    private int fadeTime;
    
    /**
     * Constructor for Message
     * @param font
     */
    Message(Font font)
    {
        this.font = font;
        display = false;
    }
    
    /**
     * Sets up the message
     * @param message the message
     * @param color the colour the message applies to
     */
    public void set(String message, Color color)
    {
        this.message = message;
        this.x = GUI.MESSAGE_X;
        this.y = GUI.MESSAGE_Y;
        this.color = color;
        this.display = message.length() != 0;
    }

    public void hide()
    {
        display = false;
    }
    
    /**
     * Checks if the message has been displayed yet
     * @return true or false
     */
    public boolean displayed()
    {
        return display;
    }
    
    /**
     * Fades the message
     */
    public void fade()
    {
        fade = true;
        fadeTime = 100;
    }

	/**
	 * Updates the message graphics
	 * @param graphics graphics for the message
	 */
    public void update(Graphics graphics)
    {
        if (display)
        {
            if (fade)
            {
                if (fadeTime > 0)
                {
                    fadeTime--;
                    if (fadeTime == 0) display = false;
                } else
                {
                    y--;
                    int alpha = color.getAlpha() - 2;
                    if (alpha < 3) display = false;
                    int r = color.getRed();
                    int g = color.getGreen();
                    int b = color.getBlue();
                    color = new Color(r, g, b, alpha);
                }
            }
            drawText(graphics, message, x, y, color);
        }
    }

    /**
     * Draw text centred in x around the x value, with outline
     *
     * @param g     The graphics context
     * @param text  The text to display
     * @param x     The screen x position
     * @param y     The screen y position
     * @param color The color to display the text in
     */
    void drawText(Graphics g, String text, int x, int y, Color color)
    {
        char chars[] = text.toCharArray();
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        Rectangle2D size = metrics.getStringBounds(text, g);
        x -= size.getWidth() / 2;
        Color black = new Color(0, 0, 0, color.getAlpha());
        g.setColor(black);
        for (int yoff = -2; yoff <= 2; yoff += 2)
        {
            for (int xoff = -2; xoff <= 2; xoff += 2)
            {
                if (yoff == 0 && xoff == 0) continue;
                g.drawChars(chars, 0, chars.length, x + xoff, y + yoff);
            }
        }
        g.setColor(color);
        g.drawChars(chars, 0, chars.length, x, y);
    }

}