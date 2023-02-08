import bagel.Image;

/**
 * Walls block ways
 */
public class Wall extends Creature{
    private final Image WALL_IMAGE = new Image("res/wall.png");

    public Wall(double x, double y){
        this.x = x;
        this.y = y;
        this.image = WALL_IMAGE;
    }
}
