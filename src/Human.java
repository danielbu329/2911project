
import java.util.Random;
import java.util.Scanner;

public class Human implements Player
{
    private String name;
    private char symbol;
    private Random rnd;

    /**
     * Create a player
     * @param name The name of the player
     * @param symbol The symbol to represent the player
     */
    public Human(String name, char symbol)
    {
        this.name = name;
        this.symbol = symbol;
        rnd = new Random();
    }

    @Override
    public String getName()
    {
        return name;
    }

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
    public int getMove(Board board, char symbol, Scanner input)
    {
        System.out.print("column (1-" + board.getColumns() + ")? ");
        int column = 0;
        while (column < 1 || column > board.getColumns())
        {
            column = input.nextInt();
        }

        return column - 1;
        //return valid(-1, board);
    }

    @Override
    public int getMove(Board board, char symbol, GUI input)
    {
        int column = input.nextInt();
        if (column >=1 && column <= board.getColumns())
        {
            System.out.println("Got input:" + column);
        }

        return column - 1;
    }

    @Override
    public char getSymbol()
    {
        return symbol;
    }
}
