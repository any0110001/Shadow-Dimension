import bagel.*;
import bagel.util.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2022
 *
 * Please enter your name below
 * @author Ziheng Cao
 */

public class ShadowDimension extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final String START_MESSAGE = "PRESS SPACE TO START\n" + "USE ARROW KEYS TO FIND GATE";
    private final String LEVEL_COMPLETE_MESSAGE = "LEVEL COMPLETE!";
    private final String LEVEL1_START_MESSAGE = "PRESS SPACE TO START\n" + "PRESS A TO ATTACK\n" + "DEFEAT NAVEC TO WIN";
    private final String TO_PLAYER = "Fae";
    private final String TO_WALL = "Wall";
    private final String TO_TREE = "Tree";
    private final String TO_SINKHOLE = "Sinkhole";
    private final String TO_DEMON = "Demon";
    private final String TO_NAVEC = "Navec";
    private final String TO_TOP_LEFT = "TopLeft";
    private final String TO_BOTTOM_RIGHT = "BottomRight";
    private final String CONGRATULATION = "CONGRATULATION!";

    private final Point BOTTOM_LEFT_CORNER_TITLE = new Point(260, 250);
    private final Point BOTTOM_LEFT_CORNER_START_MESSAGE = new Point(260+90, 250+190);
    private final Point GAME_OVER_POSITION = new Point(WINDOW_WIDTH/2-120, WINDOW_HEIGHT/2-20);
    private final Point LEVEL_COMPLETE_POSITION = new Point(WINDOW_WIDTH/2-200, WINDOW_HEIGHT/2-20);
    private final Point LEVEL1_START_POSITION = new Point(350, 350);
    private final Point CONGRATULATION_POSITION = new Point(WINDOW_WIDTH/2-200, WINDOW_HEIGHT/2-20);
    private final Point WIN_CONDITION = new Point(950, 670);

    private final Font MESSAGE_FONT = new Font("res/frostbite.ttf", 75);
    private final Font START_MESSAGE_FONT = new Font("res/frostbite.ttf", 40);


    private final Image BACKGROUND_IMAGE_0 = new Image("res/background0.png");
    private final Image BACKGROUND_IMAGE_1 = new Image("res/background1.png");

    private int gapCount = 180;

    private Player player;
    private ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<Sinkhole> sinkholes = new ArrayList<Sinkhole>();
    private ArrayList<Tree> trees = new ArrayList<Tree>();
    private ArrayList<Demon> demons = new ArrayList<>();
    private Navec navec;

    /**
     * Attributes for bounds
     */
    private Point boundTopLeft;
    private Point boundBottomRight;

    /**
     * Attributes recording the game process
     */
    private int status = 0;
    private static final int GAME_INITIALIZED = 1;
    private static final int GAME_STARTED = 2;
    private static final int LEVEL_ZERO_COMPLETED = 3;
    private static final int LEVEL_ONE_STARTED = 4;
    private static final int GAME_WON = 5;
    private static final int GAME_OVER = 9;
    protected static int timeScale = 0;
    protected static final int TIME_SCALE_UPPER = 3;
    protected static int TIME_SCALE_LOWER = -3;

    public ShadowDimension() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * Initialize and set up the game.
     */
    public ShadowDimension(String file) {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        readCSV(file);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        String file = "res/level0.csv";
        ShadowDimension game = new ShadowDimension(file);
        game.status = GAME_INITIALIZED;
        game.run();
    }

    /**
     * Method used to read file and create objects.
     */
    private void readCSV(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String text;
            while ((text = br.readLine()) != null) {
                String[] s = text.split(",");
                if(s[0].equals(TO_PLAYER)) {
                    player = new Player(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                }else if(s[0].equals(TO_WALL)){
                    walls.add(new Wall(Integer.parseInt(s[1]), Integer.parseInt(s[2])));
                }else if(s[0].equals(TO_SINKHOLE)){
                    sinkholes.add(new Sinkhole(Integer.parseInt(s[1]), Integer.parseInt(s[2])));
                }else if(s[0].equals(TO_TREE)){
                    trees.add(new Tree(Integer.parseInt(s[1]), Integer.parseInt(s[2])));
                }else if(s[0].equals(TO_DEMON)){
                    if(new Random().nextDouble()>0.5) {
                        demons.add(new Demon(Integer.parseInt(s[1]), Integer.parseInt(s[2])));
                    }else{
                        demons.add(new AggressiveDemon(Integer.parseInt(s[1]), Integer.parseInt(s[2])));
                    }
                }else if(s[0].equals(TO_NAVEC)){
                    navec = new Navec(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                    demons.add(navec);
                }else if(s[0].equals(TO_TOP_LEFT)){
                    boundTopLeft = new Point(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                }else if(s[0].equals(TO_BOTTOM_RIGHT)){
                    boundBottomRight = new Point(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to initialize level 1 objects.
     */
    private void levelOneInitialize(){
        player = null;
        walls = new ArrayList<>();
        sinkholes = new ArrayList<>();
        readCSV("res/level1.csv");
    }

    /**
     * Method used to display all Objects
     */

    private void display(){
        if(status == GAME_STARTED) {
            BACKGROUND_IMAGE_0.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        }else if(status == LEVEL_ONE_STARTED){
            BACKGROUND_IMAGE_1.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        }
        player.display();
        for (Wall w :
                walls) {
            w.display();
        }
        for (Sinkhole s :
                sinkholes) {
            s.display();
        }
        for (Tree t :
                trees) {
            t.display();
        }
        for (Demon d :
                demons) {
            d.display();
        }
    }


    /**
     * Method to update status for all living creatures
     * including check attack from demons to player
     */
    private void updateStatus(){
        ArrayList<Demon> deadDemons = new ArrayList<>();
        for (Demon d :
                demons) {
            if(!d.isAlive()){
                deadDemons.add(d);
            }
        }
        for (Demon d :
                deadDemons) {
            demons.remove(d);
        }
        display();
        player.updateStatus();
        for (Demon d :
                demons) {
            d.attack(player);
            d.updateStatus();
        }
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        switch (status){ // Split functionality for different game stages
            case GAME_INITIALIZED:
                MESSAGE_FONT.drawString(GAME_TITLE, BOTTOM_LEFT_CORNER_TITLE.x, BOTTOM_LEFT_CORNER_TITLE.y);
                START_MESSAGE_FONT.drawString(START_MESSAGE, BOTTOM_LEFT_CORNER_START_MESSAGE.x, BOTTOM_LEFT_CORNER_START_MESSAGE.y);
                if(input.wasPressed(Keys.SPACE)){
                    status = GAME_STARTED;
                }
                break;
            case GAME_STARTED:
                player.control(input, walls, trees, sinkholes, demons, boundTopLeft, boundBottomRight);
                if(!player.isAlive()){
                    status = GAME_OVER;
                }
                if(player.getX() >= WIN_CONDITION.x && player.getY() >= WIN_CONDITION.y){
                    status = LEVEL_ZERO_COMPLETED;
                }
                updateStatus();
                break;
            case LEVEL_ZERO_COMPLETED:
                if(gapCount > 0){
                    MESSAGE_FONT.drawString(LEVEL_COMPLETE_MESSAGE, LEVEL_COMPLETE_POSITION.x, LEVEL_COMPLETE_POSITION.y);
                    gapCount--;
                }else{
                    START_MESSAGE_FONT.drawString(LEVEL1_START_MESSAGE, LEVEL1_START_POSITION.x, LEVEL1_START_POSITION.y);
                    if(input.isDown(Keys.SPACE)){
                        status = LEVEL_ONE_STARTED;
                        levelOneInitialize();
                    }
                }
                break;
            case LEVEL_ONE_STARTED:
                player.control(input, walls, trees, sinkholes, demons, boundTopLeft, boundBottomRight);
                for (Demon d :
                        demons) {
                    if(d instanceof AggressiveDemon)
                        ((AggressiveDemon)d).control(walls, trees, sinkholes, boundTopLeft, boundBottomRight, timeScale);
                }
                if(!player.isAlive()){
                    status = GAME_OVER;
                }
                if(!navec.isAlive()){
                    status = GAME_WON;
                }
                updateStatus();
                break;
            case GAME_WON:
                MESSAGE_FONT.drawString(CONGRATULATION, CONGRATULATION_POSITION.x, CONGRATULATION_POSITION.y);
                break;
            case GAME_OVER:
                MESSAGE_FONT.drawString("GAME OVER!", GAME_OVER_POSITION.x, GAME_OVER_POSITION.y);
                break;
        }
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        if (input.wasPressed(Keys.L) && timeScale<TIME_SCALE_UPPER){
            timeScale++;
            System.out.println("Sped up, Speed: "+timeScale);
        }
        if (input.wasPressed(Keys.K) && timeScale>TIME_SCALE_LOWER){
            timeScale--;
            System.out.println("Sped down, Speed: "+timeScale);
        }
    }
}