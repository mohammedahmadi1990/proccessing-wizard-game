package gremlins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WizardTest {

    Wizard wizard;
    ArrayList<Gremlin> gremlins;
    ArrayList<BrickWall> brickWalls;
    ArrayList<StoneWall> stoneWalls;
    int column;
    int row;

    @BeforeEach
    void setUp() {
        column = 200;
        row = 300;
        wizard = new Wizard(column, row);
        brickWalls = new ArrayList<>();
        stoneWalls = new ArrayList<>();
        gremlins = new ArrayList<>();
    }

    @Test
    void move() {
        int stepSize = 20;
        wizard.move("UP");
        assertEquals(row -stepSize,wizard.getRow());

        wizard.move("DOWN");
        assertEquals(row,wizard.getRow());

        wizard.move("RIGHT");
        assertEquals(column +stepSize,wizard.getColumn());

        wizard.move("LEFT");
        assertEquals(column,wizard.getColumn());
    }

    @Test
    void fire() {
        wizard.fire();
        assertTrue(wizard.isFirePermit());
        assertEquals(wizard.getFireColumn(),wizard.getColumn());
        assertEquals(wizard.getFireRow(),wizard.getRow());
    }

    @Test
    void firing() {
        assertEquals(0,wizard.getFireRow());
        assertEquals(0,wizard.getFireColumn());

        wizard.fire();
        assertEquals(row,wizard.getFireRow());
        assertEquals(column,wizard.getColumn());

    }

    @Test
    void isFireFree() {
        assertTrue(wizard.isFireFree("RIGHT",stoneWalls,brickWalls,gremlins));
        assertTrue(wizard.isFireFree("LEFT",stoneWalls,brickWalls,gremlins));
        assertTrue(wizard.isFireFree("UP",stoneWalls,brickWalls,gremlins));
        assertTrue(wizard.isFireFree("DOWN",stoneWalls,brickWalls,gremlins));

        brickWalls.add(new BrickWall(101,101,4));
        stoneWalls.add(new StoneWall(201,300));
        fire();
        assertFalse(wizard.isFireFree("RIGHT",stoneWalls,brickWalls,gremlins));
        assertTrue(wizard.isFireFree("LEFT",stoneWalls,brickWalls,gremlins));
    }

    @Test
    void getColumn() {
        assertEquals(column,wizard.getColumn());
    }

    @Test
    void setColumn() {
        int value = 100;
        wizard.setColumn(value);
        assertEquals(value,wizard.getColumn());
    }

    @Test
    void getRow() {
        assertEquals(row,wizard.getRow());
    }

    @Test
    void setRow() {
        int value = 458;
        wizard.setRow(value);
        assertEquals(value,wizard.getRow());
    }

    @Test
    void getDirection() {
        String dir = "LEFT";
        wizard.move(dir);
        assertEquals(dir,wizard.getDirection());
    }

    @Test
    void getStartColumn() {
        assertEquals(column,wizard.getStartColumn());
    }

    @Test
    void getStartRow() {
        assertEquals(row,wizard.getStartRow());
    }

    @Test
    void isFirePermit() {
        assertFalse(wizard.isFirePermit());
        fire();
        assertTrue(wizard.isFirePermit());
    }

    @Test
    void getFireRow() {
        assertNotEquals(100,wizard.getFireRow());
        wizard.fire();
        assertEquals(row,wizard.getFireRow());
        wizard.move("UP");
        wizard.fire();
        assertEquals(300-20,wizard.getFireRow());
    }

    @Test
    void getFireColumn() {
        assertNotEquals(100,wizard.getFireColumn());
        wizard.fire();
        assertEquals(column,wizard.getFireColumn());
        wizard.move("LEFT");
        wizard.move("UP");
        wizard.move("UP");
        wizard.move("LEFT");
        wizard.fire();
        assertEquals(column-20-20,wizard.getFireColumn());
    }
}