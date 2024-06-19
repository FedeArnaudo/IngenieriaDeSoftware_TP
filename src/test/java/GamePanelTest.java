import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GamePanelTest {
    private GamePanel gamePanel;
    private GamePanelImageManager gamePanelImageManager;
    private GamePanelSoundManager gamePanelSoundManager;
    private GamePanelDrawManager gamePanelDrawManager;
    private GamePanelStateManager gamePanelStateManager;

    @BeforeEach
    public void setup() {
        gamePanelImageManager = mock(GamePanelImageManager.class);
        gamePanelSoundManager = mock(GamePanelSoundManager.class);
        gamePanelDrawManager = mock(GamePanelDrawManager.class);
        gamePanelStateManager = mock(GamePanelStateManager.class);
        gamePanel = new GamePanel(gamePanelImageManager, gamePanelSoundManager, gamePanelDrawManager, gamePanelStateManager);
    }

    @Test
    public void addManagers_shouldAttachManagersToGamePanel() {
        verify(gamePanelImageManager).attachToGamePanel(gamePanel);
        verify(gamePanelSoundManager).attachToGamePanel(gamePanel);
        verify(gamePanelDrawManager).attachToGamePanel(gamePanel);
        verify(gamePanelStateManager).attachToGamePanel(gamePanel);
    }

    @Test
    public void initializeGamePanel_shouldInitializeStateAndLoadResources() {
        verify(gamePanelStateManager).initializeState();
        verify(gamePanelImageManager).loadGamePanelImages();
        verify(gamePanelSoundManager).loadGamePanelSounds();
        verify(gamePanelStateManager).initializeObstacles();
    }

    @Test
    public void getFps_shouldReturnCorrectFps() {
        assertEquals(60, GamePanel.getFps());
    }

    @Test
    public void getOriginalTileSize_shouldReturnCorrectTileSize() {
        assertEquals(19, GamePanel.getOriginalTileSize());
    }

    @Test
    public void getMaxScreenCol_shouldReturnCorrectMaxScreenCol() {
        assertEquals(16, GamePanel.getMaxScreenCol());
    }

    @Test
    public void getTileSize_shouldReturnCorrectTileSize() {
        assertEquals(57, gamePanel.getTileSize());
    }

    @Test
    public void getScreenWidth_shouldReturnCorrectScreenWidth() {
        assertEquals(912, gamePanel.getScreenWidth());
    }

    @Test
    public void getScreenHeight_shouldReturnCorrectScreenHeight() {
        assertEquals(912, gamePanel.getScreenHeight());
    }

    @Test
    public void getMeteors1Number_shouldReturnCorrectMeteors1Number() {
        assertEquals(20, GamePanel.getMeteors1Number());
    }

    @Test
    public void getMeteors2Number_shouldReturnCorrectMeteors2Number() {
        assertEquals(3, GamePanel.getMeteors2Number());
    }

    @Test
    public void getMeteorsSpeedThreshold_shouldReturnCorrectMeteorsSpeedThreshold() {
        assertEquals(8, GamePanel.getMeteorsSpeedThreshold());
    }

    @Test
    public void getShipsLives_shouldReturnCorrectShipsLives() {
        assertEquals(7, GamePanel.getShipsLives());
    }

    @Test
    public void getShipsSpeed_shouldReturnCorrectShipsSpeed() {
        assertEquals(6, GamePanel.getShipsSpeed());
    }

    @Test
    public void getShipsBulletsCapacity_shouldReturnCorrectShipsBulletsCapacity() {
        assertEquals(100, GamePanel.getShipsBulletsCapacity());
    }

    @Test
    public void getShipsBulletSpeed_shouldReturnCorrectShipsBulletSpeed() {
        assertEquals(20, GamePanel.getShipsBulletSpeed());
    }

    @Test
    public void getShipCooldownTime_shouldReturnCorrectShipCooldownTime() {
        assertEquals(30, GamePanel.getShipCooldownTime());
    }

    @Test
    public void getFloatTime_shouldReturnCorrectFloatTime() {
        double floatTime = gamePanel.getFloatTime();
        assertEquals(0.0, floatTime);
    }

    @Test
    public void getScoreZoom_shouldReturnCorrectScoreZoom() {
        double scoreZoom = gamePanel.getScoreZoom();
        assertEquals(1.0, scoreZoom);
    }

    @Test
    public void getMeteors1_shouldReturnEmptyMeteors1() {
        ArrayList<Obstacle> meteors1 = gamePanel.getMeteors1();
        assertTrue(meteors1.isEmpty());
    }

    @Test
    public void getMeteors2_shouldReturnEmptyMeteors2() {
        ArrayList<Obstacle> meteors2 = gamePanel.getMeteors2();
        assertTrue(meteors2.isEmpty());
    }

    @Test
    public void getBulletPowerUps_shouldReturnEmptyBulletPowerUps() {
        ArrayList<Obstacle> bulletPowerUps = gamePanel.getBulletPowerUps();
        assertTrue(bulletPowerUps.isEmpty());
    }

    @Test
    public void getBasicPowerUps_shouldReturnEmptyBasicPowerUps() {
        ArrayList<Obstacle> basicPowerUps = gamePanel.getBasicPowerUps();
        assertTrue(basicPowerUps.isEmpty());
    }

    @Test
    public void getSuperPowerUps_shouldReturnEmptySuperPowerUps() {
        ArrayList<Obstacle> superPowerUps = gamePanel.getSuperPowerUps();
        assertTrue(superPowerUps.isEmpty());
    }

    @Test
    public void getLetterImages_shouldReturnEmptyLetterImages() {
        Map<Character, BufferedImage> letterImages = gamePanel.getLetterImages();
        assertTrue(letterImages.isEmpty());
    }

    @Test
    public void getNumberImages_shouldReturnEmptyNumberImages() {
        Map<Integer, BufferedImage> numberImages = gamePanel.getNumberImages();
        assertTrue(numberImages.isEmpty());
    }

    @Test
    public void getBulletScoreboardImage_shouldReturnNullBulletScoreboardImage() {
        BufferedImage bulletScoreboardImage = gamePanel.getBulletScoreboardImage();
        assertNull(bulletScoreboardImage);
    }

    @Test
    public void getLiveImage_shouldReturnNullLiveImage() {
        BufferedImage liveImage = gamePanel.getLiveImage();
        assertNull(liveImage);
    }

    @Test
    public void getCollingIconImage_shouldReturnNullCollingIconImage() {
        BufferedImage collingIconImage = gamePanel.getCollingIconImage();
        assertNull(collingIconImage);
    }

    @Test
    public void getKeyUpImage_shouldReturnNullKeyUpImage() {
        BufferedImage keyUpImage = gamePanel.getKeyUpImage();
        assertNull(keyUpImage);
    }

    @Test
    public void getKeyDownImage_shouldReturnNullKeyDownImage() {
        BufferedImage keyDownImage = gamePanel.getKeyDownImage();
        assertNull(keyDownImage);
    }

    @Test
    public void getKeyRightImage_shouldReturnNullKeyRightImage() {
        BufferedImage keyRightImage = gamePanel.getKeyRightImage();
        assertNull(keyRightImage);
    }

    @Test
    public void getKeyLeftImage_shouldReturnNullKeyLeftImage() {
        BufferedImage keyLeftImage = gamePanel.getKeyLeftImage();
        assertNull(keyLeftImage);
    }

    @Test
    public void getKeySpaceImage_shouldReturnNullKeySpaceImage() {
        BufferedImage keySpaceImage = gamePanel.getKeySpaceImage();
        assertNull(keySpaceImage);
    }

    @Test
    public void getStartGameSound_shouldReturnNullStartGameSound() {
        Sound startGameSound = gamePanel.getStartGameSound();
        assertNull(startGameSound);
    }

    @Test
    public void getStartGameVoiceSound_shouldReturnNullStartGameVoiceSound() {
        Sound startGameVoiceSound = gamePanel.getStartGameVoiceSound();
        assertNull(startGameVoiceSound);
    }

    @Test
    public void getWelcomeScreenVoiceSound_shouldReturnNullWelcomeScreenVoiceSound() {
        Sound welcomeScreenVoiceSound = gamePanel.getWelcomeScreenVoiceSound();
        assertNull(welcomeScreenVoiceSound);
    }

    @Test
    public void getGameOverSound_shouldReturnNullGameOverSound() {
        Sound gameOverSound = gamePanel.getGameOverSound();
        assertNull(gameOverSound);
    }

    @Test
    public void getGameOverVoiceSound_shouldReturnNullGameOverVoiceSound() {
        Sound gameOverVoiceSound = gamePanel.getGameOverVoiceSound();
        assertNull(gameOverVoiceSound);
    }

    @Test
    public void setShip_shouldChangeShip() {
        Ship newShip = mock(Ship.class);
        gamePanel.setShip(newShip);
        assertEquals(newShip, gamePanel.getShip());
    }

    @Test
    public void setLastState_shouldChangeLastState() {
        gamePanel.setLastState(GamePanel.GameState.GAME_OVER);
        assertEquals(GamePanel.GameState.GAME_OVER, gamePanel.getLastState());
    }

    @Test
    public void setCurrentState_shouldChangeCurrentState() {
        gamePanel.setCurrentState(GamePanel.GameState.PLAYING);
        assertEquals(GamePanel.GameState.PLAYING, gamePanel.getCurrentState());
    }

    @Test
    public void setStartTransitionCompleted_shouldChangeStartTransitionCompleted() {
        gamePanel.setStartTransitionCompleted(true);
        assertFalse(gamePanel.isStartTransitionNotCompleted());
    }

    @Test
    public void setLoseTransitionCompleted_shouldChangeLoseTransitionCompleted() {
        gamePanel.setLoseTransitionCompleted(true);
        assertFalse(gamePanel.isLoseTransitionNotCompleted());
    }

    @Test
    public void setRestartTransitionCompleted_shouldChangeRestartTransitionCompleted() {
        gamePanel.setRestartTransitionCompleted(true);
        assertFalse(gamePanel.isRestartTransitionNotCompleted());
    }

    @Test
    public void setScoreZoom_shouldChangeScoreZoom() {
        gamePanel.setScoreZoom(2.0);
        assertEquals(2.0, gamePanel.getScoreZoom());
    }
}
