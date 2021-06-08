package javacourse.pacman.model.model;

import java.util.Set;

import javacourse.pacman.model.level.LevelLayout;

/**
 * Class used for the implementation of Pac-Man
 *
 * @author esunata
 *
 */
public class PacManSprite extends Sprite {

    /**
     * Integer variable denoting the amount of time Pac-Man can spend in powered-up
     * state where it can eat ghosts
     */
    private final int powerUpTimeMs;

    /**
     * Boolean flag denoting Pac-Man is powered-up and can eat ghosts
     */
    private boolean powerMode;

    /**
     * From Pac-Man's perspective, one game update equals one call of
     * preMovementUpdate() method
     */
    private long numOfGameUpdatesInPowerUpMode;

    /**
     * Constructor for PacManSprite class
     *
     * @param name                name of the PacManSprite
     * @param powerUpTimeMs       amount of time Pac-Man can be in powered-up state
     *                            (in milliseconds)
     * @param startingRowIndex    first coordinate of the starting position
     * @param startingColumnIndex second coordinate of the starting position
     * @param levelLayout         LevelLayout object that describes the level where
     *                            Pac-Man sprite will exist
     */
    public PacManSprite(String name, int powerUpTimeMs, int startingRowIndex, int startingColumnIndex,
            LevelLayout levelLayout) {
        super(name, startingRowIndex, startingColumnIndex, levelLayout);
        this.powerUpTimeMs = powerUpTimeMs;
    }

    @Override
    public void preMovementUpdate() {
        if (isGhostEatingActive()) {
            numOfGameUpdatesInPowerUpMode++;
            if (getRemainingGhostEatingTime() <= 0L) {
                powerMode = false;
                numOfGameUpdatesInPowerUpMode = 0;
            }
        }
    }

    /**
     * Method that is used for enabling the Pac-Man to eat ghosts temporarily, and
     * disabling that mode as well
     */
    public void toggleGhostEatingMode() {
        powerMode = true;
    }

    /**
     * Method used for checking whether ghost eating mode is active
     *
     * @return true if Pac-Man is currently in ghost eating mode, false otherwise
     */
    public boolean isGhostEatingActive() {
        return powerMode;
    }

    /**
     * Method that returns the amount of time Pac-Man has remaining in ghost eating
     * mode if it is active currently
     *
     * @return time left in powered-up mode, in milliseconds
     * @throws IllegalStateException if method is called while not in powered-up
     *                               mode
     */
    public long getRemainingGhostEatingTime() {
        if (!isGhostEatingActive()) {
            throw new IllegalStateException("Pac-Man is currently not in ghost eating mode!");
        } else {
            return powerUpTimeMs - numOfGameUpdatesInPowerUpMode * MILLISECONDS_PER_GAME_UPDATE;
        }
    }

    /**
     * Method that returns the amount of game updates Pac-Man has spent in the ghost
     * eating mode. One game update corresponds to one call of preMovementUpdate()
     * method.
     *
     * @return amount of game updates that have passed since last time Pac-Man
     *         entered powered-up mode
     * @throws IllegalStateException if method is called while not in powered-up
     *                               mode
     */
    public long getGhostEatingUpdatesCounter() {
        if (!isGhostEatingActive()) {
            throw new IllegalStateException("Pac-Man is currently not in ghost eating mode!");
        } else {
            return numOfGameUpdatesInPowerUpMode;
        }
    }

    /**
     * Method used to move PacMan in the desired direction
     *
     * @param desiredDirection        MovementDirection value requested from outside
     *                                (i.e. player playing the game)
     * @param validMovementDirections set of MovementDirection values which are
     *                                valid respective to Pac-Man's current position
     *                                in the level
     */
    public void pacmanMove(MovementDirection desiredDirection, Set<MovementDirection> validMovementDirections) {
        if (validMovementDirections.contains(desiredDirection)) {
            move(desiredDirection);
        }
    }
}
