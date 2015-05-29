package connect4;

import java.util.Scanner;

public interface Player
{
    /**
     * The name of the player
     *
     * @return - The players name
     */
    public String getName();

    /**
     * The move for the player
     *
     * @param board  - the game board
     * @param symbol - the symbol to represent the player
     * @param input - the input channel
     * @return - which column should the drop occur in
     */
    public int getMove(Board board, char symbol, Scanner input);

    /**
     * The move for the player
     *
     * @param board  - the game board
     * @param symbol - the symbol to represent the player
     * @param input - the input channel
     * @return - which column should the drop occur in
     */
    public int getMove(Board board, char symbol, GUIInput input);

    /**
     * How is the player represented on the board
     *
     * @return the symbol that represents the player
     */
    public char getSymbol();
}