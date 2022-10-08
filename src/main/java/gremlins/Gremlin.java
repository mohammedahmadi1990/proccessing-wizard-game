package gremlins;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

public class Gremlin {

    private static final int SLIME_SPEED = 4;
    private static final int GREMLIN_SPEED = 1;
    public static final String[] DIRECTIONS = {"UP", "DOWN", "RIGHT", "LEFT"};
    private final boolean[] lastMet={false,false,false,false};
    private float column;
    private float row;
    private String direction;
    public Random rand;
    private final int MIN = 0;

    // 1 is UP, 2 is DOWN, 3 is RIGHT, 4 is LEFT
    private final int MAX = DIRECTIONS.length-1;
    private float fireColumn;
    private float fireRow;
    private boolean firePermit;
    private boolean fireDirectionFlag;
    private String fireDirection;

    /**
     * Constructor of the Gremlin class to be instantiated based on input map
     * @param column is the initial column coordination of exit door
     * @param row is the initial row coordination of exit door
     */
    public Gremlin(int column, int row) {
        this.column = column;
        this.row = row;
        rand = new Random();
        direction = DIRECTIONS[rand.nextInt((MAX - MIN) + 1) + MIN];
    }

    /**
     * based on this method gremlin disappears when an obstacle is met
     * @param stoneWalls is the coordination of stone walls
     * @param brickWalls is the coordination of brick walls
     */
    public void disappear(ArrayList<StoneWall> stoneWalls, ArrayList<BrickWall> brickWalls){
        // gremlin reborn in another random location at least 10 tile far
        int newC = 0;;
        int newR = 0;
        int nearestDistance = 10 * 20;
        boolean rightPlace = false;
        do{
            newC = (rand.nextInt(34)+1) * 20;
            newR = (rand.nextInt(29)+1) * 20;
            for (BrickWall brickWall : brickWalls)
                if (newR + 20 == brickWall.getRow() && newC == brickWall.getColumn())
                    rightPlace = true;
            for (StoneWall stoneWall : stoneWalls)
                if (newR + 20 == stoneWall.getRow() && newC == stoneWall.getColumn())
                    rightPlace = true;
        } while(!rightPlace && Math.abs(newC-column)<=nearestDistance || Math.abs(newR-row)<=nearestDistance);
        this.column = newC;
        this.row = newR;
    }

    /**
     * based on this method gremlin moves in random directions except arrival direction
     * @param stoneWalls is the coordination of stone walls
     * @param brickWalls is the coordination of brick walls
     */
    public void move(ArrayList<StoneWall> stoneWalls, ArrayList<BrickWall> brickWalls) {
        switch (direction){
            case "UP":
                if (isFree("UP", stoneWalls, brickWalls) && !lastMet[0]) {
                    this.row -= GREMLIN_SPEED;
                } else {
                    direction = DIRECTIONS[rand.nextInt((MAX - MIN) + 1) + MIN];
                    lastMet[0] = true;
                    lastMet[1] = false;
                    lastMet[2] = false;
                    lastMet[3] = false;
                }
                break;
            case "DOWN":
                if (isFree("DOWN", stoneWalls, brickWalls) && !lastMet[1]) {
                    this.row += GREMLIN_SPEED;
                } else {
                    direction = DIRECTIONS[rand.nextInt((MAX - MIN) + 1) + MIN];
                    lastMet[0] = false;
                    lastMet[1] = true;
                    lastMet[2] = false;
                    lastMet[3] = false;
                }
                break;
            case "RIGHT":
                if (isFree("RIGHT", stoneWalls, brickWalls) && !lastMet[2]) {
                    this.column += GREMLIN_SPEED;
                } else {
                    direction = DIRECTIONS[rand.nextInt((MAX - MIN) + 1) + MIN];
                    lastMet[0] = false;
                    lastMet[1] = false;
                    lastMet[2] = true;
                    lastMet[3] = false;
                }
                break;
            case "LEFT":
                if (isFree("LEFT", stoneWalls, brickWalls) && !lastMet[3]) {
                    this.column -= GREMLIN_SPEED;
                } else {
                    direction = DIRECTIONS[rand.nextInt((MAX - MIN) + 1) + MIN];
                    lastMet[0] = false;
                    lastMet[1] = false;
                    lastMet[2] = false;
                    lastMet[3] = true;
                }
                break;
        }
    }

    /**
     * based on this method gremlin checks ability to fire and updates fire's (column,row)
     * @param stoneWalls is the coordination of stone walls
     * @param brickWalls is the coordination of brick walls
     */
    public void fire(ArrayList<StoneWall> stoneWalls, ArrayList<BrickWall> brickWalls) {
        if(fireDirectionFlag){
            fireDirection = direction;
            fireDirectionFlag = false;
        }
        switch (fireDirection){
            case "UP":
                if (isFireFree("UP", stoneWalls, brickWalls)) {
                    this.fireRow -= SLIME_SPEED;
                }else{
                    this.firePermit=false;
                }
                break;
            case "DOWN":
                if (isFireFree("DOWN", stoneWalls, brickWalls)) {
                    this.fireRow += SLIME_SPEED;
                }else{
                    this.firePermit=false;
                }
                break;
            case "RIGHT":
                if (isFireFree("RIGHT", stoneWalls, brickWalls)) {
                    this.fireColumn += SLIME_SPEED;
                }else{
                    this.firePermit=false;
                }
                break;
            case "LEFT":
                if (isFireFree("LEFT", stoneWalls, brickWalls)) {
                    this.fireColumn -= SLIME_SPEED;
                }else{
                    this.firePermit=false;
                }
                break;
        }
    }

