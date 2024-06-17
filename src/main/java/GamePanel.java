import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel implements Runnable {
    private final GamePanelImageManager gamePanelImageManager;
    private final GamePanelSoundManager gamePanelSoundManager;
    private final GamePanelDrawManager gamePanelDrawManager;
    private final GamePanelStateManager gamePanelStateManager;

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
     * This object represents the ship that the user controls.
     */
    private Ship ship;

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
    private double floatTime = 0;
    private Sound welcomeScreenVoiceSound;
    private Sound startGameSound;
    private Sound startGameVoiceSound;

    /**
     * Variables for game over screen
     */
    private Sound gameOverSound;
    private Sound gameOverVoiceSound;

    /**
     * Array for scoreboard numbers
     */
    private double scoreZoom = 1.0;

    private final ArrayList<Obstacle> bulletPowerUps = new ArrayList<>();
    private final ArrayList<Obstacle> basicPowerUps = new ArrayList<>();
    private final ArrayList<Obstacle> superPowerUps = new ArrayList<>();

    private final Map<Character, BufferedImage> letterImages = new HashMap<>();
    private final Map<Integer, BufferedImage> numberImages = new HashMap<>();
    private BufferedImage bulletScoreboardImage;
    private BufferedImage liveImage;
    private BufferedImage collingIconImage;
    private BufferedImage keyUpImage;
    private BufferedImage keyDownImage;
    private BufferedImage keyRightImage;
    private BufferedImage keyLeftImage;
    private BufferedImage keySpaceImage;

    public enum GameState { NONE, WELCOME, PLAYING, PAUSE, GAME_OVER }

    private GameState lastState;
    private GameState currentState;
    private boolean startTransitionCompleted;
    private boolean loseTransitionCompleted;
    private boolean restartTransitionCompleted;

    public GamePanel(GamePanelImageManager gamePanelImageManager, GamePanelSoundManager gamePanelSoundManager, GamePanelDrawManager gamePanelDrawManager, GamePanelStateManager gamePanelStateManager) {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        this.gamePanelImageManager = gamePanelImageManager;
        this.gamePanelSoundManager = gamePanelSoundManager;
        this.gamePanelDrawManager = gamePanelDrawManager;
        this.gamePanelStateManager = gamePanelStateManager;

        addManagers();
        initializeGamePanel();
    }

    private void addManagers() {
        gamePanelImageManager.attachToGamePanel(this);
        gamePanelSoundManager.attachToGamePanel(this);
        gamePanelDrawManager.attachToGamePanel(this);
        gamePanelStateManager.attachToGamePanel(this);
    }

    private void initializeGamePanel() {
        createNewShip();
        gamePanelStateManager.initializeState();
        gamePanelImageManager.loadGamePanelImages();
        gamePanelSoundManager.loadGamePanelSounds();
        gamePanelStateManager.initializeObstacles();
    }

    private void createNewShip() {
        ShipStatsManager shipStatsManager = new ShipStatsManager(SHIPS_LIVES, SHIPS_SPEED, SHIPS_BULLETS_CAPACITY);
        ShipMovementManager shipMovementManager = new ShipMovementManager();
        ShipBulletManager shipBulletManager = new ShipBulletManager(SHIPS_BULLET_SPEED, SHIP_COOLDOWN_TIME);
        ShipPowerUpManager shipPowerUpManager = new ShipPowerUpManager();
        ShipSoundManager shipSoundManager = new ShipSoundManager();
        ShipImageManager shipImageManager = new ShipImageManager();
        ShipCollisionManager shipCollisionManager = new ShipCollisionManager();

        ship = new Ship(this, keyHandler, shipStatsManager, shipMovementManager, shipBulletManager, shipPowerUpManager, shipSoundManager, shipImageManager, shipCollisionManager);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        gamePanelSoundManager.startBackgroundMusic();

        double drawInterval = calculateDrawInterval();
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            gamePanelStateManager.updateState();
            gamePanelSoundManager.updateMusic();

            floatTime += 0.05;

            repaint();

            nextDrawTime = sleepAndCalculateNextDrawTime(drawInterval, nextDrawTime);
        }
    }

    private double calculateDrawInterval() {
        return  1000000000.0 / FPS;   //1ps/FPS   1ps/60 = 16.6666666667ms
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

        gamePanelDrawManager.drawGameElements(graphics2D);
    }

    public static int getFps() { return FPS; }

    public static int getOriginalTileSize() { return ORIGINAL_TILE_SIZE; }

    public static int getMaxScreenCol() { return MAX_SCREEN_COL; }

    public int getTileSize() { return TILE_SIZE; }

    public int getScreenWidth() { return SCREEN_WIDTH; }

    public int getScreenHeight() { return SCREEN_HEIGHT; }

    public static int getMeteors1Number() { return METEORS_1_NUMBER; }

    public static int getMeteors2Number() { return METEORS_2_NUMBER; }

    public static int getMeteorsSpeedThreshold() { return METEORS_SPEED_THRESHOLD; }

    public static int getShipsLives() { return SHIPS_LIVES; }

    public static int getShipsSpeed() { return SHIPS_SPEED; }

    public static int getShipsBulletsCapacity() { return SHIPS_BULLETS_CAPACITY; }

    public static int getShipsBulletSpeed() { return SHIPS_BULLET_SPEED; }

    public static int getShipCooldownTime() { return SHIP_COOLDOWN_TIME; }

    public GameState getLastState() { return lastState; }

    public GameState getCurrentState() { return currentState; }

    public boolean isStartTransitionNotCompleted() { return !startTransitionCompleted; }

    public boolean isLoseTransitionNotCompleted() { return !loseTransitionCompleted; }

    public boolean isRestartTransitionNotCompleted() { return !restartTransitionCompleted; }

    public KeyHandler getKeyHandler() { return keyHandler; }

    public Ship getShip() { return ship; }

    public TileManager getTileManager() { return tileManager; }

    public CollisionChecker getCollisionChecker() { return collisionChecker; }

    public double getFloatTime() { return floatTime; }

    public double getScoreZoom() { return scoreZoom; }

    public ArrayList<Obstacle> getMeteors1() { return meteors_1; }

    public ArrayList<Obstacle> getMeteors2() { return meteors_2; }

    public ArrayList<Obstacle> getBulletPowerUps() { return bulletPowerUps; }

    public ArrayList<Obstacle> getBasicPowerUps() { return basicPowerUps; }

    public ArrayList<Obstacle> getSuperPowerUps() { return superPowerUps; }

    public Map<Character, BufferedImage> getLetterImages() { return letterImages; }

    public Map<Integer, BufferedImage> getNumberImages() { return numberImages; }

    public BufferedImage getBulletScoreboardImage() { return bulletScoreboardImage; }

    public BufferedImage getLiveImage() { return liveImage; }

    public BufferedImage getCollingIconImage() { return collingIconImage; }

    public BufferedImage getKeyUpImage() { return keyUpImage; }

    public BufferedImage getKeyDownImage() { return keyDownImage; }

    public BufferedImage getKeyRightImage() { return keyRightImage; }

    public BufferedImage getKeyLeftImage() { return keyLeftImage; }

    public BufferedImage getKeySpaceImage() { return keySpaceImage; }

    public Sound getStartGameSound() { return startGameSound; }

    public Sound getStartGameVoiceSound() { return startGameVoiceSound; }

    public Sound getWelcomeScreenVoiceSound() { return welcomeScreenVoiceSound; }

    public Sound getGameOverSound() { return gameOverSound; }

    public Sound getGameOverVoiceSound() { return gameOverVoiceSound; }

    public void setShip(Ship ship) { this.ship = ship; }

    public void setLastState(GameState lastState) { this.lastState = lastState; }

    public void setCurrentState(GameState currentState) { this.currentState = currentState; }

    public void setStartTransitionCompleted(boolean startTransitionCompleted) { this.startTransitionCompleted = startTransitionCompleted; }

    public void setLoseTransitionCompleted(boolean loseTransitionCompleted) { this.loseTransitionCompleted = loseTransitionCompleted; }

    public void setRestartTransitionCompleted(boolean restartTransitionCompleted) { this.restartTransitionCompleted = restartTransitionCompleted; }

    public void setScoreZoom(double scoreZoom) { this.scoreZoom = scoreZoom; }

    public void setKeyUpImage(BufferedImage keyUpImage) { this.keyUpImage = keyUpImage; }

    public void setKeyDownImage(BufferedImage keyDownImage) { this.keyDownImage = keyDownImage; }

    public void setKeyRightImage(BufferedImage keyRightImage) { this.keyRightImage = keyRightImage; }

    public void setKeyLeftImage(BufferedImage keyLeftImage) { this.keyLeftImage = keyLeftImage; }

    public void setKeySpaceImage(BufferedImage keySpaceImage) { this.keySpaceImage = keySpaceImage; }

    public void setBulletScoreboardImage(BufferedImage bulletScoreboardImage) { this.bulletScoreboardImage = bulletScoreboardImage; }

    public void setLiveImage(BufferedImage liveImage) { this.liveImage = liveImage; }

    public void setCoolingIconImage(BufferedImage collingIconImage) { this.collingIconImage = collingIconImage; }

    public void setWelcomeScreenVoiceSound(Sound welcomeScreenVoiceSound) { this.welcomeScreenVoiceSound = welcomeScreenVoiceSound; }

    public void setStartGameSound(Sound startGameSound) { this.startGameSound = startGameSound; }

    public void setStartGameVoiceSound(Sound startGameVoiceSound) { this.startGameVoiceSound = startGameVoiceSound; }

    public void setGameOverSound(Sound gameOverSound) { this.gameOverSound = gameOverSound; }

    public void setGameOverVoiceSound(Sound gameOverVoiceSound) { this.gameOverVoiceSound = gameOverVoiceSound; }
}