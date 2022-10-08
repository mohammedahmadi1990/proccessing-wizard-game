package gremlins;

import java.util.ArrayList;

public class Wizard {

    private static final int WIZARD_STEPS = 20;
    private static final int FIRE_SPEED = 4;
    private final float startColumn;
    private final float startRow;
    private float column;
    private float row;
    private String direction;
    private float fireColumn;
    private float fireRow;
    private boolean firePermit;
    private String fireDirection;

    /**
     * Constructor of the Wizard class to be instantiated based on input map
     * @param column is the initial column coordination of wizard
     * @param row is the initial row coordination of wizard
     */
    public Wizard(float column, float row) {
        this.column = column;
        this.startColumn = column;
        this.startRow = row;
        this.row = row;
        this.direction ="RIGHT";
    }

    /**
     * Using this method wizard moves in direction for about 20 steps
     * @param direction is the direction of wizard's fire
     */
    public void move(String direction){
        if(direction.equals("RIGHT"))
            this.column+=WIZARD_STEPS;
        if(direction.equals("LEFT"))
            this.column-=WIZARD_STEPS;
        if(direction.equals("UP"))
            this.row-=WIZARD_STEPS;
        if(direction.equals("DOWN"))
            this.row+=WIZARD_STEPS;
        this.direction = direction;
    }

    /**
     * Based on this method fire permit is set to ON and the direction of the wizard is set for the fireballs
     * which has been preceded in key press
     */
    public void fire(){
        this.firePermit = true;
        this.fireDirection = direction;
        this.fireColumn = this.column;
        this.fireRow = this.row;
    }

    /**
     * This method detects the obstacles and commands to continue the direction or nor for the fire of wizard.
     * @param stoneWalls is the coordination of stone walls
     * @param brickWalls is the coordination of brick walls
     * @param gremlins is the current coordination of gremlins
     */
    public void firing(ArrayList<StoneWall> stoneWalls, ArrayList<BrickWall> brickWalls, ArrayList<Gremlin> gremlins) {
        switch (fireDirection){
            case "UP":
                if (isFireFree("UP", stoneWalls, brickWalls, gremlins)) {
                    this.fireRow -= FIRE_SPEED;
                }else{
                    this.firePermit=false;
                }
                break;
            case "DOWN":
                if (isFireFree("DOWN", stoneWalls, brickWalls, gremlins)) {
                    this.fireRow += FIRE_SPEED;
                }else{
                    this.firePermit=false;
                }
                break;
            case "RIGHT":
                if (isFireFree("RIGHT", stoneWalls, brickWalls, gremlins)) {
                    this.fireColumn += FIRE_SPEED;
                }else{
                    this.firePermit=false;
                }
                break;
            case "LEFT":
                if (isFireFree("LEFT", stoneWalls, brickWalls, gremlins)) {
                    this.fireColumn -= FIRE_SPEED;
                }else{
                    this.firePermit=false;
                }
                break;
        }
    }

