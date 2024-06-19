import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ShipTest {
    private Ship ship;
    private GamePanel gamePanel;
    private ShipStatsManager shipStatsManager;
    private ShipMovementManager shipMovementManager;
    private ShipBulletManager shipBulletManager;
    private ShipPowerUpManager shipPowerUpManager;
    private ShipSoundManager shipSoundManager;
    private ShipImageManager shipImageManager;
    private ShipCollisionManager shipCollisionManager;

    @BeforeEach
    public void setup() {
        gamePanel = mock(GamePanel.class);
        KeyHandler keyHandler = mock(KeyHandler.class);
        shipStatsManager = mock(ShipStatsManager.class);
        shipMovementManager = mock(ShipMovementManager.class);
        shipBulletManager = mock(ShipBulletManager.class);
        shipPowerUpManager = mock(ShipPowerUpManager.class);
        shipSoundManager = mock(ShipSoundManager.class);
        shipImageManager = mock(ShipImageManager.class);
        shipCollisionManager = mock(ShipCollisionManager.class);

        ship = new Ship(gamePanel, keyHandler, shipStatsManager, shipMovementManager, shipBulletManager, shipPowerUpManager, shipSoundManager, shipImageManager, shipCollisionManager);
    }

    @Test
    public void update_callsUpdateMethodsOfManagers() {
        ship.update();

        verify(shipStatsManager).updateStats();
        verify(shipMovementManager).handleMovement();
        verify(shipPowerUpManager).updatePowerUpStatus();
        verify(shipSoundManager).updateSounds();
        verify(shipBulletManager).updateCooldown();
    }

    @Test
    public void collide_callsCollideMethodOfCollisionManager() {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        int collideWithIndex = 0;

        ship.collide(Obstacle.ObstacleType.METEOR_1, obstacles, collideWithIndex);

        verify(shipCollisionManager).collide(Obstacle.ObstacleType.METEOR_1, obstacles, collideWithIndex);
    }

    @Test
    public void increaseScore_increasesScoreByGivenAmount() {
        int initialScore = ship.getScore();
        int increaseAmount = 10;

        ship.increaseScore(increaseAmount);

        assert(ship.getScore() == initialScore + increaseAmount);
    }

    @Test
    public void decreaseLives_decreasesLivesByGivenAmount() {
        int initialLives = ship.getLives();
        int decreaseAmount = 1;

        ship.decreaseLives(decreaseAmount);

        assert(ship.getLives() == initialLives - decreaseAmount);
    }

    @Test
    public void draw_callsDrawMethodsOfImageManager() {
        Graphics2D graphics2D = mock(Graphics2D.class);
        ship.draw(graphics2D);

        verify(shipImageManager).drawBullets(graphics2D);
        verify(shipImageManager).drawShip(graphics2D);
    }

    @Test
    public void increaseX_increasesXByGivenAmount() {
        int initialX = ship.getX();
        int increaseAmount = 10;

        ship.increaseX(increaseAmount);

        assert(ship.getX() == initialX + increaseAmount);
    }

    @Test
    public void decreaseX_decreasesXByGivenAmount() {
        int initialX = ship.getX();
        int decreaseAmount = 10;

        ship.decreaseX(decreaseAmount);

        assert(ship.getX() == initialX - decreaseAmount);
    }

    @Test
    public void increaseY_increasesYByGivenAmount() {
        int initialY = ship.getY();
        int increaseAmount = 10;

        ship.increaseY(increaseAmount);

        assert(ship.getY() == initialY + increaseAmount);
    }

    @Test
    public void decreaseY_decreasesYByGivenAmount() {
        int initialY = ship.getY();
        int decreaseAmount = 10;

        ship.decreaseY(decreaseAmount);

        assert(ship.getY() == initialY - decreaseAmount);
    }

    @Test
    public void increaseBulletFired_increasesBulletFiredByGivenAmount() {
        int initialBulletFired = ship.getBulletFired();
        int increaseAmount = 1;

        ship.increaseBulletFired(increaseAmount);

        assert(ship.getBulletFired() == initialBulletFired + increaseAmount);
    }

    @Test
    public void decreaseCooldownCounter_decreasesCooldownCounterByOne() {
        int initialCooldownCounter = ship.getCooldownCounter();

        ship.decreaseCooldownCounter();

        assert(ship.getCooldownCounter() == initialCooldownCounter - 1);
    }

    @Test
    public void increaseCollisionDebounce_increasesCollisionDebounceByOne() {
        int initialCollisionDebounce = ship.getCollisionDebounce();

        ship.increaseCollisionDebounce();

        assert(ship.getCollisionDebounce() == initialCollisionDebounce + 1);
    }

    @Test
    public void getGamePanel_worksCorrectly() {
        assert(ship.getGamePanel() == gamePanel);
    }

    @Test
    public void getAndSetDirection_worksCorrectly() {
        ship.setDirection(Ship.Direction.DOWN);
        assert(ship.getDirection() == Ship.Direction.DOWN);
    }

    @Test
    public void getAndSetSolidRectangle_worksCorrectly() {
        Rectangle newRectangle = new Rectangle(10, 10, 10, 10);
        ship.setSolidRectangle(newRectangle);
        assert(ship.getSolidRectangle().equals(newRectangle));
    }

    @Test
    public void getAndSetCollision_worksCorrectly() {
        ship.setCollision(true);
        assert(ship.hasCollided());
    }

    @Test
    public void getAndSetDefaultSpeed_worksCorrectly() {
        ship.setDefaultSpeed(10);
        assert(ship.getDefaultSpeed() == 10);
    }

    @Test
    public void getAndSetLastPowerUp_worksCorrectly() {
        ship.setLastPowerUp(Ship.PowerUpType.BASIC);
        assert(ship.getLastPowerUp() == Ship.PowerUpType.BASIC);
    }

    @Test
    public void getAndSetCurrentPowerUp_worksCorrectly() {
        ship.setCurrentPowerUp(Ship.PowerUpType.SUPER);
        assert(ship.getCurrentPowerUp() == Ship.PowerUpType.SUPER);
    }

    @Test
    public void getAndSetSuperPowerUpBulletCapacity_worksCorrectly() {
        ship.setSuperPowerUpBulletCapacity(10);
        assert(ship.getSuperPowerUpBulletCapacity() == 10);
    }

    @Test
    public void getAndSetCollidedWith_worksCorrectly() {
        ship.setCollidedWith(Obstacle.ObstacleType.METEOR_2);
        assert(ship.getCollidedWith() == Obstacle.ObstacleType.METEOR_2);
    }

    @Test
    public void getAndSetSingleShootSound_worksCorrectly() {
        Sound newSound = mock(Sound.class);
        ship.setSingleShootSound(newSound);
        assert(ship.getSingleShootSound() == newSound);
    }

    @Test
    public void getAndSetDoubleShootSound_worksCorrectly() {
        Sound newSound = mock(Sound.class);
        ship.setDoubleShootSound(newSound);
        assert(ship.getDoubleShootSound() == newSound);
    }

    @Test
    public void getAndSetLaserShootSound_worksCorrectly() {
        Sound newSound = mock(Sound.class);
        ship.setLaserShootSound(newSound);
        assert(ship.getLaserShootSound() == newSound);
    }

    @Test
    public void getAndSetEmptySound_worksCorrectly() {
        Sound newSound = mock(Sound.class);
        ship.setEmptySound(newSound);
        assert(ship.getEmptySound() == newSound);
    }

    @Test
    public void getAndSetImpactSound_worksCorrectly() {
        Sound newSound = mock(Sound.class);
        ship.setImpactSound(newSound);
        assert(ship.getImpactSound() == newSound);
    }

    @Test
    public void getAndSetPowerUpSound_worksCorrectly() {
        Sound newSound = mock(Sound.class);
        ship.setPowerUpSound(newSound);
        assert(ship.getPowerupSound() == newSound);
    }

    @Test
    public void getAndSetBasicPowerUpVoiceSound_worksCorrectly() {
        Sound newSound = mock(Sound.class);
        ship.setBasicPowerUpVoiceSound(newSound);
        assert(ship.getBasicPowerupVoiceSound() == newSound);
    }

    @Test
    public void getAndSetSuperPowerUpVoiceSound_worksCorrectly() {
        Sound newSound = mock(Sound.class);
        ship.setSuperPowerUpVoiceSound(newSound);
        assert(ship.getSuperPowerupVoiceSound() == newSound);
    }

    @Test
    public void getAndSetExplosionSound_worksCorrectly() {
        Sound newSound = mock(Sound.class);
        ship.setExplosionSound(newSound);
        assert(ship.getExplosionSound() == newSound);
    }

    @Test
    public void getAndSetMeteor2VoiceSound_worksCorrectly() {
        Sound newSound = mock(Sound.class);
        ship.setMeteor2VoiceSound(newSound);
        assert(ship.getMeteor2VoiceSound() == newSound);
    }
}