package connect4;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Piece
{
    private Image image;
    private int xpos, ypos;
    private int w, h;
    private boolean dropping;
    private boolean finished;
    private int yvel;
    private int ystop;
    private int column;
    private int row;
    private char symbol;
    private Board board;

    public int getColumn()
    {
        finished = false;
        dropping = false;
        return column;
    }

    public Piece(Image image, int x, int y, Board board, char symbol)
    {
        this.image = image;
        xpos = x;
        ypos = y;
        w = image.getWidth(null);
        h = image.getHeight(null);
        finished = false;
        this.board = board;
        this.symbol = symbol;
    }

    public void draw(Graphics g, int x, int y)
    {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        g.drawImage(image, x, y, null);
        xpos = x;
        ypos = y;
    }

    public void draw(Graphics g)
    {
        draw(g, xpos, ypos);
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

    public void move(int x, int y)
    {
        if (!dropping)
        {
            xpos = x;
            ypos = y;
            finished = false;
        }
    }

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

    public boolean isDropping()
    {
        return dropping;
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
