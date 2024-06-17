import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.mockito.Mockito.*;

public class CollisionCheckerTest {
    private CollisionChecker collisionChecker;
    private GamePanel gamePanel;
    private Entity entity;

    @BeforeEach
    public void setup() {
        gamePanel = mock(GamePanel.class);
        collisionChecker = new CollisionChecker(gamePanel);
        entity = mock(Entity.class);
    }

    @Test
    public void detectObjectCallsCheckCollisionForAllObstacleTypes() {
        ArrayList<Obstacle> meteors1 = new ArrayList<>();
        ArrayList<Obstacle> meteors2 = new ArrayList<>();
        ArrayList<Obstacle> bulletPowerUps = new ArrayList<>();
        ArrayList<Obstacle> basicPowerUps = new ArrayList<>();
        ArrayList<Obstacle> superPowerUps = new ArrayList<>();

        when(gamePanel.getMeteors1()).thenReturn(meteors1);
        when(gamePanel.getMeteors2()).thenReturn(meteors2);
        when(gamePanel.getBulletPowerUps()).thenReturn(bulletPowerUps);
        when(gamePanel.getBasicPowerUps()).thenReturn(basicPowerUps);
        when(gamePanel.getSuperPowerUps()).thenReturn(superPowerUps);

        collisionChecker.detectObject(entity);

        verify(gamePanel).getMeteors1();
        verify(gamePanel).getMeteors2();
        verify(gamePanel).getBulletPowerUps();
        verify(gamePanel).getBasicPowerUps();
        verify(gamePanel).getSuperPowerUps();
    }
}