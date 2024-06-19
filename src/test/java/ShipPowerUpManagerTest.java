import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class ShipPowerUpManagerTest {
    private Ship ship;
    private ShipPowerUpManager shipPowerUpManager;
    private final int POWER_UP_DURATION = 30 * GamePanel.getFps();

    @BeforeEach
    public void setUp() {
        ship = mock(Ship.class);
        shipPowerUpManager = new ShipPowerUpManager();
        shipPowerUpManager.attachToShip(ship);
    }

    @Test
    public void initializePowerUp_setsSuperPowerUpBulletCapacity() {
        shipPowerUpManager.initializePowerUp();
        verify(ship).setSuperPowerUpBulletCapacity(anyInt());
    }

    @Test
    public void updatePowerUpStatusHandlesCollisionWhenShipHasCollidedWithBasicPowerUpWithNonePowerUp() {
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.BASIC_POWER_UP);
        when(ship.getLastPowerUp()).thenReturn(Ship.PowerUpType.NONE);
        when(ship.getCurrentPowerUp()).thenReturn(Ship.PowerUpType.NONE);
        shipPowerUpManager.updatePowerUpStatus();
        verify(ship).setLastPowerUp(Ship.PowerUpType.NONE);
        verify(ship).setCurrentPowerUp(Ship.PowerUpType.BASIC);
    }

    @Test
    public void updatePowerUpStatusHandlesCollisionWhenShipHasCollidedWithBasicPowerUpWithBasicPowerUp() {
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.BASIC_POWER_UP);
        when(ship.getLastPowerUp()).thenReturn(Ship.PowerUpType.BASIC);
        when(ship.getCurrentPowerUp()).thenReturn(Ship.PowerUpType.BASIC);
        shipPowerUpManager.updatePowerUpStatus();
        verify(ship).setLastPowerUp(Ship.PowerUpType.BASIC);
        verify(ship, never()).setCurrentPowerUp(Ship.PowerUpType.BASIC);
    }


    @Test
    public void updatePowerUpStatusHandlesCollisionWhenShipHasCollidedWithSuperPowerUpWithNonePowerUp() {
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.SUPER_POWER_UP);
        when(ship.getLastPowerUp()).thenReturn(Ship.PowerUpType.NONE);
        when(ship.getCurrentPowerUp()).thenReturn(Ship.PowerUpType.NONE);
        shipPowerUpManager.updatePowerUpStatus();
        verify(ship).setLastPowerUp(Ship.PowerUpType.NONE);
        verify(ship).setCurrentPowerUp(Ship.PowerUpType.SUPER);
    }

    @Test
    public void updatePowerUpStatusHandlesCollisionWhenShipHasCollidedWithSuperPowerUpWithSuperPowerUp() {
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.SUPER_POWER_UP);
        when(ship.getLastPowerUp()).thenReturn(Ship.PowerUpType.SUPER);
        when(ship.getCurrentPowerUp()).thenReturn(Ship.PowerUpType.SUPER);
        shipPowerUpManager.updatePowerUpStatus();
        verify(ship).setLastPowerUp(Ship.PowerUpType.SUPER);
        verify(ship, never()).setCurrentPowerUp(Ship.PowerUpType.SUPER);
    }

    @Test
    public void updatePowerUpStatusHandlesCollisionWhenShipHasCollidedWithSuperPowerUpWithBasicPowerUp() {
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.SUPER_POWER_UP);
        when(ship.getLastPowerUp()).thenReturn(Ship.PowerUpType.BASIC);
        when(ship.getCurrentPowerUp()).thenReturn(Ship.PowerUpType.BASIC);
        shipPowerUpManager.updatePowerUpStatus();
        verify(ship).setLastPowerUp(Ship.PowerUpType.BASIC);
        verify(ship).setCurrentPowerUp(Ship.PowerUpType.SUPER);
    }

    @Test
    public void updatePowerUpStatusHandlesCollisionWhenShipHasCollidedWithBasicPowerUpWithSuperPowerUp() {
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.BASIC_POWER_UP);
        when(ship.getLastPowerUp()).thenReturn(Ship.PowerUpType.SUPER);
        when(ship.getCurrentPowerUp()).thenReturn(Ship.PowerUpType.SUPER);
        shipPowerUpManager.updatePowerUpStatus();
        verify(ship).setLastPowerUp(Ship.PowerUpType.SUPER);
        verify(ship, never()).setCurrentPowerUp(Ship.PowerUpType.BASIC);
    }

    @Test
    public void updatePowerUpStatusHandlesPowerUpExpiryWhenBasicPowerUpIsActiveAndThePowerUpDurationHasBeenReached() {
        when(ship.hasCollided()).thenReturn(false);
        when(ship.getCurrentPowerUp()).thenReturn(Ship.PowerUpType.BASIC);

        for (int i = 0; i < POWER_UP_DURATION; i++) {
            shipPowerUpManager.updatePowerUpStatus();
        }

        verify(ship).setLastPowerUp(Ship.PowerUpType.BASIC);
        verify(ship).setCurrentPowerUp(Ship.PowerUpType.NONE);
        verify(ship).setBulletsCapacity(anyInt());
        verify(ship).setBulletFired(0);
        verify(ship).setSpeed(anyInt());
        verify(ship).setShootingStrategy(any());
    }

    @Test
    public void updatePowerUpStatusHandlesPowerUpExpiryWhenSuperPowerUpIsActiveAndThePowerUpDurationHasBeenReached() {
        when(ship.hasCollided()).thenReturn(false);
        when(ship.getCurrentPowerUp()).thenReturn(Ship.PowerUpType.SUPER);

        for (int i = 0; i < POWER_UP_DURATION; i++) {
            shipPowerUpManager.updatePowerUpStatus();
        }

        verify(ship).setLastPowerUp(Ship.PowerUpType.SUPER);
        verify(ship).setCurrentPowerUp(Ship.PowerUpType.NONE);
        verify(ship).setBulletsCapacity(anyInt());
        verify(ship).setBulletFired(0);
        verify(ship).setSpeed(anyInt());
        verify(ship).setShootingStrategy(any());
    }

    @Test
    public void updatePowerUpStatusHandlesCollisionWhenShipHasNotCollided() {
        when(ship.hasCollided()).thenReturn(false);
        when(ship.getCurrentPowerUp()).thenReturn(Ship.PowerUpType.NONE);
        shipPowerUpManager.updatePowerUpStatus();
        verify(ship, never()).setLastPowerUp(any());
        verify(ship, never()).setCurrentPowerUp(any());
        verify(ship, never()).setBulletsCapacity(anyInt());
        verify(ship, never()).setBulletFired(0);
        verify(ship, never()).setSpeed(anyInt());
        verify(ship, never()).setShootingStrategy(any());
    }
}