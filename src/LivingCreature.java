import bagel.Font;
import bagel.util.Colour;
import bagel.util.Point;

/**
 * all  living creatures which can attach and be targeted
 * having health, can be invincible and having health bar
 */
public abstract class LivingCreature extends Creature implements Attackable, Targetable{
    protected final int INVINCIBLE_PERIOD = 180;
    protected int invincibleCount;
    protected int health;
    protected int maxHealth;

    protected final int GREEN_HEALTH = 65;
    protected final int ORANGE_HEALTH = 35;
    protected final Point HEALTH_BAR_PLAYER = new Point(20, 25);
    protected final Font HEALTH_BAR_FONT_PLAYER = new Font("res/frostbite.ttf", 30);
    protected final Font HEALTH_BAR_FONT_DEMON = new Font("res/frostbite.ttf", 15);
    protected final Colour GREEN = new Colour(0,0.8,0.2);
    protected final Colour ORANGE = new Colour(0.9,0.6,0);
    protected final Colour RED = new Colour(1,0,0);

    /**
     * Method to implement invincibility
     */
    protected void updateStatus(){
        if(invincibleCount > 0)
            invincibleCount--;
    }

    /**
     * Method to make the object invincible
     */
    protected void goInvincible(){
        invincibleCount = INVINCIBLE_PERIOD;
    }

    /**
     * @return Whether the living creature is still alive
     */
    protected boolean isAlive(){
        return health != 0;
    }

    /**
     * if the attacker succeeds to attack, then damage is dealt
     * @param dmg
     * @param attacker
     */
    public void targeted(double dmg, Attackable attacker) {
        if(invincibleCount==0) {
            if (health > dmg) {
                health -= dmg;
                goInvincible();
            } else {
                health = 0;
            }
            System.out.println(attacker + " inflicts " + dmg + " damage points on "+ this +".  " + this
                    + "’s current health: " + this.health + "/" + this.maxHealth);
        }
    }
}
