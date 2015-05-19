
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
    private int key;
    private int playing = -1;
    private int curr;
    private int currX, currY;

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
        gameboard = load("board.png");
        background = getBackground();
        columns = new int[]{19, 119, 219, 319, 419, 519, 619};
        rows = new int[]{118, 218, 318, 418, 518, 618};
        board = new Board(NUM_COLUMNS, NUM_ROWS);
        screen = new BufferedImage(701, 701, BufferedImage.TYPE_INT_ARGB);
        update(screen.getGraphics());
        backbuffer = new BufferedImage(701, 701, BufferedImage.TYPE_INT_ARGB);
        update(backbuffer.getGraphics());
        key = 0;
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
        board.reset();
    }

    //Mouse Event Handling
    public void mousePressed(MouseEvent event){
        int x = event.getX();
        int y = event.getY();
        if(event.isMetaDown()){
            Graphics g = getGraphics();
            Rectangle r = getBounds();
            g.clearRect(0, 0, r.width, r.height);
            g.dispose();
        } else {
            last_x = x;
            last_y = y;
        }
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

    public void mouseClicked(MouseEvent event){}
    public void mouseEntered(MouseEvent event){}
    public void mouseExited(MouseEvent event){}
    public void mouseReleased(MouseEvent event){}

    public void mouseDragged(MouseEvent event){
        int x = event.getX();
        int y = event.getY();
        if(!event.isMetaDown()) {
            //don't process the right button drag
            Graphics g = screen.getGraphics();
            g.drawImage(red, x, y, null);
            g.drawLine(last_x, last_y, x, y);
            g.dispose();
            last_x = x;
            last_y = y;
        }
    }

    public void mouseMoved(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        //don't process the right button drag
        Graphics g = getGraphics();
        g.drawImage(red, x, y, null);
        g.dispose();
    }

    public void update(Graphics g) {
        g.setColor(background);
        g.fillRect(0, 0, 1000, 1000);
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

    public void paint(Graphics g) {
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
                    System.out.println("Winner");
                }
                Thread.sleep ( 50 );
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
        if (board.drop(move, player.getSymbol()))
        {
            System.out.println("Move:" + move);
            curr = 1 - curr;        //Alternate player
            char winner = board.check();
            if (winner == ' ') return 0;
            if (winner == players[0].getSymbol()) return 1;
            if (winner == players[1].getSymbol()) return 2;
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
        key = e.getKeyChar() - '0';
        System.out.println("Key:" + e.getKeyChar() + " Num:" + key);
    }

    public int nextInt()
    {
        int column = key - '0';
        key = 0;
        return column;
    }
}