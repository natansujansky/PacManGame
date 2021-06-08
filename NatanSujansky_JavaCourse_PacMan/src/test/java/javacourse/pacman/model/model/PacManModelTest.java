package javacourse.pacman.model.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javacourse.pacman.model.level.LevelCellComponent;
import javacourse.pacman.model.level.LevelLayout;
import javacourse.pacman.model.model.GhostSprite;
import javacourse.pacman.model.model.MovementDirection;
import javacourse.pacman.model.model.PacManModel;
import javacourse.pacman.model.model.PacManSprite;
import javacourse.pacman.model.model.SpriteFactory;

public class PacManModelTest {

    private PacManModel testModel;
    private LevelLayout layoutMock;
    private static MockedStatic<SpriteFactory> factoryMock;
    private PacManSprite pacManMock;
    private GhostSprite ghostMock;

    /**
     * Mock static methods in SpriteFactory
     */
    @BeforeAll
    public static void initStaticMock() {
        factoryMock = Mockito.mockStatic(SpriteFactory.class);
    }

    /**
     * Deregister static mock
     */
    @AfterAll
    public static void closeStaticMock() {
        factoryMock.close();
    }

    /**
     * Initialize the PacManModel and required mocks
     */
    @BeforeEach
    public void initLevel() {
        testModel = new PacManModel();

        layoutMock = Mockito.mock(LevelLayout.class);
        Mockito.when(layoutMock.getLevelHeight()).thenReturn(3);
        Mockito.when(layoutMock.getLevelWidth()).thenReturn(3);
        Mockito.when(layoutMock.getComponent(0, 0)).thenReturn(LevelCellComponent.WALL);
        Mockito.when(layoutMock.getComponent(0, 1)).thenReturn(LevelCellComponent.PACMAN_START);
        Mockito.when(layoutMock.getComponent(0, 2)).thenReturn(LevelCellComponent.WALL);
        Mockito.when(layoutMock.getComponent(1, 0)).thenReturn(LevelCellComponent.EMPTY);
        Mockito.when(layoutMock.getComponent(1, 1)).thenReturn(LevelCellComponent.BIG_DOT);
        Mockito.when(layoutMock.getComponent(1, 2)).thenReturn(LevelCellComponent.SMALL_DOT);
        Mockito.when(layoutMock.getComponent(2, 0)).thenReturn(LevelCellComponent.WALL);
        Mockito.when(layoutMock.getComponent(2, 1)).thenReturn(LevelCellComponent.GHOST_START);
        Mockito.when(layoutMock.getComponent(2, 2)).thenReturn(LevelCellComponent.WALL);

        pacManMock = Mockito.mock(PacManSprite.class);
        ghostMock = Mockito.mock(GhostSprite.class);
        Mockito.when(SpriteFactory.createPacMan("Pac-Man", 8_000, 0, 1, layoutMock)).thenReturn(pacManMock);
        Mockito.when(SpriteFactory.createGhost("Ghost", 12_000, 2, 1, layoutMock)).thenReturn(ghostMock);
    }

    /**
     * Test for the game restart without provided layout
     */
    @Test
    public void startWithoutLayout() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> testModel.restartGame());
        assertEquals("No level layout defined yet!", exception.getMessage());

        exception = assertThrows(IllegalStateException.class, () -> testModel.getLevelLayout());
        assertEquals("No level layout defined yet!", exception.getMessage());

        exception = assertThrows(IllegalStateException.class, () -> testModel.componentAt(0, 0));
        assertEquals("No level layout defined yet!", exception.getMessage());

        exception = assertThrows(IllegalStateException.class, () -> testModel.getPacMan());
        assertEquals("No Pac-Man defined yet!", exception.getMessage());

        exception = assertThrows(IllegalStateException.class, () -> testModel.getGhosts());
        assertEquals("No ghosts defined yet!", exception.getMessage());

        exception = assertThrows(IllegalStateException.class, () -> testModel.updateGameStatus(MovementDirection.DOWN));
        assertEquals("No level layout defined yet!", exception.getMessage());
    }

    /**
     * Test for the correct game initialization
     */
    @Test
    public void startWithLayout() {
        testModel.initializeNewGame(layoutMock);
        Mockito.verify(layoutMock).getLevelHeight();
        Mockito.verify(layoutMock).getLevelWidth();

        assertEquals(layoutMock, testModel.restartGame());
        assertEquals(layoutMock, testModel.getLevelLayout());
        assertFalse(testModel.isGameOver());
        assertFalse(testModel.isPlayerVictorious());
        assertEquals(LevelCellComponent.PACMAN_START, testModel.componentAt(0, 1));
        assertEquals(LevelCellComponent.GHOST_START, testModel.componentAt(2, 1));
        assertEquals(LevelCellComponent.SMALL_DOT, testModel.componentAt(1, 2));
        assertEquals(LevelCellComponent.WALL, testModel.componentAt(2, 0));
        assertEquals(LevelCellComponent.BIG_DOT, testModel.componentAt(1, 1));
        assertEquals(LevelCellComponent.EMPTY, testModel.componentAt(1, 0));

        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
                () -> testModel.componentAt(-1, 1));
        assertEquals("rowIndex out of bounds!", exception.getMessage());

        exception = assertThrows(IndexOutOfBoundsException.class, () -> testModel.componentAt(0, 4));
        assertEquals("columnIndex out of bounds!", exception.getMessage());

        assertEquals(pacManMock, testModel.getPacMan());
        List<GhostSprite> testList = new ArrayList<>();
        testList.add(ghostMock);
        assertEquals(testList, testModel.getGhosts());
    }

    /**
     * Test for updateGameStatus method
     */
    @Test
    public void updateGameStatusTest() {
        testModel.initializeNewGame(layoutMock);
        assertEquals(LevelCellComponent.BIG_DOT, testModel.componentAt(1, 1));
        Set<MovementDirection> movementSet = new HashSet<>();
        Mockito.when(pacManMock.getRowIndex()).thenReturn(0, 1);
        Mockito.when(pacManMock.getColumnIndex()).thenReturn(1, 1);
        movementSet.add(MovementDirection.DOWN);
        movementSet.add(MovementDirection.UP);
        Mockito.doAnswer(invocation -> {
            pacManMock.setPosition(1, 1);
            return null;
        }).when(pacManMock).pacmanMove(MovementDirection.DOWN, movementSet);
        Mockito.when(pacManMock.hasSameCoordinates(ghostMock)).thenReturn(false);
        Mockito.when(ghostMock.getRowIndex()).thenReturn(2);
        Mockito.when(ghostMock.getColumnIndex()).thenReturn(1);
        Mockito.when(ghostMock.hasSameCoordinates(pacManMock)).thenReturn(false);
        testModel.updateGameStatus(MovementDirection.DOWN);

        Mockito.verify(pacManMock).preMovementUpdate();
        Mockito.verify(ghostMock).preMovementUpdate();
        Mockito.verify(pacManMock).toggleGhostEatingMode();
        Mockito.verify(ghostMock).ghostMove(movementSet, MovementDirection.UP, false);

        assertEquals(LevelCellComponent.EMPTY, testModel.componentAt(0, 1));
        assertEquals(LevelCellComponent.BIG_DOT, testModel.componentAt(1, 1));
    }
}