    /**
     * based on this method gremlin moves in random directions except arrival direction
     * @param direction is the current direction of this gremlin
     * @param stoneWalls is the coordination of stone walls
     * @param brickWalls is the coordination of brick walls
     * @return the status of obstacle meeting i4 directions, whether to continue moving or not
     */
    public boolean isFree(String direction, ArrayList<StoneWall> stoneWalls, ArrayList<BrickWall> brickWalls){
        // Check whether obstacle is in the next step
        int steps = 20;
        switch (direction) {
            case "UP":
                for (BrickWall brickWall : brickWalls)
                    if (row - steps == brickWall.getRow() && column == brickWall.getColumn())
                        return false;
                for (StoneWall stoneWall : stoneWalls)
                    if (row - steps == stoneWall.getRow() && column == stoneWall.getColumn())
                        return false;
                break;
            case "DOWN":
                for (BrickWall brickWall : brickWalls)
                    if (row + steps == brickWall.getRow() && column == brickWall.getColumn())
                        return false;
                for (StoneWall stoneWall : stoneWalls)
                    if (row + steps == stoneWall.getRow() && column == stoneWall.getColumn())
                        return false;
                break;
            case "RIGHT":
                for (BrickWall brickWall : brickWalls)
                    if (column + steps == brickWall.getColumn() && row == brickWall.getRow())
                        return false;
                for (StoneWall stoneWall : stoneWalls)
                    if (column + steps == stoneWall.getColumn() && row == stoneWall.getRow())
                        return false;
                break;
            case "LEFT":
                for (BrickWall brickWall : brickWalls)
                    if (column - steps == brickWall.getColumn() && row == brickWall.getRow())
                        return false;
                for (StoneWall stoneWall : stoneWalls)
                    if (column - steps == stoneWall.getColumn() && row == stoneWall.getRow())
                        return false;
                break;
        }
        return true;
    }

    /**
     * Based on this method gremlin's fireball checks to continue moving or not
     * @param direction is the current direction of this gremlin
     * @param stoneWalls is the coordination of stone walls
     * @param brickWalls is the coordination of brick walls
     * @return the status of obstacle meeting in 4 directions, whether to continue moving or not
     */
    public boolean isFireFree(String direction, ArrayList<StoneWall> stoneWalls, ArrayList<BrickWall> brickWalls){
        // Check whether obstacle is in the next step
        int steps;
        for (int i = 0; i < 10 ; i++) {
            steps = i;
            switch (direction) {
                case "UP":
                    for (BrickWall brickWall : brickWalls)
                        if (this.fireRow - steps == brickWall.getRow() && this.fireColumn == brickWall.getColumn()) {
                            return false;
                        }
                    for (StoneWall stoneWall : stoneWalls)
                        if (this.fireRow - steps == stoneWall.getRow() && this.fireColumn == stoneWall.getColumn()) {
                            return false;
                        }
                    break;
                case "DOWN":
                    for (BrickWall brickWall : brickWalls)
                        if (this.fireRow + steps == brickWall.getRow() && this.fireColumn == brickWall.getColumn()) {
                            return false;
                        }
                    for (StoneWall stoneWall : stoneWalls)
                        if (this.fireRow + steps == stoneWall.getRow() && this.fireColumn == stoneWall.getColumn()) {
                            return false;
                        }
                    break;
                case "RIGHT":
                    for (BrickWall brickWall : brickWalls)
                        if (this.fireColumn + steps == brickWall.getColumn() && this.fireRow == brickWall.getRow()) {
                            return false;
                        }
                    for (StoneWall stoneWall : stoneWalls)
                        if (this.fireColumn + steps == stoneWall.getColumn() && this.fireRow == stoneWall.getRow()) {
                            return false;
                        }
                    break;
                case "LEFT":
                    for (BrickWall brickWall : brickWalls)
                        if (this.fireColumn - steps == brickWall.getColumn() && this.fireRow == brickWall.getRow()) {
                            return false;
                        }
                    for (StoneWall stoneWall : stoneWalls)
                        if (this.fireColumn - steps == stoneWall.getColumn() && this.fireRow == stoneWall.getRow()) {
                            return false;
                        }
                    break;
            }
        }
        return true;
    }

    /**
     * Getter method to get the column of this Gremlin
     * @return the amount of column
     */
    public float getColumn() {
        return column;
    }

    /**
     * Getter method to get the row of this Gremlin
     * @return the amount of row
     */
    public float getRow() {
        return row;
    }

    /**
     * Getter method to get the permit status for firing of the gremlin
     * @return the amount of firePermit
     */
    public boolean isFirePermit() {
        return firePermit;
    }

    /**
     * Setter method to set the permit status for firing of the gremlin
     * @param firePermit is the amount of firePermit
     */
    public void setFirePermit(boolean firePermit) {
        this.fireColumn = this.column;
        this.fireRow = this.row;
        this.firePermit = firePermit;
    }

    /**
     * Getter method to get the fire's column of the gremlin
     * @return the amount of fireColumn
     */
    public float getFireColumn() {
        return fireColumn;
    }

    /**
     * Getter method to get the fire's row of the gremlin
     * @return the amount of fireRow
     */
    public float getFireRow() {
        return fireRow;
    }

    /**
     * Setter method to set the status of change in fire direction of the gremlin
     * @param fireDirectionFlag is the amount of fireDirectionFlag
     */
    public void setFireDirectionFlag(boolean fireDirectionFlag) {
        this.fireDirectionFlag = fireDirectionFlag;
    }
}
