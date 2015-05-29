package connect4;

public class Instructions
{
    private String moves;
    private int pos;
    private static int step;
    
    private static String instructions[] = new String[]
    	    {
    	        "The idea of the game\n" +
    	        "is to get 4 pieces in a\n" +
    	        "straight line.",

    	        "You can drop a piece with\n" +
    	        "the keys 1-7, or move the\n" +
    	        "piece with the mouse and\n" +
    	        "press the mouse button to\n" +
    	        "drop it, or use arrow keys\n" +
    	        "and return key).",

    	        "4535251",

    	        "F1-F4",

    	        "Player 1 completed a horizontal\n" +
    	        "line.",

    	        "5",

    	        "C5-F5",

    	        "Player 2 completed a vertical line.",

    	        "45566774677",

    	        "F4-C7",

    	        "Player 1 completed a diagonal line.",

    	        "F4-F7",

    	        "Player 2 got a bonus line since it\n" +
    	        "completed a line when the pieces\n" +
    	        "fell.",

    	        "",

    	        "",

    	        "",

    	        "",

    	        "Have fun!",
    	    };
    
    /**
     * Constructor for Instructions
     * @param popup a pop-up text box
     */
    public Instructions(Popup popup)
    {
        int x = 350, y = 350;

        while (true)
        {
            String cmd = instructions[step++];
            if (cmd.length() == 0) continue;
            if (numeric(cmd))
            {
                moves = cmd;
                break;
            }
            if (cmd.length() == 5)
            {
                popup.line(cmd);
            }
            else
            {
                popup.update(x, y, cmd);
                break;
            }
        }
    }
    
    /**
     * Makes sample moves
     * @return the moves
     */
    public int getMove()
    {
        if (moves == null) return 0;
        if (pos < moves.length()) return moves.charAt(pos++) - '0';
        moves = null;
        return 0;
    }
    
    /**
     * Checks if the tutorial is complete
     * @return true or false
     */
    public boolean complete()
    {
        if (moves == null) return true;
        return pos == moves.length();
    }
    
    
    private boolean numeric(String string)
    {
        try
        {
            Double.parseDouble(string);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
    
    /**
     * Starts the tutorial
     */
    public static void start()
    {
        step = 0;
    }
    
    /**
     * Advances to the next set of instructions
     * @param popup a pop-up text box
     * @return the new set of instructions
     */
    public static Instructions next(Popup popup)
    {
        if (step < instructions.length)
        {
            return new Instructions(popup);
        }
        return null;
    }
}

