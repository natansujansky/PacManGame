package javacourse.pacman.general;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Properties;

/**
 * Used to represent game properties.
 */
public class GameProperties {

    /**
     * Used to represent property names
     */
    private enum GamePropertyNames {
        CELL_SIZE_PROPERTY("cell_size"), FRAMES_PER_SECOND("frames_per_second"),
        GHOST_DEATH_BLINK_START_MS("ghost_death_blink_start_ms");

        // TODO: add whatever you need here

        private final String nameString;

        GamePropertyNames(final String nameString) {
            this.nameString = nameString;
        }

        /**
         * @return property name as used in the property file
         */
        public String getName() {
            return nameString;
        }
    }

    private static final String GAME_PROPERTIES_RESOURCE_PATH = "/game.properties";
    private static final GameProperties gameProperties = new GameProperties();

    private final Properties properties;

    private final EnumMap<GamePropertyNames, String> gamePropertiesMap;

    /**
     * @return singleton instance of the game properties
     */
    public static GameProperties getGameProperties() {
        return gameProperties;
    }

    private GameProperties() {
        properties = new Properties();

        try (InputStream inputStream = getClass().getResourceAsStream(GAME_PROPERTIES_RESOURCE_PATH)) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("Failed to read game properties file: " + GAME_PROPERTIES_RESOURCE_PATH);
            e.printStackTrace();
        }

        gamePropertiesMap = new EnumMap<>(GamePropertyNames.class);
        for (GamePropertyNames gamePropertyName : GamePropertyNames.values()) {
            verifyPropertyExists(gamePropertyName.getName());
            gamePropertiesMap.put(gamePropertyName, properties.getProperty(gamePropertyName.getName()));
        }
    }

    /**
     * @return PacMan game cell dimension. Refers to both width and height.
     */
    public double getCellDimension() {
        return Double.parseDouble(gamePropertiesMap.get(GamePropertyNames.CELL_SIZE_PROPERTY));
    }

    /**
     * @return amount of frames/updates per second that the game should perform
     */
    public int getFramesPerSecond() {
        return Integer.parseInt(gamePropertiesMap.get(GamePropertyNames.FRAMES_PER_SECOND));
    }

    /**
     * @return amount of milliseconds of ghost dead time remaining when they should
     *         start blinking
     */
    public int getGhostDeathBlinkStartMs() {
        return Integer.parseInt(gamePropertiesMap.get(GamePropertyNames.GHOST_DEATH_BLINK_START_MS));
    }

    /**
     * Used to verify that the property exists in the property file.
     *
     * @param property property name
     *
     * @throws IllegalArgumentException if property doesn't exist
     */
    private void verifyPropertyExists(String property) {
        if (!properties.stringPropertyNames().contains(property)) {
            throw new IllegalArgumentException(property + " not present in the properties file.");
        }
    }

}
