import java.util.Random;
import java.util.Scanner;

public class Computer implements Player
{
    private int smart;
    private char symbol;
    private Random rnd;

    /**
     * Create a computer player
     * @param smart - the level of AI to use
     * @param symbol - the symbol to represent the player
     */
    public Computer(int smart, char symbol)
    {
        this.smart = smart;
        this.symbol = symbol;
        rnd = new Random();
    }

    @Override
    public String getName()
    {
        return "Computer";
    }

    @Override
    public int getMove(Board board, char other, Scanner input)
    {
        if (smart < 0) return valid(-1, board);

        int columns = board.getColumns();
        int consider = (1 << columns) - 1;      //The rows that should be considered

        //First we try dropping in each column and see if that gives a win
        for (int column = 0; column < columns; column++)
        {
            if (!board.drop(column, symbol))
            {
                //If we can't drop in a column (it is full, we remove it from the consider list)
                consider &= ~(1 << column);     //clear the bit representing the column
                continue;
            }
            char check = board.check();         //Check if this was winning move
            if (check == this.symbol)           //We won, might as well make this move
            {
                return column;
            }
            //Now consider the moves the opponent would make, if there is a win for the player
            //we need to remove the current column from the list to consider
            for (int opponent = 0; opponent < columns; opponent++)
            {
                board.restore();
                board.drop(column, symbol);     //Make the same move again
                if (board.drop(opponent, other))    //If we can drop
                {
                    check = board.check();          //See if the opponent would win in this case
                    //If making the move would mean opponent can win next turn, we don't want to
                    //consider making this move
                    if (check == other)
                    {
                        consider &= ~(1 << column);     //clear the bit representing the column
                    }
                }
            }
            board.restore();
        }

        if (consider == 0) return valid(-1, board);         //Just return the first valid move

        if (smart == 0)     //At smart == 0 wins approx 90% of the time against random player
        {
            return valid(consider, board);                  //Make any valid move
        }

        int wins[] = new int[columns];
        int losses[] = new int[columns];
        int curr = 0;
        int maxDepth = 6;
        int maxTrials = 1000;
        if (smart == 2)
        {
            maxDepth = 9;
            maxTrials = 4000;
        }
        if (smart == 3)
        {
            maxDepth = 13;
            maxTrials = 8000;
        }

        for (int trial = 0; trial < maxTrials; trial++)
        {
            board.restore();
            char winner = 0;
            int initial = 0;
            for (int depth = 0; depth < maxDepth; depth++)
            {
                char drop = symbol;
                if ((depth & 1) == 1) drop = other;
                int column;
                if (depth == 0)
                {
                    column = valid(consider, board);
                    initial = column;
                }
                else
                {
                    column = valid(-1, board);
                }
                board.drop(column, drop);
                winner = board.check();
                if (winner != 0) break;
            }

            if (winner == symbol) wins[initial]++;
            if (winner == other) losses[initial]++;
        }

        int best = Integer.MIN_VALUE;
        int bestColumn = valid(consider, board);                  //Make any valid move;

        for (int column = 0; column < columns; column++)
        {
            if ((consider & (1 << column)) != 0)
            {
                int score = wins[column] - 4 * losses[column];
                if (score > best)
                {
                    best = score;
                    bestColumn = column;
                }
            }
        }

        return bestColumn;
    }

    @Override
    public int getMove(Board board, char symbol, GUI input)
    {
        return getMove(board, symbol, (Scanner)null);
    }

    /**
     * Return any valid move
     * @param consider - the columns we should consider
     * @param board - the existing board
     * @return the column where drop is to be done
     */
    private int valid(int consider, Board board)
    {
        int columns = board.getColumns();
        int column = rnd.nextInt(columns);
        //we get random column, and return first matching one, this is bounded time check
        for (int test = 0; test < columns; test++)
        {
            if ((consider & (1 << column)) != 0)
            {
                if (board.drop(column, symbol)) return column;
            }
            column++;
            column %= columns;
        }
        //Ok, we have to make a losing move, so check again for any possible move
        for (int test = 0; test < columns; test++)
        {
            if (board.drop(column, symbol)) return column;
            column++;
            column %= columns;
        }

        return -1;
    }

    @Override
    public char getSymbol()
    {
        return symbol;
    }
}
