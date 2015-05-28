package connect4;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class Popup
{
    private int x, y, w, h;
    private int alpha;
    private static Font font;
    private boolean line;
    private int sx, sy, ex, ey;
    private String[] text;
    private Color color;
    private int timer;

    public Popup(Font font)
    {
        Popup.font = font;
    }

    public void update(String text)
    {
        update(350, 400, text);
    }

    public void update(int x, int y, String text)
    {
        update(x, y, text, Color.YELLOW);
    }

    private void update(int x, int y, String text, Color color)
    {
        alpha = 127;
        this.text = text.split("\n");
        this.color = color;
        h = this.text.length * 48 + 10;
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        int widest = 0;
        for (String str : this.text)
        {
            int width = (int)(font.getStringBounds(str, frc).getWidth());
            if (width > widest) widest = width;
        }
        w = widest + 20;
        this.x = x - w / 2;
        this.y = y - h / 2;
        timer = h * 2;
    }

    public int getX(String coords)
    {
        return (coords.charAt(1)-'1') * 100 + 51;
    }

    public int getY(String coords)
    {
        return (coords.charAt(0)-'A') * 100 + 150;
    }

    private void background(Graphics g)
    {
        Color background = new Color(0, 0, 0, alpha);
        g.setColor(background);
        g.fillRect(x, y, w, h);
    }

    /**
     * Display the graphics on the screen
     * @param g The graphics context to draw to
     */
    public boolean paint(Graphics g)
    {
        if (alpha > 1)
        {
            if (timer > 0)
            {
                timer--;
            }
            else
            {
                alpha--;
            }
            background(g);
            Color fontColor = new Color(color.getRGB() + (alpha << 25), true);
            g.setColor(fontColor);
            g.setFont(font);
            int ypos = 40 + y;
            for (String str : text)
            {
                g.drawString(str, 10 + x, ypos);
                ypos += 48;
            }
            if (line)
            {
                Graphics2D g2 = (Graphics2D) g;
                g.setColor(new Color(alpha << 25, true));
                g2.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(sx, sy, ex, ey);   //thick
                g.setColor(fontColor);
                g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(sx, sy, ex, ey);   //thick
            }
            return true;
        }
        line = false;
        return false;
    }

    public void line(String cmd)
    {
        line = true;
        String end = cmd.split("-")[1];
        sx = getX(cmd);
        sy = getY(cmd);
        ex = getX(end);
        ey = getY(end);
    }
}
