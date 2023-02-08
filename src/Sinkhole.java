import bagel.Image;

/**
 * Sinkhole deals damage to player that dropped in
 */
public class Sinkhole extends Creature implements Attackable{
    private final Image SINKHOLE_IMAGE = new Image("res/sinkhole.png");
    private final double DAMAGE = 30;

    public Sinkhole(double x, double y){
        this.x = x;
        this.y = y;
        this.image = SINKHOLE_IMAGE;
    }

    @Override
    public void attack(Targetable target) {
        target.targeted(DAMAGE, this);
    }

    @Override
    public String toString() {
        return "Sinkhole";
    }
}
