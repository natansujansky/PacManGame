package javacourse.pacman.model.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javacourse.pacman.model.level.LevelCellComponent;
import javacourse.pacman.model.level.LevelLayout;

/**
 * Class that connects level layout, game properties and sprites into a model of
 * Pac-Man game. It will represents the game state, allow the usre to fetch
 * information like whether it is game over, what is the current score or to
 * perform operations like game restart, re-initialize the model for a new
 * level, etc.
 *
 * @author esunata
 *
 */
public class PacManModel {

    /**
     * LevelLayout variable that describes the level used in the game
     */
    private LevelLayout levelLayout;

    /**
     * This map is used to keep track of the static (non-moving) elements of the
     * level
     */
    private LevelCellComponent[][] levelMap;

    private boolean isGameOver;

    private boolean isPlayerVictorious;

    /**
     * Pac-Man sprite currently present in the game
     */
    private PacManSprite pacMan;

    /**
     * List of ghost sprites currently present in the game
     */
    private List<GhostSprite> ghosts;

    private int numOfBigDots;

    private int numOfSmallDots;

    private long score;

    private static final int SMALL_DOT_POINTS = 10;
    private static final int BIG_DOT_POINTS = 50;
    private static final int EAT_GHOST_POINTS = 100;

    private static final int PACMAN_POWER_TIME_MS = 8_000;
    private static final int GHOST_DEAD_TIME_MS = 12_000;

    /**
     * Constructor for the PacManModel class
     */
    public PacManModel() {
    }

    /**
     * Method that initializes the new game using the provided level layout
     *
     * @param levelLayout object used as descriptor of level layout
     */
    public void initializeNewGame(LevelLayout levelLayout) {
        this.levelLayout = levelLayout;
        restartGame();
    }

    /**
     * Method that re-initializes the game using the current level layout
     *
     * @return LevelLayout object used for game setup
     * @throws IllegalStateException in case there hasn't been a level layout
     *                               provided yet
     */
    public LevelLayout restartGame() {
        if (levelLayout == null) {
            throw new IllegalStateException("No level layout defined yet!");
        }
        this.isGameOver = false;
        this.isPlayerVictorious = false;
        this.score = 0;
        this.numOfBigDots = 0;
        this.numOfSmallDots = 0;
        this.ghosts = new ArrayList<>();
        int levelHeight = levelLayout.getLevelHeight();
        int levelWidth = levelLayout.getLevelWidth();
        levelMap = new LevelCellComponent[levelHeight][levelWidth];
        for (int i = 0; i < levelHeight; i++) {
            for (int j = 0; j < levelWidth; j++) {
                levelMap[i][j] = levelLayout.getComponent(i, j);
                switch (levelLayout.getComponent(i, j)) {
                case PACMAN_START:
                    pacMan = SpriteFactory.createPacMan("Pac-Man", PACMAN_POWER_TIME_MS, i, j, levelLayout);
                    break;
                case GHOST_START:
                    GhostSprite ghost = SpriteFactory.createGhost("Ghost", GHOST_DEAD_TIME_MS, i, j, levelLayout);
                    ghosts.add(ghost);
                    break;
                case BIG_DOT:
                    numOfBigDots++;
                    break;
                case SMALL_DOT:
                    numOfSmallDots++;
                    break;
                default:
                    break;
                }
            }
        }
        return levelLayout;
    }

    /**
     * Getter method for the current levelLayout
     *
     * @return LevelLayout object that is currently used in the game
     * @throws IllegalStateException in case there hasn't been a level layout
     *                               provided yet
     */
    public LevelLayout getLevelLayout() {
        if (levelLayout == null) {
            throw new IllegalStateException("No level layout defined yet!");
        } else {
            return levelLayout;
        }
    }

