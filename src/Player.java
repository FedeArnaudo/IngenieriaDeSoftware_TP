import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Player extends Entity{
    GamePanel gamePanel;
    KeyHandler keyHandler;

    private final ArrayList<BufferedImage> bufferedImages;
    private final Random random;


    public Player(GamePanel gamePanel, KeyHandler keyHandler){
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        random = new Random();
        collisionOn = true;
        bufferedImages = new ArrayList<>();
        solidRectangle = new Rectangle(2, 5, (gamePanel.tileSize - 2), (gamePanel.tileSize - 5));
        solidAreaDefaultX = solidRectangle.x;
        solidAreaDefaultY = solidRectangle.y;
        setDefaultValues();
        getPlayerImage();
    }
    public ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }
    public void setDefaultValues(){
        x = 400;
        y = 850;
        speed = 6;
        direction = "up";
    }

    public void getPlayerImage() {
        try {
            ship1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip194.png")));
            ship2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip195.png")));
            ship3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip196.png")));
            ship4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip197.png")));
            ship5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/MainShip198.png")));

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
    public void update(){
        if(keyHandler.leftPressed){
            direction = "left";
            if(x > 0) {
                x -= speed;
            }
        }
        else if (keyHandler.rightPressed) {
            direction = "right";
            if(x < (gamePanel.screenWidth - gamePanel.tileSize)){
                x += speed;
            }
        }
        else {
            direction = "up";
        }
    }

    public void draw(Graphics2D graphics2D){

        BufferedImage bufferedImage = bufferedImages.get(random.nextInt(getBufferedImages().size()));

        graphics2D.drawImage(bufferedImage, x, y, gamePanel.tileSize, gamePanel.tileSize, null);
    }
}
