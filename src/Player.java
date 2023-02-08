import bagel.*;
import bagel.util.Point;

import java.util.ArrayList;

/**
 * Player class
 * player is a living creature that can move and is controlled by user
 */
public class Player extends LivingCreature implements Moveable{
    private final Image PLAYER_IMAGE_LEFT = new Image("res/fae/faeLeft.png");
    private final Image PLAYER_IMAGE_RIGHT = new Image("res/fae/faeRight.png");
    private final Image PLAYER_ATTACK_IMAGE_LEFT = new Image("res/fae/faeAttackLeft.png");
    private final Image PLAYER_ATTACK_IMAGE_RIGHT = new Image("res/fae/faeAttackRight.png");
    private final int MAX_HEALTH= 100;
    private double speed = 2;
    private boolean facingLeft = true;
    private final int ATTACK_COOLING = 120;
    private final int ATTACKING_PERIOD = 60;
    private int attackCounting;
    private int attackCoolingCounting;
    private double damage = 20;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.maxHealth = MAX_HEALTH;
        this.health = MAX_HEALTH;
    }

    /**
     * implement updateStatus so that it implements checking attack status
     */
    @Override
    public void updateStatus() {
        super.updateStatus();
        if(attackCounting > 0) {
            attackCounting--;
            if(attackCounting == 0){
                attackCoolingCounting = ATTACK_COOLING;
            }
        }
        if(attackCoolingCounting > 0){
            attackCoolingCounting--;
        }
    }

    @Override
    public void attack(Targetable target) {
        target.targeted(damage, this);
    }

    @Override
    public void move(int direction, double speed) {
        switch (direction){
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
        }
    }

    @Override
    public void display() {
        if(facingLeft && attackCounting==0){
            PLAYER_IMAGE_LEFT.drawFromTopLeft(x,y);
        }else if(!facingLeft && attackCounting == 0){
            PLAYER_IMAGE_RIGHT.drawFromTopLeft(x,y);
        }else if(facingLeft && attackCounting != 0){
            PLAYER_ATTACK_IMAGE_LEFT.drawFromTopLeft(x,y);
        }else{
            PLAYER_ATTACK_IMAGE_RIGHT.drawFromTopLeft(x,y);
        }
        int percentage = (int)Math.round((double)health/MAX_HEALTH*100);
        DrawOptions colour;
        if(percentage >= GREEN_HEALTH) {
            colour = new DrawOptions().setBlendColour(GREEN);
        }else if(percentage >= ORANGE_HEALTH){
            colour = new DrawOptions().setBlendColour(ORANGE);
        }else{
            colour = new DrawOptions().setBlendColour(RED);
        }
        HEALTH_BAR_FONT_PLAYER.drawString(percentage + " %", HEALTH_BAR_PLAYER.x, HEALTH_BAR_PLAYER.y, colour);
    }

    @Override
    public double getWidth() {
        return PLAYER_IMAGE_LEFT.getWidth();
    }

    @Override
    public double getHeight() {
        return PLAYER_IMAGE_LEFT.getHeight();
    }

    @Override
    public String toString() {
        return "Fae";
    }

    /**
     * Method to take inputs and control the movement of player
     * @param input
     * @param walls
     * @param trees
     * @param sinkholes
     * @param demons
     * @param boundTopLeft
     * @param boundBottomRight
     */
    public void control(Input input, ArrayList<Wall> walls, ArrayList<Tree> trees, ArrayList<Sinkhole> sinkholes,
                        ArrayList<Demon> demons, Point boundTopLeft, Point boundBottomRight){
        if (input.isDown(Keys.LEFT)) {
            move(LEFT, speed);
            facingLeft = true;
            if(!moveIsValid(boundTopLeft, boundBottomRight, walls, trees)) {
                move(RIGHT, speed);
            }
        }
        if (input.isDown(Keys.RIGHT)) {
            move(RIGHT, speed);
            facingLeft = false;
            if(!moveIsValid(boundTopLeft, boundBottomRight, walls, trees)) {
                move(LEFT, speed);
            }
        }
        if (input.isDown(Keys.UP)) {
            move(UP, speed);
            if(!moveIsValid(boundTopLeft, boundBottomRight, walls, trees)) {
                move(DOWN, speed);
            }
        }
        if (input.isDown(Keys.DOWN)) {
            move(DOWN, speed);
            if(!moveIsValid(boundTopLeft, boundBottomRight, walls, trees)) {
                move(UP, speed);
            }
        }
        if (input.isDown(Keys.A) && attackCoolingCounting == 0){
            attackCounting = ATTACKING_PERIOD;
        }
        if(attackCounting != 0){
            checkAttacking(demons);
        }
        sinkholes.remove(checkDropInHole(sinkholes));
    }

    /**
     * if the move is valid
     * @param boundTopLeft
     * @param boundBottomRight
     * @param walls
     * @param trees
     * @return true if valid, false if invalid
     */
    private boolean moveIsValid(Point boundTopLeft, Point boundBottomRight, ArrayList<Wall> walls, ArrayList<Tree> trees){
        if(x < boundTopLeft.x || y < boundTopLeft.y
                || x > boundBottomRight.x || y > boundBottomRight.y){
            return false;
        }
        for (Wall w :
                walls) {
            if (w.overlapWith(this)) {
                return false;
            }
        }
        for (Tree t :
                trees) {
            if (t.overlapWith(this)) {
                return false;
            }
        }
        return true;
    }

    /**
     * check if the player dropped in a hole
     * @param sinkholes
     * @return the sinkhole that the player dropped in, if not dropped in, return null
     */
    private Sinkhole checkDropInHole(ArrayList<Sinkhole> sinkholes){
        for (Sinkhole s :
                sinkholes) {
            if (s.overlapWith(this)) {
                s.attack(this);
                return s;
            }
        }
        return null;
    }

    /**
     * Mehtod to check if player is attacking a demon.
     * @param demons
     */
    private void checkAttacking(ArrayList<Demon> demons){
        for (Demon d :
                demons) {
            if (d.overlapWith(this)) {
                d.attack(this);
                attack(d);
            }
        }
    }
}
