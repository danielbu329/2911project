package connect4;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

public class GUI extends JFrame implements Runnable
{
	private Board board;
    private Player[] players;

    static final int NUM_COLUMNS = 7;
    static final int NUM_ROWS = 6;
    public static final int NUM_PLAYERS = 2;

    private Thread animation;
    private int[] columns;
    private int[] rows;
    private int[] score;
    private int[] bonus;
    private int playing = -1;
    private int curr;
    private Piece[] pieces;
    private GUIInput input;
    private GUIPanel panel;
    private Menu menu;
    private Mode mode;
    private Mode changeMode;
    private int timer = 0;
    private Random rnd;
    Message message;
    private Sound start;
    private Sound drop2;
    private Image yellow;
    private Image red;
    private Image myellow;
    private Image mred;
    private Image gameboard;
    private Font font;


    public static final int START_Y = 30;
    private static final int BONUS_TIME = 50;
    static final int MESSAGE_X = 350;
    static final int MESSAGE_Y = 200;

    /**
     * Entry point
     *
     * @param args - command line arguments
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                GUI gui = new GUI();
                gui.setVisible(true);
            }
        });
    }

    /**
     * Construct the GUI
     */
    private GUI() {
    	loadResources();
    	init();
    }

    void loadResources()
    {
        yellow = load("yellow.png");
        red = load("red.png");
        myellow = load("iconMenuCoinYellow.png");
        mred = load("iconMenuCoinRed.png");
        font = loadFont();
        gameboard = load("board.png");
        start = new Sound("start.wav");
        drop2 = new Sound("drop2.wav");
    }

    void init()
    {
        setTitle("Connect 4");
        setSize(701, 750);
        setResizable(false);
        setIconImage(load("programicon.png"));
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = getSize().width;
        int h = getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;

        // Move the window
        setLocation(x, y);
        setLocation(150, 50);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        rnd = new Random();
        score = new int[NUM_PLAYERS];
        bonus = new int[NUM_PLAYERS];
        board = new Board(yellow, red);
        pieces = new Piece[NUM_PLAYERS];
        changeMode = null;
        pieces[0] = new Piece(yellow, START_Y, board, 'O');
        pieces[1] = new Piece(red, START_Y, board, 'X');
        message = new Message(font);
        columns = new int[]{19, 119, 219, 319, 419, 519, 619};
        rows = new int[]{118, 218, 318, 418, 518, 618};
        menu = new Menu(this, myellow, mred);
        mode = changeMode;
        players = menu.getPlayers();
        panel = new GUIPanel(this, red, yellow, pieces[0], gameboard, font, board, getBackground());
        input = new GUIInput(this, panel, pieces[0]);
        getContentPane().add(panel);
        animation = new Thread(this);
        animation.start();
        menu.updateTimer(0);
    }


