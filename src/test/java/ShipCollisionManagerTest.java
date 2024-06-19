import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.util.ArrayList;

public class ShipCollisionManagerTest {
    private Ship ship;
    private ShipCollisionManager shipCollisionManager;
    private CollisionChecker collisionChecker;

    @BeforeEach
    public void setup() {
        ship = mock(Ship.class);
        GamePanel gamePanel = mock(GamePanel.class);
        collisionChecker = mock(CollisionChecker.class);
        when(ship.getGamePanel()).thenReturn(gamePanel);
        when(gamePanel.getCollisionChecker()).thenReturn(collisionChecker);
        shipCollisionManager = new ShipCollisionManager();
        shipCollisionManager.attachToShip(ship);
    }

    @Test
    public void initializeCollisionInitializesCollisionState() {
        Rectangle solidRectangle = mock(Rectangle.class);
        when(ship.getSolidRectangle()).thenReturn(solidRectangle);


        shipCollisionManager.initializeCollision();

        verify(ship).setCollision(false);
        verify(ship).setCollisionDebounce(0);
        verify(ship).setSolidRectangle(any());
        verify(ship).setSolidAreaDefaultX(anyInt());
        verify(ship).setSolidAreaDefaultY(anyInt());
    }

    @Test
    public void handleCollisionDetectsCollisionWhenShipHasNotCollided() {
        when(ship.hasCollided()).thenReturn(false);

        shipCollisionManager.handleCollision();

        verify(collisionChecker).detectObject(ship);
    }

    @Test
    public void handleCollisionIncreasesDebounceWhenShipHasCollided() {
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollisionDebounce()).thenReturn(5);

        shipCollisionManager.handleCollision();

        verify(ship).increaseCollisionDebounce();
    }

    @Test
    public void handleCollisionResetsCollisionWhenDebounceExceeded() {
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollisionDebounce()).thenReturn(11);

        shipCollisionManager.handleCollision();

        verify(ship).setCollision(false);
        verify(ship).setCollisionDebounce(0);
    }

    @Test
    public void collide_setsCollisionState() {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        Obstacle obstacle = mock(Obstacle.class);
        obstacles.add(obstacle);

        shipCollisionManager.collide(Obstacle.ObstacleType.METEOR_1, obstacles, 0);

        verify(ship).setCollidedWith(Obstacle.ObstacleType.METEOR_1);
        verify(ship).setCollision(true);
        verify(obstacle).setCollision(true);
    }
}