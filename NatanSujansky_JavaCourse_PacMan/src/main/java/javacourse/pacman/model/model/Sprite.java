package javacourse.pacman.model.model;

import javacourse.pacman.model.level.LevelCellComponent;
import javacourse.pacman.model.level.LevelLayout;

/**
 * Abstract class that implements basic sprite properties such as starting
 * location, current location, movement direction and some methods for movement
 * or checking whether it's sharing the same position as another sprite
 *
 * @author esunata
 *
 */
public abstract class Sprite {

    /**
     * String variable denoting the name of the sprite
     */
    private final String spriteName;

    /**
     * Integer variable denoting the current row sprite is currently located in
     */
    private int rowIndex;

    /**
     * Integer variable denoting the current column sprite is currently located in
     */
    private int columnIndex;

    /**
     * LevelLayout variable that describes the level sprite is moving in
     */
    private final LevelLayout levelLayout;

    /**
     * MovementDirection variable denoting the direction sprite is currently moving
     * in
     */
    private MovementDirection direction;

    /**
     * There are 5 game updates per second
     */
    static final long MILLISECONDS_PER_GAME_UPDATE = 200L;

    /**
     * Constructor for Sprite class
     *
     * @param name                name of the sprite
     * @param startingRowIndex    first coordinate of the starting position
     * @param startingColumnIndex second coordinate of the starting position
     * @param levelLayout         LevelLayout object that describes the level where
     *                            sprite will exist
     * @throws IllegalArgumentException if given position is out of level bounds
     */
    public Sprite(String name, int startingRowIndex, int startingColumnIndex, LevelLayout levelLayout) {
        if (startingRowIndex < 0 || startingRowIndex >= levelLayout.getLevelHeight() || startingColumnIndex < 0
                || startingColumnIndex >= levelLayout.getLevelWidth()) {
            throw new IllegalArgumentException("Given sprite position is out of level bounds!");
        }
        this.spriteName = name;
        this.rowIndex = startingRowIndex;
        this.columnIndex = startingColumnIndex;
        this.levelLayout = levelLayout;
    }

    /**
     * Method that is invoked by the model prior to making the game move
     */
    public abstract void preMovementUpdate();

    /**
     * Getter method for the sprite name
     *
     * @return name of the sprite
     */
    public String getSpriteName() {
        return spriteName;
    }

    /**
     * Getter method for the current row index
     *
     * @return current row sprite is currently located in
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * Getter method for the current column index
     *
     * @return current column sprite is currently located in
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * Getter method for the current direction
     *
     * @return MovementDirection enum value denoting the direction sprite is
     *         currently moving in
     */
    public MovementDirection getMovementDirection() {
        return direction;
    }

    /**
     * Method that checks whether this sprite shares the same coordinates as the
     * given sprite
     *
     * @param other Sprite object
     * @return true if two sprites share the same coordinates, false otherwise
     * @throws IllegalArgumentException if the given sprite is the same as current
     *                                  one
     */
    public boolean hasSameCoordinates(Sprite other) {
        if (other == null) {
            throw new IllegalArgumentException("Provided sprite cannot be null!");
        }
        if (other.equals(this)) {
            throw new IllegalArgumentException("Provided sprite has to be different than the current one!");
        }
        return other.rowIndex == rowIndex && other.columnIndex == columnIndex;
    }

    /**
     * Method that makes the sprint move one cell in the specified movement
     * direction. If sprite is located on the border of the level layout which has
     * no wall, it can teleport to the opposing side of the level if there also
     * isn't a wall there.
     *
     * @param movementDirection enum value denoting the the direction sprite should
     *                          be moving in
     */
    public void move(MovementDirection movementDirection) {
        direction = movementDirection;
        int wrappedRow = rowIndex;
        int wrappedColumn = columnIndex;
        switch (movementDirection) {
        case UP:
            wrappedRow = rowIndex - 1 < 0 ? levelLayout.getLevelHeight() - 1 : rowIndex - 1;
            break;
        case DOWN:
            wrappedRow = rowIndex + 1 >= levelLayout.getLevelHeight() ? 0 : rowIndex + 1;
            break;
        case LEFT:
            wrappedColumn = columnIndex - 1 < 0 ? levelLayout.getLevelWidth() - 1 : columnIndex - 1;
            break;
        case RIGHT:
            wrappedColumn = columnIndex + 1 >= levelLayout.getLevelWidth() ? 0 : columnIndex + 1;
            break;
        default:
            break;
        }
        if (!levelLayout.getComponent(wrappedRow, wrappedColumn).equals(LevelCellComponent.WALL)) {
            rowIndex = wrappedRow;
            columnIndex = wrappedColumn;
        }
    }

    /**
     * Method that makes the sprite move one cell in the direction that sprite is
     * already moving in.
     */
    public void move() {
        move(direction);
    }

    /**
     * Setter method for the position of the sprite
     *
     * @throws IllegalArgumentException if given position is out of level bounds
     */
    protected void setPosition(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= levelLayout.getLevelHeight() || columnIndex < 0
                || columnIndex >= levelLayout.getLevelWidth()) {
            throw new IllegalArgumentException("Given sprite position is out of level bounds!");
        } else {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
        }
    }
}
