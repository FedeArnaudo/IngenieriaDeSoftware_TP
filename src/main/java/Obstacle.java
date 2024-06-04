package main.java;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;

public class Obstacle extends Entity{
    private final GamePanel gamePanel;
    private final ArrayList<BufferedImage> bufferedImages;
    private final ArrayList<BufferedImage> explosionImages;
    private int explosionAnimationCounter;
    private final String obstacleType;
    private final SecureRandom random ;

    public Obstacle(GamePanel gamePanel, String obstacleType, int speed){
        this.gamePanel = gamePanel;
        this.obstacleType = obstacleType;
        this.speed = speed;

        random = new SecureRandom();
        collision = false;
        bufferedImages = new ArrayList<>();
        explosionImages = new ArrayList<>();
        solidRectangle = new Rectangle(18, 36, 20, 20);
        solidAreaDefaultX = solidRectangle.x;
        solidAreaDefaultY = solidRectangle.y;
        explosionAnimationCounter = 0;

        setDefaultValues();
        setObastacleImages();
        setExplosionImage();
    }

    private ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }

    public void setDefaultValues(){
        x = random.nextInt(GamePanel.getMaxScreenCol()) * gamePanel.getTileSize();

        switch (obstacleType){
            case "meteor_1":
                y = - gamePanel.getTileSize() * 15;
                break;
            case "meteor_2":
                y = - gamePanel.getTileSize() * 20;
                break;
            case "bullet_power_up":
                y = - gamePanel.getTileSize() * 30;
                break;
            case "basic_power_up":
                y = - gamePanel.getTileSize() * 50;
                break;
            case "super_power_up":
                y = - gamePanel.getTileSize() * 100;
                break;
        }

        collision = false;
        direction = "down";
    }

    public void setObastacleImages() {
        switch (obstacleType){
            case "meteor_1":
                setMeteor1Images();
                break;
            case "meteor_2":
                setMeteor2Images();
                break;
            case "bullet_power_up":
                setBulletPowerUpImages();
                break;
            case "basic_power_up":
                setBasicPowerupImages();
                break;
            case "super_power_up":
                setSuperPowerupImages();
                break;
        }
    }

    private void setMeteor1Images() {
        try {
            BufferedImage meteor1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/meteor/meteor_1_1.png")));
            BufferedImage meteor2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/meteor/meteor_1_2.png")));
            BufferedImage meteor3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/meteor/meteor_1_3.png")));

            bufferedImages.add(meteor1);
            bufferedImages.add(meteor2);
            bufferedImages.add(meteor3);
        }
        catch (IOException e){
            throw new RuntimeException("Error loading meteor image", e);
        }
    }

    private void setMeteor2Images() {
        try {
            BufferedImage meteor1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/meteor/meteor_2_1.png")));
            BufferedImage meteor2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/meteor/meteor_2_2.png")));
            BufferedImage meteor3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/meteor/meteor_2_3.png")));

            bufferedImages.add(meteor1);
            bufferedImages.add(meteor2);
            bufferedImages.add(meteor3);
        }
        catch (IOException e){
            throw new RuntimeException("Error loading meteor image", e);
        }
    }

    private void setBulletPowerUpImages() {
        try {
            BufferedImage bulletPowerUp = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/powerups/bullet_power_up.png")));

            bufferedImages.add(bulletPowerUp);
        }
        catch (IOException e){
            throw new RuntimeException("Error loading bullet power up image", e);
        }
    }

    private void setBasicPowerupImages() {
        try {
            BufferedImage powerUp = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/powerups/basic_power_up.png")));

            bufferedImages.add(powerUp);
        }
        catch (IOException e){
            throw new RuntimeException("Error loading power up image", e);
        }
    }

    private void setSuperPowerupImages() {
        try {
            BufferedImage powerUp1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/powerups/super_power_up_1.png")));
            BufferedImage powerUp2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/powerups/super_power_up_2.png")));
            BufferedImage powerUp3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/powerups/super_power_up_3.png")));
            BufferedImage powerUp4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/powerups/super_power_up_4.png")));

            bufferedImages.add(powerUp1);
            bufferedImages.add(powerUp2);
            bufferedImages.add(powerUp3);
            bufferedImages.add(powerUp4);
        }
        catch (IOException e){
            throw new RuntimeException("Error loading power up image", e);
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

        switch (obstacleType){
            case "meteor_1":
                if (y > gamePanel.getScreenHeight()){
                    setDefaultValues();
                    collision = false;
                }else if (collision){
                    explosionAnimationCounter++;
                    if(explosionAnimationCounter > 10){
                        setDefaultValues();
                        explosionAnimationCounter = 0;
                    }
                }
                break;
            case "meteor_2":
                if (y > gamePanel.getScreenHeight() * 10){
                    setDefaultValues();
                    collision = false;
                }else if (collision){
                    explosionAnimationCounter++;
                    if(explosionAnimationCounter > 10){
                        setDefaultValues();
                        explosionAnimationCounter = 0;
                    }
                }
                break;
            case "bullet_power_up":
                if (y > gamePanel.getScreenHeight() * 20){
                    setDefaultValues();
                    collision = false;
                }else if (collision){
                    explosionAnimationCounter++;
                    if(explosionAnimationCounter > 10){
                        setDefaultValues();
                        explosionAnimationCounter = 0;
                    }
                }
                break;
            case "basic_power_up":
                if (y > gamePanel.getScreenHeight() * 35){
                    setDefaultValues();
                    collision = false;
                }else if (collision){
                    explosionAnimationCounter++;
                    if(explosionAnimationCounter > 10){
                        setDefaultValues();
                        explosionAnimationCounter = 0;
                    }
                }
                break;
            case "super_power_up":
                if (y > gamePanel.getScreenHeight() * 75){
                    setDefaultValues();
                    collision = false;
                }else if (collision){
                    explosionAnimationCounter++;
                    if(explosionAnimationCounter > 10){
                        setDefaultValues();
                        explosionAnimationCounter = 0;
                    }
                }
                break;
        }
    }

    @Override
    public void draw(Graphics2D graphics2D){
        // Check if the obstacle is within the game panel dimensions
        if (x >= 0 && x <= gamePanel.getScreenWidth() && y >= -gamePanel.getTileSize() && y <= gamePanel.getScreenHeight()) {
            BufferedImage bufferedImage;
            if(collision && obstacleType.contains("meteor")){
                bufferedImage = explosionImages.get(random.nextInt(explosionImages.size()));
            }
            else {
                bufferedImage = bufferedImages.get(random.nextInt(getBufferedImages().size()));
                //graphics2D.setColor(Color.RED);
                //graphics2D.drawRect(x + solidAreaDefaultX, y + solidAreaDefaultY, solidRectangle.width, solidRectangle.height);
            }
            graphics2D.drawImage(bufferedImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
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

    public String getObstacleType() {
        return obstacleType;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
