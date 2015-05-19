//package connect4;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Piece
{
    private Image image;
    private int xpos, ypos;
    private int w, h;
    private int pixels[];
    private int lastX, lastY;
    private boolean dropping;
    private boolean finished;
    private int yvel;
    private int ystop;

    public int getColumn()
    {
        finished = false;
        dropping = false;
        return column;
    }

    private int column;

    public Piece(Image image, int x, int y)
    {
        this.image = image;
        xpos = x;
        ypos = y;
        w = image.getWidth(null);
        h = image.getHeight(null);
        pixels = new int[w * h];
        lastX = lastY = 0;
        finished = false;
    }

    public void draw(Graphics g, BufferedImage background, int x, int y)
    {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        //int pixels = background.
        //BufferedImage segment = background.getSubimage(xpos, ypos, w, h);
        //g.drawImage(segment, xpos, ypos, null);
        g.drawImage(image, x, y, null);
        xpos = x;
        ypos = y;
    }

    public void draw(Graphics g, BufferedImage backgrond)
    {
        draw(g, backgrond, xpos, ypos);
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
        }
    }

    public void drop(int column, int endY)
    {
        if (!dropping)
        {
            dropping = true;
            yvel = 0;
            ystop = endY;
            this.column = column;
        }
    }

    public void setImage(Image image)
    {
        this.image = image;
    }
}
