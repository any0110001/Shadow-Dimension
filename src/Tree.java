import bagel.Image;

/**
 * Trees block ways
 */
public class Tree extends Creature{
    private final Image TREE_IMAGE = new Image("res/tree.png");

    public Tree(double x, double y){
        this.x = x;
        this.y = y;
        this.image = TREE_IMAGE;
    }
}