    /**
     * Method used for fetching current component at the specified location in the
     * level map
     *
     * @param rowIndex    first coordinate of the position
     * @param columnIndex second coordinate of the position
     * @return LevelCellComponent enum value of the component on the specified
     *         location
     * @throws IllegalStateException     in case there hasn't been a level layout
     *                                   provided yet
     * @throws IndexOutOfBoundsException in case given index is out of bounds of the
     *                                   level layout
     */
    public LevelCellComponent componentAt(int rowIndex, int columnIndex) {
        if (levelLayout == null) {
            throw new IllegalStateException("No level layout defined yet!");
        }
        if (rowIndex < 0 || rowIndex >= levelLayout.getLevelHeight()) {
            throw new IndexOutOfBoundsException("rowIndex out of bounds!");
        }
        if (columnIndex < 0 || columnIndex >= levelLayout.getLevelWidth()) {
            throw new IndexOutOfBoundsException("columnIndex out of bounds!");
        }
        return levelMap[rowIndex][columnIndex];
    }

    /**
     * Getter method for the Pac-Man sprite
     *
     * @return PacManSprite object that is currently used in the game
     * @throws IllegalStateException in case there hasn't been a Pac-Man defined yet
     */
    public PacManSprite getPacMan() {
        if (pacMan == null) {
            throw new IllegalStateException("No Pac-Man defined yet!");
        } else {
            return pacMan;
        }
    }

    /**
     * Getter method for the list of ghost sprites
     *
     * @return list of ghost sprites currently used in the game
     * @throws IllegalStateException in case there hasn't been any ghosts defined
     *                               yet
     */
    public List<GhostSprite> getGhosts() {
        if (ghosts == null) {
            throw new IllegalStateException("No ghosts defined yet!");
        } else {
            return List.copyOf(ghosts);
        }
    }

    /**
     * Getter method for the isGameOver flag value
     *
     * @return true is game is over, false otherwise
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Getter method for the isPlayerVictorious flag value
     *
     * @return true is player is victorious, false otherwise
     */
    public boolean isPlayerVictorious() {
        return isPlayerVictorious;
    }

