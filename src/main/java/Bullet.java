package main.java;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Bullet extends Entity{
    GamePanel gamePanel;
    KeyHandler keyHandler;
    Ship ship;

    private final ArrayList<BufferedImage> bufferedImages;
    private final Random random;
    public boolean shootFlag;           // to indicate that the bullet has been fired

    public Bullet(GamePanel gamePanel, KeyHandler keyHandler, Ship ship){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.ship = ship;

        random = new Random();
        collisionOn = true;
        bufferedImages = new ArrayList<>();
        solidRectangle = new Rectangle(24, 18, (gamePanel.getTileSize() - 48), (gamePanel.getTileSize() - 36));
        solidAreaDefaultX = solidRectangle.x;
        solidAreaDefaultY = solidRectangle.y;
        shootFlag = false;

        setDefaultValues();
        getBulletImage();
    }

    public ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }

    public void setDefaultValues(){
        x = ship.getX();
        y = ship.getY();
        speed = 12;
        direction = "up";
    }

    public void getBulletImage() {
        try {
            BufferedImage bullet1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/bullet/bullet1.png")));
            BufferedImage bullet2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/bullet/bullet2.png")));
            BufferedImage bullet3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/bullet/bullet3.png")));
            BufferedImage bullet4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/bullet/bullet4.png")));
            BufferedImage bullet5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/bullet/bullet5.png")));

            bufferedImages.add(bullet1);
            bufferedImages.add(bullet2);
            bufferedImages.add(bullet3);
            bufferedImages.add(bullet4);
            bufferedImages.add(bullet5);
        }
        catch (IOException e){
            throw new RuntimeException("Error loading bullet image", e);
        }
    }

    @Override
    public void update(){
        handleShooting();
        resetPosition();
    }

    private void handleShooting() {
        if (shootFlag) {
            shoot();
        }
    }

    private void shoot(){
        y -= getSpeed();

        // detectObject
        Entity entityCollision = gamePanel.collisionChecker.detectObjet(this);
        if(entityCollision != null){
            for(int i = 0; i < gamePanel.meteors.size(); i++){
                if(gamePanel.meteors.get(i).equals(entityCollision)){
                    gamePanel.meteors.remove(i);
                    this.collisionOn = false;
                }
            }
        }
    }

    /**
     * This method updates the position of the bullet when it has not yet been fired
     */
    private void resetPosition() {
        if (!shootFlag) {
            x = ship.getX();
            y = ship.getY();
            direction = "up";
        }
    }

    @Override
    public void draw(Graphics2D graphics2D){
        drawBullet(graphics2D);
    }

    private void drawBullet(Graphics2D graphics2D) {
        BufferedImage bufferedImage = bufferedImages.get(random.nextInt(getBufferedImages().size()));
        graphics2D.drawImage(bufferedImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
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
}
