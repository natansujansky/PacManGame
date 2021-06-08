package javacourse.pacman.model.model;

import java.util.Random;
import java.util.Set;

import javacourse.pacman.model.level.LevelLayout;

/**
 * Class used for implementation of a ghost
 *
 * @author esunata
 *
 */
public class GhostSprite extends Sprite {

    /**
     * Integer variable denoting the amount of time Ghost will spend inactive after
     * being eaten before respawning back to its starting position
     */
    private final int deathTimeMs;

    /**
     * Boolean variable denoting whether the ghost is currently dead or not
     */
    private boolean isDead;

    /**
     * From ghost's perspective, one game update equals one call of
     * preMovementUpdate() method
     */
    private long numOfGameUpdatesWhileDead;

    /**
     * First variable used to keep track of the starting position of the ghost
     */
    private int startingRowIndex;

    /**
     * Second variable used to keep track of the starting position of the ghost
     */
    private int startingColumnIndex;

    /**
     * Random object used for choosing ghost direction
     */
    private Random random;

    /**
     * Constructor for GhostSprite class
     *
     * @param name                name of the GhostSprite
     * @param deathTimeMs         amount of time ghost can be dead/inactive after
     *                            being eaten by Pac-Man (in milliseconds)
     * @param startingRowIndex    first coordinate of the starting position
     * @param startingColumnIndex second coordinate of the starting position
     * @param levelLayout         LevelLayout object that describes the level where
     *                            ghost sprite will exist
     */
    public GhostSprite(String name, int deathTimeMs, int startingRowIndex, int startingColumnIndex,
            LevelLayout levelLayout) {
        super(name, startingRowIndex, startingColumnIndex, levelLayout);
        this.startingRowIndex = startingRowIndex;
        this.startingColumnIndex = startingColumnIndex;
        this.deathTimeMs = deathTimeMs;
        this.random = new Random();
    }

    @Override
    public void preMovementUpdate() {
        if (isGhostDead()) {
            numOfGameUpdatesWhileDead++;
            if (getRemainingDeathTime() <= 0L) {
                isDead = false;
                numOfGameUpdatesWhileDead = 0;
            }
        }
    }

    /**
     * Method used for killing the ghost, which starts a death timer. After it
     * expires, ghost will respawn at its starting position.
     */
    public void killGhost() {
        isDead = true;
        setPosition(startingRowIndex, startingColumnIndex);
    }

    /**
     * Method used for checking if the ghost is dead
     *
     * @return true if ghost is currently dead, false otherwise
     */
    public boolean isGhostDead() {
        return isDead;
    }

    /**
     * Method that is used for fetching the remaining time ghost needs to spend in
     * dead state
     *
     * @return time left in dead state, in milliseconds
     * @throws IllegalStateException if method is called while not in dead state
     */
    public long getRemainingDeathTime() {
        if (!isGhostDead()) {
            throw new IllegalStateException("Ghost is currently not dead!");
        } else {
            return deathTimeMs - numOfGameUpdatesWhileDead * MILLISECONDS_PER_GAME_UPDATE;
        }
    }

    /**
     * Method that returns the amount of game updates ghost has spent as dead. One
     * game update corresponds to one call of preMovementUpdate() method.
     *
     * @return amount of game updates that have passed since last time ghost was
     *         killed
     * @throws IllegalStateException if method is called while not in dead state
     */
    public long getGhostDeathUpdateCounter() {
        if (!isGhostDead()) {
            throw new IllegalStateException("Ghost is currently not dead!");
        } else {
            return numOfGameUpdatesWhileDead;
        }
    }

    /**
     * Method used to move ghost in some direction. Ghost itself can decide based on
     * the provided (or stored) information in which direction it should move.
     *
     * @param validMovementDirections set of MovementDirection values which are
     *                                valid respective to ghost's current position
     *                                in the level
     * @param pacManDirection         direction of Pac-Man in case it is directly
     *                                visible to the ghost, otherwise NONE
     * @param isGhostEatingModeActive flag denoting whether Pac-Man is currently
     *                                powered-up or not
     */
    public void ghostMove(Set<MovementDirection> validMovementDirections, MovementDirection pacManDirection,
            boolean isGhostEatingModeActive) {
        if (!pacManDirection.equals(MovementDirection.NONE)) {
            if (!isGhostEatingModeActive) {
                move(pacManDirection);
                return;
            } else if (validMovementDirections.size() > 1) {
                validMovementDirections.removeIf(x -> x.equals(pacManDirection));
            }
        } else {
            if (validMovementDirections.contains(getMovementDirection())) {
                move(getMovementDirection());
                return;
            }
        }
        int setSize = validMovementDirections.size();
        int enumIndex = random.nextInt(setSize);
        int i = 0;
        for (MovementDirection direction : validMovementDirections) {
            if (i == enumIndex) {
                move(direction);
                return;
            }
            i++;
        }
    }
}