    /**
     * Method that is used for updating the entire Pac-Man game model. One call of
     * this method equals one iteration of game model update. If the game is over
     * upon method call, it does nothing.
     *
     * @param desiredPacManMovementDirection MovementDirection enum value
     * @throws IllegalStateException in case there hasn't been a level layout
     *                               provided yet
     */
    public void updateGameStatus(MovementDirection desiredPacManMovementDirection) {
        if (levelLayout == null) {
            throw new IllegalStateException("No level layout defined yet!");
        }
        if (isGameOver) {
            return;
        } else {
            pacMan.preMovementUpdate();
            for (GhostSprite ghost : ghosts) {
                ghost.preMovementUpdate();
            }
            levelMap[pacMan.getRowIndex()][pacMan.getColumnIndex()] = LevelCellComponent.EMPTY;
            pacMan.pacmanMove(desiredPacManMovementDirection, getValidMovementDirections(pacMan));
            for (GhostSprite ghost : ghosts) {
                checkGhostCollisions(ghost);
                if (isGameOver) {
                    return;
                }
            }
            if (levelMap[pacMan.getRowIndex()][pacMan.getColumnIndex()] == LevelCellComponent.BIG_DOT) {
                numOfBigDots--;
                score += BIG_DOT_POINTS;
                pacMan.toggleGhostEatingMode();
            }
            if (levelMap[pacMan.getRowIndex()][pacMan.getColumnIndex()] == LevelCellComponent.SMALL_DOT) {
                numOfSmallDots--;
                score += SMALL_DOT_POINTS;
            }
            if (numOfBigDots == 0 && numOfSmallDots == 0) {
                isGameOver = true;
                isPlayerVictorious = true;
                return;
            }
            for (GhostSprite ghost : ghosts) {
                if (!ghost.isGhostDead()) {
                    ghost.ghostMove(getValidMovementDirections(ghost), getDirectionTowardsPacMan(ghost),
                            pacMan.isGhostEatingActive());
                    checkGhostCollisions(ghost);
                    if (isGameOver) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * Getter method for the game score
     *
     * @return current game score long value
     */
    public long getGameScore() {
        if (levelLayout == null) {
            throw new IllegalStateException("No level layout defined yet!");
        }
        return score;
    }

    /**
     * Helper method that checks for collisions between Pac-Man and given ghost and
     * updates game state accordingly
     *
     * @param ghost given GhostSprite object
     */
    private void checkGhostCollisions(GhostSprite ghost) {
        if (pacMan.hasSameCoordinates(ghost) && !ghost.isGhostDead()) {
            if (pacMan.isGhostEatingActive()) {
                ghost.killGhost();
                score += EAT_GHOST_POINTS;
            } else {
                isGameOver = true;
            }
        }
    }

    /**
     * Helper method used for calculation of valid movement directions respective to
     * the given sprite current position
     *
     * @param sprite Sprite object for which valid movement directions are requested
     * @return set of MovementDirection enum values that are valid for given
     *         sprite's current position in the level layout
     */
    private Set<MovementDirection> getValidMovementDirections(Sprite sprite) {
        Set<MovementDirection> validDirections = new HashSet<>();
        int wrappedRow;
        int wrappedColumn;
        wrappedRow = sprite.getRowIndex() - 1 < 0 ? levelLayout.getLevelHeight() - 1 : sprite.getRowIndex() - 1;
        if (!levelLayout.getComponent(wrappedRow, sprite.getColumnIndex()).equals(LevelCellComponent.WALL)) {
            validDirections.add(MovementDirection.UP);
        }
        wrappedRow = sprite.getRowIndex() + 1 >= levelLayout.getLevelHeight() ? 0 : sprite.getRowIndex() + 1;
        if (!levelLayout.getComponent(wrappedRow, sprite.getColumnIndex()).equals(LevelCellComponent.WALL)) {
            validDirections.add(MovementDirection.DOWN);
        }
        wrappedColumn = sprite.getColumnIndex() - 1 < 0 ? levelLayout.getLevelWidth() - 1 : sprite.getColumnIndex() - 1;
        if (!levelLayout.getComponent(sprite.getRowIndex(), wrappedColumn).equals(LevelCellComponent.WALL)) {
            validDirections.add(MovementDirection.LEFT);
        }
        wrappedColumn = sprite.getColumnIndex() + 1 >= levelLayout.getLevelWidth() ? 0 : sprite.getColumnIndex() + 1;
        if (!levelLayout.getComponent(sprite.getRowIndex(), wrappedColumn).equals(LevelCellComponent.WALL)) {
            validDirections.add(MovementDirection.RIGHT);
        }
        return validDirections;
    }

    /**
     * Helper method that fetches the direction of PacMan respective to the given
     * ghost, in case there are no walls between them
     *
     * @param ghost provided GhostSprite object for which PacMan direction is
     *              requested
     * @return MovementDirection enum value denoting the direction of PacMan if
     *         visible to the given ghost, otherwise NONE
     * @throws IllegalStateException in case method is called when PacMan and given
     *                               ghost are sharing the same coordinates
     */
    private MovementDirection getDirectionTowardsPacMan(GhostSprite ghost) {
        if (ghost.hasSameCoordinates(pacMan)) {
            return MovementDirection.NONE;
        }
        int lesserIndex;
        int greaterIndex;
        boolean isWallBetween = false;
        if (ghost.getRowIndex() == pacMan.getRowIndex()) {
            lesserIndex = Math.min(ghost.getColumnIndex(), pacMan.getColumnIndex());
            greaterIndex = Math.max(ghost.getColumnIndex(), pacMan.getColumnIndex());
            for (int i = lesserIndex; i < greaterIndex; i++) {
                if (levelLayout.getComponent(ghost.getRowIndex(), i).equals(LevelCellComponent.WALL)) {
                    isWallBetween = true;
                    break;
                }
            }
            if (!isWallBetween) {
                if (lesserIndex == ghost.getColumnIndex()) {
                    return MovementDirection.RIGHT;
                } else {
                    return MovementDirection.LEFT;
                }
            }
        } else if (ghost.getColumnIndex() == pacMan.getColumnIndex()) {
            lesserIndex = Math.min(ghost.getRowIndex(), pacMan.getRowIndex());
            greaterIndex = Math.max(ghost.getRowIndex(), pacMan.getRowIndex());
            for (int i = lesserIndex; i < greaterIndex; i++) {
                if (levelLayout.getComponent(i, ghost.getColumnIndex()).equals(LevelCellComponent.WALL)) {
                    isWallBetween = true;
                    break;
                }
            }
            if (!isWallBetween) {
                if (lesserIndex == ghost.getRowIndex()) {
                    return MovementDirection.DOWN;
                } else {
                    return MovementDirection.UP;
                }
            }
        }
        return MovementDirection.NONE;
    }
}
