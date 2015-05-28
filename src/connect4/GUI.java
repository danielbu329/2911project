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
    private Player players[];

    public static final int NUM_COLUMNS = 7;
    public static final int NUM_ROWS = 6;
    public static final int NUM_PLAYERS = 2;

    private Thread animation;
    private Image yellow;
    private Image red;
    private Image Myellow;
    private Image Mred;
    private int[] columns;
    private int[] rows;
    private int[] score;
    private int[] bonus;
    private int playing = -1;
    private int curr;
    Piece pieces[];
    private GUIInput input;
    private GUIPanel panel;
    private Menu menu;
    private Mode mode;
    private Mode changeMode;
    int timer = 0;
    private Random rnd;
    private int bonusTime;
    Message message;
    Sound start;
    Sound drop1;
    Sound drop2;
    Sound hand;

    public static final int START_Y = 30;
    public static final int BONUS_TIME = 50;
    public static final int MESSAGE_X = 350;
    public static final int MESSAGE_Y = 200;

    /**
     * Entry point
     * @param args - command line arguments
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                GUI frame = new GUI();
                frame.setVisible(true);
            }
        });
    }

    /**
     * Construct the GUI
     */
    public GUI()
    {
        setTitle("Connect 4");
        setSize(701, 750);
        setResizable(false);
        setIconImage(load("programicon.png"));
        setLocation(150, 50);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        rnd = new Random();
        score = new int[NUM_PLAYERS];
        bonus = new int[NUM_PLAYERS];
        yellow = load("yellow.png");
        red = load("red.png");
        Myellow = load("iconMenuCoinYellow.png");
        Mred = load("iconMenuCoinRed.png");
        Font font = loadFont("fake-receipt.ttf", 80);
        board = new Board(NUM_COLUMNS, NUM_ROWS, yellow, red);
        pieces = new Piece[NUM_PLAYERS];
        changeMode = null;
        pieces[0] = new Piece(yellow, 0, START_Y, board, 'O');
        pieces[1] = new Piece(red, 0, START_Y, board, 'X');
        message = new Message(font);
        Image gameboard = load("board.png");
        columns = new int[]{19, 119, 219, 319, 419, 519, 619};
        rows = new int[]{118, 218, 318, 418, 518, 618};
        menu = new Menu(this, Myellow, Mred);
        start = new Sound("start.wav");
        hand = new Sound("hand.wav");
        drop1 = new Sound("drop1.wav");
        drop2 = new Sound("drop2.wav");
        mode = changeMode;
        players = menu.getPlayers();
        panel = new GUIPanel(this, red, yellow, pieces[0], gameboard, font, board, getBackground());
        input = new GUIInput(this, panel, pieces[0]);
        setVisible(true);
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
                animation.sleep(1);
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
        pieces[curr].move(columns[3], START_Y);
        pieces[1].move(columns[3], START_Y);
        message.hide();
        bonusTime = 0;
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
            BufferedImage image = ImageIO.read(new File("src/connect4/" + filename));
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
    Font loadFont(String filename, int size)
    {
        try
        {
            Font font = Font.createFont(Font.TRUETYPE_FONT,
                    new FileInputStream("src/connect4/" + filename));
            return font.deriveFont(Font.PLAIN, size);
        }
        catch (IOException err)
        {
            System.out.println("Exception:" + err);
        }
        catch (FontFormatException err)
        {
            System.out.println("Exception:" + err);
        }
        return new Font("Calibri", Font.PLAIN, size);
    }

    /**
     * The code that runs to handle update
      */
    @Override
    public void run()
    {
        while ( Thread.currentThread() == animation )
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
                                MESSAGE_X, MESSAGE_Y, Color.BLUE);
                        /*
                        message.set(mode == Mode.ENDLESS ? "Game Over" : "Draw",
                                    MESSAGE_X, MESSAGE_Y, Color.BLUE);
                        */
                        break;
                    case 1:
                        message.set(mode.endless() ? "BONUS " + bonus[0] : "Winner",
                                MESSAGE_X, MESSAGE_Y, Color.YELLOW);
                        if (mode.endless())
                        {
                            message.fade();
                            playing = 3;
                        }
                        break;
                    case 2:
                        message.set(mode.endless() ? "BONUS " + bonus[1] : "Winner",
                                MESSAGE_X, MESSAGE_Y, Color.RED);
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
                        }
                        else
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
                                bonusTime = BONUS_TIME;
                            }
                        }
                        break;
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
        char symbol = players[1-curr].getSymbol();
        Piece current = pieces[curr];
        if (player instanceof Human || !current.isDropping())
        {
            board.save();   //The AI will probably modify the state of the board
            //int move = player.getMove(board, players[1-curr].getSymbol(), input);
            int move = player.getMove(board, symbol, input);
            if (mode == Mode.SPEED && player instanceof Human && !current.isDropping())
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
            //if (board.drop(current.getColumn(), player.getSymbol()))
            if (true)
            {
                current.dropped();
                board.save();
                curr = 1 - curr;        //Alternate player
                current = pieces[curr];
                //current.setImage(curr == 1 ? red : yellow);
                current.move(columns[3], START_Y);
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
}
