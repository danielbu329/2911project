package connect4;

import java.awt.*;

class Board
{
    private final Column[] columns;
    private final Column[] savedColumns;
    private final Piece[] pieces;
    private final boolean[][] match;
    private final Column blank;
    private final int w;
    private final int h;
    private boolean blink;
    private int flash;
    private Image yellow;
    private Image red;
    private boolean finish;

    public boolean isFinished()
    {
        return finish;
    }

    /**
     * Create a new game board
     *
     * @param columns - number of columns wide
     * @param rows    - number of rows high
     */
    public Board(int columns, int rows)
    {
        this.columns = new Column[columns];
        savedColumns = new Column[columns];
        match = new boolean[rows][columns];
        pieces = new Piece[rows * columns];

        blank = new Column(rows);
        for (int column = 0; column < columns; column++)
        {
            this.columns[column] = new Column(rows);
            savedColumns[column] = new Column(rows);
        }
        w = columns;
        h = rows;
        flash = 0;
    }
    
    /**
     * Contructor for Board
     * @param yellow yellow piece image
     * @param red red piece image
     */
    public Board(Image yellow, Image red)
    {
        this(GUI.NUM_COLUMNS, GUI.NUM_ROWS);
        this.yellow = yellow;
        this.red = red;
    }


    /**
     * Return the size of the board
     * @return the width of the board
     */
    public int getColumns()
    {
        return w;
    }

    /**
     * Check if 4 tiles in a row are the same
     *
     * @param row      start position
     * @param col      column start position
     * @param row_step how much to step by
     * @param col_step how much to step by
     * @param set      Set the match array with this value (for flashing win line(s))
     * @param win      The value to return if there is not a match
     * @return 0 if no line, or the symbol if a line is found
     */
    private char check4(int row, int col, int row_step, int col_step, boolean set, char win)
    {
        char line = columns[col].at(row);
        if (line == ' ') return win;

        for (int check = 3; check > 0; check--)
        {
            row += row_step;
            col += col_step;
            if (columns[col].at(row) != line) return win;
        }

        for (int check = 4; check > 0; check--)
        {
            match[row][col] = set;
            row -= row_step;
            col -= col_step;
        }

        if (win != 0) return win;

        return line;
    }

    /**
     * Check if a horizontal line is present on the board
     *
     * @param row - the row to check
     * @param set Set the match on winning
     * @param win Return if no match
     * @return 0 if no line, or the symbol if a line is found
     */
    private char checkRow(int row, boolean set, char win)
    {
        for (int col = w - 4; col >= 0; col--)
        {
            win = check4(row, col, 0, 1, set, win);
        }
        return win;
    }

    /**
     * Check if a vertical line is present on the board
     *
     * @param column - the column to check
     * @param set set the match array
     * @param win the value to return if no win detected
     * @return 0 if no line, or the symbol if a line is found
     */
    private char checkColumn(int column, boolean set, char win)
    {
        for (int row = h - 4; row >= 0; row--)
        {
            win = check4(row, column, 1, 0, set, win);
        }
        return win;
    }

    /**
     * Check if a diagonal line is present on the board
     *
     * @param set set the match array
     * @param win the value to return if no win detected
     * @return 0 if no line, or the symbol if a line is found
     */
    private char checkDiagonal(boolean set, char win)
    {
        for (int column = w - 1; column >= 0; column--)
        {
            for (int row = h - 4; row >= 0; row--)
            {
                if (column <= w - 4)
                {
                    win = check4(row, column, 1, 1, set, win);
                }
                if (column >= 4)
                {
                    win = check4(row, column, 1, -1, set, win);
                }
            }
            for (int row = h - 1; row >= 4; row--)
            {
                if (column <= w - 4)
                {
                    win = check4(row, column, -1, 1, set, win);
                }
                if (column >= 4)
                {
                    win = check4(row, column, -1, -1, set, win);
                }
            }
        }
        return win;

    }

    /**
     * Check if a line is present on the board
     *
     * @return 0 if no line, or the symbol if a line is found
     */
    public char check(boolean set)
    {
        char win = 0;

        for (int row = 0; row < h; row++)
        {
            win = checkRow(row, set, win);
        }

        for (int column = 0; column < w; column++)
        {
            win = checkColumn(column, set, win);
        }

        win = checkDiagonal(set, win);
        if (win != 0) return win;

        for (int column = 0; column < w; column++)
        {
            if (columns[column].at(0) == ' ') return 0; //Board is not full
        }

        return ' ';     //No one wins
    }

    public char check()
    {
        return check(false);
    }

    /**
     * Check if 5 tiles in a row are the same or blank
     * If any tiles above are set then we can ignore
     * Check -XXX- the positions marked X for 2 the same
     *
     * @param col start position
     * @return 0 if no columns where we need to drop, or bit mask otherwise
     */
    private int test5(int col)
    {
        int row = h - 1;
        if (columns[col].at(row) != ' ') return 0;
        if (columns[col + 4].at(row) != ' ') return 0;

        char ch1 = columns[col + 1].at(row);
        char ch2 = columns[col + 2].at(row);
        char ch3 = columns[col + 3].at(row);

        int blank = 0;
        int spaces = 0;

        if (ch1 == ' ')
        {
            blank = col + 1;
            spaces++;
        }
        if (ch2 == ' ')
        {
            blank = col + 2;
            spaces++;
        }
        if (ch3 == ' ')
        {
            blank = col + 3;
            spaces++;
        }

        if (spaces != 1) return 0;

        return (1 << col) + (1 << (col + 4)) + (1 << blank);
    }

