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
import java.util.Random;

public class GamePanel extends JPanel implements Runnable{

    /**
     * Screen Settings
     */
    private static final int ORIGINAL_TILE_SIZE  = 19;   //  19x19
    private static final int SCALE = 3;
    private static final int MAX_SCREEN_COL = 16;
    private static final int MAX_SCREEN_ROW = 16;
    private static final int FPS = 60;

    private final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;   // 19x3
    private final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    private final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

    /**
     * Game Settings
     */
    private static final int METEORS_NUMBER = 9;
    private static final int METEORS_SPEED_THRESHOLD = 8;
    private static final int SHIPS_LIVES = 5;
    private static final int SHIPS_SPEED = 6;
    private static final int SHIPS_BULLETS_CAPACITY = 100;
    private static final int SHIPS_BULLET_SPEED = 20;
    private static final int SHIP_COOLDOWN_TIME = 30;

    /**
     * This object is used to handle keyboard inputs from the user.
     */
    KeyHandler keyHandler = new KeyHandler();

    /**
     * This object represents the player's ship in the game.
     */
    Ship ship = new Ship(this, keyHandler, SHIPS_LIVES, SHIPS_SPEED, SHIPS_BULLETS_CAPACITY, SHIPS_BULLET_SPEED, SHIP_COOLDOWN_TIME);

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
     * This object is used to check for collisions between entities.
     */
    public CollisionChecker collisionChecker = new CollisionChecker(this);

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
    private double scoreZoom = 1.0;
    private int lastScore = 0;

    /**
     * Variables for bullet scoreboard
     */
    Font arialFont = new Font("Courier", Font.PLAIN, 24);
    private BufferedImage bulletScoreboardImage;

    /**
     * Variables for lives scoreboard
     */
    private BufferedImage liveImage;

    /**
     * Variables for cooldown countdown
     */
    private int dotCounter;
    private BufferedImage collingIconImage;

    private final Random randomMeteorSpeed;

    public GamePanel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        randomMeteorSpeed = new Random();
        dotCounter = 1;

        loadLetterImages();
        loadNumberImages();
        loadBulletScoreboardImage();
        loadLiveImage();
        loadCoolingImage();
        initializeMeteors();
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

    private void loadNumberImages() {
        try {
            for (int i = 0; i <= 9; i++) {
                BufferedImage numberImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/numbers/number_" + i + ".png")));
                numberImages.add(numberImage);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load number images", e);
        }
    }

    private void loadBulletScoreboardImage() {
        try {
            bulletScoreboardImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/scoreboard/bullet_scoreboard.png")));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load bullet scoreboard image", e);
        }
    }

