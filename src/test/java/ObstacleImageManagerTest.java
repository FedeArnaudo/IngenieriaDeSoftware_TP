import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class ObstacleImageManagerTest {
    private ObstacleImageManager obstacleImageManager;
    private Obstacle obstacle;
    private GamePanel gamePanel;

    @BeforeEach
    public void setup() {
        obstacleImageManager = new ObstacleImageManager();
        obstacle = mock(Obstacle.class);
        gamePanel = mock(GamePanel.class);
        when(obstacle.getGamePanel()).thenReturn(gamePanel);
    }

    @Test
    public void attachToObstacle_doesNotThrowException() {
        assertDoesNotThrow(() -> obstacleImageManager.attachToObstacle(obstacle));
    }

    @Test
    public void draw_doesNotDrawWhenObstacleIsNotVisible() {
        Graphics2D graphics2D = mock(Graphics2D.class);
        when(obstacle.getX()).thenReturn(-1);
        when(obstacle.getY()).thenReturn(-1);

        obstacleImageManager.attachToObstacle(obstacle);
        obstacleImageManager.draw(graphics2D);

        verify(graphics2D, never()).drawImage(any(), anyInt(), anyInt(), anyInt(), anyInt(), any());
    }

    @Test
    public void draw_drawsWhenObstacleIsVisible() {
        Graphics2D graphics2D = mock(Graphics2D.class);
        when(obstacle.getX()).thenReturn(0);
        when(obstacle.getY()).thenReturn(0);
        when(gamePanel.getTileSize()).thenReturn(10);
        when(gamePanel.getScreenWidth()).thenReturn(800);
        when(gamePanel.getScreenHeight()).thenReturn(600);
        when(obstacle.getObstacleType()).thenReturn(Obstacle.ObstacleType.METEOR_1); // Stub the getObstacleType() method

        obstacleImageManager.attachToObstacle(obstacle);
        obstacleImageManager.draw(graphics2D);

        verify(graphics2D, times(1)).drawImage(any(), anyInt(), anyInt(), anyInt(), anyInt(), any());
    }
}