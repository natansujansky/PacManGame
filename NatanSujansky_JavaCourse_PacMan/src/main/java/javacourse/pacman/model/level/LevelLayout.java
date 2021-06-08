package javacourse.pacman.model.level;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class that serves as level layout descriptor for the pacman game
 *
 * @author Natan
 *
 */
public final class LevelLayout {

    /**
     * Name of the level
     */
    private final String levelName;

    /**
     * Constant denoting the minimum required level width and height
     */
    private static final int MINIMUM_LEVEL_DIMENSION = 10;

    /**
     * Constant denoting the number of Pac-Men required in the level
     */
    private static final int NUM_OF_PACMEN = 1;

    /**
     * 'Map' of the level in string array format
     */
    private final String[] levelLines;

    /**
     * Width of the level in cells as unit
     */
    private int levelWidth;

    /**
     * Height of the level in cells as unit
     */
    private int levelHeight;

    /**
     * Number of ghosts in the level
     */
    private int numOfGhosts = 0;

    /**
     * Number of small dots in the level
     */
    private int numOfSmallDots = 0;

    /**
     * Number of big dots in the level
     */
    private int numOfBigDots = 0;

    /**
     * Constructor method for the LevelLayout class. It reads the file on the
     * provided path and constructs the level representation. The file has to define
     * at least one ghost and at least one small dot, and exactly one Pac-Man. In
     * addition to that, level must be of rectangular shape.
     *
     * @param name      level name
     * @param levelPath path to the resource file defining the level layout
     * @throws IOException in case provided resource file cannot be read
     */
    public LevelLayout(String name, String levelPath) throws IOException {
        this.levelName = name;
        levelLines = getLevelFileLines(levelPath);
        verifyLevelFormat(levelLines);
    }

    /**
     * Getter method for level name
     *
     * @return name of the level in string format
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * Getter method for the level width
     *
     * @return levelWidth in cells as unit
     */
    public int getLevelWidth() {
        return levelWidth;
    }

    /**
     * Getter method for the level height
     *
     * @return levelHeight in cells as unit
     */
    public int getLevelHeight() {
        return levelHeight;
    }

    /**
     * Getter method for the number of ghosts defined
     *
     * @return number of ghosts in the level
     */
    public int getNumberOfGhosts() {
        return numOfGhosts;
    }

    /**
     * Getter method for the number of small dots defined
     *
     * @return number of small dots in the level
     */
    public int getNumberOfSmallDots() {
        return numOfSmallDots;
    }

    /**
     * Getter method for the number of big dots defined
     *
     * @return number of big dots in the level
     */
    public int getNumberOfBigDots() {
        return numOfBigDots;
    }

    /**
     * Method for fetching the level component at the specified coordinates. Top
     * left corner has both row and column index equal to 0.
     *
     * @param rowIndex    number of the row where wanted element is located
     * @param columnIndex number of the column where wanted element is located
     * @return LevelCellComponent object located at the given coordinates in the
     *         level
     * @throws IndexOutOfBoundsException in case given index is out of bounds of the
     *                                   level layout
     */
    public LevelCellComponent getComponent(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= levelHeight) {
            throw new IndexOutOfBoundsException("rowIndex out of bounds!");
        }
        if (columnIndex < 0 || columnIndex >= levelWidth) {
            throw new IndexOutOfBoundsException("columnIndex out of bounds!");
        }

        return LevelCellComponent.fromCellValue(levelLines[rowIndex].split("")[columnIndex]);
    }

    /**
     * Helper method that is used for formatting the file content of the level
     * layout file on the given path
     *
     * @param levelPath path to the resource file defining the level layout
     * @return 'Map' of the level in string array format
     * @throws IOException in case there is an I/O error during reading of the given
     *                     file content
     */
    private String[] getLevelFileLines(String levelPath) throws IOException {
        String fileContent;
        try (InputStream inputStream = getClass().getResourceAsStream(levelPath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("No resource found on the provided path: " + levelPath);
            }
            fileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        String[] lines = fileContent.split("\r?\n");
        fileContent = Arrays.stream(lines).map(x -> x.replaceAll(" ", "")).map(x -> x.replaceAll("#.*", "").trim())
                .filter(x -> !x.isEmpty()).collect(Collectors.joining("\n"));

        return fileContent.split("\r?\n");
    }

    /**
     * Helper method that checks if given level map conforms to the rules of the
     * game
     *
     * @param levelLines 'Map' of the level in string array format
     * @throws IllegalArgumentException in case given level map is incorrectly
     *                                  defined
     */
    private void verifyLevelFormat(String[] levelLines) {
        if (levelLines.length < MINIMUM_LEVEL_DIMENSION) {
            throw new IllegalArgumentException("Level height cannot be less than " + MINIMUM_LEVEL_DIMENSION + "!");
        } else {
            levelHeight = levelLines.length;
        }
        levelWidth = levelLines[0].length();
        if (levelWidth < MINIMUM_LEVEL_DIMENSION) {
            throw new IllegalArgumentException("Level width cannot be less than " + MINIMUM_LEVEL_DIMENSION + "!");
        }

        int numOfPacMen = 0;
        for (int i = 0; i < levelHeight; i++) {
            if (levelLines[i].length() != levelWidth) {
                throw new IllegalArgumentException("Level must be rectangular!");
            }
            for (String symbol : levelLines[i].split("")) {
                switch (LevelCellComponent.fromCellValue(symbol)) {
                case PACMAN_START:
                    numOfPacMen++;
                    if (numOfPacMen > NUM_OF_PACMEN) {
                        throw new IllegalArgumentException("There can only be one Pac-Man!");
                    }
                    break;
                case SMALL_DOT:
                    numOfSmallDots++;
                    break;
                case BIG_DOT:
                    numOfBigDots++;
                    break;
                case GHOST_START:
                    numOfGhosts++;
                    break;
                default:
                    break;
                }
            }
        }
        if (numOfPacMen != 1) {
            throw new IllegalArgumentException("Level must define one Pac-Man!");
        }
        if (numOfSmallDots == 0) {
            throw new IllegalArgumentException("Level must define at least one small dot!");
        }
        if (numOfGhosts == 0) {
            throw new IllegalArgumentException("Level must define at least one ghost!");
        }
    }
}
