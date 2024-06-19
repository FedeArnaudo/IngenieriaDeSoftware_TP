import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class BulletShootingManagerTest {
    private Bullet bullet;
    private BulletShootingManager bulletShootingManager;

    @BeforeEach
    void setUp() {
        bullet = mock(Bullet.class);
        bulletShootingManager = new BulletShootingManager();
        bulletShootingManager.addToBullet(bullet);
    }

    @Test
    void handleShootingWhenBulletIsShot() {
        when(bullet.isShoot()).thenReturn(true);
        bulletShootingManager.handleShooting();
        verify(bullet).decreaseY(anyInt());
    }

    @Test
    void handleShootingWhenBulletIsNotShot() {
        when(bullet.isShoot()).thenReturn(false);
        bulletShootingManager.handleShooting();
        verify(bullet, never()).decreaseY(anyInt());
    }

    @Test
    void resetToShipPositionWhenBulletIsNotShot() {
        when(bullet.isShoot()).thenReturn(false);
        when(bullet.getShip()).thenReturn(mock(Ship.class));
        when(bullet.getShip().getX()).thenReturn(10);
        when(bullet.getShip().getY()).thenReturn(20);
        bulletShootingManager.resetPosition();
        verify(bullet).setX(anyInt());
        verify(bullet).setY(anyInt());
    }

    @Test
    void resetPositionWhenBulletIsShotAndHasCollided() {
        when(bullet.isShoot()).thenReturn(true);
        when(bullet.hasCollided()).thenReturn(true);
        when(bullet.getShip()).thenReturn(mock(Ship.class));
        when(bullet.getShip().getX()).thenReturn(10);
        when(bullet.getShip().getY()).thenReturn(20);
        bulletShootingManager.resetPosition();
        verify(bullet).setX(anyInt());
        verify(bullet).setY(anyInt());
        verify(bullet).setShoot(false);
        verify(bullet).setCollision(false);
    }

    @Test
    void resetPositionWhenBulletIsShotAndHasNotCollided() {
        when(bullet.isShoot()).thenReturn(true);
        when(bullet.hasCollided()).thenReturn(false);
        bulletShootingManager.resetPosition();
        verify(bullet, never()).setX(anyInt());
        verify(bullet, never()).setY(anyInt());
        verify(bullet, never()).setShoot(anyBoolean());
        verify(bullet, never()).setCollision(anyBoolean());
    }

    @Test
    void resetPositionWhenBulletIsShotAndYIsLessThanZero() {
        when(bullet.isShoot()).thenReturn(true);
        when(bullet.getY()).thenReturn(-1);
        when(bullet.getShip()).thenReturn(mock(Ship.class));
        when(bullet.getShip().getX()).thenReturn(10);
        when(bullet.getShip().getY()).thenReturn(20);
        bulletShootingManager.resetPosition();
        verify(bullet).setX(anyInt());
        verify(bullet).setY(anyInt());
        verify(bullet).setShoot(false);
        verify(bullet).setCollision(false);
    }

    @Test
    void resetPositionWhenBulletIsShotAndYIsGreaterThanZero() {
        when(bullet.isShoot()).thenReturn(true);
        when(bullet.getY()).thenReturn(1);
        when(bullet.getShip()).thenReturn(mock(Ship.class));
        when(bullet.getShip().getX()).thenReturn(10);
        when(bullet.getShip().getY()).thenReturn(20);
        bulletShootingManager.resetPosition();
        verify(bullet, never()).setX(anyInt());
        verify(bullet, never()).setY(anyInt());
        verify(bullet, never()).setShoot(anyBoolean());
        verify(bullet, never()).setCollision(anyBoolean());
    }

    @Test
    void resetPositionWhenBulletIsShotAndHasCollidedAndYIsLessThanZero() {
        when(bullet.isShoot()).thenReturn(true);
        when(bullet.hasCollided()).thenReturn(true);
        when(bullet.getY()).thenReturn(-1);
        when(bullet.getShip()).thenReturn(mock(Ship.class));
        when(bullet.getShip().getX()).thenReturn(10);
        when(bullet.getShip().getY()).thenReturn(20);
        bulletShootingManager.resetPosition();
        verify(bullet).setX(anyInt());
        verify(bullet).setY(anyInt());
        verify(bullet).setShoot(false);
        verify(bullet).setCollision(false);
    }
}