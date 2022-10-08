package gremlins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoneWallTest {

    private StoneWall stoneWall;

    @BeforeEach
    void setUp() {
        stoneWall = new StoneWall(400,250);
    }

    @Test
    void getColumn() {
        assertEquals(400,stoneWall.getColumn());
    }

    @Test
    void getRow() {
        assertEquals(250,stoneWall.getRow());
    }
}