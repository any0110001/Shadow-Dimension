import bagel.Image;
import bagel.util.Rectangle;

/**
 * abstract class for all creatures,
 * for storing the location of creature
 * and also the image
 */
public abstract class Creature{
    protected double x;
    protected double y;
    protected Image image;

    /**
     * check overlap with other Creatures
     * @param c
     * @return whether this overlaps with c
     */
    public boolean overlapWith(Creature c){
        return new Rectangle(x, y, this.getWidth(), this.getHeight())
                .intersects(new Rectangle(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth(){
        return image.getWidth();
    }

    public double getHeight(){
        return image.getHeight();
    }

    public void display() {
        this.image.drawFromTopLeft(x, y);
    }
}
