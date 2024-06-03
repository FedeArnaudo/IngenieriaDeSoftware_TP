package test.java;

import main.java.Bullet;
import main.java.GamePanel;
import main.java.KeyHandler;
import main.java.Ship;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class BulletTest {
    private GamePanel gamePanel;
    private KeyHandler keyHandler;
    private Ship ship;
    private Bullet bullet;

    @BeforeEach
    public void setUp() {
        gamePanel = mock(GamePanel.class);
        keyHandler = mock(KeyHandler.class);
        ship = mock(Ship.class);
        bullet = new Bullet(gamePanel, keyHandler, ship, 12);
    }

    @Test
    public void bulletMovesUpWhenShot() throws NoSuchFieldException, IllegalAccessException {
        Field field = bullet.getClass().getSuperclass().getDeclaredField("y");
        field.setAccessible(true);
        field.set(bullet, 850);
        int initialY = bullet.getY();
        bullet.setShootFlag(true);
        bullet.update();
        Assertions.assertEquals(initialY - bullet.getSpeed(), bullet.getY());
    }

    @Test
    public void bulletResetsPositionWhenNotShot() {
        bullet.setShootFlag(false);
        bullet.update();
        Assertions.assertEquals(ship.getX(), bullet.getX());
        Assertions.assertEquals(ship.getY(), bullet.getY());
    }

    @Test
    public void bulletResetsPositionWhenOffScreen() throws NoSuchFieldException, IllegalAccessException {
        Field field = bullet.getClass().getSuperclass().getDeclaredField("y");
        field.setAccessible(true);
        field.set(bullet, -1);
        bullet.update();
        Assertions.assertEquals(ship.getX(), bullet.getX());
        Assertions.assertEquals(ship.getY(), bullet.getY());
    }
}