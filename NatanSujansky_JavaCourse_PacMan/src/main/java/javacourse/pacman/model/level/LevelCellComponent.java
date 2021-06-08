package javacourse.pacman.model.level;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Used to represent a cell in the PacMan game level.
 *
 * Note that all cells types except WALL are a combination of EMPTY cell with an
 * additional entity (like dot or ghost).
 */
public enum LevelCellComponent {
    /**
     * Denotes an empty space where PacMan and ghosts can move.
     */
    EMPTY("E"),
    /**
     * Denotes a space containing a small dot.
     */
    SMALL_DOT("S"),
    /**
     * Denotes a space containing a big dot.
     */
    BIG_DOT("B"),
    /**
     * Denotes a wall. Neither PacMan nor ghost can move past the wall.
     */
    WALL("W"),
    /**
     * Denotes ghost starting position.
     */
    GHOST_START("G"),
    /**
     * Denotes PacMan starting position.
     */
    PACMAN_START("P");

    /**
     * String representation of the cell component.
     *
     * E.g. WALL will be represented by "W" in the level configuration file.
     */
    public final String cellValue;

    /**
     * Default LevelCellComponent constructor.
     *
     * @param cellValue string representation of the cell
     */
    LevelCellComponent(String cellValue) {
        this.cellValue = cellValue;
    }

    /**
     * Used to get LevelCellComponent corresponding to the specified cell value.
     *
     * @param cellValue cell value as string
     * @return cell value corresponding to the given string
     *
     * @throws IllegalArgumentException if cellValue string is invalid
     */
    public static LevelCellComponent fromCellValue(String cellValue) {
        return Arrays.stream(values()).filter(cellVal -> cellVal.cellValue.equals(cellValue)).findFirst().orElseThrow(
                () -> new IllegalArgumentException(cellValue + " is not a valid level cell component value."));
    }

    /**
     * @return Set of valid character cell components.
     */
    public static Set<Character> getValidStringComponents() {
        return Arrays.stream(values()).map(c -> c.cellValue.charAt(0)).collect(Collectors.toUnmodifiableSet());
    }
}