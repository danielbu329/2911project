package connect4;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TimerIcon implements Icon
{
    public static final int TIMER_HEIGHT = 17;

    private Image timerImages[];

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        if (value < 0) value = 0;
        if (value > 63) value = 63;
        this.value = value;
    }

    private int value;

    public TimerIcon()
    {
        timerImages = new Image[64];
        createTimerImages();
        value = 63;
    }

    private void createTimerImages()
    {
        Color red = new Color(255, 12, 3);
        Color yellow = new Color(255, 250, 16);
        Color green = new Color(0, 250, 32);

        for (int frame = 0; frame < 64; frame++)
        {
            Image image;
            int width = 380 * frame / 64 + 1;
            image = new BufferedImage(width, TIMER_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Color fill;
            if (frame > 50)
            {
                fill = green;
            }
            else if (frame > 30)
            {
                fill = merge(yellow, green, frame - 30);
            }
            else if (frame > 10)
            {
                fill = merge(red, yellow, frame - 10);
            }
            else
            {
                fill = red;
            }
            Graphics g = image.getGraphics();
            g.setColor(fill);
            //g.fill3DRect(0, 0, width, TIMER_HEIGHT, false);
            g.fillRect(0, 0, width, TIMER_HEIGHT);
            g.dispose();
            timerImages[frame] = image;
        }
    }

    private Color merge(Color start, Color end, int step)
    {
        int sr = start.getRed();
        int sg = start.getGreen();
        int sb = start.getBlue();
        int er = end.getRed();
        int eg = end.getGreen();
        int eb = end.getBlue();
        int r = (er - sr) * step / 20 + sr;
        int g = (eg - sg) * step / 20 + sg;
        int b = (eb - sb) * step / 20 + sb;

        return new Color(r, g, b);
    }


    @Override
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        g.drawImage(timerImages[value], x, y, null);
    }

    @Override
    public int getIconWidth()
    {
        return timerImages[63].getWidth(null);
    }

    @Override
    public int getIconHeight()
    {
        return TIMER_HEIGHT;
    }
}
