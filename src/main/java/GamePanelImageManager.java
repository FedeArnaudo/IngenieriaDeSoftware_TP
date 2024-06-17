import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class GamePanelImageManager {
    private GamePanel gamePanel;

    // Constants for file paths
    private static final String LETTERS_PATH = "letters/letter_";
    private static final String NUMBERS_PATH = "numbers/number_";
    private static final String KEYS_PATH = "keys/key_";
    private static final String BULLET_SCOREBOARD_PATH = "scoreboard/bullet_scoreboard.png";
    private static final String LIVE_IMAGE_PATH = "lives/live.png";
    private static final String COOLING_IMAGE_PATH = "cooling/cooling_icon.png";

    public void attachToGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void loadGamePanelImages() {
        loadLetterImages();
        loadNumberImages();
        loadKeyImages();
        loadBulletScoreboardImage();
        loadLiveImage();
        loadCoolingImage();
    }

    private void loadLetterImages() {
        for (char ch = 'a'; ch <= 'z'; ch++) {
            gamePanel.getLetterImages().put(ch, loadImage(LETTERS_PATH + ch + ".png"));
        }
    }

    private void loadNumberImages() {
        for (int i = 0; i <= 9; i++) {
            gamePanel.getNumberImages().put(i, loadImage(NUMBERS_PATH + i + ".png"));
        }
    }

    private void loadKeyImages() {
        gamePanel.setKeyUpImage(loadImage(KEYS_PATH + "up.png"));
        gamePanel.setKeyDownImage(loadImage(KEYS_PATH + "down.png"));
        gamePanel.setKeyRightImage(loadImage(KEYS_PATH + "right.png"));
        gamePanel.setKeyLeftImage(loadImage(KEYS_PATH + "left.png"));
        gamePanel.setKeySpaceImage(loadImage(KEYS_PATH + "space.png"));
    }

    private void loadBulletScoreboardImage() {
        gamePanel.setBulletScoreboardImage(loadImage(BULLET_SCOREBOARD_PATH));
    }

    private void loadLiveImage() {
        gamePanel.setLiveImage(loadImage(LIVE_IMAGE_PATH));
    }

    private void loadCoolingImage() {
        gamePanel.setCoolingIconImage(loadImage(COOLING_IMAGE_PATH));
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Failed to load image: " + path, e);
        }
    }
}
