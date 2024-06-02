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
        ship = new Ship(gamePanel, keyHandler, 100);
    }

    @Test
    public void shootingWhenSpacePressed() {
        keyHandler.spacePressed = true;
        ship.update();
        Assertions.assertEquals(1, ship.bulletFired);
    }

    @Test
    public void notShootingWhenSpaceNotPressed() {
        keyHandler.spacePressed = false;
        ship.update();
        Assertions.assertEquals(0, ship.bulletFired);
    }

    @Test
    public void notShootingWhenBulletCapacityReached() {
        ship.bulletFired = ship.bulletsCapacity;

        keyHandler.spacePressed = true;

        ship.update();

        // Verify that spacePressed is still true, indicating a shot was not fired
        Assertions.assertTrue(keyHandler.spacePressed);

        // Verify that bulletFired has not increased
        Assertions.assertEquals(ship.bulletsCapacity, ship.bulletFired);
    }

    @Test
    public void movingLeftWhenLeftPressed() {
        keyHandler.leftPressed = true;
        int initialX = ship.getX();
        ship.update();
        Assertions.assertEquals(initialX - ship.getSpeed(), ship.getX());
    }

    @Test
    public void notMovingLeftWhenLeftNotPressed() {
        keyHandler.leftPressed = false;
        int initialX = ship.getX();
        ship.update();
        Assertions.assertEquals(initialX, ship.getX());
    }

    @Test
    public void movingRightWhenRightPressed() {
        keyHandler.rightPressed = true;
        int initialX = ship.getX();

        when(gamePanel.getScreenWidth()).thenReturn(912);
        when(gamePanel.getTileSize()).thenReturn(57);

        ship.update();
        Assertions.assertEquals(initialX + ship.getSpeed(), ship.getX());
    }

    @Test
    public void notMovingRightWhenRightNotPressed() {
        keyHandler.rightPressed = false;
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