import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class ShipStatsManagerTest {
    private Ship ship;
    private ShipStatsManager shipStatsManager;

    @BeforeEach
    public void setup() {
        ship = mock(Ship.class);
        shipStatsManager = new ShipStatsManager(3, 5, 10);
        shipStatsManager.attachToShip(ship);
    }

    @Test
    public void initializeStats_setsInitialValues() {
        shipStatsManager.initializeStats();

        verify(ship).setLives(3);
        verify(ship).setSpeed(5);
        verify(ship).setDefaultSpeed(5);
        verify(ship).setBulletsCapacity(10);
        verify(ship).setScore(0);
        verify(ship).setFinalScore(0);
    }

    @Test
    public void updateStats_incrementsScoreWhenAlive() {
        when(ship.hasCollided()).thenReturn(false);

        for (int i = 0; i < 31; i++) {
            shipStatsManager.updateStats();
        }

        verify(ship).increaseScore(1);
    }

    @Test
    public void updateStats_resetsScoreWhenMaxScoreReached() {
        when(ship.getScore()).thenReturn(999999);

        shipStatsManager.updateStats();

        verify(ship).setFinalScore(999999);
        verify(ship).setScore(0);
    }

    @Test
    public void handleCollision_decreasesLivesWhenMeteor1() {
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.METEOR_1);

        shipStatsManager.updateStats();

        verify(ship).decreaseLives(1);
    }

    @Test
    public void handleCollision_decreasesLivesWhenMeteor2() {
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.METEOR_2);

        shipStatsManager.updateStats();

        verify(ship).decreaseLives(2);
    }

    @Test
    public void handleCollision_resetsBulletFiredWhenBulletPowerUp() {
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.BULLET_POWER_UP);

        shipStatsManager.updateStats();

        verify(ship).setBulletFired(0);
    }

    @Test
    public void handleCollision_appliesBasicPowerUp() {
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.BASIC_POWER_UP);
        when(ship.getLastPowerUp()).thenReturn(Ship.PowerUpType.NONE);

        shipStatsManager.updateStats();

        verify(ship).setBulletsCapacity(anyInt());
        verify(ship).setBulletFired(0);
        verify(ship).setShootingStrategy(any(DoubleBulletStrategy.class));
    }

    @Test
    public void handleCollision_appliesSuperPowerUp() {
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.SUPER_POWER_UP);
        when(ship.getLastPowerUp()).thenReturn(Ship.PowerUpType.NONE);

        shipStatsManager.updateStats();

        verify(ship).setLives(7);
        verify(ship).setSpeed(anyInt());
        verify(ship).setBulletsCapacity(anyInt());
        verify(ship).setBulletFired(0);
        verify(ship).setShootingStrategy(any(LaserBulletStrategy.class));
    }
}