
import java.util.Arrays;

public class Column
{
    private char cells[];

    /**
     * Create a column
     * @param rows - the size of the column
     */
    public Column(int rows)
    {
        cells = new char[rows];
        for (int i = 0; i < rows; i++) cells[i] = ' ';
    }

    /**
     * The size of a column
     * @return number of cells
     */
    public int getRows()
    {
        return cells.length;
    }

    /**
     * Drop a symbol into the column
     * @param symbol the symbol to fill
     * @return false if unable to drop
     */
    public boolean drop(char symbol)
    {
        if (cells[0] != ' ') return false;
        for (int y = cells.length - 1; y >= 0; y--)
        {
            if (cells[y] == ' ')
            {
                cells[y] = symbol;
                break;
            }
        }
        return true;
    }

    /**
     * To get the character in the column
     * @param row which row to return
     * @return the symbol
     */
    public char at(int row)
    {
        return cells[row];
    }

    public void copy(Column column)
    {
        for (int i = 0; i < cells.length; i++)
        {
            cells[i] = column.cells[i];
        }
    }

    @Override
    public String toString()
    {
        return new String(cells);
    }
}
