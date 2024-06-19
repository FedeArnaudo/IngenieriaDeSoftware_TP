import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GamePanelImageManagerTest {
    private GamePanel gamePanel;
    private GamePanelImageManager gamePanelImageManager;

    @BeforeEach
    public void setup() {
        gamePanel = mock(GamePanel.class);
        gamePanelImageManager = GamePanelImageManager.getInstanceGamePanelImageManager();
        gamePanelImageManager.attachToGamePanel(gamePanel);
    }

    @Test
    public void loadGamePanelImages_loadsAllImages() {
        gamePanelImageManager.loadGamePanelImages();

        verify(gamePanel, times(26)).getLetterImages();
        verify(gamePanel, times(10)).getNumberImages();
        verify(gamePanel).setKeyUpImage(any(BufferedImage.class));
        verify(gamePanel).setKeyDownImage(any(BufferedImage.class));
        verify(gamePanel).setKeyRightImage(any(BufferedImage.class));
        verify(gamePanel).setKeyLeftImage(any(BufferedImage.class));
        verify(gamePanel).setKeySpaceImage(any(BufferedImage.class));
        verify(gamePanel).setBulletScoreboardImage(any(BufferedImage.class));
        verify(gamePanel).setLiveImage(any(BufferedImage.class));
        verify(gamePanel).setCoolingIconImage(any(BufferedImage.class));
    }

    @Test
    public void loadGamePanelImages_throwsException_whenImageNotFound() {
        doThrow(new RuntimeException()).when(gamePanel).setKeyUpImage(any(BufferedImage.class));

        assertThrows(RuntimeException.class, () -> gamePanelImageManager.loadGamePanelImages());
    }
}