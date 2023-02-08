import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Rectangle;

/**
 * Demons are living creatures
 * Note that this class is used to instantiate Stationary Demons as well as being a parent class
 * of Aggressive Demon and also Navec
 */
public class Demon extends LivingCreature {
    private final Image DEMON_LEFT_IMAGE = new Image("res/demon/demonLeft.png");
    private final Image DEMON_RIGHT_IMAGE = new Image("res/demon/demonRight.png");
    private final Image DEMON_INVINCIBLE_LEFT_IMAGE = new Image("res/demon/demonInvincibleLeft.png");
    private final Image DEMON_INVINCIBLE_RIGHT_IMAGE = new Image("res/demon/demonInvincibleRight.png");
    private final Image DEMON_FIRE_IMAGE = new Image("res/demon/demonFire.png");
    protected Image leftImage;
    protected Image rightImage;
    protected Image invincibleLeftImage;
    protected Image invincibleRightImage;
    protected Image fireImage;
    protected final int MAX_HEALTH = 40;
    protected boolean facingLeft = true;
    protected double damage = 10;
    protected double range = 150;
    protected double fireWidth;
    protected double fireHeight;

    public Demon(double x, double y){
        this.x = x;
        this.y = y;
        this.image = DEMON_LEFT_IMAGE; // this is the image in Creature which is used by method getWidth and getHeight
        this.maxHealth = MAX_HEALTH;
        this.health = MAX_HEALTH;
        this.leftImage = DEMON_LEFT_IMAGE;
        this.rightImage = DEMON_RIGHT_IMAGE;
        this.invincibleLeftImage = DEMON_INVINCIBLE_LEFT_IMAGE;
        this.invincibleRightImage = DEMON_INVINCIBLE_RIGHT_IMAGE;
        this.fireImage = DEMON_FIRE_IMAGE;
        this.fireWidth = fireImage.getWidth();
        this.fireHeight = fireImage.getHeight();
    }

    @Override
    public void attack(Targetable target) {
        if(target instanceof LivingCreature) {
            double targetX = ((LivingCreature) target).getX() + 0.5 * ((LivingCreature) target).getWidth();
            double targetY = ((LivingCreature) target).getY() + 0.5 * ((LivingCreature) target).getHeight();
            double x = this.x + 0.5 * this.getWidth();
            double y = this.y + 0.5 * this.getHeight();
            if (Math.sqrt(Math.pow(targetX - x, 2) + Math.pow(targetY - y, 2)) <= this.range) {
                if (targetX <= x && targetY <= y) {
                    fireImage.drawFromTopLeft(x - fireWidth - this.getWidth() * 0.5, y - fireHeight - this.getHeight() * 0.5, new DrawOptions().setRotation(0));
                    if(new Rectangle(x - fireWidth - this.getWidth() * 0.5, y - fireHeight - this.getHeight() * 0.5, fireWidth,fireHeight)
                            .intersects(new Rectangle(((LivingCreature) target).getX(),((LivingCreature) target).getY(),
                                    ((LivingCreature) target).getWidth(),((LivingCreature) target).getHeight()))){
                        target.targeted(damage, this);
                    }
                } else if (targetX <= x && targetY > y) {
                    fireImage.drawFromTopLeft(x - fireHeight - this.getWidth() * 0.5, y + this.getHeight() * 0.5, new DrawOptions().setRotation(Math.PI *3/2));
                    if(new Rectangle(x - fireHeight - this.getWidth() * 0.5, y + this.getHeight() * 0.5, fireWidth,fireHeight)
                            .intersects(new Rectangle(((LivingCreature) target).getX(),((LivingCreature) target).getY(),
                                    ((LivingCreature) target).getWidth(),((LivingCreature) target).getHeight()))){
                        target.targeted(damage, this);
                    }
                } else if (targetX > x && targetY <= y) {
                    fireImage.drawFromTopLeft(x + this.getWidth() * 0.5, y - fireWidth - this.getHeight() * 0.5, new DrawOptions().setRotation(Math.PI / 2));
                    if(new Rectangle(x + this.getWidth() * 0.5, y - fireWidth - this.getHeight() * 0.5, fireWidth,fireHeight)
                            .intersects(new Rectangle(((LivingCreature) target).getX(),((LivingCreature) target).getY(),
                                    ((LivingCreature) target).getWidth(),((LivingCreature) target).getHeight()))){
                        target.targeted(damage, this);
                    }
                } else {
                    fireImage.drawFromTopLeft(x + this.getWidth() * 0.5, y + this.getHeight() * 0.5, new DrawOptions().setRotation(Math.PI));
                    if(new Rectangle(x + this.getWidth() * 0.5, y + this.getHeight() * 0.5, fireWidth,fireHeight)
                            .intersects(new Rectangle(((LivingCreature) target).getX(),((LivingCreature) target).getY(),
                                    ((LivingCreature) target).getWidth(),((LivingCreature) target).getHeight()))){
                        target.targeted(damage, this);
                    }
                }
            }
        }
    }

    @Override
    public void display() {
        if(facingLeft && invincibleCount==0){
            leftImage.drawFromTopLeft(x,y);
        }else if(!facingLeft && invincibleCount == 0){
            rightImage.drawFromTopLeft(x,y);
        }else if(facingLeft && invincibleCount != 0){
            invincibleLeftImage.drawFromTopLeft(x,y);
        }else{
            invincibleRightImage.drawFromTopLeft(x,y);
        }

        int percentage = (int)Math.round((double)health/maxHealth*100);
        DrawOptions colour;
        if(percentage >= GREEN_HEALTH) {
            colour = new DrawOptions().setBlendColour(GREEN);
        }else if(percentage >= ORANGE_HEALTH){
            colour = new DrawOptions().setBlendColour(ORANGE);
        }else{
            colour = new DrawOptions().setBlendColour(RED);
        }
        HEALTH_BAR_FONT_DEMON.drawString(percentage + " %", x, y-6, colour);
    }

    @Override
    public String toString() {
        return "Demon";
    }
}
