package connect4;

public class Tutorial extends Computer
{
    /**
     * Create a tutorial player
     * @param symbol - the symbol to represent the player
     */
    public Tutorial(char symbol)
    {
        super(0, symbol);
    }

    @Override
    public int getMove(Board board, char symbol, GUIInput input)
    {
        return input.nextMove();
    }
}
