package connect4;

import javax.swing.*;
import java.awt.*;

public class PieceIcon implements Icon
{
    private Image image;

    PieceIcon(Image image)
    {
        this.image = image;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        g.drawImage(image, x, y, null);
    }

    @Override
    public int getIconWidth()
    {
        return image.getWidth(null);
    }

    @Override
    public int getIconHeight()
    {
        return image.getHeight(null);
    }
}
