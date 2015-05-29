package connect4;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GUIPanel extends JPanel
{
    private GUI gui;
    private Popup popup;
    private Font font;
    private Piece current;
    private Color background;
    private BufferedImage screen;
    private Board board;
    private Image yellow;
    private Image red;
    private Image gameboard;
    private volatile boolean drawing;
    private volatile boolean suspended;
    private boolean tutorial;
    private Instructions instructions;
    
    /**
     * Contructor for GUIPanel
     * @param main the GUI
     * @param red red coin image
     * @param yellow yellow coin image
     * @param current the current piece (red or yellow)
     * @param gameboard the gameboard image
     * @param font the font used in the GUI
     * @param board The board
     * @param background The background
     */
    GUIPanel(GUI main, Image red, Image yellow, Piece current, Image gameboard, Font font, Board board, Color background)
    {
        this.red = red;
        this.yellow = yellow;
        this.font = font;
        this.gameboard = gameboard;
        this.current = current;
        this.board = board;
        this.background = background;
        gui = main;
        screen = new BufferedImage(701, 701, BufferedImage.TYPE_INT_ARGB);
        suspended = false;
        popup = new Popup(new Font("Serif", Font.BOLD, 40));
        update(screen.getGraphics());
    }

    /**
     * Update the graphics object
     * @param g The graphics context
     */
    public void update(Graphics g)
    {
        g.setColor(background);
        g.fillRect(0, 0, 1000, 1000);
        current.draw(g);
        board.draw(g, gui.getRows(), gui.getColumns(), red, yellow);
        g.drawImage(gameboard, 0, 100, null);
        gui.message.update(g);
        if (tutorial)
        {
            if (!popup.paint(g))
            {
                if (instructions == null || instructions.complete())
                {
                    instructions = Instructions.next(popup);
                }
                if (instructions == null) tutorial = false;
            }
        }
    }

    /**
     * Display the graphics on the screen
     * @param g The graphics context to draw to
     */
    public void paint(Graphics g)
    {
        if (!suspended)
        {
            drawing = true;
            update(screen.getGraphics());
            g.drawImage(screen, 0, 0, null);
            drawing = false;
        }
    }

    public void setCurrent(Piece current)
    {
        this.current = current;
    }

    public boolean isDrawing()
    {
        return drawing;
    }

    public void suspend(boolean suspend)
    {
        suspended = suspend;
    }

    public void reset(boolean tutorial)
    {
        this.tutorial = tutorial;
        Instructions.start();
    }

    public int nextMove()
    {
        if (instructions == null) return -1;
        return instructions.getMove() - 1;
    }
}
