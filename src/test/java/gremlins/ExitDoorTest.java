package gremlins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExitDoorTest {

    ExitDoor exitDoor;

    @BeforeEach
    void setUp() {
        exitDoor = new ExitDoor(10,14);
    }

    @Test
    void getColumn() {
        assertEquals(10,exitDoor.getColumn());
    }

    @Test
    void getRow() {
        assertEquals(14,exitDoor.getRow());
    }
}