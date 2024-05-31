package main.java;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{

    /**
     * Screen Settings
     */
    private static final int ORIGINAL_TILE_SIZE  = 19;   //  19x19
    private static final int SCALE = 3;
    private static final int MAX_SCREEN_COL = 16;
    private static final int MAX_SCREEN_ROW = 16;
    private static final int FPS = 60;
    private static final int METEORS_NUMBER = 6;
    private static final int SHIPS_BULLETS_CAPACITY = 100;

    private final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    private final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    private final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

    /**
     * This object is used to handle keyboard inputs from the user.
     */
    KeyHandler keyHandler = new KeyHandler();

    /**
     * This object represents the player's ship in the game.
     */
    Ship ship = new Ship(this, keyHandler, SHIPS_BULLETS_CAPACITY);

    /**
     * This object represents the meteors that travel vertically across the map.
     */
    ArrayList<Meteor> meteors = new ArrayList<>();

    /**
     * This object is used to manage the tiles that are drawn on the screen.
     */
    TileManager tileManager = new TileManager(this);

    /**
     * This object is used to manage the game thread.
     */
    Thread gameThread;

    private double timer = 0;
    private int drawCount = 0;


    public GamePanel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        initializeMeteors();
    }

    private void initializeMeteors() {
        for(int i = 0; i < METEORS_NUMBER; i++){
            meteors.add(new Meteor(this));
        }
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
        double drawInterval = calculateDrawInterval();
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null){
            updateGame();
            repaint();
            drawCount++;

            nextDrawTime = sleepAndCalculateNextDrawTime(drawInterval, nextDrawTime);
        }
    }

    private double calculateDrawInterval() {
        return 1000000000/FPS;   //1ps/FPS   1ps/60 = 16.6666666667ms
    }

    private void updateGame() {
        updatePlayer();
        updateMeteors();
    }

    private void updatePlayer() {
        ship.update();
    }

    private void updateMeteors() {
        for (Meteor meteor: meteors){
            if(meteor.y == -TILE_SIZE){
                resetMeteorPosition(meteor);
            }
            meteor.update();
        }
    }

    private void resetMeteorPosition(Meteor meteor) {
        for(int i = 0; i < meteors.size(); i++){
            while (true){
                if(meteor.equals(meteors.get(i)) || (!meteor.equals(meteors.get(i)) && meteor.x != meteors.get(i).x)){
                    break;
                }
                else if(!meteor.equals(meteors.get(i)) && meteor.x == meteors.get(i).x){
                    meteor.setDefaultValues();
                    i = 0;
                }
            }
        }
    }

    private double sleepAndCalculateNextDrawTime(double drawInterval, double nextDrawTime) {
        try {
            double remainingTime = nextDrawTime - System.nanoTime();
            remainingTime = remainingTime/1000000;

            if(remainingTime < 0){
                remainingTime = 0;
            }

            Thread.sleep((long) remainingTime);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return nextDrawTime + drawInterval;
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        Graphics2D  graphics2D = (Graphics2D) graphics;

        drawGameElements(graphics2D);
        updateFPSCounter(graphics2D);
    }

    private void drawGameElements(Graphics2D graphics2D) {
        try {
            tileManager.draw(graphics2D);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ship.draw(graphics2D);

        for(Meteor meteor: meteors){
            meteor.draw(graphics2D);
        }
    }

    private void updateFPSCounter(Graphics2D graphics2D) {
        timer = System.nanoTime() - timer;
        if(timer > 1000000000){
            graphics2D.setFont(new Font("Arial", Font.PLAIN, 10));
            graphics2D.drawString("FPS: " + drawCount, 750, 30);
            graphics2D.dispose();
            drawCount = 0;
            timer = 0;
        }
    }

    public static int getOriginalTileSize() {
        return ORIGINAL_TILE_SIZE;
    }

    public static int getMaxScreenCol() {
        return MAX_SCREEN_COL;
    }

    public int getTileSize() {
        return TILE_SIZE;
    }

    public int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public int getScreenHeight() {
        return SCREEN_HEIGHT;
    }
}
