package javacourse.pacman.model.model;

import javacourse.pacman.model.level.LevelLayout;

/**
 * Factory helper class whose purpose is to move the initialization of Sprite
 * objects outside of other classes
 *
 * @author Natan
 *
 */
public class SpriteFactory {

    /**
     * Static method used for the creation of new PacManSprite objects
     *
     * @param name                name of the PacManSprite
     * @param powerUpTimeMs       amount of time Pac-Man can be in powered-up state
     *                            (in milliseconds)
     * @param startingRowIndex    first coordinate of the starting position
     * @param startingColumnIndex second coordinate of the starting position
     * @param levelLayout         LevelLayout object that describes the level where
     *                            Pac-Man sprite will exist
     * @return PacManSprite object
     */
    public static PacManSprite createPacMan(String name, int powerUpTimeMs, int startingRowIndex,
            int startingColumnIndex, LevelLayout levelLayout) {
        return new PacManSprite(name, powerUpTimeMs, startingRowIndex, startingColumnIndex, levelLayout);
    }

    /**
     * Static method used for the creation of new GhostSprite objects
     *
     * @param name                name of the GhostSprite
     * @param deathTimeMs         amount of time ghost can be dead/inactive after
     *                            being eaten by Pac-Man (in milliseconds)
     * @param startingRowIndex    first coordinate of the starting position
     * @param startingColumnIndex second coordinate of the starting position
     * @param levelLayout         LevelLayout object that describes the level where
     *                            ghost sprite will exist
     * @return GhostSprite object
     */
    public static GhostSprite createGhost(String name, int deathTimeMs, int startingRowIndex, int startingColumnIndex,
            LevelLayout levelLayout) {
        return new GhostSprite(name, deathTimeMs, startingRowIndex, startingColumnIndex, levelLayout);
    }

}
