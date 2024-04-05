import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Meteor extends Entity{
    private GamePanel gamePanel;
    private ArrayList<BufferedImage> bufferedImages;
    private Random random;

    public Meteor(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        bufferedImages = new ArrayList<>();
        random = new Random();
        setDefaultValues();
        getMeteorImage();

    }

    public ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }

    public void setDefaultValues(){
        x = random.nextInt((gamePanel.screenWidth - gamePanel.tileSize));
        y = 0;
        speed = random.nextInt(4) + 2;
        direction = "down";
    }

    public void getMeteorImage() {
        try {
            meteor1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/meteor/meteor1.png")));
            meteor2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/meteor/meteor2.png")));
            meteor3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/meteor/meteor3.png")));

            bufferedImages.add(meteor1);
            bufferedImages.add(meteor2);
            bufferedImages.add(meteor3);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void update(){
        y += speed;
        if(y > gamePanel.screenHeight){
            setDefaultValues();
        }
    }
    public void draw(Graphics2D graphics2D){

        BufferedImage bufferedImage = bufferedImages.get(random.nextInt(getBufferedImages().size()));

        graphics2D.drawImage(bufferedImage, x, y, gamePanel.tileSize, gamePanel.tileSize, null);
    }
}
