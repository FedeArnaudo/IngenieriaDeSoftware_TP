import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import static org.mockito.Mockito.*;

public class LaserBulletStrategyTest {
    private LaserBulletStrategy laserBulletStrategy;
    private Ship ship;
    private KeyHandler keyHandler;
    private Bullet bullet;
    private Sound laserShootSound;
    private Sound emptySound;

    @BeforeEach
    public void setup() {
        laserBulletStrategy = new LaserBulletStrategy();
        ship = mock(Ship.class);
        keyHandler = mock(KeyHandler.class);
        bullet = mock(Bullet.class);
        laserShootSound = mock(Sound.class);
        emptySound = mock(Sound.class);

        when(ship.getKeyHandler()).thenReturn(keyHandler);
        when(ship.getBullets()).thenReturn(new ArrayList<>(Arrays.asList(bullet, bullet)));
        when(ship.getLaserShootSound()).thenReturn(laserShootSound);
        when(ship.getEmptySound()).thenReturn(emptySound);
    }

    @Test
    public void shouldShootWhenSpacePressedAndBulletsAvailable() {
        when(ship.getBulletFired()).thenReturn(1);
        when(ship.getBulletsCapacity()).thenReturn(3);
        when(keyHandler.isSpacePressed()).thenReturn(true);

        laserBulletStrategy.handleShooting(ship);

        verify(ship).increaseBulletFired(1);
        verify(laserShootSound).play();
    }

    @Test
    public void shouldNotShootWhenSpaceNotPressed() {
        when(keyHandler.isSpacePressed()).thenReturn(false);

        laserBulletStrategy.handleShooting(ship);

        verify(ship, never()).increaseBulletFired(1);
        verify(laserShootSound, never()).play();
    }

    @Test
    public void shouldPlayEmptySoundWhenNoBulletsAvailable() {
        when(ship.getBulletFired()).thenReturn(2);
        when(ship.getBulletsCapacity()).thenReturn(2);
        when(keyHandler.isSpacePressed()).thenReturn(true);

        laserBulletStrategy.handleShooting(ship);

        verify(emptySound).play();
    }

    @Test
    public void shouldUpdateAllBullets() {
        laserBulletStrategy.updateBullets(ship);

        verify(bullet, times(2)).update();
    }
}