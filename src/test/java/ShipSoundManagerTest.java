import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class ShipSoundManagerTest {
    private Ship ship;
    private Sound sound;
    private ShipSoundManager shipSoundManager;

    @BeforeEach
    public void setup() {
        ship = mock(Ship.class);
        sound = mock(Sound.class);
        shipSoundManager = new ShipSoundManager();
        shipSoundManager.attachToShip(ship);
    }

    @Test
    public void initializeSounds_setsSoundsOnShip() {
        shipSoundManager.initializeSounds();

        verify(ship).setSingleShootSound(any(Sound.class));
        verify(ship).setDoubleShootSound(any(Sound.class));
        verify(ship).setLaserShootSound(any(Sound.class));
        verify(ship).setEmptySound(any(Sound.class));
        verify(ship).setImpactSound(any(Sound.class));
        verify(ship).setPowerUpSound(any(Sound.class));
        verify(ship).setSuperPowerUpVoiceSound(any(Sound.class));
        verify(ship).setBasicPowerUpVoiceSound(any(Sound.class));
        verify(ship).setExplosionSound(any(Sound.class));
        verify(ship).setMeteor2VoiceSound(any(Sound.class));
    }

    @Test
    public void updateSounds_playsImpactSoundOnMeteorCollision() {
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.METEOR_1);
        when(ship.getImpactSound()).thenReturn(sound);

        shipSoundManager.updateSounds();

        verify(sound).play();
    }

    @Test
    public void updateSounds_playsPowerupSoundOnBulletPowerUpCollision() {
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.BULLET_POWER_UP);
        when(ship.getPowerupSound()).thenReturn(sound);

        shipSoundManager.updateSounds();

        verify(sound).play();
    }

    @Test
    public void updateSounds_playsBasicPowerupSoundsOnBasicPowerUpCollision() {
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.BASIC_POWER_UP);
        when(ship.getLastPowerUp()).thenReturn(Ship.PowerUpType.NONE);
        when(ship.getPowerupSound()).thenReturn(sound);
        when(ship.getBasicPowerupVoiceSound()).thenReturn(sound);

        shipSoundManager.updateSounds();

        verify(sound, times(2)).play();
    }

    @Test
    public void updateSounds_playsSuperPowerupSoundsOnSuperPowerUpCollision() {
        when(ship.hasCollided()).thenReturn(true);
        when(ship.getCollisionDebounce()).thenReturn(10);
        when(ship.getCollidedWith()).thenReturn(Obstacle.ObstacleType.SUPER_POWER_UP);
        when(ship.getLastPowerUp()).thenReturn(Ship.PowerUpType.NONE);
        when(ship.getPowerupSound()).thenReturn(sound);
        when(ship.getSuperPowerupVoiceSound()).thenReturn(sound);

        shipSoundManager.updateSounds();

        verify(sound, times(2)).play();
    }
}