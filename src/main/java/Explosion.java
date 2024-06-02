package main.java;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Explosion extends Entity{
    private GamePanel gamePanel;
    private final Entity entity;
    private int sequence;
    private final ArrayList<BufferedImage> bufferedImages;
    public Explosion(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        entity = null;
        bufferedImages = new ArrayList<>();

        setDefaultValues();
        setExplosionImage();
        sequence = 0;
    }
    public void setDefaultValues(){
        //x = entity.getX();
        //y = entity.getY();
    }
    public ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }
    public void setExplosionImage() {
        try {
            BufferedImage exp1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/Explosion1.png")));
            BufferedImage exp2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/Explosion1.png")));
            BufferedImage exp3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/Explosion1.png")));
            BufferedImage exp4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/Explosion1.png")));
            BufferedImage exp5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/Explosion1.png")));
            BufferedImage exp6 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/explosion/Explosion1.png")));

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
            e.printStackTrace();
        }
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence() {
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
    public void update() {  }

    @Override
    public void draw(Graphics2D graphics2D) {
        if(getSequence() < getBufferedImages().size()){
            BufferedImage bufferedImage = bufferedImages.get(sequence);
            graphics2D.drawImage(bufferedImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
            setSequence();
        }
    }
}
