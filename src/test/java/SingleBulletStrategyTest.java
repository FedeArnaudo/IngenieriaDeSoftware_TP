import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import static org.mockito.Mockito.*;

public class SingleBulletStrategyTest {
    private SingleBulletStrategy singleBulletStrategy;
    private Ship ship;
    private KeyHandler keyHandler;
    private Bullet bullet;
    private Sound singleShootSound;
    private Sound emptySound;

    @BeforeEach
    public void setup() {
        singleBulletStrategy = new SingleBulletStrategy();
        ship = mock(Ship.class);
        keyHandler = mock(KeyHandler.class);
        bullet = mock(Bullet.class);
        singleShootSound = mock(Sound.class);
        emptySound = mock(Sound.class);

        when(ship.getKeyHandler()).thenReturn(keyHandler);
        when(ship.getBullets()).thenReturn(new ArrayList<>(Arrays.asList(bullet, bullet)));
        when(ship.getSingleShootSound()).thenReturn(singleShootSound);
        when(ship.getEmptySound()).thenReturn(emptySound);
    }

    @Test
    public void shouldShootWhenSpacePressedAndBulletsAvailableAndBulletNotFired() {
        when(ship.getBulletFired()).thenReturn(1);
        when(ship.getBulletsCapacity()).thenReturn(3);
        when(keyHandler.isSpacePressed()).thenReturn(true);
        when(keyHandler.isBulletNotFiredInCurrentKeyPress()).thenReturn(true);

        singleBulletStrategy.handleShooting(ship);

        verify(ship).increaseBulletFired(1);
        verify(singleShootSound).play();
    }

    @Test
    public void shouldNotShootWhenSpaceNotPressed() {
        when(keyHandler.isSpacePressed()).thenReturn(false);

        singleBulletStrategy.handleShooting(ship);

        verify(ship, never()).increaseBulletFired(1);
        verify(singleShootSound, never()).play();
    }

    @Test
    public void shouldPlayEmptySoundWhenNoBulletsAvailable() {
        when(ship.getBulletFired()).thenReturn(2);
        when(ship.getBulletsCapacity()).thenReturn(2);
        when(keyHandler.isSpacePressed()).thenReturn(true);
        when(keyHandler.isBulletNotFiredInCurrentKeyPress()).thenReturn(true);

        singleBulletStrategy.handleShooting(ship);

        verify(emptySound).play();
    }

    @Test
    public void shouldUpdateAllBullets() {
        singleBulletStrategy.updateBullets(ship);

        verify(bullet, times(2)).update();
    }
}