    /**
     * Start a new game
     */
    private void startgame()
    {
        while (panel.isDrawing())
        {
            panel.suspend(true);
            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        panel.suspend(false);
        panel.reset(mode.tutorial());
        changeMode = null;
        playing = -1;
        curr = 0;
        pieces[0].reset();
        pieces[1].reset();
        score[0] = 0;
        score[1] = 0;
        menu.setScore(0, 0);
        menu.setScore(0, 1);
        board.reset();
        board.clear();
        board.save();
        panel.setCurrent(pieces[curr]);
        input.setCurrent(pieces[curr], players[curr]);
        pieces[curr].move(columns[3]);
        pieces[1].move(columns[3]);
        message.hide();
        input.reset();
        if (animation != null)
        {
            menu.updateTimer(0);
        }
    }

    /**
     * Load an image from a file (or JAR)
     * @param filename The filename to load
     * @return An image
     */
    BufferedImage load(String filename)
    {
        try
        {
            BufferedImage image = ImageIO.read(new File("src/connect4/images/" + filename));
            return image;
        }
        catch (IOException err)
        {
            System.out.println("Exception:" + err);
        }
        return null;
    }

    /**
     * Load a font from a file (or JAR)
     * @param filename The file to load
     * @param size The size of the font
     * @return The font (or default if not possible)
     */
    Font loadFont()
    {
        try
        {
            Font font = Font.createFont(Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("fake-receipt.ttf"));
            return font.deriveFont(Font.PLAIN, 80);
        }
        catch (Exception err)
        {

        }
        try
        {
            Font font = Font.createFont(Font.TRUETYPE_FONT,
                    new FileInputStream("src/connect4/" + "fake-receipt.ttf"));
            return font.deriveFont(Font.PLAIN, 80);
        }
        catch (IOException | FontFormatException err)
        {
            System.out.println("Exception:" + err);
        }
        return new Font("Calibri", Font.PLAIN, 80);
    }

    /**
     * The code that runs to handle update
      */
    @Override
    public void run()
    {
        while (Thread.currentThread() == animation)
        {
            if (changeMode != null)
            {
                mode = changeMode;
                startgame();
            }
            panel.repaint();
            try
            {
                switch (playing)
                {
                    case 0:
                        message.set(mode.endless() ? "Game Over" : "Draw",
                                Color.BLUE);
                        /*
                        message.set(mode == Mode.ENDLESS ? "Game Over" : "Draw",
                                    MESSAGE_X, MESSAGE_Y, Color.BLUE);
                        */
                        break;
                    case 1:
                        message.set(mode.endless() ? "BONUS " + bonus[0] : "Winner",
                                Color.YELLOW);
                        if (mode.endless())
                        {
                            message.fade();
                            playing = 3;
                        }
                        break;
                    case 2:
                        message.set(mode.endless() ? "BONUS " + bonus[1] : "Winner",
                                Color.RED);
                        if (mode.endless())
                        {
                            message.fade();
                            playing = 3;
                        }
                        break;
                    case 3:
                        if (!message.displayed())
                        {
                            if (menu.playSounds()) start.play();
                            board.fall(columns, rows);
                            playing = 4;
                        }
                        break;
                    case 4:
                        if (board.isFinished())
                        {
                            playing = 5;
                        }
                        break;
                    case 5:
                    {
                        char winner = board.check();
                        if (winner != 0)
                        {
                            if (winner == players[0].getSymbol()) playing = 1;
                            if (winner == players[1].getSymbol()) playing = 2;
                        } else
                        {
                            playing = -1;
                        }
                        // DELIBERATE FALL THROUGH
                    }
                    default:
                        if (playing < 0)
                        {
                            playing = play();
                        }
                        if (playing > 0)
                        {
                            board.check(true);
                            char play1 = players[0].getSymbol();
                            char play2 = players[1].getSymbol();
                            if (mode.endless())
                            {
                                bonus[0] = board.bonus(play1);
                                bonus[1] = board.bonus(play2);
                                score[0] += bonus[0];
                                score[1] += bonus[1];
                                menu.setScore(score[0], 0);
                                menu.setScore(score[1], 1);
                            }
                        }
                        break;
                }
                Thread.sleep(20);
            }
            catch (InterruptedException e)
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
        char symbol = players[1 - curr].getSymbol();
        Piece current = pieces[curr];
        if (player instanceof Human || current.finishedDropping())
        {
            board.save();   //The AI will probably modify the state of the board
            //int move = player.getMove(board, players[1-curr].getSymbol(), input);
            int move = player.getMove(board, symbol, input);
            if (mode == Mode.SPEED && player instanceof Human && current.finishedDropping())
            {
                timer--;
                menu.updateTimer(timer);
                if (timer == 0)
                {
                    move = rnd.nextInt(board.getColumns());
                    while (board.canDrop(move) < 0)
                    {
                        move++;
                        if (move > board.getColumns()) move = 0;
                    }
                }
            }
            //Ensure the board is in the same state as before we made the move
            board.restore();
            int index = board.canDrop(move);
            if (index >= 0)
            {
                if (menu.playSounds()) drop2.play();
                current.drop(columns[move], START_Y, move, rows[index], index, current.getSymbol());
            }
        }
        if (current.isFinished())
        {
            current.dropped();
            board.save();
            curr = 1 - curr;        //Alternate player
            current = pieces[curr];
            //current.setImage(curr == 1 ? red : yellow);
            current.move(columns[3]);
            input.setCurrent(current, players[curr]);
            panel.setCurrent(current);
            timer = 63;
            char winner = board.check();
            if (winner == ' ') return 0;
            if (winner != 0)
            {
                if (winner == players[0].getSymbol()) return 1;
                if (winner == players[1].getSymbol()) return 2;
            }
        }
        return -1;
    }

    public int[] getRows()
    {
        return rows;
    }

    public int[] getColumns()
    {
        return columns;
    }

    public Mode mode(String item)
    {
        changeMode = Mode.valueOf(item.toUpperCase());
        return changeMode;
    }

    public Mode getMode()
    {
        if (mode == null) return Mode.NORMAL;
        return mode;
    }
    
    public int getPlaying() {
    	return playing;
    }
}
