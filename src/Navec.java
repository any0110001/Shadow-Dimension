import bagel.Image;

/**
 * Navec is a special Aggressive Demon
 */
public class Navec extends AggressiveDemon{
    private final Image NAVEC_LEFT_IMAGE = new Image("res/navec/navecLeft.png");
    private final Image NAVEC_RIGHT_IMAGE = new Image("res/navec/navecRight.png");
    private final Image NAVEC_INVINCIBLE_LEFT_IMAGE = new Image("res/navec/navecInvincibleLeft.png");
    private final Image NAVEC_INVINCIBLE_RIGHT_IMAGE = new Image("res/navec/navecInvincibleRight.png");
    private final Image NAVEC_FIRE_IMAGE = new Image("res/navec/navecFire.png");
    private final int SCALE = 2;

    public Navec(double x, double y) {
        super(x, y);
        this.image = NAVEC_LEFT_IMAGE;
        this.maxHealth = MAX_HEALTH*SCALE;
        this.health = MAX_HEALTH*SCALE;
        this.range = 200;
        this.damage = this.damage*SCALE;
        this.leftImage = NAVEC_LEFT_IMAGE;
        this.rightImage = NAVEC_RIGHT_IMAGE;
        this.invincibleLeftImage = NAVEC_INVINCIBLE_LEFT_IMAGE;
        this.invincibleRightImage = NAVEC_INVINCIBLE_RIGHT_IMAGE;
        this.fireImage = NAVEC_FIRE_IMAGE;
        this.fireWidth = fireImage.getWidth();
        this.fireHeight = fireImage.getHeight();
    }

    @Override
    public String toString() {
        return "Navec";
    }
}
