import java.awt.*;
import java.util.ArrayList;

public class Ship extends Entity{
    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;

    private final ShipMovementManager shipMovementManager;
    private final ShipBulletManager shipBulletManager;
    private final ShipStatsManager shipStatsManager;
    private final ShipPowerUpManager shipPowerUpManager;
    private final ShipSoundManager shipSoundManager;
    private final ShipImageManager shipImageManager;
    private final ShipCollisionManager shipCollisionManager;

    /**
     * Variables for bullets
     */
    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private int bulletsCapacity;
    private int bulletFired;

    /**
     * Variables for ship
     */
    private int lives;
    private int score;
    private int finalScore;
    private int cooldownCounter;

    private ShootingStrategy shootingStrategy;

    private Obstacle.ObstacleType collidedWith;
    private int collisionDebounce;

    public enum PowerUpType { NONE, BULLET, BASIC, SUPER }

    private PowerUpType lastPowerUp;
    private PowerUpType currentPowerUp;

    private int superPowerUpBulletCapacity;
    private int defaultSpeed;

    private Sound singleShootSound;
    private Sound doubleShootSound;
    private Sound laserShootSound;
    private Sound emptySound;
    private Sound impactSound;
    private Sound powerupSound;
    private Sound basicPowerupVoiceSound;
    private Sound superPowerupVoiceSound;
    private Sound explosionSound;
    private Sound meteor2VoiceSound;

    public Ship(GamePanel gamePanel, KeyHandler keyHandler, ShipStatsManager shipStatsManager, ShipMovementManager shipMovementManager, ShipBulletManager shipBulletManager, ShipPowerUpManager shipPowerUpManager, ShipSoundManager shipSoundManager, ShipImageManager shipImageManager, ShipCollisionManager shipCollisionManager){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        shootingStrategy = new SingleBulletStrategy();

        this.shipStatsManager = shipStatsManager;
        this.shipMovementManager = shipMovementManager;
        this.shipBulletManager = shipBulletManager;
        this.shipPowerUpManager = shipPowerUpManager;
        this.shipSoundManager = shipSoundManager;
        this.shipImageManager = shipImageManager;
        this.shipCollisionManager = shipCollisionManager;

        addManagers();
        initializeShip();
        setDefaultValues();
    }

    private void addManagers(){
        shipStatsManager.attachToShip(this);
        shipMovementManager.attachToShip(this);
        shipBulletManager.attachToShip(this);
        shipPowerUpManager.attachToShip(this);
        shipSoundManager.attachToShip(this);
        shipImageManager.attachToShip(this);
        shipCollisionManager.attachToShip(this);
    }

    private void initializeShip(){
        shipStatsManager.initializeStats();
        shipBulletManager.initializeCooldown();
        shipPowerUpManager.initializePowerUp();
        shipSoundManager.initializeSounds();
        shipImageManager.initializeImages();
        shipBulletManager.initializeBullets();
        shipCollisionManager.initializeCollision();
    }

    private void setDefaultValues(){
        x = gamePanel.getScreenWidth() / 2;
        y = (int) (gamePanel.getScreenHeight() * 0.75);
        direction = Direction.UP;
        shipPowerUpManager.resetPowerUp();
    }

    @Override
    public void update(){
        shipCollisionManager.handleCollision();
        shipMovementManager.handleMovement();
        shipPowerUpManager.updatePowerUpStatus();
        shipStatsManager.updateStats();
        shipSoundManager.updateSounds();
        shootingStrategy.handleShooting(this);
        shootingStrategy.updateBullets(this);
        shipBulletManager.updateCooldown();
    }

    public void collide(Obstacle.ObstacleType collideWithType, ArrayList<Obstacle> obstacles, int collideWithIndex){
        shipCollisionManager.collide(collideWithType, obstacles, collideWithIndex);
    }

    @Override
    public void draw(Graphics2D graphics2D){
        shipImageManager.drawBullets(graphics2D);
        shipImageManager.drawShip(graphics2D);
    }


    public void drawWelcomeScreen(Graphics2D graphics2D){
        shipImageManager.drawWelcomeScreen(graphics2D);
    }

    @Override
    public GamePanel getGamePanel() { return gamePanel; }

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    @Override
    public int getSpeed() { return speed; }

    @Override
    public Direction getDirection() { return direction; }

    @Override
    public Rectangle getSolidRectangle() { return solidRectangle; }

    @Override
    public boolean hasCollided() { return collision; }

    public int getCollisionDebounce() { return collisionDebounce; }

    public Obstacle.ObstacleType getCollidedWith() { return collidedWith; }

    public ArrayList<Bullet> getBullets() { return bullets; }

    public int getScore() { return score; }

    public int getFinalScore() { return finalScore; }

    public int getLives() { return lives; }

    public int getBulletsCapacity() { return bulletsCapacity; }

    public int getBulletFired() { return bulletFired; }

