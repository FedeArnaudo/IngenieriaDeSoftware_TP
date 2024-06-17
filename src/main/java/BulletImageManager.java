import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class BulletImageManager {
    private static final String BASE_PATH = "bullet/bullet";
    private static final String FILE_EXTENSION = ".png";
    private static final int IMAGE_COUNT = 5;

    private Bullet bullet;
    private ArrayList<BufferedImage> bufferedImages;
    private Random random;

    public void addToBullet(Bullet bullet) {
        this.bullet = bullet;
        this.bufferedImages = new ArrayList<>();
        this.random = new Random();
    }

    public void initializeImages() {
        for (int i = 1; i <= IMAGE_COUNT; i++) {
            String imagePath = BASE_PATH + i + FILE_EXTENSION;
            try {
                BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                bufferedImages.add(image);
            } catch (IOException | NullPointerException e) {
                throw new RuntimeException("Error loading bullet image: " + imagePath, e);
            }
        }
    }

    public void drawBullet(Graphics2D graphics2D) {
        if (!bullet.hasCollided()) {
            int shipPositionY = bullet.getShip().getY();

            // Only draw the bullet if it's not at the ship's position
            if (bullet.getY() != shipPositionY) {
                BufferedImage bufferedImage = bufferedImages.get(random.nextInt(bufferedImages.size()));
                graphics2D.drawImage(bufferedImage, bullet.getX(), bullet.getY(),
                        bullet.getGamePanel().getTileSize(), bullet.getGamePanel().getTileSize(), null);

                // Debugging rectangle (uncomment if needed)
                // graphics2D.setColor(Color.RED);
                // graphics2D.drawRect(bullet.getX() + solidAreaDefaultX, bullet.getY() + solidAreaDefaultY, solidRectangle.width, solidRectangle.height);
            }
        }
    }
}
