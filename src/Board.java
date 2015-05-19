//package connect4;

import java.awt.*;

public class Board
{
    private Column[] columns;
    private Column[] savedColumns;
    private Column blank;
    private int w;
    private int h;

    /**
     * Create a new game board
     * @param columns - number of columns wide
     * @param rows - number of rows high
     */
    public Board(int columns, int rows)
    {
        this.columns = new Column[columns];
        savedColumns = new Column[columns];
        blank = new Column(rows);
        for (int column = 0; column < columns; column++)
        {
            this.columns[column] = new Column(rows);
            savedColumns[column] = new Column(rows);
        }
        w = columns;
        h = rows;
    }

    /**
     * Return the size of the board
     * @return thw width of the board
     */
    public int getColumns()
    {
        return w;
    }

    /**
     * Check if 4 tiles in a row are the same
     * @param row start position
     * @param col
     * @param row_step how much to step by
     * @param col_step
     * @return 0 if no line, or the symbol if a line is found
     */
    private char check4(int row, int col, int row_step, int col_step)
    {
        char line = columns[col].at(row);
        if (line == ' ') return 0;

        for (int check = 3; check > 0; check--)
        {
            row += row_step;
            col += col_step;
            if (columns[col].at(row) != line) return 0;
        }
        return line;
    }

    /**
     * Check if a line is present on the board
     * @param row - the row to check
     * @return 0 if no line, or the symbol if a line is found
     */
    private char checkRow(int row)
    {
        char win = 0;
        for (int col = w - 4; col >= 0; col--)
        {
            win = check4(row, col, 0, 1);
            if (win != 0) return win;
        }
        return 0;
    }

    /**
     * Check if a line is present on the board
     * @param column - the column to check
     * @return 0 if no line, or the symbol if a line is found
     */
    private char checkColumn(int column)
    {
        char win = 0;
        for (int row = h - 4; row >= 0; row--)
        {
            win = check4(row, column, 1, 0);
            if (win != 0) return win;
        }
        return 0;
    }

    /**
     * Check if a line is present on the board
     * @return 0 if no line, or the symbol if a line is found
     */
    private char checkDiagonal()
    {
        char win = 0;
        for (int column = w - 1; column >= 0; column--)
        {
            for (int row = h - 4; row >= 0; row--)
            {
                if (column < w - 4)
                {
                    win = check4(row, column, 1, 1);
                    if (win != 0) return win;
                }
                if (column >= 4)
                {
                    win = check4(row, column, 1, -1);
                    if (win != 0) return win;
                }
            }
            for (int row = h - 1; row >= 4; row--)
            {
                if (column < w - 4)
                {
                    win = check4(row, column, -1, 1);
                    if (win != 0) return win;
                }
                if (column >= 4)
                {
                    win = check4(row, column, -1, -1);
                    if (win != 0) return win;
                }
            }
        }
        return 0;

    }

    /**
     * Check if a line is present on the board
     * @return 0 if no line, or the symbol if a line is found
     */
    public char check()
    {
        char win = 0;

        for (int row = 0; row < h; row++)
        {
            win = checkRow(row);
            if (win != 0) return win;
        }

        for (int column = 0; column < w; column++)
        {
            win = checkColumn(column);
            if (win != 0) return win;
        }

        win = checkDiagonal();
        if (win != 0) return win;

        for (int column = 0; column < w; column++)
        {
            if (columns[column].at(0) == ' ') return 0; //Board is not full
        }

        return ' ';     //No one wins
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
     * @param move which column to drop into
     * @param symbol which symbol are we dropping
     * @return false if not able to drop
     */
    public boolean drop(int move, char symbol)
    {
        if (move < 0 || move >= getColumns()) return false;
        Column column = columns[move];
        return column.drop(symbol);
    }

    public int canDrop(int move)
    {
        if (move < 0 || move >= getColumns()) return -1;
        Column column = columns[move];
        return column.canDrop();
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
            System.out.print((char)('A' + row));
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
        for (int row = 0; row < columns[0].getRows(); row++)
        {
            for (int column = 0; column < getColumns(); column++)
            {
                char ch = savedColumns[column].at(row);
                if (ch == ' ') continue;
                int x = cols[column];
                int y = rows[row];
                Image cell = yellow;
                if (ch == 'X') cell = red;
                g.drawImage(cell, x, y, null);
            }
        }
    }
}
