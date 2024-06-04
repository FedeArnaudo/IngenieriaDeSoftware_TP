package main.java;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Ship extends Entity{
    GamePanel gamePanel;
    KeyHandler keyHandler;

    private final ArrayList<BufferedImage> normalShipImages;
    private final ArrayList<BufferedImage> powerupShipImages;
    private BufferedImage damagedShipImage;
    private final Random random;

    /**
     * Variables for bullets
     */
    ArrayList<Bullet> bullets = new ArrayList<>();
    private int bulletsCapacity;
    private int bulletFired;

    /**
     * Variables for player
     */
    private int lives;
    private int score;
    private int finalScore;
    private final int bulletSpeed;
    private final int cooldownTime;
    private int cooldownCounter;
    private int aliveCounter;

    private ShootingStrategy shootingStrategy = new SingleBulletStrategy(); // default strategy

    private String collidedWith;
    private int collisionDebounce;

    private String powerup;
    private int powerupCounter;
    private final int superPowerupBulletCapacity;
    private final int defaultSpeed;

    private final Sound singleShootSound;
    private final Sound doubleShootSound;
    private final Sound laserShootSound;
    private final Sound emptySound;
    private final Sound impactSound;
    private final Sound powerupSound;
    private final Sound basicPowerupVoiceSound;
    private final Sound superPowerupVoiceSound;
    private final Sound explosionSound;
    private final Sound meteor2VoiceSound;

    public Ship(GamePanel gamePanel, KeyHandler keyHandler, int lives, int speed, int bulletsCapacity, int bulletSpeed, int cooldownTime){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.lives = lives;
        this.bulletsCapacity = bulletsCapacity;
        this.bulletSpeed = bulletSpeed;
        this.cooldownTime = cooldownTime;

        score = 0;
        finalScore = 0;
        cooldownCounter = cooldownTime * GamePanel.getFps();
        random = new Random();
        collision = false;
        collisionDebounce = 0;
        powerupCounter = 0;
        superPowerupBulletCapacity = 1000;
        defaultSpeed = speed;
        normalShipImages = new ArrayList<>();
        powerupShipImages = new ArrayList<>();
        solidRectangle = new Rectangle(3, 5, 52, 40);
        solidAreaDefaultX = solidRectangle.x;
        solidAreaDefaultY = solidRectangle.y;
        singleShootSound = new Sound("res/sounds/single_shoot_sound.wav");
        doubleShootSound = new Sound("res/sounds/double_shoot_sound.wav");
        laserShootSound = new Sound("res/sounds/laser_shoot_sound.wav");
        emptySound = new Sound("res/sounds/empty_bullet.wav");
        impactSound = new Sound("res/sounds/impact.wav");
        powerupSound = new Sound("res/sounds/start_game.wav");
        superPowerupVoiceSound = new Sound("res/sounds/super_power_up.wav");
        basicPowerupVoiceSound = new Sound("res/sounds/basic_power_up.wav");
        explosionSound = new Sound("res/sounds/explosion.wav");
        meteor2VoiceSound = new Sound("res/sounds/meteor_2_voice.wav");

        setDefaultValues();
        initializeBullets();
        setShipImages();
    }

    private void setDefaultValues(){
        x = gamePanel.getScreenWidth() / 2;
        y = (int) (gamePanel.getScreenHeight() * 0.75);
        speed = defaultSpeed;
        direction = "up";
        powerup = "none";
    }

    private void loseBasicPowerup() {
        powerup = "none";
        powerupCounter = 0;
        setShootingStrategy(new SingleBulletStrategy());
    }

    private void loseSuperPowerup() {
        powerup = "none";
        powerupCounter = 0;
        speed = defaultSpeed;
        bulletFired = 0;
        bulletsCapacity = 100;
        setShootingStrategy(new SingleBulletStrategy());
    }

    private void initializeBullets() {
        for(int i = 0; i < superPowerupBulletCapacity; i++){
            bullets.add(new Bullet(gamePanel, keyHandler, this, bulletSpeed));
        }
        bulletFired = 0;
    }

    public ArrayList<BufferedImage> getNormalShipImages() {
        return normalShipImages;
    }

    public ArrayList<BufferedImage> getPowerupShipImages() {
        return powerupShipImages;
    }

    public void setShipImages() {
        try {
            BufferedImage ship1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip194.png")));
            BufferedImage ship2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip195.png")));
            BufferedImage ship3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip196.png")));
            BufferedImage ship4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip197.png")));
            BufferedImage ship5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip198.png")));

            normalShipImages.add(ship1);
            normalShipImages.add(ship2);
            normalShipImages.add(ship3);
            normalShipImages.add(ship4);
            normalShipImages.add(ship5);

            ship1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/powerup_ship/powerup_ship_1.png")));
            ship2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/powerup_ship/powerup_ship_2.png")));
            ship3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/powerup_ship/powerup_ship_3.png")));
            ship4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/powerup_ship/powerup_ship_4.png")));
            ship5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/powerup_ship/powerup_ship_5.png")));

            powerupShipImages.add(ship1);
            powerupShipImages.add(ship2);
            powerupShipImages.add(ship3);
            powerupShipImages.add(ship4);
            powerupShipImages.add(ship5);

            damagedShipImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/damaged_ship.png")));
        }
        catch (IOException e){
            throw new RuntimeException("Could not load player image", e);
        }
    }

    @Override
    public void update(){
        shootingStrategy.handleShooting(this);
        shootingStrategy.updateBullets(this);
        checkPowerup();
        handleMovement();
        updateScore();

        if(bulletFired == bulletsCapacity){
            cooldownCounter--;
        }

        if(cooldownCounter == 0){
            bulletFired = 50;

            cooldownCounter = cooldownTime * GamePanel.getFps();
        }
    }

    private void handleMovement() {
        if(keyHandler.getLeftPressed()){
            direction = "left";
            if(getX() > 0) {
                moveLeft();
            }
        }

        else if (keyHandler.getRightPressed()) {
            direction = "right";
            if(getX() < (gamePanel.getScreenWidth() - gamePanel.getTileSize())){
                moveRight();
            }
        }

        if (keyHandler.getUpPressed()) {
            direction = "up";
            if (getY() > 0) {
                moveUp();
            }
        }

        if (keyHandler.getDownPressed()) {
            direction = "down";
            if (getY() < (gamePanel.getScreenHeight() - gamePanel.getTileSize())) {
                moveDown();
            }
        }

        // detectObject
        if (!collision) {
            gamePanel.collisionChecker.detectObjet(this);
        }

        if (collision && collisionDebounce <= 10){
            collisionDebounce++;
        }
        else if (collision){
            collision = false;
            collisionDebounce = 0;
        }
    }

    private void moveLeft() {
        x -= getSpeed();
    }

    private void moveRight() {
        x += getSpeed();
    }

    private void moveUp() {
        y -= getSpeed();
    }

    private void moveDown() {
        y += getSpeed();
    }

    private void updateScore() {
        if (aliveCounter == 30){
            score += 1;
            aliveCounter = 0;
        }
        else {
            aliveCounter++;
        }

        if (score > 999999 || lives == 0) {
            finalScore = score;
            score = 0;
        }
    }

    private void checkPowerup() {
        if (powerup.equals("basic_power_up") || powerup.equals("super_power_up")) {
            powerupCounter++;
            if (powerup.equals("super_power_up") && powerupCounter == 30 * GamePanel.getFps()) {
                loseSuperPowerup();
            } else if (powerup.equals("basic_power_up") && powerupCounter == 30 * GamePanel.getFps()) {
                loseBasicPowerup();
            }
        }
    }

    private void setShootingStrategy(ShootingStrategy shootingStrategy) {
        this.shootingStrategy = shootingStrategy;
    }

    public void collide(String collideWithType, ArrayList<Obstacle> obstacles, int collideWithIndex){
        collidedWith = collideWithType;

        switch (collideWithType){
            case "meteor_1":
                lives--;
                impactSound.play();
                break;

            case "meteor_2":
                lives -= 2;
                impactSound.play();
                break;

            case "bullet_power_up":
                bulletFired = 0;
                powerupSound.play();
                break;

            case "basic_power_up":
                if (powerup.equals("none")){
                    powerup = "basic_power_up";
                    bulletsCapacity = bulletsCapacity * 2;
                    bulletFired = 0;
                    setShootingStrategy(new DoubleBulletStrategy());
                    powerupSound.play();
                    basicPowerupVoiceSound.play();
                }
                break;

            case "super_power_up":
                if (powerup.equals("none") || powerup.equals("basic_power_up")){
                    powerup = "super_power_up";
                    lives = 7;
                    speed += 3;
                    bulletsCapacity = superPowerupBulletCapacity;
                    bulletFired = 0;
                    setShootingStrategy(new LaserBulletStrategy());
                    powerupSound.play();
                    superPowerupVoiceSound.play();
                }
                break;
        }

        obstacles.get(collideWithIndex).setCollision(true);
        this.collision = true;
    }

    @Override
    public void draw(Graphics2D graphics2D){
        drawBullets(graphics2D);
        drawPlayer(graphics2D);
    }

    private void drawBullets(Graphics2D graphics2D) {
        for (int i = 0; i < bulletsCapacity; i++){
            bullets.get(i).draw(graphics2D);
        }
    }

    private void drawPlayer(Graphics2D graphics2D) {
        if (collision && collisionDebounce <= 10 && (collidedWith.equals("meteor_1") || collidedWith.equals("meteor_2"))){
            graphics2D.drawImage(damagedShipImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
        } else if (powerup.equals("super_power_up")) {
            BufferedImage bufferedImage = powerupShipImages.get(random.nextInt(getPowerupShipImages().size()));
            graphics2D.drawImage(bufferedImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
        } else {
            BufferedImage bufferedImage = normalShipImages.get(random.nextInt(getNormalShipImages().size()));
            graphics2D.drawImage(bufferedImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
        }
        //graphics2D.setColor(Color.RED);
        //graphics2D.drawRect(x + solidAreaDefaultX, y + solidAreaDefaultY, solidRectangle.width, solidRectangle.height);
    }

    public void drawWelcomeScreen(Graphics2D graphics2D){
        int x = gamePanel.getScreenWidth() / 2 + 150;
        int y = (int)(gamePanel.getScreenHeight() * 0.24);

        BufferedImage bufferedImage = normalShipImages.get(random.nextInt(getNormalShipImages().size()));
        graphics2D.drawImage(bufferedImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() { return y; }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public String getDirection() {
        return direction;
    }

    @Override
    public Rectangle getSolidRectangle() {
        return solidRectangle;
    }

    @Override
    public int getSolidAreaDefaultX() {
        return solidAreaDefaultX;
    }

    @Override
    public int getSolidAreaDefaultY() {
        return solidAreaDefaultY;
    }

    @Override
    public boolean getCollision() {
        return collision;
    }

    public int getScore() {
        return score;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public int getLives() {
        return lives;
    }

    public int getBulletsCapacity() {
        return bulletsCapacity;
    }

    public int getBulletFired() {
        return bulletFired;
    }

    public int getCooldownCounter() {
        return cooldownCounter;
    }

    public Sound getSingleShootSound() {
        return singleShootSound;
    }

    public Sound getDoubleShootSound() {
        return doubleShootSound;
    }

    public Sound getLaserShootSound() {
        return laserShootSound;
    }

    public Sound getEmptySound() {
        return emptySound;
    }

    public Sound getExplosionSound() {
        return explosionSound;
    }

    public Sound getMeteor2VoiceSound(){
        return meteor2VoiceSound;
    }

    public void increaseBulletFired(int bulletFired) {
        this.bulletFired += bulletFired;
    }

    public void setBulletFired(int bulletFired) {
        this.bulletFired = bulletFired;
    }

    public void increaseScore(int score) {
        this.score += score;
    }
}