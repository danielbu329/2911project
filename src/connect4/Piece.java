package connect4;

import java.awt.*;

public class Piece
{
    private Image image;
    private int xpos, ypos;
    private boolean dropping;
    private boolean finished;
    private int yvel;
    private int ystop;
    private int column;
    private int row;
    private char symbol;
    private final Board board;

    /**
     * Gets the column
     * @return the column the piece is in
     */
    public int getColumn()
    {
        finished = false;
        dropping = false;
        return column;
    }

	/**
	 * Constructor for Piece
	 * @param image the piece image
	 * @param y the y position of the piece
	 * @param board the current state of the board
	 * @param symbol red or yellow depending on which player
	 */
    public Piece(Image image, int y, Board board, char symbol)
    {
        this.image = image;
        xpos = 0;
        ypos = y;
        finished = false;
        this.board = board;
        this.symbol = symbol;
    }
    
    /**
     * Draws the image
     * @param g graphics for the image
     */
    public void draw(Graphics g)
    {
        g.drawImage(image, xpos, ypos, null);
        update();
    }
    
    public boolean isFinished()
    {
        return finished;
    }

    private void update()
    {
        if (dropping)
        {
            if (ypos < ystop)
            {
                yvel += 2;
            }
            ypos += yvel;
            if (ypos > ystop)
            {
                ypos = ystop;
                yvel = -yvel / 2;
                if (yvel == 0)
                {
                    board.set(row, column, symbol);
                    finished = true;
                }
            }
        }
    }

    public void move(int x)
    {
        if (!dropping)
        {
            xpos = x;
            ypos = GUI.START_Y;
            finished = false;
        }
    }
    
    /**
     * Drops the piece
     * @param x the X position of the piece
     * @param y the y position
     * @param column the column
     * @param endY the resulting y position
     * @param row the resulting row
     * @param symbol red or yellow
     */
    public void drop(int x, int y, int column, int endY, int row, char symbol)
    {
        if (!dropping)
        {
            dropping = true;
            finished = false;
            xpos = x;
            ypos = y;
            yvel = 0;
            ystop = endY;
            this.column = column;
            this.row = row;
            this.symbol = symbol;
        }
    }

    public void setImage(Image image)
    {
        this.image = image;
    }

    public boolean finishedDropping()
    {
        return !dropping;
    }

    public char getSymbol()
    {
        return symbol;
    }

    public void dropped()
    {
        dropping = false;
    }

    public void reset()
    {
        dropping = false;
        finished = true;
    }
}