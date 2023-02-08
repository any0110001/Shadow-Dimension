/**
 * Moveable interface
 */
public interface Moveable {
    /**
     * indicate left  direction
     */
    int LEFT = 1;
    /**
     * indicate right direction
     */
    int RIGHT = -1;
    /**
     * indicate up direction
     */
    int UP = 2;
    /**
     * indicate down direction
     */
    int DOWN = -2;

    void move(int direction, double speed);
}
