package test.java;

import main.java.Bullet;
import main.java.GamePanel;
import main.java.KeyHandler;
import main.java.Ship;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class ShipTest {
    private GamePanel gamePanel;
    private KeyHandler keyHandler;
    private Ship ship;

    @BeforeEach
    public void setUp() {
        gamePanel = mock(GamePanel.class);
        keyHandler = mock(KeyHandler.class);
        ship = new Ship(gamePanel, keyHandler, 100, 4);
    }

    @Test
    public void shootingWhenSpacePressed() {
        when(keyHandler.getSpacePressed()).thenReturn(true);
        ship.update();
        Assertions.assertEquals(1, ship.getBulletFired());
    }

    @Test
    public void notShootingWhenSpaceNotPressed() {
        when(keyHandler.getSpacePressed()).thenReturn(false);
        ship.update();
        Assertions.assertEquals(0, ship.getBulletFired());
    }

    @Test
    public void notShootingWhenBulletCapacityReached() {
        ship.setBulletFired(ship.getBulletsCapacity());

        when(keyHandler.getSpacePressed()).thenReturn(true);

        ship.update();

        // Verify that spacePressed is still true, indicating a shot was not fired
        Assertions.assertTrue(keyHandler.getSpacePressed());

        // Verify that bulletFired has not increased
        Assertions.assertEquals(ship.getBulletsCapacity(), ship.getBulletFired());
    }

    @Test
    public void movingLeftWhenLeftPressed() {
        when(keyHandler.getLeftPressed()).thenReturn(true);
        int initialX = ship.getX();
        ship.update();
        Assertions.assertEquals(initialX - ship.getSpeed(), ship.getX());
    }

    @Test
    public void notMovingLeftWhenLeftNotPressed() {
        when(keyHandler.getLeftPressed()).thenReturn(false);
        int initialX = ship.getX();
        ship.update();
        Assertions.assertEquals(initialX, ship.getX());
    }

    @Test
    public void movingRightWhenRightPressed() {
        when(keyHandler.getRightPressed()).thenReturn(true);
        int initialX = ship.getX();

        when(gamePanel.getScreenWidth()).thenReturn(912);
        when(gamePanel.getTileSize()).thenReturn(57);

        ship.update();
        Assertions.assertEquals(initialX + ship.getSpeed(), ship.getX());
    }

    @Test
    public void notMovingRightWhenRightNotPressed() {
        when(keyHandler.getRightPressed()).thenReturn(false);
        int initialX = ship.getX();
        ship.update();
        Assertions.assertEquals(initialX, ship.getX());
    }
    @Test
    public void detectCollisionBtwBulletMeteor(){
        keyHandler.spacePressed = true;
        ship.update();
    }
}