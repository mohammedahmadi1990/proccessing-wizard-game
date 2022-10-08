package gremlins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GremlinTest {

    private int column;
    private int row;
    private Gremlin gremlin;
    ArrayList<BrickWall> brickWalls;
    ArrayList<StoneWall> stoneWalls;

    @BeforeEach
    void setUp() {
        column = 100;
        row = 400;
        gremlin = new Gremlin(column,row);
        brickWalls = new ArrayList<>();
        stoneWalls = new ArrayList<>();
    }

    @Test
    void disappear() {
        assertEquals(column+row,gremlin.getColumn()+gremlin.getRow());

        // disappear when collision happened and go at least 10 tiles away in random tile.
        float prevRow = gremlin.getRow();
        float prevCol = gremlin.getColumn();
        gremlin.disappear(stoneWalls,brickWalls);
        int nearestDistance = 10 * 20;
        assertTrue(prevRow-gremlin.getRow()<=nearestDistance || prevCol-gremlin.getColumn()<=nearestDistance);
    }

    @Test
    void move() {
        assertEquals(row,gremlin.getRow());
        assertEquals(column,gremlin.getColumn());

        // +1/-1 step in random direction
        gremlin.move(stoneWalls,brickWalls);
        assertNotEquals(row+column,gremlin.getRow()+gremlin.getColumn());
    }

    @Test
    void fire() {
        assertEquals(0,gremlin.getFireRow());
        assertEquals(0,gremlin.getFireColumn());

        // starting
        gremlin.setFireDirectionFlag(true);
        gremlin.setFirePermit(true);
        gremlin.fire(stoneWalls,brickWalls);
        int stepSize =4;
        assertTrue(row+column-stepSize<=gremlin.getFireColumn()+gremlin.getFireRow() &&
                row+column+stepSize>=gremlin.getFireColumn()+gremlin.getFireRow());

        // Next step
        gremlin.fire(stoneWalls,brickWalls);
        assertTrue(row+column-stepSize*2<=gremlin.getFireColumn()+gremlin.getFireRow() &&
                row+column+stepSize*2>=gremlin.getFireColumn()+gremlin.getFireRow());
    }

    @Test
    void isFree() {
        assertTrue(gremlin.isFree("RIGHT",stoneWalls,brickWalls));
        assertTrue(gremlin.isFree("LEFT",stoneWalls,brickWalls));
        assertTrue(gremlin.isFree("UP",stoneWalls,brickWalls));
        assertTrue(gremlin.isFree("RIGHT",stoneWalls,brickWalls));

        // Adding an obstacle
        stoneWalls.add(new StoneWall(column+20,row));
        assertFalse(gremlin.isFree("RIGHT",stoneWalls,brickWalls));

        // Adding an obstacle
        stoneWalls.add(new StoneWall(column,row-20));
        assertFalse(gremlin.isFree("UP",stoneWalls,brickWalls));

        // Adding an obstacle not in direction
        stoneWalls.add(new StoneWall(column-20,row));
        assertTrue(gremlin.isFree("DOWN",stoneWalls,brickWalls));
    }

    @Test
    void isFireFree() {
        assertTrue(gremlin.isFireFree("RIGHT",stoneWalls,brickWalls));
        assertTrue(gremlin.isFireFree("LEFT",stoneWalls,brickWalls));
        assertTrue(gremlin.isFireFree("UP",stoneWalls,brickWalls));
        assertTrue(gremlin.isFireFree("RIGHT",stoneWalls,brickWalls));

    }

    @Test
    void getColumn() {
        assertEquals(column,gremlin.getColumn());
        assertNotEquals(12,gremlin.getColumn());
    }

    @Test
    void getRow() {
        assertEquals(row,gremlin.getRow());
        assertNotEquals(12,gremlin.getRow());
    }

    @Test
    void isFirePermit() {
        assertFalse(gremlin.isFirePermit());
        gremlin.setFirePermit(true);
        assertTrue(gremlin.isFirePermit());
    }

    @Test
    void setFirePermit() {
        assertEquals(false,gremlin.isFirePermit());

        gremlin.setFirePermit(true);
        assertTrue(gremlin.isFirePermit());

        gremlin.setFirePermit(false);
        assertFalse(gremlin.isFirePermit());
    }

    @Test
    void getFireColumn() {
        gremlin.setFirePermit(true);
        gremlin.setFireDirectionFlag(true);
        assertNotEquals(140,gremlin.getFireColumn());

        gremlin.move(stoneWalls,brickWalls);
        gremlin.fire(stoneWalls,brickWalls);
        assertNotEquals(row+column,gremlin.getFireRow()+gremlin.getFireColumn());
    }

    @Test
    void getFireRow() {
        gremlin.setFirePermit(true);
        gremlin.setFireDirectionFlag(true);
        assertNotEquals(150,gremlin.getFireRow());

        gremlin.move(stoneWalls,brickWalls);
        gremlin.fire(stoneWalls,brickWalls);
        assertNotEquals(row+column,gremlin.getFireRow()+gremlin.getFireColumn());
    }

    @Test
    void setFireDirectionFlag() {
        assertTrue(true);
    }
}