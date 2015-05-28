package connect4;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.*;

public class GUIInput implements MouseListener, MouseMotionListener, KeyListener
{
    private int column;
    private Piece current;
    private boolean drop;
    private Player player;
    private GUIPanel panel;

    public void setCurrent(Piece current, Player player)
    {
        this.current = current;
        this.player = player;
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        column = (int) b.getX() / 100 + 1;
        try {
      	    // These coordinates are screen coordinates
      	    int xCoord = (int) b.getX()+1;
      	    int yCoord = (int) b.getY();

      	    // Move the cursor one pixel to the right and profit
      	    // They'll never know
      	    Robot robot = new Robot();
      	    robot.mouseMove(xCoord, yCoord);
      	} catch (AWTException e) {
      	}
        //column = 4;
    }

    GUIInput(GUI gui, GUIPanel panel, Piece current)
    {
        this.current = current;
        this.panel = panel;
        gui.addMouseListener(this);
        gui.addMouseMotionListener(this);
        gui.addKeyListener(this);
        drop = false;
        moveCurrent(0);
    }

    /**
     * Get the x value of a column
     * @param event The mouse event
     * @return The mouse value snapped to a column
     */
    private int getX(MouseEvent event)
    {
        int x = (event.getX()) / 100;
        return x * 100 + 19;
    }

    public void mouseClicked(MouseEvent event){}
    public void mouseEntered(MouseEvent event){}
    public void mouseExited(MouseEvent event){}
    public void mouseReleased(MouseEvent event){}
    public void mouseDragged(MouseEvent event){ }

    //Mouse Event Handling

    /**
     * The mouse was clicked
     * @param event Where the mouse was click
     */
    public void mousePressed(MouseEvent event){
        if (player instanceof Human)
        {
            column = getX(event) / 100 + 1;
            drop = true;
        }
    }

    /**
     * Those mouse was moved
     * @param event Where the mouse was moved
     */
    public void mouseMoved(MouseEvent event) {
        if (player instanceof Human)
        {
            int x = getX(event);
            column = (getX(event) - 19) / 100 + 1;
            current.move(x, GUI.START_Y);
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}

    @Override
    /**
     * A key is pressed
     */
    public void keyReleased(KeyEvent e)
    {
        if (player instanceof Human)
        {
            int number = e.getKeyChar() - '0';
            if (number >= 1 && number <= Connect4.NUM_COLUMNS)
            {
                column = number;
                drop = true;
            }
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_LEFT:
                    moveCurrent(-1);
                    break;
                case KeyEvent.VK_RIGHT:
                    moveCurrent(+1);
                    break;
                case KeyEvent.VK_SPACE:
                case KeyEvent.VK_ENTER:
                    drop = true;
                    break;
            }
        }
    }

    public void moveCurrent(int add)
    {
        column += add;
        if (column < 1) column = 1;
        if (column > Connect4.NUM_COLUMNS) column = Connect4.NUM_COLUMNS;
        int x = (column - 1)  * 100 + 19;
        current.move(x, GUI.START_Y);
    }

    /**
     * Get the column to drop (to replace scanner.nextInt())
     * @return The column value
     */
    public int nextInt()
    {
        if (drop)
        {
            drop = false;
            return column;
        }
        return 0;
    }

    public void reset()
    {
        drop = false;
    }

    public int nextMove()
    {
        return panel.nextMove();
    }
}
