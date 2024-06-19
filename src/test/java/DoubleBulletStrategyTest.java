import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import static org.mockito.Mockito.*;

public class DoubleBulletStrategyTest {
    private DoubleBulletStrategy doubleBulletStrategy;
    private Ship ship;
    private KeyHandler keyHandler;
    private Bullet bullet;
    private Sound doubleShootSound;
    private Sound emptySound;

    @BeforeEach
    public void setup() {
        doubleBulletStrategy = new DoubleBulletStrategy();
        ship = mock(Ship.class);
        keyHandler = mock(KeyHandler.class);
        bullet = mock(Bullet.class);
        doubleShootSound = mock(Sound.class);
        emptySound = mock(Sound.class);

        when(ship.getKeyHandler()).thenReturn(keyHandler);
        when(ship.getBullets()).thenReturn(new ArrayList<>(Arrays.asList(bullet, bullet)));
        when(ship.getDoubleShootSound()).thenReturn(doubleShootSound);
        when(ship.getEmptySound()).thenReturn(emptySound);
    }

    @Test
    public void shouldShootTwiceWhenSpacePressedAndBulletsAvailableAndRun3Times() {
        when(ship.getBulletFired()).thenReturn(1);
        when(ship.getBulletsCapacity()).thenReturn(3);
        when(keyHandler.isSpacePressed()).thenReturn(true);
        when(keyHandler.isBulletNotFiredInCurrentKeyPress()).thenReturn(true);

        doubleBulletStrategy.handleShooting(ship);
        doubleBulletStrategy.handleShooting(ship);
        doubleBulletStrategy.handleShooting(ship);

        verify(ship, times(2)).increaseBulletFired(1);
        verify(doubleShootSound).play();
    }

    @Test
    public void shouldSetBulletFiredInCurrentKeyPressWhenShootCountIs2() {
        when(ship.getBulletFired()).thenReturn(1);
        when(ship.getBulletsCapacity()).thenReturn(3);
        when(keyHandler.isSpacePressed()).thenReturn(true);
        when(keyHandler.isBulletNotFiredInCurrentKeyPress()).thenReturn(true);

        doubleBulletStrategy.handleShooting(ship);
        doubleBulletStrategy.handleShooting(ship);
        doubleBulletStrategy.handleShooting(ship);

        verify(keyHandler).setBulletFiredInCurrentKeyPress(true);
    }

    @Test
    public void shouldPlayEmptySoundWhenNoBulletsAvailable() {
        when(ship.getBulletFired()).thenReturn(2);
        when(ship.getBulletsCapacity()).thenReturn(2);
        when(keyHandler.isSpacePressed()).thenReturn(true);
        when(keyHandler.isBulletNotFiredInCurrentKeyPress()).thenReturn(true);

        doubleBulletStrategy.handleShooting(ship);

        verify(emptySound).play();
    }

    @Test
    public void shouldUpdateAllBullets() {
        doubleBulletStrategy.updateBullets(ship);

        verify(bullet, times(2)).update();
    }
}