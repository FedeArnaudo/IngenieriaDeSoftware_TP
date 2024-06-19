import java.util.Random;
import java.util.ArrayList;

public class GamePanelStateManager {
    private GamePanel gamePanel;
    private final Random randomMeteorSpeed;
    private int lastScore;
    private static GamePanelStateManager instance = new GamePanelStateManager();
    private GamePanelStateManager(){
        randomMeteorSpeed = new Random();
        lastScore = 0;
    }
    public static GamePanelStateManager getInstanceGamePanelStateManager(){
        return instance;
    }

    public void attachToGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void initializeState() {
        gamePanel.setLastState(GamePanel.GameState.NONE);
        gamePanel.setCurrentState(GamePanel.GameState.WELCOME);
        gamePanel.setStartTransitionCompleted(false);
        gamePanel.setLoseTransitionCompleted(false);
        gamePanel.setRestartTransitionCompleted(false);
    }

    public void initializeObstacles() {
        addObstacles(gamePanel.getMeteors1(), Obstacle.ObstacleType.METEOR_1, GamePanel.getMeteors1Number(), GamePanel.getMeteorsSpeedThreshold());
        addObstacles(gamePanel.getMeteors2(), Obstacle.ObstacleType.METEOR_2, GamePanel.getMeteors2Number(), 6);
        addObstacles(gamePanel.getBulletPowerUps(), Obstacle.ObstacleType.BULLET_POWER_UP, 2, 4);
        addObstacles(gamePanel.getBasicPowerUps(), Obstacle.ObstacleType.BASIC_POWER_UP, 2, 4);
        addObstacles(gamePanel.getSuperPowerUps(), Obstacle.ObstacleType.SUPER_POWER_UP, 1, 2);
    }

    private void addObstacles(ArrayList<Obstacle> obstacles, Obstacle.ObstacleType type, int number, int speed) {
        for (int i = 0; i < number; i++) {
            ObstacleImageManager obstacleImageManager = new ObstacleImageManager();
            ObstacleMovementManager obstacleMovementManager = new ObstacleMovementManager();
            Obstacle obstacle = new Obstacle(gamePanel, type, randomMeteorSpeed.nextInt(speed) + 2, obstacleImageManager, obstacleMovementManager);
            obstacles.add(obstacle);
        }
    }

    public void updateState() {
        GamePanel.GameState currentState = gamePanel.getCurrentState();
        switch (currentState) {
            case WELCOME:
                handleWelcomeState();
                break;
            case PLAYING:
                handlePlayingState();
                break;
            case PAUSE:
                handlePauseState();
                break;
            case GAME_OVER:
                handleGameOverState();
                break;
        }
    }

    private void handleWelcomeState() {
        if (gamePanel.getKeyHandler().isEnterPressed()) {
            transitionState(GamePanel.GameState.WELCOME, GamePanel.GameState.PLAYING);
            gamePanel.getKeyHandler().setEnterPressed(false);
        }
    }

    private void handlePlayingState() {
        if (gamePanel.getKeyHandler().isPPressed()) {
            transitionState(GamePanel.GameState.PLAYING, GamePanel.GameState.PAUSE);
        } else if (gamePanel.getShip().getLives() <= 0) {
            transitionState(GamePanel.GameState.PLAYING, GamePanel.GameState.GAME_OVER);
        } else {
            updateGame();
        }
    }

    private void handlePauseState() {
        if (gamePanel.getKeyHandler().isPPressed()) {
            transitionState(GamePanel.GameState.PAUSE, GamePanel.GameState.PLAYING);
        }
    }

    private void handleGameOverState() {
        if (gamePanel.getKeyHandler().isEnterPressed()) {
            transitionState(GamePanel.GameState.GAME_OVER, GamePanel.GameState.PLAYING);
            gamePanel.setLoseTransitionCompleted(false);
            gamePanel.setRestartTransitionCompleted(false);
            restartGame();
            gamePanel.getKeyHandler().setEnterPressed(false);
        }
    }

    private void transitionState(GamePanel.GameState lastState, GamePanel.GameState newState) {
        gamePanel.setLastState(lastState);
        gamePanel.setCurrentState(newState);
    }

    private void restartGame() {
        Ship ship = createNewShip();
        gamePanel.setShip(ship);

        resetObstacles(gamePanel.getMeteors1());
        resetObstacles(gamePanel.getMeteors2());
        resetObstacles(gamePanel.getBulletPowerUps());
        resetObstacles(gamePanel.getBasicPowerUps());
        resetObstacles(gamePanel.getSuperPowerUps());
    }

    private Ship createNewShip() {
        ShipStatsManager shipStatsManager = new ShipStatsManager(GamePanel.getShipsLives(), GamePanel.getShipsSpeed(), GamePanel.getShipsBulletsCapacity());
        ShipMovementManager shipMovementManager = new ShipMovementManager();
        ShipBulletManager shipBulletManager = new ShipBulletManager(GamePanel.getShipsBulletSpeed(), GamePanel.getShipCooldownTime());
        ShipPowerUpManager shipPowerUpManager = new ShipPowerUpManager();
        ShipSoundManager shipSoundManager = new ShipSoundManager();
        ShipImageManager shipImageManager = new ShipImageManager();
        ShipCollisionManager shipCollisionManager = new ShipCollisionManager();

        return new Ship(gamePanel, gamePanel.getKeyHandler(), shipStatsManager, shipMovementManager, shipBulletManager, shipPowerUpManager, shipSoundManager, shipImageManager, shipCollisionManager);
    }

    private void resetObstacles(ArrayList<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            obstacle.setDefaultValues();
        }
    }

    private void updateGame() {
        updateShip();
        updateObstacles(gamePanel.getMeteors1());
        updateObstacles(gamePanel.getMeteors2());
        updateObstacles(gamePanel.getBulletPowerUps());
        updateObstacles(gamePanel.getBasicPowerUps());
        updateObstacles(gamePanel.getSuperPowerUps());
    }

    private void updateShip() {
        gamePanel.getShip().update();

        if (gamePanel.getShip().getScore() / 100 > lastScore) {
            gamePanel.setScoreZoom(1.5);
            lastScore = gamePanel.getShip().getScore() / 100;
        }
    }

    private void updateObstacles(ArrayList<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            obstacle.update();
        }
    }
}
