package javacourse.pacman.model.level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import javacourse.pacman.model.level.LevelCellComponent;
import javacourse.pacman.model.level.LevelLayout;

/**
 * Unit test class for LevelLayout class
 *
 * @author esunata
 *
 */
public class LevelLayoutTest {

    private LevelLayout testLayout;

    /**
     * Test for the normal behaviour of the constructor method
     */
    @Test
    public void contructorTestPositive() {
        try {
            testLayout = new LevelLayout("Level", "/levels/level1.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("Level", testLayout.getLevelName(), "Level name not as expected!");
        assertEquals(21, testLayout.getLevelHeight(), "Level height not as expected!");
        assertEquals(19, testLayout.getLevelWidth(), "Level width not as expected!");
        assertEquals(2, testLayout.getNumberOfGhosts(), "Number of ghosts not as expected!");
        assertEquals(142, testLayout.getNumberOfSmallDots(), "Number of small dots not as expected!");
        assertEquals(4, testLayout.getNumberOfBigDots(), "Number of big dots not as expected!");
        assertEquals(LevelCellComponent.PACMAN_START, testLayout.getComponent(15, 9),
                "Cell component not as expected!");

        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
                () -> testLayout.getComponent(-1, 20));
        assertEquals("rowIndex out of bounds!", exception.getMessage());

        exception = assertThrows(IndexOutOfBoundsException.class, () -> testLayout.getComponent(0, 19));
        assertEquals("columnIndex out of bounds!", exception.getMessage());
    }

    /**
     * Test for constructor method when given level is incorrectly defined
     */
    @Test
    public void contructorTestNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new LevelLayout("Level", "/levels/errorLevel1.txt"));
        assertEquals("Level must be rectangular!", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class,
                () -> new LevelLayout("Level", "/levels/errorLevel2.txt"));
        assertEquals("Level width cannot be less than 10!", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class,
                () -> new LevelLayout("Level", "/levels/errorLevel3.txt"));
        assertEquals("There can only be one Pac-Man!", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class,
                () -> new LevelLayout("Level", "/levels/errorLevel4.txt"));
        assertEquals("Level must define one Pac-Man!", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class,
                () -> new LevelLayout("Level", "/levels/errorLevel5.txt"));
        assertEquals("Level must define at least one ghost!", exception.getMessage());
    }
}
