package gremlins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrickWallTest {

    private BrickWall brickWall;

    @BeforeEach
    void setUp() {
        brickWall = new BrickWall(10,20,4);
    }

    @Test
    void getColumn() {
        assertEquals(10,brickWall.getColumn());
    }

    @Test
    void getRow() {
        assertEquals(20,brickWall.getRow());
    }

    @Test
    void getStatus() {
        assertEquals(4,brickWall.getStatus());
    }

    @Test
    void setStatus() {
        int status = 45;
        brickWall.setStatus(status);
        assertEquals(status,brickWall.getStatus());
    }
}