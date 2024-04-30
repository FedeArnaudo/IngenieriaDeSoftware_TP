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
    private final SecureRandom random ;

    public Meteor(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        random = new SecureRandom();
        collisionOn = true;
        bufferedImages = new ArrayList<>();
        solidRectangle = new Rectangle(18, 36, 20, 20);
        solidAreaDefaultX = solidRectangle.x;
        solidAreaDefaultY = solidRectangle.y;
        setDefaultValues();
        getMeteorImage();

    }

    public ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }

    public void setDefaultValues(){
        x = random.nextInt(gamePanel.maxScreenCol) * gamePanel.tileSize;
        y = - gamePanel.tileSize;
        speed = random.nextInt(8) + 2;
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