    public int getCooldownCounter() { return cooldownCounter; }

    public KeyHandler getKeyHandler() { return keyHandler; }

    public int getDefaultSpeed() { return defaultSpeed; }

    public PowerUpType getLastPowerUp() { return lastPowerUp; }

    public PowerUpType getCurrentPowerUp() { return currentPowerUp; }

    public int getSuperPowerUpBulletCapacity() { return superPowerUpBulletCapacity; }

    public Sound getSingleShootSound() { return singleShootSound; }

    public Sound getDoubleShootSound() { return doubleShootSound; }

    public Sound getLaserShootSound() { return laserShootSound; }

    public Sound getEmptySound() { return emptySound; }

    public Sound getImpactSound() { return impactSound; }

    public Sound getPowerupSound() { return powerupSound; }

    public Sound getBasicPowerupVoiceSound() { return basicPowerupVoiceSound; }

    public Sound getSuperPowerupVoiceSound() { return superPowerupVoiceSound; }

    public Sound getExplosionSound() { return explosionSound; }

    public Sound getMeteor2VoiceSound(){ return meteor2VoiceSound; }

    @Override
    public void setX(int x) { this.x = x; }

    @Override
    public void setY(int y) { this.y = y; }

    @Override
    public void setDirection(Direction direction) { this.direction = direction; }

    @Override
    public void setSpeed(int speed) { this.speed = speed; }

    @Override
    public void setCollision(boolean collision) { this.collision = collision; }

    @Override
    public void setSolidRectangle(Rectangle rectangle) { this.solidRectangle = rectangle; }

    @Override
    public void setSolidAreaDefaultX(int solidAreaDefaultX) { this.solidAreaDefaultX = solidAreaDefaultX; }

    @Override
    public void setSolidAreaDefaultY(int solidAreaDefaultY) { this.solidAreaDefaultY = solidAreaDefaultY; }

    public void setDefaultSpeed(int defaultSpeed) { this.defaultSpeed = defaultSpeed; }

    public void increaseX(int increment) { x += increment; }

    public void decreaseX(int decrement) { x -= decrement; }

    public void increaseY(int increment) { y += increment; }

    public void decreaseY(int decrement) { y -= decrement; }

    public void setBulletFired(int bulletFired) { this.bulletFired = bulletFired; }

    public void setBulletsCapacity(int bulletsCapacity) { this.bulletsCapacity = bulletsCapacity; }

    public void setCooldownCounter(int cooldownCounter) { this.cooldownCounter = cooldownCounter; }

    public void setScore(int score) { this.score = score; }

    public void setFinalScore(int finalScore) { this.finalScore = finalScore; }

    public void setLives(int lives) { this.lives = lives; }

    public void setLastPowerUp(PowerUpType powerUp) { this.lastPowerUp = powerUp; }

    public void setCurrentPowerUp(PowerUpType powerUp) { this.currentPowerUp = powerUp; }

    public void setSuperPowerUpBulletCapacity(int superPowerUpBulletCapacity) { this.superPowerUpBulletCapacity = superPowerUpBulletCapacity; }

    public void setShootingStrategy(ShootingStrategy shootingStrategy) { this.shootingStrategy = shootingStrategy; }

    public void setCollisionDebounce(int collisionDebounce) { this.collisionDebounce = collisionDebounce; }

    public void setCollidedWith(Obstacle.ObstacleType collidedWith) { this.collidedWith = collidedWith; }

    public void setSingleShootSound(Sound singleShootSound) { this.singleShootSound = singleShootSound; }

    public void setDoubleShootSound(Sound doubleShootSound) { this.doubleShootSound = doubleShootSound; }

    public void setLaserShootSound(Sound laserShootSound) { this.laserShootSound = laserShootSound; }

    public void setEmptySound(Sound emptySound) { this.emptySound = emptySound; }

    public void setImpactSound(Sound impactSound) { this.impactSound = impactSound; }

    public void setPowerUpSound(Sound powerupSound) { this.powerupSound = powerupSound; }

    public void setBasicPowerUpVoiceSound(Sound basicPowerupVoiceSound) { this.basicPowerupVoiceSound = basicPowerupVoiceSound; }

    public void setSuperPowerUpVoiceSound(Sound superPowerupVoiceSound) { this.superPowerupVoiceSound = superPowerupVoiceSound; }

    public void setExplosionSound(Sound explosionSound) { this.explosionSound = explosionSound; }

    public void setMeteor2VoiceSound(Sound meteor2VoiceSound) { this.meteor2VoiceSound = meteor2VoiceSound; }

    public void increaseBulletFired(int bulletFired) { this.bulletFired += bulletFired; }

    public void increaseScore(int score) { this.score += score; }

    public void decreaseLives(int decrease) { lives -= decrease; }

    public void decreaseCooldownCounter () { cooldownCounter--; }

    public void increaseCollisionDebounce() { collisionDebounce++; }
}