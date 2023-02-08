import bagel.util.Point;

import java.util.ArrayList;
import java.util.Random;

/**
 * Aggressive Demons are demons that can move
 */
public class AggressiveDemon extends Demon implements Moveable{
    private double speed = new Random().nextDouble()*0.5 + 0.2;
    private int currentDirection = new Random().nextInt(5)-3;

    public AggressiveDemon(double x, double y) {
        super(x, y);
        if(currentDirection == 0){
            currentDirection = LEFT;
        }
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

    /**
     * Method to control demons' movement
     * @param walls
     * @param trees
     * @param sinkholes
     * @param boundTopLeft
     * @param boundBottomRight
     * @param timeScale
     */
    public void control(ArrayList<Wall> walls, ArrayList<Tree> trees, ArrayList<Sinkhole> sinkholes,
                        Point boundTopLeft, Point boundBottomRight, int timeScale){
        move(currentDirection, speed*(1+0.5*timeScale));
        if(!moveIsValid(boundTopLeft, boundBottomRight,  walls, trees, sinkholes)) {
            currentDirection = -currentDirection;
            move(currentDirection, speed*(1+0.5*timeScale));
        }
    }

    /**
     * if the move is valid
     * @param boundTopLeft
     * @param boundBottomRight
     * @param walls
     * @param trees
     * @return true if valid, false if invalid
     */
    private boolean moveIsValid(Point boundTopLeft, Point boundBottomRight, ArrayList<Wall> walls,
                                ArrayList<Tree> trees, ArrayList<Sinkhole> sinkholes){
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
        for (Sinkhole s :
                sinkholes) {
            if (s.overlapWith(this)) {
                return false;
            }
        }
        return true;
    }
}
