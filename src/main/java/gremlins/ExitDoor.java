package gremlins;

public class ExitDoor {

    private final int column;
    private final int row;

    /**
     * Constructor of the ExitDoor class to be instantiated based on input map
     * @param column is the initial column coordination of exit door
     * @param row is the initial row coordination of exit door
     */
    public ExitDoor(int column, int row) {
        this.column = column;
        this.row = row;
    }

    /**
     * Getter method to get the column of this exit door
     * @return the amount of column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Getter method to get the row of this exit door
     * @return the amount of row
     */
    public int getRow() {
        return row;
    }
}