    private void loadLiveImage() {
        try {
            liveImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/lives/live.png")));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load live image", e);
        }
    }

    private void loadCoolingImage() {
        try {
            collingIconImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/cooling/cooling_icon.png")));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load cooling image", e);
        }
    }

    private void initializeMeteors() {
        for(int i = 0; i < METEORS_NUMBER; i++){
            meteors.add(new Meteor(this, randomMeteorSpeed.nextInt(METEORS_SPEED_THRESHOLD) + 2));
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
        return (double) 1000000000 /FPS;   //1ps/FPS   1ps/60 = 16.6666666667ms
    }

    private void updateGame() {
        updatePlayer();
        updateMeteors();

        if (ship.getScore() / 1000 > lastScore) {
            lastScore = ship.getScore() / 1000;
            scoreZoom = 1.5; // Set zoom level to 2 (double size)
        }
    }

    private void updatePlayer() {
        ship.update();
    }

    private void updateMeteors() {
        for (Meteor meteor: meteors){
            meteor.update();
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
            throw new RuntimeException("Failed to draw game elements", e);
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

            if (ship.getBulletFired() == ship.getBulletsCapacity()) {
                drawCooldownCountdown(graphics2D);
            } else {
                drawBulletsLeft(graphics2D);
            }

            drawLives(graphics2D);
        }
    }

    private void drawWelcomeScreen(Graphics2D graphics2D) {
        String word = "spaceships";
        int startX = (getScreenWidth() - word.length() * letterImages.get('a').getWidth() * 2) / 2;
        int y = 350;

        drawFloatingText(graphics2D, "spaceships", letterImages, startX, y, floatTime);

        // Draw the "press enter to play" text
        drawCenteredText(graphics2D, "PRESS ENTER TO PLAY", new Font("Courier New", Font.PLAIN, 20));
    }

    private void drawPauseScreen(Graphics2D graphics2D) {
        graphics2D.setColor(Color.decode("#000422"));
        graphics2D.fillRect(0, 0, getScreenWidth(), getScreenHeight());

        String word = "pause";
        int startX = (getScreenWidth() - word.length() * letterImages.get('a').getWidth() * 2) / 2;
        int y = (int)(getScreenHeight() * 0.39);

        drawFloatingText(graphics2D, "pause", letterImages, startX, y, floatTime);

        // Draw the "press P to resume" text
        drawCenteredText(graphics2D, "PRESS P TO RESUME", new Font("Courier New", Font.PLAIN, 20));
    }

    private void drawScore(Graphics2D graphics2D) {
        String scoreString = String.format("%06d", ship.getScore());
        int padding = 20; // padding from the corner
        int sizeMultiplier = 2; // make the score bigger
        int x = getScreenWidth() - (numberImages.get(0).getWidth() * sizeMultiplier * scoreString.length()) - padding;

        for (char c : scoreString.toCharArray()) {
            int number = Character.getNumericValue(c);
            BufferedImage numberImage = numberImages.get(number);
            graphics2D.drawImage(numberImage, x, padding, (int)(numberImage.getWidth() * sizeMultiplier * scoreZoom), (int)(numberImage.getHeight() * sizeMultiplier * scoreZoom), null);
            x += numberImage.getWidth() * sizeMultiplier;
        }

        // Gradually decrease scoreZoom back to 1
        if (scoreZoom > 1.0) {
            scoreZoom -= 0.015;
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
        if (bulletsLeft <= ship.getBulletsCapacity() * 0.1) {
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

    public void drawCooldownCountdown(Graphics2D graphics2D) {
        String cooldownCountdownString = String.format("%d", (ship.getCooldownCounter() / 60) + 1);
        int x_padding = 40;
        int y_padding = 20;
        int x = getScreenWidth() - graphics2D.getFontMetrics(arialFont).stringWidth(cooldownCountdownString) - x_padding;
        int y = y_padding + graphics2D.getFontMetrics(arialFont).getHeight() * 2 + graphics2D.getFontMetrics(arialFont).getHeight() / 2;

        graphics2D.setFont(arialFont);
        graphics2D.setColor(Color.BLUE);
        graphics2D.drawString(cooldownCountdownString, x, y);

        // Draw "COOLING" text and dots
        String coolingText = "COOLING";

        StringBuilder dotsBuilder = new StringBuilder();
        for (int i = 0; i < dotCounter; i++) {
            dotsBuilder.append(".");
        }
        String dots = dotsBuilder.toString();

        int coolingX = getScreenWidth() - x_padding * 2 - graphics2D.getFontMetrics(arialFont).stringWidth(coolingText) - 40; // Subtract 20 to move the text 20 pixels to the left
        graphics2D.drawString(coolingText + dots, coolingX, y);

        int imageX = x + graphics2D.getFontMetrics(arialFont).stringWidth(cooldownCountdownString) + 5; // Adjust this as needed
        int imageY = y - collingIconImage.getHeight() + 1; // Adjust this as needed
        graphics2D.drawImage(collingIconImage, imageX, imageY, null);

        if (ship.getCooldownCounter() % 20 == 0) {
            dotCounter = (dotCounter % 3) + 1;
        }
    }

    public static int getFps() {
        return FPS;
    }

    private void drawFloatingText(Graphics2D graphics2D, String text, Map<Character, BufferedImage> letterImages, int startX, int y, double floatTime) {
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            BufferedImage img = letterImages.get(ch);
            if (img != null) {
                int floatOffset = (int)(Math.sin(floatTime + i) * 5); // Calculate a floatOffset for each letter
                graphics2D.drawImage(img, startX + i * img.getWidth() * 2, y + floatOffset, img.getWidth() * 2, img.getHeight() * 2, null); // Add the floatOffset to the y-position
            }
        }
    }

    private void drawCenteredText(Graphics2D graphics2D, String text, Font font) {
        graphics2D.setFont(font);
        FontMetrics metrics = graphics2D.getFontMetrics(font);
        int x = (getScreenWidth() - metrics.stringWidth(text)) / 2;
        int y = (int)(getScreenHeight() * 0.75);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(text, x, y);
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

    public ArrayList<Meteor> getMeteors() {
        return meteors;
    }
}
