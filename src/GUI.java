//package connect4;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame implements MouseListener, MouseMotionListener, Runnable, KeyListener
{
    private Board board;
    private Player players[];

    public static final int NUM_COLUMNS = 7;
    public static final int NUM_ROWS = 6;
    public static final int NUM_PLAYERS = 2;

    private Thread animation;
    private int last_x;
    private int last_y;
    private Image yellow;
    private Image red;
    private Image gameboard;
    private Color background;
    private int[] columns;
    private int[] rows;
    private BufferedImage screen;
    private BufferedImage backbuffer;
    private int playing = -1;
    private int curr;
    private int column;
    Piece current;

    public static final int START_Y = 30;

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater ( new Runnable()
        {
            @Override
            public void run()
            {
                GUI frame = new GUI();
                frame.setVisible(true);
            }
        });
    }

    public GUI()
    {
        setTitle("Connect 4");
        setSize(701, 701);
        setResizable(false);
        setLocation(150, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        last_x = last_y = 0;
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        yellow = load("yellow.png");
        red = load("red.png");
        current = new Piece(yellow, 0, START_Y);
        gameboard = load("board.png");
        background = getBackground();
        columns = new int[]{19, 119, 219, 319, 419, 519, 619};
        rows = new int[]{118, 218, 318, 418, 518, 618};
        board = new Board(NUM_COLUMNS, NUM_ROWS);
        screen = new BufferedImage(701, 701, BufferedImage.TYPE_INT_ARGB);
        update(screen.getGraphics());
        backbuffer = new BufferedImage(701, 701, BufferedImage.TYPE_INT_ARGB);
        update(backbuffer.getGraphics());
        players = new Player[NUM_PLAYERS];
        players[0] = new Human("Human", 'O');
        //players[0] = new Computer(1, 'O');
        players[1] = new Computer(1, 'X');
        startgame();
        animation = new Thread(this);
        animation.start();
    }

    private void startgame()
    {
        playing = -1;
        curr = 0;
        column = 0;
        board.reset();
    }

    BufferedImage load(String filename)
    {
        try
        {
            BufferedImage image = ImageIO.read(new File("src/" + filename));
            return image;
        }
        catch (IOException err)
        {
            System.out.println("Exception:" + err);
        }
        return null;
    }

    private int getX(MouseEvent event)
    {
        int x = (event.getX() + 40) / 100;
        return x * 100 + 19;
    }

    public void mouseClicked(MouseEvent event){}
    public void mouseEntered(MouseEvent event){}
    public void mouseExited(MouseEvent event){}
    public void mouseReleased(MouseEvent event){}

    //Mouse Event Handling
    public void mousePressed(MouseEvent event){
        column = getX(event) / 100 + 1;
    }

    public void mouseDragged(MouseEvent event){ }

    public void mouseMoved(MouseEvent event) {
        int x = getX(event);
        current.move(x, START_Y);
    }

    public void update(Graphics g) {
        g.setColor(background);
        g.fillRect(0, 0, 1000, 1000);
        current.draw(g, backbuffer);
        board.draw(g, rows, columns, red, yellow);
        g.drawImage(gameboard, 0, 100, null);
        /*
        for (int y : rows)
        {
            for (int x : columns)
            {
                g.drawImage(red, x, y, null);
            }
        }
       */
    }

    public void paint(Graphics g)
    {
        update(screen.getGraphics());
        g.drawImage(screen, 0, 0, null);
    }

    @Override
    public void run()
    {
        while ( Thread.currentThread() == animation )
        {
            repaint ();
            try
            {
                if (playing < 0)
                {
                    playing = play();
                }
                else
                {
                	players[curr].getName();
                	JOptionPane.showMessageDialog(new JFrame(), players[1-curr].getName()+" is the Winner!", "Results", JOptionPane.INFORMATION_MESSAGE);
                	System.out.println("Winner");
                	System.exit(0);
                }
                Thread.sleep ( 20 );
            }
            catch ( InterruptedException e )
            {
                //System.out.println ( "Exception: " + e.getMessage() );
            }
        }
    }

    /**
     * Play the game
     */
    private int play()
    {
        Player player = players[curr];
        player = players[curr];
        board.save();   //The AI will probably modify the state of the board
        //if (player instanceof Human) System.out.print(player.getName() + " which ");
        int move = player.getMove(board, players[curr].getSymbol(), this);
        //Ensure the board is in the same state as before we made the move
        board.restore();
        int index = board.canDrop(move);
        if (index >= 0)
        {
            current.move(columns[move], START_Y);
            current.drop(move, rows[index]);
        }
        //current.drop(618);
        if (current.isFinished())
        {
            if (board.drop(current.getColumn(), player.getSymbol()))
            {
                board.save();
                System.out.println("Move:" + move);
                curr = 1 - curr;        //Alternate player
                current.setImage(curr == 1 ? red : yellow);
                char winner = board.check();
                if (winner == ' ') return 0;
                if (winner == players[0].getSymbol()) return 1;
                if (winner == players[1].getSymbol()) return 2;
            }
        }
        return -1;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyPressed(KeyEvent e)
    {

    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        column = e.getKeyChar() - '0';
    }

    public int nextInt()
    {
        int previous = column;
        column = 0;
        return previous;
    }
}
