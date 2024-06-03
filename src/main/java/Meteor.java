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
    private BufferedImage explosionImage;
    private int explosionAnimationCounter;
    private final SecureRandom random ;

    public Meteor(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        random = new SecureRandom();
        collision = false;
        bufferedImages = new ArrayList<>();
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
            explosionImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion1.png")));
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
        if(collision){
            graphics2D.drawImage(explosionImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
        }
        else {
            BufferedImage bufferedImage = bufferedImages.get(random.nextInt(getBufferedImages().size()));
            graphics2D.drawImage(bufferedImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
            //graphics2D.setColor(Color.RED);
            //graphics2D.drawRect(x + solidAreaDefaultX, y + solidAreaDefaultY, solidRectangle.width, solidRectangle.height);
        }
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
