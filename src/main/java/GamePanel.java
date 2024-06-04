package main.java;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GamePanel extends JPanel implements Runnable {

    /**
     * Screen Settings
     */
    private static final int ORIGINAL_TILE_SIZE = 19;   //  19x19
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
    private static final int METEORS_1_NUMBER = 20;
    private static final int METEORS_2_NUMBER = 3;
    private static final int METEORS_SPEED_THRESHOLD = 8;
    private static final int SHIPS_LIVES = 7;
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
    ArrayList<Obstacle> meteors_1 = new ArrayList<>();
    ArrayList<Obstacle> meteors_2 = new ArrayList<>();

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
    private Sound welcomeScreenVoiceSound;
    private Sound startGameSound;
    private Sound startGameVoiceSound;
    BufferedImage keyUpImage;
    BufferedImage keyDownImage;
    BufferedImage keyRightImage;
    BufferedImage keyLeftImage;
    BufferedImage keySpaceImage;

    /**
     * Variables for pause screen
     */
    private boolean isPaused = false;

    /**
     * Variables for game over screen
     */
    private boolean gameOver = false;
    private int gameOverCounter = 0;
    private Sound gameOverSound;
    private Sound gameOverVoiceSound;

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

    /**
     * Variables for meteors
     */
    private final Random randomMeteorSpeed;

    private final ArrayList<Obstacle> bulletPowerUps = new ArrayList<>();
    private final ArrayList<Obstacle> basicPowerUps = new ArrayList<>();
    private final ArrayList<Obstacle> superPowerUps = new ArrayList<>();

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        randomMeteorSpeed = new Random();
        dotCounter = 1;

        loadLetterImages();
        loadNumberImages();
        loadKeyImages();
        loadBulletScoreboardImage();
        loadLiveImage();
        loadCoolingImage();
        loadGamePanelSounds();
        initializeObstacles();
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

    private void loadKeyImages() {
        try {
            keyUpImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/keys/key_up.png")));
            keyDownImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/keys/key_down.png")));
            keyRightImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/keys/key_right.png")));
            keyLeftImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/keys/key_left.png")));
            keySpaceImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/keys/key_space.png")));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load key images", e);
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

    private void loadGamePanelSounds() {
        welcomeScreenVoiceSound = new Sound("res/sounds/welcome_to_spaceships.wav");
        startGameSound = new Sound("res/sounds/start_game.wav");
        startGameVoiceSound = new Sound("res/sounds/start_game_voice.wav");
        gameOverSound = new Sound("res/sounds/game_over.wav");
        gameOverVoiceSound = new Sound("res/sounds/game_over_voice.wav");
    }

    private void initializeObstacles() {
        for (int i = 0; i < METEORS_1_NUMBER; i++) {
            meteors_1.add(new Obstacle(this, "meteor_1", randomMeteorSpeed.nextInt(METEORS_SPEED_THRESHOLD) + 2));
        }

        for (int i = 0; i < METEORS_2_NUMBER; i++) {
            meteors_2.add(new Obstacle(this, "meteor_2", randomMeteorSpeed.nextInt(6) + 2));
        }

        for (int i = 0; i < 2; i++) {
            bulletPowerUps.add(new Obstacle(this, "bullet_power_up", randomMeteorSpeed.nextInt(4) + 2));
        }

        for (int i = 0; i < 2; i++) {
            basicPowerUps.add(new Obstacle(this, "basic_power_up", randomMeteorSpeed.nextInt(4) + 2));
        }

        for (int i = 0; i < 1; i++) {
            superPowerUps.add(new Obstacle(this, "super_power_up", randomMeteorSpeed.nextInt(2) + 2));
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        Clip backgroundMusic = playMusic("res/music/welcome_screen_background.wav");
        welcomeScreenVoiceSound.play();
        Clip pauseMusic = null;

        double drawInterval = calculateDrawInterval();
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            if (!welcomeScreen && !gameOver) { // Check if the game is paused

                if (keyHandler.getPPressed() && !isPaused) {
                    isPaused = true;

                    backgroundMusic.stop();
                    pauseMusic = playMusic("res/music/pause_background.wav");

                    keyHandler.setPPressed(false);
                } else if (keyHandler.getPPressed() && isPaused) {
                    isPaused = false;

                    if (pauseMusic != null) {
                        pauseMusic.stop();
                    }

                    backgroundMusic.start();

                    keyHandler.setPPressed(false);
                }
            }

            if (!isPaused) { // Check if the game is not paused
                if (gameOver) { // Game over screen
                    if (gameOverCounter == 0) {
                        gameOverSound.play();
                        gameOverVoiceSound.play();
                        backgroundMusic = playNewMusic(backgroundMusic, "res/music/game_over_background.wav");
                    }

                    gameOverCounter++;
                }

                if (keyHandler.getEnterPressed() && welcomeScreen) { // Pressed enter key on welcome screen -> start the game
                    welcomeScreen = false;

                    startGameSound.play();
                    startGameVoiceSound.play();
                    backgroundMusic = playNewMusic(backgroundMusic, "res/music/background.wav");

                    keyHandler.setEnterPressed(false);
                }

                if (keyHandler.getEnterPressed() && gameOver) { // Pressed enter key on game over screen -> restart the game
                    restartGame();

                    startGameSound.play();
                    startGameVoiceSound.play();
                    backgroundMusic = playNewMusic(backgroundMusic, "res/music/background.wav");

                    keyHandler.setEnterPressed(false);
                }

                if (!welcomeScreen && !gameOver) { // Update the game if it is not paused
                    updateGame();
                }
            }

            floatTime += 0.05;

            repaint();

            nextDrawTime = sleepAndCalculateNextDrawTime(drawInterval, nextDrawTime);
        }
    }

    private void restartGame() {
        gameOver = false;
        gameOverCounter = 0;

        ship = new Ship(this, keyHandler, SHIPS_LIVES, SHIPS_SPEED, SHIPS_BULLETS_CAPACITY, SHIPS_BULLET_SPEED, SHIP_COOLDOWN_TIME);

        for (Obstacle obstacle : meteors_1) {
            obstacle.setDefaultValues();
        }

        for (Obstacle obstacle : meteors_2) {
            obstacle.setDefaultValues();
        }

        for (Obstacle obstacle : bulletPowerUps) {
            obstacle.setDefaultValues();
        }

        for (Obstacle obstacle : basicPowerUps) {
            obstacle.setDefaultValues();
        }

        for (Obstacle obstacle : superPowerUps) {
            obstacle.setDefaultValues();
        }
    }

    private double calculateDrawInterval() {
        return (double) 1000000000 / FPS;   //1ps/FPS   1ps/60 = 16.6666666667ms
    }

    private void updateGame() {
        updatePlayer();
        updateObstacles();
    }

    private void updatePlayer() {
        ship.update();

        if (ship.getLives() <= 0) {
            gameOver = true;
        }

        if (ship.getScore() / 100 > lastScore) {
            scoreZoom = 1.5;
            lastScore = ship.getScore() / 100;
        }
    }

    private void updateObstacles() {
        for (Obstacle meteor_1 : meteors_1) {
            meteor_1.update();
        }

        for (Obstacle meteor_2 : meteors_2) {
            meteor_2.update();
        }

        for (Obstacle bulletPowerUp : bulletPowerUps) {
            bulletPowerUp.update();
        }

        for (Obstacle basicPowerUp : basicPowerUps) {
            basicPowerUp.update();
        }

        for (Obstacle superPowerUp : superPowerUps) {
            superPowerUp.update();
        }
    }

    private double sleepAndCalculateNextDrawTime(double drawInterval, double nextDrawTime) {
        try {
            double remainingTime = nextDrawTime - System.nanoTime();
            remainingTime = remainingTime / 1000000;

            if (remainingTime < 0) {
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

        if (welcomeScreen) { // Draw the welcome screen
            drawWelcomeScreen(graphics2D);
            ship.drawWelcomeScreen(graphics2D);
        } else if (gameOver) { // Draw the game over screen
            drawGameOverScreen(graphics2D);
        } else { // Draw the game
            ship.draw(graphics2D);

            drawObstacles(graphics2D);

            drawScore(graphics2D);

            if (ship.getBulletFired() == ship.getBulletsCapacity()) {
                drawCooldownCountdown(graphics2D);
            } else {
                drawBulletsLeft(graphics2D);
            }

            drawLives(graphics2D);
        }
    }

    private void drawObstacles(Graphics2D graphics2D) {
        for (Obstacle meteor_1 : meteors_1) {
            meteor_1.draw(graphics2D);
        }

        for (Obstacle meteor_2 : meteors_2) {
            meteor_2.draw(graphics2D);
        }

        for (Obstacle bulletPowerUp : bulletPowerUps) {
            bulletPowerUp.draw(graphics2D);
        }

        for (Obstacle basicPowerUp : basicPowerUps) {
            basicPowerUp.draw(graphics2D);
        }

        for (Obstacle superPowerUp : superPowerUps) {
            superPowerUp.draw(graphics2D);
        }
    }

    private void drawWelcomeScreen(Graphics2D graphics2D) {
        String word = "spaceships";
        int startX = (getScreenWidth() - word.length() * letterImages.get('a').getWidth() * 2) / 2 - 60;
        int y = (int) (getScreenHeight() * 0.25);

        drawFloatingText(graphics2D, "spaceships", letterImages, startX, y, floatTime);

        // Draw the "press enter to play" text
        drawCenteredText(graphics2D, "PRESS ENTER TO PLAY", new Font("Courier New", Font.PLAIN, 20));

        drawInstructions(graphics2D);
    }

    private void drawPauseScreen(Graphics2D graphics2D) {
        graphics2D.setColor(Color.decode("#000422"));
        graphics2D.fillRect(0, 0, getScreenWidth(), getScreenHeight());

        String word = "pause";
        int startX = (getScreenWidth() - word.length() * letterImages.get('a').getWidth() * 2) / 2;
        int y = (int) (getScreenHeight() * 0.25);

        drawFloatingText(graphics2D, "pause", letterImages, startX, y, floatTime);

        // Draw the "press P to resume" text
        drawCenteredText(graphics2D, "PRESS P TO RESUME", new Font("Courier New", Font.PLAIN, 20));

        drawInstructions(graphics2D);
    }

    private void drawGameOverScreen(Graphics2D graphics2D) {
        String word = "game over";
        int startX = (getScreenWidth() - word.length() * letterImages.get('a').getWidth() * 2) / 2;
        int y = (int) (getScreenHeight() * 0.25);

        drawFloatingText(graphics2D, word, letterImages, startX, y, floatTime);

        // Draw the "press enter to restart" text
        drawCenteredText(graphics2D, "PRESS ENTER TO RESTART", new Font("Courier New", Font.PLAIN, 20));

        // Draw the score
        String scoreText = String.format("%06d", ship.getFinalScore());
        int sizeMultiplier = 2; // make the score bigger
        int totalWidth = numberImages.get(0).getWidth() * sizeMultiplier * scoreText.length();
        int x = (getScreenWidth() - totalWidth) / 2; // center the score

        // Draw the "YOUR FINAL SCORE:" text above the score
        String finalScoreText = "YOUR FINAL SCORE:";
        graphics2D.setFont(new Font("Courier New", Font.PLAIN, 20));
        graphics2D.setColor(Color.WHITE);
        int finalScoreTextX = (getScreenWidth() - graphics2D.getFontMetrics().stringWidth(finalScoreText)) / 2;
        int finalScoreTextY = (int) (getScreenHeight() * 0.65) - 30; // 30 pixels above the score
        graphics2D.drawString(finalScoreText, finalScoreTextX, finalScoreTextY);

        for (char c : scoreText.toCharArray()) {
            int number = Character.getNumericValue(c);
            BufferedImage numberImage = numberImages.get(number);
            graphics2D.drawImage(numberImage, x, (int) (getScreenHeight() * 0.65), numberImage.getWidth() * sizeMultiplier, numberImage.getHeight() * sizeMultiplier, null);
            x += numberImage.getWidth() * sizeMultiplier;
        }
    }

    private void drawScore(Graphics2D graphics2D) {
        String scoreString = String.format("%06d", ship.getScore());
        int padding = 20; // padding from the corner
        int sizeMultiplier = 2; // make the score bigger
        int x = getScreenWidth() - (numberImages.get(0).getWidth() * sizeMultiplier * scoreString.length()) - padding;

        for (char c : scoreString.toCharArray()) {
            int number = Character.getNumericValue(c);
            BufferedImage numberImage = numberImages.get(number);
            graphics2D.drawImage(numberImage, x, padding, (int) (numberImage.getWidth() * sizeMultiplier * scoreZoom), (int) (numberImage.getHeight() * sizeMultiplier * scoreZoom), null);
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
                int floatOffset = (int) (Math.sin(floatTime + i) * 5); // Calculate a floatOffset for each letter
                graphics2D.drawImage(img, startX + i * img.getWidth() * 2, y + floatOffset, img.getWidth() * 2, img.getHeight() * 2, null); // Add the floatOffset to the y-position
            }
        }
    }

    private void drawCenteredText(Graphics2D graphics2D, String text, Font font) {
        graphics2D.setFont(font);
        FontMetrics metrics = graphics2D.getFontMetrics(font);
        int x = (getScreenWidth() - metrics.stringWidth(text)) / 2;
        int y = (int) (getScreenHeight() * 0.85);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(text, x, y);
    }

    private void drawInstructions(Graphics2D graphics2D) {
        int y = 450; // adjust this as needed
        int padding = 5; // decrease this value to bring the images closer together
        int x = (getScreenWidth() - (keyUpImage.getWidth() + keyDownImage.getWidth() + keyRightImage.getWidth() + keyLeftImage.getWidth() + padding * 2)) / 4; // center the images
        int spaceX = ((getScreenWidth() - keySpaceImage.getWidth()) / 4) * 3; // center the space key

        // Draw the up key
        graphics2D.drawImage(keyUpImage, x + keyUpImage.getWidth() + padding, y, null);

        // Draw the left key
        graphics2D.drawImage(keyLeftImage, x, y + keyLeftImage.getHeight() + padding, null);

        // Draw the down key
        graphics2D.drawImage(keyDownImage, x + keyDownImage.getWidth() + padding, y + keyDownImage.getHeight() + padding, null);

        // Draw the right key
        graphics2D.drawImage(keyRightImage, x + keyRightImage.getWidth() * 2 + padding * 2, y + keyRightImage.getHeight() + padding, null);

        // Draw the space key below the arrow keys
        graphics2D.drawImage(keySpaceImage, spaceX, y + keySpaceImage.getHeight() + padding, null);

        // Set the font and color for the text
        graphics2D.setFont(new Font("Courier New", Font.PLAIN, 20));
        graphics2D.setColor(Color.WHITE);

        // Calculate the position of the "MOVE" text and draw it
        String moveText = "MOVE";
        int moveTextWidth = graphics2D.getFontMetrics().stringWidth(moveText);
        int moveTextX = x + keyDownImage.getWidth() + padding;
        int moveTextY = y + keyDownImage.getHeight() * 3;
        graphics2D.drawString(moveText, moveTextX, moveTextY);

        // Calculate the position of the "SHOOT" text and draw it
        String shootText = "SHOOT";
        int shootTextWidth = graphics2D.getFontMetrics().stringWidth(shootText);
        int shootTextX = spaceX + (keySpaceImage.getWidth() - shootTextWidth) / 2;
        int shootTextY = y + keyDownImage.getHeight() * 3;
        graphics2D.drawString(shootText, shootTextX, shootTextY);
    }

    public Clip playMusic(String filePath) {
        try {
            URL url = this.getClass().getClassLoader().getResource(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("Could not play music", e);
        }
    }

    private Clip playNewMusic(Clip backgroundMusic, String musicPath) {
        backgroundMusic.stop();
        return playMusic(musicPath);
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

    public ArrayList<Obstacle> getMeteors1() {
        return meteors_1;
    }

    public ArrayList<Obstacle> getMeteors2() {
        return meteors_2;
    }

    public ArrayList<Obstacle> getBulletPowerUps() {
        return bulletPowerUps;
    }

    public ArrayList<Obstacle> getBasicPowerUps() {
        return basicPowerUps;
    }

    public ArrayList<Obstacle> getSuperPowerUps() {
        return superPowerUps;
    }

}
