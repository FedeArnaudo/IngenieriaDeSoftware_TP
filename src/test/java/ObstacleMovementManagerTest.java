import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class ObstacleMovementManagerTest {
    private Obstacle obstacle;
    private ObstacleMovementManager obstacleMovementManager;

    @BeforeEach
    public void setup() {
        obstacle = mock(Obstacle.class);
        GamePanel gamePanel = mock(GamePanel.class);
        obstacleMovementManager = new ObstacleMovementManager();
        obstacleMovementManager.attachToObstacle(obstacle);
        when(obstacle.getGamePanel()).thenReturn(gamePanel);
        when(obstacle.getObstacleType()).thenReturn(Obstacle.ObstacleType.METEOR_1);
    }

    @Test
    public void handleMovementMovesDownAndChecksPosition() {
        obstacleMovementManager.handleMovement();
        verify(obstacle, times(1)).increaseY(anyInt());
        verify(obstacle, times(1)).getGamePanel();
    }

    @Test
    public void handleCollisionOrResetResetsObstacleAfterHasCollided10Times() {
        when(obstacle.hasCollided()).thenReturn(true);
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        verify(obstacle, times(1)).setDefaultValues();
    }

    @Test
    public void handleCollisionOrResetDoesNotResetsObstacleAfterHasCollidedLessThan10Times() {
        when(obstacle.hasCollided()).thenReturn(true);
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        obstacleMovementManager.handleMovement();
        verify(obstacle, never()).setDefaultValues();
    }

    @Test
    public void handleCollisionOrResetResetsObstacleWhenPositionExceedsResetPosition() {
        GamePanel gamePanel = mock(GamePanel.class);
        when(obstacle.getGamePanel()).thenReturn(gamePanel);
        when(gamePanel.getScreenHeight()).thenReturn(912);
        when(obstacle.getY()).thenReturn(913);
        obstacleMovementManager.handleMovement();
        verify(obstacle, times(1)).setCollision(false);
    }
}