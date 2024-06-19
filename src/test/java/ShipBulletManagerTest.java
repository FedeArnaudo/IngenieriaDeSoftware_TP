import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShipBulletManagerTest {
    private Ship ship;
    private ShipBulletManager shipBulletManager;

    @BeforeEach
    public void setup() {
        ship = mock(Ship.class);
        shipBulletManager = new ShipBulletManager(10, 5);
        shipBulletManager.attachToShip(ship);
    }

    @Test
    public void initializeCooldownSetsCorrectCooldownCounter() {
        shipBulletManager.initializeCooldown();
        verify(ship).setCooldownCounter(anyInt());
    }

    @Test
    public void initializeBulletsAddsBulletsToShip() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        GamePanel gamePanel = mock(GamePanel.class);
        when(ship.getSuperPowerUpBulletCapacity()).thenReturn(100);
        when(ship.getBullets()).thenReturn(bullets);
        when(ship.getGamePanel()).thenReturn(gamePanel);

        shipBulletManager.initializeBullets();

        assertEquals(100, bullets.size());
        verify(ship).setBulletFired(0);
    }

    @Test
    public void updateCooldown_decreasesCooldownWhenAllBulletsFired() {
        when(ship.getBulletFired()).thenReturn(5);
        when(ship.getBulletsCapacity()).thenReturn(5);

        shipBulletManager.updateCooldown();

        verify(ship).decreaseCooldownCounter();
    }

    @Test
    public void updateCooldownDoesNotDecreaseCooldownWhenNotAllBulletsFired() {
        when(ship.getBulletFired()).thenReturn(4);
        when(ship.getBulletsCapacity()).thenReturn(5);

        shipBulletManager.updateCooldown();

        verify(ship, never()).decreaseCooldownCounter();
    }

    @Test
    public void updateCooldownResetsCooldownWhenCounterIsZero() {
        when(ship.getBulletFired()).thenReturn(5);
        when(ship.getBulletsCapacity()).thenReturn(5);
        when(ship.getCooldownCounter()).thenReturn(0);

        shipBulletManager.updateCooldown();

        verify(ship).setBulletFired(50);
        verify(ship).setCooldownCounter(anyInt());
    }
}