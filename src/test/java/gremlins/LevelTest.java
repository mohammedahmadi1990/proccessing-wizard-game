package gremlins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    Level level;
    String layout;
    float wizardCoolDown;
    float enemyCoolDown;
    char[][] map;

    @BeforeEach
    void setUp() {
        layout = "level1.txt";
        wizardCoolDown = (float) 0.3333;
        enemyCoolDown = 3;
        map = new char[33][36];
        try (BufferedReader br = new BufferedReader(new FileReader(layout))) {
            String line;
            int r = 0;
            while ((line = br.readLine()) != null) {
                for (int c = 0; c < 36; c++)
                    map[r][c] = line.charAt(c);
                r++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        level = new Level(layout,wizardCoolDown,enemyCoolDown,map);
    }

    @Test
    void getWizardCoolDown() {
        assertEquals(wizardCoolDown,level.getWizardCoolDown());
    }

    @Test
    void getEnemyCoolDown() {
        assertEquals(enemyCoolDown,level.getEnemyCoolDown());
    }

    @Test
    void getMap() {
        assertArrayEquals(map,level.getMap());
    }
}