    /**
     * Checks for potential length 5 line since that is impossible to stop
     * once it reaches length 3
     * O       O
     * --X---- --X---O --XX--O --XXX-O Now X will win next turn, no matter where O goes
     *
     * @return potential locations to drop as bitmask (or 0 if no restrictions)
     */
    public int checkFive()
    {
        int move = 0;

        for (int col = w - 5; col >= 0; col--)
        {
            move |= test5(col);
        }

        if (move == 0) return -1;

        return move;
    }

    /**
     * Save a copy of the board, for AI testing possible outcomes
     */
    public void save()
    {
        for (int column = 0; column < w; column++)
        {
            savedColumns[column].copy(columns[column]);
        }
    }

    /**
     * Restore the board back to the saved board
     */
    public void restore()
    {
        for (int column = 0; column < w; column++)
        {
            columns[column].copy(savedColumns[column]);
        }
    }

    /**
     * Drop a piece onto the board
     *
     * @param move   which column to drop into
     * @param symbol which symbol are we dropping
     * @return false if not able to drop
     */
    public boolean drop(int move, char symbol)
    {
        if (move < 0 || move >= getColumns()) return false;
        Column column = columns[move];
        return column.drop(symbol);
    }
    
    /**
     * Checks if the drop is a legal move
     * @param move the column requested
     * @return true if legal, false otherwise
     */
    public int canDrop(int move)
    {
        if (move < 0 || move >= getColumns()) return -1;
        Column column = columns[move];
        return column.canDrop();
    }
    
    /**
     * Calculates the drop and bounce physics of the coin
     * @param colPos the column it's falling in
     * @param rowPos the row position it needs to bouce on
     */
    public void fall(int colPos[], int rowPos[])
    {
        finish = false;
        int i = 0;
        for (int row = h - 1; row >= 0; row--)
        {
            for (int col = 0; col < w; col++)
            {
                Column column = columns[col];
                Column saved = savedColumns[col];
                if (match[row][col])
                {
                    column.clear(row);
                    saved.clear(row);
                    match[row][col] = false;
                    int y = row - 1;
                    while (y >= 0)
                    {
                        if (match[y][col])
                        {
                            column.clear(y);
                            saved.clear(y);
                            y--;
                        } else
                        {
                            char symbol = column.at(y);
                            Image image = null;
                            if (symbol == 'X') image = red;
                            if (symbol == 'O') image = yellow;
                            if (image != null)
                            {
                                column.clear(y);
                                saved.clear(y);
                                match[y][col] = true;
                                Piece piece = new Piece(image, 0, this, 'O');
                                piece.drop(colPos[col], rowPos[y], col, rowPos[row], row, symbol);
                                pieces[i++] = piece;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Draw the board
     */
    public void draw()
    {
        System.out.print("  ");
        String line = "  -------------------------------".substring(0, getColumns() * 2 + 2);
        for (int column = 1; column <= getColumns(); column++)
        {
            System.out.print(column);
            System.out.print(' ');
        }
        System.out.println();
        System.out.println(line);
        for (int row = 0; row < columns[0].getRows(); row++)
        {
            System.out.print((char) ('A' + row));
            for (int column = 0; column < getColumns(); column++)
            {
                System.out.print("|");
                System.out.print(columns[column].at(row));
            }
            System.out.println("|");
            System.out.println(line);
        }
        System.out.println();
    }
    
    /**
     * Resets the board
     */
    public void reset()
    {
        for (int column = 0; column < w; column++)
        {
            columns[column].copy(blank);
        }
    }

    /**
     * For debugging, allows a cell to be set
     */
    void set(String index)
    {
        int row = index.toUpperCase().charAt(0) - 'A';
        int col = index.charAt(1) - '1';
        columns[col].set(row);
    }

    public void draw(Graphics g, int[] rows, int[] cols, Image red, Image yellow)
    {
        flash++;
        if (flash > 3)
        {
            blink = !blink;
            flash = 0;
        }
        for (int row = 0; row < columns[0].getRows(); row++)
        {
            for (int column = 0; column < getColumns(); column++)
            {
                if (blink && match[row][column]) continue;
                char ch = savedColumns[column].at(row);
                if (ch == ' ') continue;
                int x = cols[column];
                int y = rows[row];
                Image cell = yellow;
                if (ch == 'X') cell = red;
                g.drawImage(cell, x, y, null);
            }
        }
        finish = true;
        for (int i = 0; i < pieces.length; i++)
        {
            Piece piece = pieces[i];
            if (piece != null)
            {
                piece.draw(g);
                if (piece.isFinished())
                {
                    pieces[i] = null;
                } else
                {
                    finish = false;
                }
            }
        }
    }
    
    /**
     * Clears a line of coins
     */
    public void clear()
    {
        for (int row = 0; row < h; row++)
        {
            for (int col = 0; col < w; col++)
            {
                match[row][col] = false;
            }
        }
    }
    
    /**
     * Calculates points for endless mode
     * The points awarded are 2^n + 2n, where n is the number of coins in a row
     * @param symbol red or yellow
     * @return The amount of points received from forming one (or more) lines
     */
    public int bonus(char symbol)
    {
        int count = 0;
        for (int row = 0; row < columns[0].getRows(); row++)
        {
            for (int column = 0; column < getColumns(); column++)
            {
                if (match[row][column] && savedColumns[column].at(row) == symbol) count++;
            }
        }
        int bonus = 0;
        count -= 3;
        while (count >= 0)
        {
            count--;
            bonus++;
            bonus *= 2;
        }

        return bonus;
    }
    
    public void set(int row, int column, char symbol)
    {
        columns[column].set(row, symbol);
        savedColumns[column].set(row, symbol);
    }
}