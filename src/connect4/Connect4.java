package connect4;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Connect4
{
    private Scanner input;
    private Board board;
    private Player players[];

    public static final int NUM_COLUMNS = 7;
    public static final int NUM_ROWS = 6;
    public static final int NUM_PLAYERS = 2;
    public static final int ATTEMPTS = 1000;

    /**
     * Create an instance of the game
     */
    private Connect4()
    {
        board = new Board(NUM_COLUMNS, NUM_ROWS);
        players = new Player[NUM_PLAYERS];
        //players[0] = new Human("Human", 'O');
        players[0] = new Computer(1, 'O');
        players[1] = new Computer(1, 'X');
    }
    
    /**
     * Gets the name of the player
     * @param num the player number
     * @return the name of the player
     */
    private String getName(int num)
    {
        System.out.print("Enter the name of player " + num + ": ");
        return input.next();
    }
    
    /**
     * Starts the game (in the console)
     */
    private void start()
    {
        input = new Scanner(System.in);
        System.out.println("Welcome to Connect 4");
        System.out.println("1: Play 1 player");
        System.out.println("2: Play 2 players");
        System.out.println("3: Test AI");
        System.out.println("4: Test Board");

        int choice = 0;
        while (choice < 1 || choice > 4)
        {
            choice = input.nextInt();
        }

        switch (choice)
        {
            case 1:
                players[0] = new Human(getName(1), 'O');
                players[1] = new Computer(difficult(), 'X');
                break;
            case 2:
                players[0] = new Human(getName(1), 'O');
                players[1] = new Human(getName(2), 'X');
                break;
            case 3:
                testAI();
                return;
            case 4:
                testBoard();
                return;
        }

        while (true)
        {
            play(true);
            System.out.printf("Play again (y/n)");
            if (input.next().equalsIgnoreCase("n")) break;
        }
    }
    
    /**
     * Tests the board
     */
    private void testBoard()
    {
        while (true)
        {
            board.reset();
            char winner = 0;
            while (winner == 0)
            {
                board.draw();
                System.out.print("Enter coordinate(A1-F7): ");
                String coord = input.next();
                if (coord.length() == 2)
                {
                    board.set(coord);
                }
                winner = board.check();
            }
            System.out.println("Found a line");
            board.draw();
        }
    }
    
    /**
     * Requests and sets difficulty level
     * @return the difficulty level
     */
    private int difficult()
    {
        System.out.println("Enter difficulty level:");
        System.out.println("1: Easy (Random)");
        System.out.println("2: Medium (Current/Next)");
        System.out.println("3: Hard (Monte Carlo)");

        int choice = 0;
        while (choice < 1 || choice > 3)
        {
            choice = input.nextInt();
        }
        return choice - 2;
    }

    /**
     * The entry point
     * @param args - not used
     */
    public static void main(String args[])
    {
        //Connect4 game = new Connect4();
        //game.start();
    }
    
    /**
     * Runs AI vs. AI games and records the data
     */
    private void testAI()
    {
        String desc[] = new String[]{"Random", "Current/Next", "Monte Carlo"};
        for (int ai1 = 0; ai1 <= 2; ai1++)
        {
            for (int ai2 = 0; ai2 <= 2; ai2++)
            {
                int wins[] = new int[3];
                players[0] = new Computer(ai1 - 1, 'O');
                players[1] = new Computer(ai2 - 1, 'X');
                for (int attempt = 0; attempt < ATTEMPTS; attempt++)
                {
                    wins[play(false)]++;
                }
                System.out.println(desc[ai1] + " vs " + desc[ai2]);
                System.out.print(wins[0] / 10. + "% draws, " + desc[ai1] + " wins " + wins[1] / 10.);
                System.out.println("% " + desc[ai2] + " wins " + wins[2] / 10. + "%");
            }
        }
    }

    /**
     * Play the game
     * @param display
     */
    private int play(boolean display)
    {
        int curr = 0;
        Player player = players[curr];
        char winner = 0;
        board.reset();
        while (winner == 0)
        {
            player = players[curr];
            curr = 1 - curr;        //Alternate player
            while (true)
            {
                if (display) board.draw();
                board.save();   //The AI will probably modify the state of the board
                if (player instanceof Human) System.out.print(player.getName() + " which ");
                int move = player.getMove(board, players[curr].getSymbol(), input);
                //Ensure the board is in the same state as before we made the move
                board.restore();
                if (board.drop(move, player.getSymbol())) break;
            }
            winner = board.check();
        }
        if (display)
        {
            board.draw();
            if (winner == ' ')
            {
                System.out.println("GAME OVER - Draw");
            } else
            {
                System.out.println("Congratulations to " + player.getName());
            }
        }
        if (winner == ' ') return 0;
        if (winner == players[0].getSymbol()) return 1;
        return 2;
    }
}
