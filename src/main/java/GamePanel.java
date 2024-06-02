package main.java;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class GamePanel extends JPanel implements Runnable{

    /**
     * Screen Settings
     */
    private static final int ORIGINAL_TILE_SIZE  = 19;   //  19x19
    private static final int SCALE = 3;
    private static final int MAX_SCREEN_COL = 16;
    private static final int MAX_SCREEN_ROW = 16;
    private static final int FPS = 60;

    private final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    private final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    private final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

    /**
     * Game Settings
     */
    private static final int METEORS_NUMBER = 6;
    private static final int SHIPS_LIVES = 5;
    private static final int SHIPS_BULLETS_CAPACITY = 100;

    /**
     * This object is used to handle keyboard inputs from the user.
     */
    KeyHandler keyHandler = new KeyHandler();

    /**
     * This object represents the player's ship in the game.
     */
    Ship ship = new Ship(this, keyHandler, SHIPS_LIVES, SHIPS_BULLETS_CAPACITY);

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

    /**
     * Variables for welcome screen
     */
    private boolean welcomeScreen = true;
    private final Map<Character, BufferedImage> letterImages = new HashMap<>();
    private double floatTime = 0;

    /**
     * Variables for pause screen
     */
    private boolean isPaused = false;

    /**
     * Array for scoreboard numbers
     */
    private final ArrayList<BufferedImage> numberImages = new ArrayList<>();

    /**
     * Variables for bullet scoreboard
     */
    Font arialFont = new Font("Courier", Font.PLAIN, 24);
    private BufferedImage bulletScoreboardImage;

    /**
     * Variables for lives scoreboard
     */
    private BufferedImage liveImage;

    public GamePanel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        loadLetterImages();

        try {
            for (int i = 0; i <= 9; i++) {
                BufferedImage numberImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/numbers/number_" + i + ".png")));
                numberImages.add(numberImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bulletScoreboardImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/scoreboard/bullet_scoreboard.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            liveImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/lives/live.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    public void run() {
        double drawInterval = calculateDrawInterval();
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            if (!welcomeScreen) {
                if (keyHandler.getPPressed() && !isPaused) {
                    isPaused = true;
                    keyHandler.setPPressed(false);
                } else if (keyHandler.getPPressed() && isPaused) {
                    isPaused = false;
                    keyHandler.setPPressed(false);
                }
            }

            if (!isPaused) {
                if (keyHandler.getEnterPressed() && welcomeScreen) {
                    welcomeScreen = false;
                    keyHandler.setEnterPressed(false);
                }

                if (!welcomeScreen) {
                    updateGame();
                }
            }

            floatTime += 0.05;

            repaint();

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

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;

        if (isPaused) {
            drawPauseScreen(graphics2D);
        } else {
            drawGameElements(graphics2D);
        }
    }

    private void drawGameElements(Graphics2D graphics2D) {
        try {
            tileManager.draw(graphics2D);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (welcomeScreen){
            drawWelcomeScreen(graphics2D);
        }
        else {
            ship.draw(graphics2D);

            for(Meteor meteor: meteors){
                meteor.draw(graphics2D);
            }

            drawScore(graphics2D);
            drawBulletsLeft(graphics2D);
            drawLives(graphics2D);
        }
    }

    private void loadLetterImages() {
        try {
            for (char ch = 'a'; ch <= 'z'; ch++) {
                BufferedImage img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/letters/letter_" + ch + ".png")));
                letterImages.put(ch, img);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load letter images", e);
        }
    }

    private void drawWelcomeScreen(Graphics2D graphics2D) {
        String word = "spaceships";
        int startX = (getScreenWidth() - word.length() * letterImages.get('a').getWidth() * 2) / 2;
        int y = 350;

        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            BufferedImage img = letterImages.get(ch);
            if (img != null) {
                int floatOffset = (int)(Math.sin(floatTime + i) * 5); // Calculate a floatOffset for each letter
                graphics2D.drawImage(img, startX + i * img.getWidth() * 2, y + floatOffset, img.getWidth() * 2, img.getHeight() * 2, null); // Add the floatOffset to the y-position
            }
        }

        // Draw the "press enter to play" text
        String enterText = "PRESS ENTER TO PLAY";
        Font font = new Font("Courier New", Font.PLAIN, 20);
        graphics2D.setFont(font);
        FontMetrics metrics = graphics2D.getFontMetrics(font);
        int x = (getScreenWidth() - metrics.stringWidth(enterText)) / 2;
        int yEnterText = (int)(getScreenHeight() * 0.75);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(enterText, x, yEnterText);
    }

    private void drawPauseScreen(Graphics2D graphics2D) {
        graphics2D.setColor(Color.decode("#000422"));
        graphics2D.fillRect(0, 0, getScreenWidth(), getScreenHeight());

        String word = "pause";
        int startX = (getScreenWidth() - word.length() * letterImages.get('a').getWidth() * 2) / 2;
        int y = (int)(getScreenHeight() * 0.39);

        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            BufferedImage img = letterImages.get(ch);
            if (img != null) {
                int floatOffset = (int)(Math.sin(floatTime + i) * 5); // Calculate a floatOffset for each letter
                graphics2D.drawImage(img, startX + i * img.getWidth() * 2, y + floatOffset, img.getWidth() * 2, img.getHeight() * 2, null); // Add the floatOffset to the y-position
            }
        }

        String enterText = "PRESS P TO RESUME";
        Font font = new Font("Courier New", Font.PLAIN, 20);
        graphics2D.setFont(font);
        FontMetrics metrics = graphics2D.getFontMetrics(font);
        int x = (getScreenWidth() - metrics.stringWidth(enterText)) / 2;
        int yEnterText = (int)(getScreenHeight() * 0.75);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(enterText, x, yEnterText);
    }

    private void drawScore(Graphics2D graphics2D) {
        String scoreString = String.format("%06d", ship.getScore());
        int padding = 20; // padding from the corner
        int sizeMultiplier = 2; // make the score bigger
        int x = getScreenWidth() - (numberImages.get(0).getWidth() * sizeMultiplier * scoreString.length()) - padding;

        for (char c : scoreString.toCharArray()) {
            int number = Character.getNumericValue(c);
            BufferedImage numberImage = numberImages.get(number);
            graphics2D.drawImage(numberImage, x, padding, numberImage.getWidth() * sizeMultiplier, numberImage.getHeight() * sizeMultiplier, null);
            x += numberImage.getWidth() * sizeMultiplier;
        }
    }

    private void drawBulletsLeft(Graphics2D graphics2D) {
        int bulletsLeft = ship.getBulletsCapacity() - ship.getBulletFired();
        String bulletsLeftString = String.format("%d", bulletsLeft);
        int x_padding = 40;
        int y_padding = 20;
        int x = getScreenWidth() - graphics2D.getFontMetrics(arialFont).stringWidth(bulletsLeftString) - x_padding;
        int y = y_padding + graphics2D.getFontMetrics(arialFont).getHeight() * 2 + graphics2D.getFontMetrics(arialFont).getHeight() / 2;

        graphics2D.setFont(arialFont);
        if (bulletsLeft == 0) {
            graphics2D.setColor(Color.RED);
        } else {
            graphics2D.setColor(Color.WHITE);
        }
        graphics2D.drawString(bulletsLeftString, x, y);

        // Draw the image to the right of the bullet counter
        int imageX = x + graphics2D.getFontMetrics(arialFont).stringWidth(bulletsLeftString) + 5; // Adjust this as needed
        int imageY = y - bulletScoreboardImage.getHeight(); // Adjust this as needed
        graphics2D.drawImage(bulletScoreboardImage, imageX, imageY, null);
    }

    private void drawLives(Graphics2D graphics2D) {
        int padding = 30; // padding from the corner
        int x = padding;

        int imageWidth = liveImage.getWidth() + 10; // double the width
        int imageHeight = liveImage.getHeight() + 10; // double the height

        for (int i = 0; i < ship.getLives(); i++) {
            graphics2D.drawImage(liveImage, x, padding, imageWidth, imageHeight, null);
            x += imageWidth + padding; // Move the x-coordinate for the next image
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
