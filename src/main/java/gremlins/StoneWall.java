package gremlins;

public class StoneWall {

    private final int column;
    private final int row;

    /**
     * Constructor of the StoneWall class to be instantiated based on input map
     * @param column is the initial column coordination of stone wall
     * @param row is the initial row coordination of stone wall
     */
    public StoneWall(int column, int row) {
        this.column = column;
        this.row = row;
    }

    /**
     * Getter method to get the column of this stone wall
     * @return the amount of column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Getter method to get the row of this stone wall
     * @return the amount of row
     */
    public int getRow() {
        return row;
    }

}
