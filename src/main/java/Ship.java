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
    private final int lives;
    private int score;
    private final int cooldownTime;
    private int cooldownCounter;

    private ShootingStrategy shootingStrategy = new SingleBulletStrategy(); // default strategy

    public Ship(GamePanel gamePanel, KeyHandler keyHandler, int lives, int bulletsCapacity, int cooldownTime){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.lives = lives;
        this.bulletsCapacity = bulletsCapacity;
        this.cooldownTime = cooldownTime;

        score = 0;
        cooldownCounter = cooldownTime * GamePanel.getFps();
        random = new Random();
        collisionOn = true;
        bufferedImages = new ArrayList<>();
        solidRectangle = new Rectangle(2, 5, (gamePanel.getTileSize() - 2), (gamePanel.getTileSize() - 5));
        solidAreaDefaultX = solidRectangle.x;
        solidAreaDefaultY = solidRectangle.y;

        setDefaultValues();
        initializeBullets();
        getPlayerImage();
    }

    private ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }

    private void setDefaultValues(){
        x = 400;
        y = 850;
        speed = 6;
        direction = "up";
    }

    private void initializeBullets() {
        for(int i = 0; i < bulletsCapacity; i++){
            bullets.add(new Bullet(gamePanel, keyHandler, this));
        }
        bulletFired = 0;
    }

    private void getPlayerImage() {
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
        resetScore();

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
    }

    private void moveLeft() {
        x -= getSpeed();
    }

    private void moveRight() {
        x += getSpeed();
    }

    private void resetScore() {
        if (score > 999999) {
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
        BufferedImage bufferedImage = bufferedImages.get(random.nextInt(getBufferedImages().size()));
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
}