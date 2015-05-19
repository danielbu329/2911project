
import java.util.Scanner;

public interface Player
{
    /**
     * The name of the player
     * @return - The players name
     */
    public String getName();

    /**
     * The move for the player
     * @return - which column should the drop occur in
     * @param board - the game board
     * @param symbol
     * @param input
     */
    public int getMove(Board board, char symbol, Scanner input);

    /**
     * The move for the player
     * @return - which column should the drop occur in
     * @param board - the game board
     * @param symbol
     * @param input
     */
    public int getMove(Board board, char symbol, GUI input);

    /**
     * How is the player represented on the board
     * @return the symbol that represents the player
     */
    public char getSymbol();
}
