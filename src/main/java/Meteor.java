package main.java;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;

public class Meteor extends Entity{
    private final GamePanel gamePanel;
    private final ArrayList<BufferedImage> bufferedImages;
    private final ArrayList<BufferedImage> explosionImages;
    private int explosionAnimationCounter;
    private final SecureRandom random ;

    public Meteor(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        random = new SecureRandom();
        collision = false;
        bufferedImages = new ArrayList<>();
        explosionImages = new ArrayList<>();
        solidRectangle = new Rectangle(18, 36, 20, 20);
        solidAreaDefaultX = solidRectangle.x;
        solidAreaDefaultY = solidRectangle.y;
        explosionAnimationCounter = 0;

        setDefaultValues();
        setMeteorImage();
        setExplosionImage();
    }

    private ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }

    public void setDefaultValues(){
        x = random.nextInt(GamePanel.getMaxScreenCol()) * gamePanel.getTileSize();
        y = - gamePanel.getTileSize();
        speed = random.nextInt(6) + 2;
        collision = false;
        direction = "down";
    }

    public void setMeteorImage() {
        try {
            BufferedImage meteor1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/meteor/meteor1.png")));
            BufferedImage meteor2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/meteor/meteor2.png")));
            BufferedImage meteor3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/meteor/meteor3.png")));

            bufferedImages.add(meteor1);
            bufferedImages.add(meteor2);
            bufferedImages.add(meteor3);
        }
        catch (IOException e){
            throw new RuntimeException("Error loading meteor image", e);
        }
    }

    private void setExplosionImage() {
        try {
            BufferedImage explosion1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion1.png")));
            BufferedImage explosion2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion2.png")));
            BufferedImage explosion3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion3.png")));
            BufferedImage explosion4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion4.png")));
            BufferedImage explosion5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion5.png")));
            BufferedImage explosion6 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion6.png")));

            explosionImages.add(explosion1);
            explosionImages.add(explosion2);
            explosionImages.add(explosion3);
            explosionImages.add(explosion4);
            explosionImages.add(explosion5);
            explosionImages.add(explosion6);
        }
        catch (IOException e){
            throw new RuntimeException("Error loading explosion image", e);
        }
    }

    @Override
    public void update(){
        y += getSpeed();

        if(y > gamePanel.getScreenHeight()){
            setDefaultValues();
        }
        else if (collision){
            explosionAnimationCounter++;
            if(explosionAnimationCounter > 10){
                setDefaultValues();
                explosionAnimationCounter = 0;
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics2D){
        BufferedImage bufferedImage;
        if(collision){
            bufferedImage = explosionImages.get(random.nextInt(explosionImages.size()));
        }
        else {
            bufferedImage = bufferedImages.get(random.nextInt(getBufferedImages().size()));
            //graphics2D.setColor(Color.RED);
            //graphics2D.drawRect(x + solidAreaDefaultX, y + solidAreaDefaultY, solidRectangle.width, solidRectangle.height);
        }
        graphics2D.drawImage(bufferedImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

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

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
