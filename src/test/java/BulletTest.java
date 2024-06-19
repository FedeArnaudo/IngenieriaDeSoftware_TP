import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BulletTest {
    private Bullet bullet;
    private Ship ship;
    private BulletShootingManager bulletShootingManager;
    private BulletCollisionManager bulletCollisionManager;
    private BulletImageManager bulletImageManager;

    @BeforeEach
    public void setup() {
        GamePanel gamePanel = mock(GamePanel.class);
        ship = mock(Ship.class);
        bulletShootingManager = mock(BulletShootingManager.class);
        bulletCollisionManager = mock(BulletCollisionManager.class);
        bulletImageManager = mock(BulletImageManager.class);
        bullet = new Bullet(gamePanel, ship, 10, bulletShootingManager, bulletCollisionManager, bulletImageManager);
    }

    @Test
    public void update_callsHandleShootingAndHandleCollisionAndResetPosition() {
        bullet.update();

        verify(bulletShootingManager).handleShooting();
        verify(bulletCollisionManager).handleCollision();
        verify(bulletShootingManager).resetPosition();
    }

    @Test
    public void collide_callsCollideInBulletCollisionManager() {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        bullet.collide(Obstacle.ObstacleType.METEOR_1, obstacles, 0);

        verify(bulletCollisionManager).collide(Obstacle.ObstacleType.METEOR_1, obstacles, 0);
    }

    @Test
    public void draw_callsDrawBulletInBulletImageManager() {
        Graphics2D graphics2D = mock(Graphics2D.class);
        bullet.draw(graphics2D);

        verify(bulletImageManager).drawBullet(graphics2D);
    }

    @Test
    public void setShoot_changesShootStatus() {
        bullet.setShoot(true);

        assertTrue(bullet.isShoot());
    }

    @Test
    public void decreaseY_decreasesYCoordinate() {
        int initialY = bullet.getY();
        bullet.decreaseY(5);

        assertEquals(initialY - 5, bullet.getY());
    }

    @Test
    public void getX_returnsCorrectX() {
        bullet.setX(10);
        assertEquals(10, bullet.getX());
    }

    @Test
    public void getY_returnsCorrectY() {
        bullet.setY(20);
        assertEquals(20, bullet.getY());
    }

    @Test
    public void getSpeed_returnsCorrectSpeed() {
        bullet.setSpeed(30);
        assertEquals(30, bullet.getSpeed());
    }

    @Test
    public void getDirection_returnsCorrectDirection() {
        bullet.setDirection(Entity.Direction.UP);
        assertEquals(Entity.Direction.UP, bullet.getDirection());
    }

    @Test
    public void getSolidRectangle_returnsCorrectSolidRectangle() {
        Rectangle rectangle = new Rectangle(10, 20, 30, 40);
        bullet.setSolidRectangle(rectangle);
        assertEquals(rectangle, bullet.getSolidRectangle());
    }

    @Test
    public void hasCollided_returnsCorrectCollisionStatus() {
        bullet.setCollision(true);
        assertTrue(bullet.hasCollided());
    }

    @Test
    public void getShip_returnsCorrectShip() {
        assertEquals(ship, bullet.getShip());
    }

    @Test
    public void isShoot_returnsCorrectShootStatus() {
        bullet.setShoot(true);
        assertTrue(bullet.isShoot());
    }

    @Test
    public void setX_changesX() {
        bullet.setX(50);
        assertEquals(50, bullet.getX());
    }

    @Test
    public void setY_changesY() {
        bullet.setY(60);
        assertEquals(60, bullet.getY());
    }

    @Test
    public void setDirection_changesDirection() {
        bullet.setDirection(Entity.Direction.DOWN);
        assertEquals(Entity.Direction.DOWN, bullet.getDirection());
    }

    @Test
    public void setSpeed_changesSpeed() {
        bullet.setSpeed(70);
        assertEquals(70, bullet.getSpeed());
    }

    @Test
    public void setCollision_changesCollisionStatus() {
        bullet.setCollision(false);
        assertFalse(bullet.hasCollided());
    }

    @Test
    public void setSolidRectangle_changesSolidRectangle() {
        Rectangle rectangle = new Rectangle(50, 60, 70, 80);
        bullet.setSolidRectangle(rectangle);
        assertEquals(rectangle, bullet.getSolidRectangle());
    }
}