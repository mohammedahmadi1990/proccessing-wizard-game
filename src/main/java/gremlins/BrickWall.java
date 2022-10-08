package gremlins;

public class BrickWall {

    private final int column;
    private final int row;
    private int status;

    /**
     * Constructor of the BrickWall class to be instantiated based on input map
     * @param column is the initial column coordination of brick wall
     * @param row is the initial row coordination of brick wall
     */
    public BrickWall(int column, int row, int status) {
        this.column = column;
        this.row = row;
        this.status = status;
    }

    /**
     * Getter method to get the column of this brick wall
     * @return the amount of column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Getter method to get the row of this brick wall
     * @return the amount of row
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter method to get the status of this brick wall whether has been shot or not
     * @return the amount of status field
     */
    public int getStatus() {
        return status;
    }

    /**
     * Setter method to set the status of this brick wall
     * @param status is the amount of status field
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
