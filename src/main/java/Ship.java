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

    private final ArrayList<BufferedImage> bufferedImages;
    private BufferedImage damagedShipImage;
    private final Random random;

    /**
     * Variables for bullets
     */
    ArrayList<Bullet> bullets = new ArrayList<>();
    private final int bulletsCapacity;
    private int bulletFired;

    /**
     * Variables for player
     */
    private int lives;
    private int score;
    private int bulletSpeed;
    private final int cooldownTime;
    private int cooldownCounter;
    private int aliveCounter;

    private ShootingStrategy shootingStrategy = new SingleBulletStrategy(); // default strategy

    private int collisionDebounce;

    public Ship(GamePanel gamePanel, KeyHandler keyHandler, int lives, int speed, int bulletsCapacity, int bulletSpeed, int cooldownTime){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.lives = lives;
        this.speed = speed;
        this.bulletsCapacity = bulletsCapacity;
        this.bulletSpeed = bulletSpeed;
        this.cooldownTime = cooldownTime;

        score = 0;
        cooldownCounter = cooldownTime * GamePanel.getFps();
        random = new Random();
        collision = false;
        collisionDebounce = 0;
        bufferedImages = new ArrayList<>();
        solidRectangle = new Rectangle(3, 5, 52, 40);
        solidAreaDefaultX = solidRectangle.x;
        solidAreaDefaultY = solidRectangle.y;

        setDefaultValues();
        initializeBullets();
        setPlayerImage();
    }

    private void setDefaultValues(){
        x = 400;
        y = 850;
        direction = "up";
    }

    private void initializeBullets() {
        for(int i = 0; i < bulletsCapacity; i++){
            bullets.add(new Bullet(gamePanel, keyHandler, this, bulletSpeed));
        }
        bulletFired = 0;
    }

    public ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }

    public void setPlayerImage() {
        try {
            BufferedImage ship1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip194.png")));
            BufferedImage ship2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip195.png")));
            BufferedImage ship3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip196.png")));
            BufferedImage ship4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip197.png")));
            BufferedImage ship5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip198.png")));

            bufferedImages.add(ship1);
            bufferedImages.add(ship2);
            bufferedImages.add(ship3);
            bufferedImages.add(ship4);
            bufferedImages.add(ship5);

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
        handleMovement();
        changeShootingStrategy();
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

        else { direction = "up";}

        // detectObject
        if (!collision) {
            Entity entityCollision = gamePanel.collisionChecker.detectObjet(this);
            if (entityCollision != null) {
                for (int i = 0; i < gamePanel.getMeteors().size(); i++) {
                    if (gamePanel.getMeteors().get(i).equals(entityCollision)) {
                        gamePanel.getMeteors().get(i).setCollision(true);
                        collision = true;
                        lives--;
                    }
                }
            }
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

    private void updateScore() {
        if (aliveCounter == 30){
            score += 1;
            aliveCounter = 0;
        }
        else {
            aliveCounter++;
        }

        if (score > 999999 || lives == 0) {
            score = 0;
        }
    }

    private void setShootingStrategy(ShootingStrategy shootingStrategy) {
        this.shootingStrategy = shootingStrategy;
    }

    private void changeShootingStrategy() {
        if (keyHandler.getWPressed()) {
            if (shootingStrategy instanceof SingleBulletStrategy) {
                setShootingStrategy(new DoubleBulletStrategy());
            } else if (shootingStrategy instanceof DoubleBulletStrategy) {
                setShootingStrategy(new LaserBulletStrategy());
            } else {
                setShootingStrategy(new SingleBulletStrategy());
            }
            keyHandler.setWPressed(false);
        }
    }

    @Override
    public void draw(Graphics2D graphics2D){
        drawBullets(graphics2D);
        drawPlayer(graphics2D);
    }

    private void drawBullets(Graphics2D graphics2D) {
        for (Bullet bullet: bullets){
            bullet.draw(graphics2D);
        }
    }

    private void drawPlayer(Graphics2D graphics2D) {
        if (collision && collisionDebounce <= 10){
            graphics2D.drawImage(damagedShipImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
        }
        else {
            BufferedImage bufferedImage = bufferedImages.get(random.nextInt(getBufferedImages().size()));
            graphics2D.drawImage(bufferedImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
        }
        //graphics2D.setColor(Color.RED);
        //graphics2D.drawRect(x + solidAreaDefaultX, y + solidAreaDefaultY, solidRectangle.width, solidRectangle.height);
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