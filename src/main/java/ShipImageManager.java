import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ShipImageManager {
    private Ship ship;
    private final ArrayList<BufferedImage> normalShipImages;
    private final ArrayList<BufferedImage> powerUpShipImages;
    private BufferedImage damagedShipImage;
    private final Random random = new Random();

    private static final String[] NORMAL_SHIP_IMAGE_PATHS = {
            "player/ship_1.png",
            "player/ship_2.png",
            "player/ship_3.png",
            "player/ship_4.png",
            "player/ship_5.png"
    };

    private static final String[] POWERUP_SHIP_IMAGE_PATHS = {
            "powerup_ship/powerup_ship_1.png",
            "powerup_ship/powerup_ship_2.png",
            "powerup_ship/powerup_ship_3.png",
            "powerup_ship/powerup_ship_4.png",
            "powerup_ship/powerup_ship_5.png"
    };

    private static final String DAMAGED_SHIP_IMAGE_PATH = "player/damaged_ship.png";

    public ShipImageManager() {
        this.normalShipImages = new ArrayList<>();
        this.powerUpShipImages = new ArrayList<>();
    }
    public void attachToShip(Ship ship) {
        this.ship = ship;
    }

    public void initializeImages() {
        loadImages(normalShipImages, NORMAL_SHIP_IMAGE_PATHS);
        loadImages(powerUpShipImages, POWERUP_SHIP_IMAGE_PATHS);
        damagedShipImage = loadImage(DAMAGED_SHIP_IMAGE_PATH);
    }

    private void loadImages(ArrayList<BufferedImage> images, String[] paths) {
        for (String path : paths) {
            images.add(loadImage(path));
        }
    }

    private BufferedImage loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
            if (image == null) {
                throw new IOException("Image not found: " + path);
            }
            return image;
        } catch (IOException e) {
            throw new RuntimeException("Error loading image: " + path, e);
        }
    }

    public void drawShip(Graphics2D graphics2D) {
        BufferedImage shipImage;

        if (isDamaged()) {
            shipImage = damagedShipImage;
        } else if (ship.getCurrentPowerUp().equals(Ship.PowerUpType.SUPER)) {
            shipImage = powerUpShipImages.get(random.nextInt(powerUpShipImages.size()));
        } else {
            shipImage = normalShipImages.get(random.nextInt(normalShipImages.size()));
        }

        drawImage(graphics2D, shipImage, ship.getX(), ship.getY());
    }

    private boolean isDamaged() {
        return ship.hasCollided() && ship.getCollisionDebounce() <= 10 &&
                (ship.getCollidedWith().equals(Obstacle.ObstacleType.METEOR_1) ||
                        ship.getCollidedWith().equals(Obstacle.ObstacleType.METEOR_2));
    }

    public void drawWelcomeScreen(Graphics2D graphics2D) {
        int x = ship.getGamePanel().getScreenWidth() / 2 + 150;
        int y = (int) (ship.getGamePanel().getScreenHeight() * 0.24);

        BufferedImage bufferedImage = normalShipImages.get(random.nextInt(normalShipImages.size()));
        drawImage(graphics2D, bufferedImage, x, y);
    }

    public void drawBullets(Graphics2D graphics2D) {
        for (int i = 0; i < ship.getBulletsCapacity(); i++){
            ship.getBullets().get(i).draw(graphics2D);
        }
    }

    private void drawImage(Graphics2D graphics2D, BufferedImage image, int x, int y) {
        int tileSize = ship.getGamePanel().getTileSize();
        graphics2D.drawImage(image, x, y, tileSize, tileSize, null);
    }
}
