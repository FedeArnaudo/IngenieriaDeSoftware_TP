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
     * The bullets array list.
     */
    ArrayList<Bullet> bullets = new ArrayList<>();
    public int bulletsCapacity;
    public int bulletFired;

    public Ship(GamePanel gamePanel, KeyHandler keyHandler, int bulletsCapacity){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.bulletsCapacity = bulletsCapacity;

        random = new Random();
        collisionOn = true;
        bufferedImages = new ArrayList<>();
        solidRectangle = new Rectangle(3, 5, 52, 40);
        solidAreaDefaultX = solidRectangle.x;
        solidAreaDefaultY = solidRectangle.y;

        setDefaultValues();
        initializeBullets();
        setPlayerImage();
    }
    public void setDefaultValues(){
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
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void shoot(){
        bulletFired++;
        bullets.get(bulletFired-1).shootFlag = true;
    }

    @Override
    public void update(){
        handleShooting();
        updateBullets();
        handleMovement();
    }

    private void handleShooting() {
        if (keyHandler.spacePressed && bulletFired < bullets.size()) {
            shoot();
            keyHandler.spacePressed = false;
        }
    }

    private void updateBullets() {
        Bullet bulletDelete = null;
        for (Bullet bullet: bullets){
            bullet.update();
            if(!bullet.collisionOn){
                bulletDelete = bullet;
            }
        }
        if(bulletDelete != null){
            bullets.remove(bulletDelete);
        }
    }

    private void handleMovement() {
        if(keyHandler.leftPressed){
            direction = "left";
            if(getX() > 0) {
                moveLeft();
            }
        }

        else if (keyHandler.rightPressed) {
            direction = "right";
            if(getX() < (gamePanel.getScreenWidth() - gamePanel.getTileSize())){
                moveRight();
            }
        }

        else { direction = "up";}

        // detectObject
        if(gamePanel.collisionChecker.detectObjet(this) != null){

        }
    }

    private void moveLeft() {
        x -= getSpeed();
    }

    private void moveRight() {
        x += getSpeed();
    }

    @Override
    public void draw(Graphics2D graphics2D){
        drawBullets(graphics2D);
        drawPlayer(graphics2D);
    }

    private void drawBullets(Graphics2D graphics2D) {
        for (Bullet bullet: bullets){
            if(bullet != null){
                bullet.draw(graphics2D);
            }
        }
    }

    private void drawPlayer(Graphics2D graphics2D) {
        BufferedImage bufferedImage = bufferedImages.get(random.nextInt(getBufferedImages().size()));
        graphics2D.drawImage(bufferedImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
        graphics2D.setColor(Color.RED);
        graphics2D.drawRect(x + solidAreaDefaultX, y + solidAreaDefaultY, solidRectangle.width, solidRectangle.height);
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
}