    /**
     * Using this method wizard's fireball gets assured that the next 10 pixels are free to continue moving
     * @param direction is the direction of wizard's fire
     * @param stoneWalls is the coordination of stone walls
     * @param brickWalls is the coordination of brick walls
     * @param gremlins is the current coordination of gremlins
     * @return the status obstacle/collision in next steps
     */
    public boolean isFireFree(String direction, ArrayList<StoneWall> stoneWalls, ArrayList<BrickWall> brickWalls, ArrayList<Gremlin> gremlins){
        // Check whether obstacle is in the next step
        int steps;
        for (int i = 0; i < 10 ; i++) {
            steps = i;
            switch (direction) {
                case "UP":
                    for (BrickWall brickWall : brickWalls)
                        if (this.fireRow - steps == brickWall.getRow() && this.fireColumn == brickWall.getColumn()) {
                            if(brickWall.getStatus()>0)
                                brickWall.setStatus(0);
                            return false;
                        }
                    for (StoneWall stoneWall : stoneWalls)
                        if (this.fireRow - steps == stoneWall.getRow() && this.fireColumn == stoneWall.getColumn()) {
                            return false;
                        }
                    for (int j = 0; j <gremlins.size() ; j++){
                        if (this.fireRow - steps == gremlins.get(j).getFireRow() && this.fireColumn == gremlins.get(j).getFireColumn()){
                            gremlins.get(j).setFirePermit(false);
                            this.firePermit=false;
                        }
                            if (this.fireRow - steps == gremlins.get(j).getRow() && this.fireColumn == gremlins.get(j).getColumn()) {
                                gremlins.get(j).disappear(stoneWalls, brickWalls);
                                return false;
                            }
                    }
                    break;
                case "DOWN":
                    for (BrickWall brickWall : brickWalls)
                        if (this.fireRow + steps == brickWall.getRow() && this.fireColumn == brickWall.getColumn()) {
                            if(brickWall.getStatus()>0)
                                brickWall.setStatus(0);
                            return false;
                        }
                    for (StoneWall stoneWall : stoneWalls)
                        if (this.fireRow + steps == stoneWall.getRow() && this.fireColumn == stoneWall.getColumn()) {
                            return false;
                        }
                    for (int j = 0; j <gremlins.size() ; j++) {
                        if (this.fireRow - steps == gremlins.get(j).getFireRow() && this.fireColumn == gremlins.get(j).getFireColumn()){
                            gremlins.get(j).setFirePermit(false);
                            this.firePermit=false;
                        }
                        if (this.fireRow + steps == gremlins.get(j).getRow() && this.fireColumn == gremlins.get(j).getColumn()) {
                            gremlins.get(j).disappear(stoneWalls, brickWalls);
                            return false;
                        }
                    }
                    break;
                case "RIGHT":
                    for (BrickWall brickWall : brickWalls)
                        if (this.fireColumn + steps == brickWall.getColumn() && this.fireRow == brickWall.getRow()) {
                            if(brickWall.getStatus()>0)
                                brickWall.setStatus(0);
                            return false;
                        }
                    for (StoneWall stoneWall : stoneWalls)
                        if (this.fireColumn + steps == stoneWall.getColumn() && this.fireRow == stoneWall.getRow()) {
                            return false;
                        }
                    for (int j = 0; j <gremlins.size() ; j++) {
                        if (this.fireRow - steps == gremlins.get(j).getFireRow() && this.fireColumn == gremlins.get(j).getFireColumn()){
                            gremlins.get(j).setFirePermit(false);
                            this.firePermit=false;
                        }
                        if (this.fireColumn + steps == gremlins.get(j).getColumn() && this.fireRow == gremlins.get(j).getRow()) {
                            gremlins.get(j).disappear(stoneWalls, brickWalls);
                            return false;
                        }
                    }
                    break;
                case "LEFT":
                    for (BrickWall brickWall : brickWalls)
                        if (this.fireColumn - steps == brickWall.getColumn() && this.fireRow == brickWall.getRow()) {
                            if(brickWall.getStatus()>0)
                                brickWall.setStatus(0);
                            return false;
                        }
                    for (StoneWall stoneWall : stoneWalls)
                        if (this.fireColumn - steps == stoneWall.getColumn() && this.fireRow == stoneWall.getRow()) {
                            return false;
                        }
                    for (int j = 0; j <gremlins.size() ; j++) {
                        if (this.fireRow - steps == gremlins.get(j).getFireRow() && this.fireColumn == gremlins.get(j).getFireColumn()) {
                            gremlins.get(j).setFirePermit(false);
                            this.firePermit=false;
                        }
                        if (this.fireColumn - steps == gremlins.get(j).getColumn() && this.fireRow == gremlins.get(j).getRow()) {
                            gremlins.get(j).disappear(stoneWalls, brickWalls);
                            return false;
                        }
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * Getter method to get the column of the coordination of wizard
     * @return the amount of column
     */
    public float getColumn() {
        return column;
    }

    /**
     * Setter method column of coordination of the wizard
     * @param column to enter the amount of column field
     */
    public void setColumn(float column) {
        this.column = column;
    }

    /**
     * Getter method to get the row of the coordination of wizard
     * @return the amount of row
     */
    public float getRow() {
        return row;
    }

    /**
     * Setter method row of coordination of the wizard
     * @param row to enter the amount of row field
     */
    public void setRow(float row) {
        this.row = row;
    }

    /**
     * Getter method for direction field
     * @return the amount of direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Getter method to get the column of starting point of wizard
     * @return the amount of startColumn
     */
    public float getStartColumn() {
        return startColumn;
    }

    /**
     * Getter method to get the row of starting point of wizard
     * @return the amount of startRow
     */
    public float getStartRow() {
        return startRow;
    }

    /**
     * Getter method to check the permit to fire or not
     * @return the amount of firePermit
     */
    public boolean isFirePermit() {
        return firePermit;
    }

    /**
     * Getter method for fireRow field
     * @return the amount of fireRow
     */
    public float getFireRow() {
        return fireRow;
    }

     /**
     * Getter method for fireColumn field
     * @return the amount of fireColumn
     */
    public float getFireColumn() {
        return fireColumn;
    }

}
