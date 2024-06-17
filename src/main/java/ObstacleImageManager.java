import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ObstacleImageManager {
    private Obstacle obstacle;
    private final List<BufferedImage> bufferedImages = new ArrayList<>();
    private final List<BufferedImage> explosionImages = new ArrayList<>();
    private final Random random = new Random();

    private static final Map<Obstacle.ObstacleType, String[]> OBSTACLE_IMAGES = new HashMap<Obstacle.ObstacleType, String[]>() {{
        put(Obstacle.ObstacleType.METEOR_1, new String[]{"meteor/meteor_1_1.png", "meteor/meteor_1_2.png", "meteor/meteor_1_3.png"});
        put(Obstacle.ObstacleType.METEOR_2, new String[]{"meteor/meteor_2_1.png", "meteor/meteor_2_2.png", "meteor/meteor_2_3.png"});
        put(Obstacle.ObstacleType.BULLET_POWER_UP, new String[]{"powerups/bullet_power_up.png"});
        put(Obstacle.ObstacleType.BASIC_POWER_UP, new String[]{"powerups/basic_power_up.png"});
        put(Obstacle.ObstacleType.SUPER_POWER_UP, new String[]{"powerups/super_power_up_1.png", "powerups/super_power_up_2.png", "powerups/super_power_up_3.png", "powerups/super_power_up_4.png"});
    }};

    private static final String[] EXPLOSION_IMAGES = {
            "explosion/explosion_1.png", "explosion/explosion_2.png", "explosion/explosion_3.png",
            "explosion/explosion_4.png", "explosion/explosion_5.png", "explosion/explosion_6.png"
    };

    public void attachToObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
        initializeImages();
    }

    private void initializeImages() {
        if (obstacle != null) {
            loadObstacleImages();
            loadExplosionImages();
        }
    }

    private void loadObstacleImages() {
        String[] imagePaths = OBSTACLE_IMAGES.get(obstacle.getObstacleType());
        if (imagePaths != null) {
            loadImages(bufferedImages, imagePaths);
        }
    }

    private void loadExplosionImages() {
        loadImages(explosionImages, EXPLOSION_IMAGES);
    }

    private void loadImages(List<BufferedImage> images, String... paths) {
        for (String path : paths) {
            try {
                BufferedImage img = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
                if (img != null) {
                    images.add(img);
                } else {
                    throw new IOException("Image not found: " + path);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error loading image: " + path, e);
            }
        }
    }

    public void draw(Graphics2D graphics2D) {
        if (isObstacleVisible()) {
            BufferedImage bufferedImage = getBufferedImage();
            int x = obstacle.getX();
            int y = obstacle.getY();
            int tileSize = obstacle.getGamePanel().getTileSize();
            graphics2D.drawImage(bufferedImage, x, y, tileSize, tileSize, null);
        }
    }

    private boolean isObstacleVisible() {
        int x = obstacle.getX();
        int y = obstacle.getY();
        GamePanel gamePanel = obstacle.getGamePanel();
        int tileSize = gamePanel.getTileSize();
        return x >= 0 && x <= gamePanel.getScreenWidth() && y >= -tileSize && y <= gamePanel.getScreenHeight();
    }

    private BufferedImage getBufferedImage() {
        List<BufferedImage> images = obstacle.hasCollided() &&
                (obstacle.getObstacleType() == Obstacle.ObstacleType.METEOR_1 || obstacle.getObstacleType() == Obstacle.ObstacleType.METEOR_2)
                ? explosionImages : bufferedImages;
        return images.get(random.nextInt(images.size()));
    }
}
