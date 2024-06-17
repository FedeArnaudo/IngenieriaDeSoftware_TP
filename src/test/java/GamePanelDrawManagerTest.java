import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.image.BufferedImage;

import static org.mockito.Mockito.*;

public class GamePanelDrawManagerTest {
    @Mock
    private GamePanel gamePanel;
    private Ship ship;

    @Mock
    private Graphics2D graphics2D;

    @Mock
    private TileManager tileManager;

    private GamePanelDrawManager gamePanelDrawManager;

    @BeforeEach
    public void setUp() {
        gamePanel = mock(GamePanel.class);
        tileManager = mock(TileManager.class);
        graphics2D = mock(Graphics2D.class);
        FontMetrics fontMetrics = mock(FontMetrics.class);
        when(graphics2D.getFontMetrics(any(Font.class))).thenReturn(fontMetrics);
        when(graphics2D.getFontMetrics()).thenReturn(fontMetrics);
        when(fontMetrics.stringWidth(anyString())).thenReturn(10);

        ship = mock(Ship.class);
        BufferedImage bulletScoreboardImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        when(ship.getBulletsCapacity()).thenReturn(10);
        when(ship.getBulletFired()).thenReturn(0);
        when(gamePanel.getShip()).thenReturn(ship);
        when(gamePanel.getTileManager()).thenReturn(tileManager);
        when(gamePanel.getBulletScoreboardImage()).thenReturn(bulletScoreboardImage);
        when(gamePanel.getScreenWidth()).thenReturn(800); // replace with your screen width
        when(gamePanel.getScreenHeight()).thenReturn(600); // replace with your screen height

        // Mocking getLetterImages() method


        // Mocking getKeyUpImage(), getKeyDownImage(), getKeyRightImage(), getKeyLeftImage(), getKeySpaceImage() methods
        BufferedImage keyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        when(gamePanel.getKeyUpImage()).thenReturn(keyImage);
        when(gamePanel.getKeyDownImage()).thenReturn(keyImage);
        when(gamePanel.getKeyRightImage()).thenReturn(keyImage);
        when(gamePanel.getKeyLeftImage()).thenReturn(keyImage);
        when(gamePanel.getKeySpaceImage()).thenReturn(keyImage);



        gamePanelDrawManager = new GamePanelDrawManager();
        gamePanelDrawManager.attachToGamePanel(gamePanel);
    }

    @Test
    public void drawObstaclesCallsDrawMethodOfEachObstacle() {
        Obstacle obstacle = mock(Obstacle.class);
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        obstacles.add(obstacle);
        when(gamePanel.getMeteors1()).thenReturn(obstacles);
        gamePanelDrawManager.drawObstacles(graphics2D);
        verify(obstacle, times(1)).draw(graphics2D);
    }

    @Test
    public void drawScoreDecreasesScoreZoomWhenGreaterThanOne() {
        Ship ship = mock(Ship.class);
        when(ship.getScore()).thenReturn(100);
        when(gamePanel.getShip()).thenReturn(ship);
        when(gamePanel.getScoreZoom()).thenReturn(1.5);

        gamePanelDrawManager.drawScore(graphics2D);

        verify(gamePanel, times(1)).setScoreZoom(anyDouble());
    }

    @Test
    public void drawPlayingScreenCallsDrawMethodOfShipAndObstacles() {
        Ship ship = mock(Ship.class);
        when(ship.getBulletFired()).thenReturn(0);
        when(ship.getBulletsCapacity()).thenReturn(10);
        when(gamePanel.getShip()).thenReturn(ship);
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.PLAYING);

        gamePanelDrawManager.drawGameElements(graphics2D);

        verify(ship, times(1)).draw(graphics2D);
    }

    @Test
    public void drawWelcomeScreenCallsDrawMethodOfShip() {
        Map<Character, BufferedImage> letterImages = new HashMap<>();
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        letterImages.put('a', image);
        when(gamePanel.getLetterImages()).thenReturn(letterImages);
        when(gamePanel.getShip()).thenReturn(ship);
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.WELCOME);

        gamePanelDrawManager.drawGameElements(graphics2D);

        verify(ship, times(1)).drawWelcomeScreen(graphics2D);
    }

    @Test
    public void drawPauseScreenFillsBackgroundWithColor() {
        Map<Character, BufferedImage> letterImages = new HashMap<>();
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        letterImages.put('a', image);
        when(gamePanel.getLetterImages()).thenReturn(letterImages);

        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.PAUSE);

        gamePanelDrawManager.drawGameElements(graphics2D);

        verify(graphics2D, times(1)).setColor(Color.decode("#000422"));
        verify(graphics2D, times(1)).fillRect(0, 0, gamePanel.getScreenWidth(), gamePanel.getScreenHeight());
    }

    @Test
    public void drawGameOverScreenCallsDrawMethodOfFinalScore() {
        Ship ship = mock(Ship.class);
        when(ship.getFinalScore()).thenReturn(100);
        when(gamePanel.getShip()).thenReturn(ship);
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.GAME_OVER);

        gamePanelDrawManager.drawGameElements(graphics2D);

        verify(gamePanel.getShip(), times(1)).getFinalScore();
    }

    @Test
    public void drawLivesCallsDrawImageWithPaddingForEachLife() {
        Ship ship = mock(Ship.class);
        when(ship.getLives()).thenReturn(3);
        when(gamePanel.getShip()).thenReturn(ship);
        when(gamePanel.getLiveImage()).thenReturn(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));

        gamePanelDrawManager.drawLives(graphics2D);

        verify(graphics2D, times(3)).drawImage(any(), anyInt(), anyInt(), anyInt(), anyInt(), any());
    }

    @Test
    public void drawBulletsLeftDrawsCorrectNumberOfBullets() {
        Ship ship = mock(Ship.class);
        when(ship.getBulletsCapacity()).thenReturn(10);
        when(ship.getBulletFired()).thenReturn(5);
        when(gamePanel.getShip()).thenReturn(ship);

        gamePanelDrawManager.drawBulletsLeft(graphics2D);

        verify(graphics2D).drawString(eq("5"), anyInt(), anyInt());
    }

    @Test
    public void drawCooldownCountdownDrawsCorrectCooldownTime() {
        BufferedImage image = mock(BufferedImage.class);
        when(image.getHeight()).thenReturn(1);
        when(image.getWidth()).thenReturn(1);
        when(gamePanel.getCollingIconImage()).thenReturn(image);
        when(ship.getCooldownCounter()).thenReturn(0);
        when(ship.getBulletsCapacity()).thenReturn(10);
        when(ship.getBulletFired()).thenReturn(10);
        when(gamePanel.getShip()).thenReturn(ship);

        gamePanelDrawManager.drawCooldownCountdown(graphics2D);

        verify(graphics2D).drawString(eq("1"), anyInt(), anyInt());
    }

    @Test
    public void drawGameOverScreenCallsDrawFinalScore() {
        Ship ship = mock(Ship.class);
        when(ship.getFinalScore()).thenReturn(100);
        when(gamePanel.getShip()).thenReturn(ship);
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.GAME_OVER);

        gamePanelDrawManager.drawGameOverScreen(graphics2D);

        verify(gamePanel.getShip(), times(1)).getFinalScore();
    }
}