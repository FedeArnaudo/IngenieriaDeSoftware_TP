package main.java;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Explosion extends Entity{
    private final GamePanel gamePanel;
    private int sequence;
    private final ArrayList<BufferedImage> bufferedImages;

    public Explosion(GamePanel gamePanel){
        this.gamePanel = gamePanel;

        bufferedImages = new ArrayList<>();
        sequence = 0;

        setDefaultValues();
        setExplosionImage();
    }

    public void setDefaultValues(){
        //x = entity.getX();
        //y = entity.getY();
    }

    public void setExplosionImage() {
        try {
            BufferedImage exp1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion1.png")));
            BufferedImage exp2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion2.png")));
            BufferedImage exp3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion3.png")));
            BufferedImage exp4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion4.png")));
            BufferedImage exp5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion5.png")));
            BufferedImage exp6 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/explosion6.png")));

            bufferedImages.add(exp1);
            bufferedImages.add(exp1);
            bufferedImages.add(exp1);

            bufferedImages.add(exp2);
            bufferedImages.add(exp2);
            bufferedImages.add(exp2);
            bufferedImages.add(exp2);
            bufferedImages.add(exp2);

            bufferedImages.add(exp3);
            bufferedImages.add(exp4);
            bufferedImages.add(exp5);
            bufferedImages.add(exp6);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }

    public int getSequence() {
        return sequence;
    }

    public void increaseSequence() {
        sequence++;
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

    @Override
    public void update() {  }

    @Override
    public void draw(Graphics2D graphics2D) {
        if(getSequence() < getBufferedImages().size()){
            BufferedImage bufferedImage = bufferedImages.get(sequence);
            graphics2D.drawImage(bufferedImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
            increaseSequence();
        }
    }
}
