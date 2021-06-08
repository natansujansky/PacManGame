package javacourse.pacman.model.model;

public enum MovementDirection {
    UP, DOWN, LEFT, RIGHT, NONE;

    /**
     * @return opposite MovementDirection value
     */
    public MovementDirection getOpposite() {
        switch (this) {
        case UP:
            return DOWN;
        case DOWN:
            return UP;
        case LEFT:
            return RIGHT;
        case RIGHT:
            return LEFT;
        default:
            return NONE;
        }
    }

}
