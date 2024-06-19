import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class BulletCollisionManagerTest {
    private Bullet bullet;
    private BulletCollisionManager bulletCollisionManager;

    @BeforeEach
    public void setUp() {
        bullet = mock(Bullet.class);
        bulletCollisionManager = new BulletCollisionManager();
        bulletCollisionManager.addToBullet(bullet);
    }

    @Test
    public void initializeBulletSetsCorrectSolidRectangle() {
        GamePanel gamePanel = mock(GamePanel.class);
        when(gamePanel.getTileSize()).thenReturn(100);
        when(bullet.getGamePanel()).thenReturn(gamePanel);
        bulletCollisionManager.initializeBullet();
        verify(bullet, times(1)).setSolidRectangle(new Rectangle(24, 18, 52, 64));
    }

    @Test
    public void initializeBulletSetsCorrectSolidAreaDefaultXAndY() {
        GamePanel gamePanel = mock(GamePanel.class);
        when(gamePanel.getTileSize()).thenReturn(100);
        when(bullet.getGamePanel()).thenReturn(gamePanel);
        bulletCollisionManager.initializeBullet();
        verify(bullet, times(1)).setSolidAreaDefaultX(24);
        verify(bullet, times(1)).setSolidAreaDefaultY(18);
    }

    @Test
    public void handleCollisionWhenBulletIsNotShooting() {
        when(bullet.isShoot()).thenReturn(false);
        bulletCollisionManager.handleCollision();
        verify(bullet, times(0)).getGamePanel();
    }

    @Test
    public void handleCollisionWhenBulletIsShooting() {
        when(bullet.isShoot()).thenReturn(true);
        GamePanel gamePanel = mock(GamePanel.class);
        CollisionChecker collisionChecker = mock(CollisionChecker.class);
        when(bullet.getGamePanel()).thenReturn(gamePanel);
        when(gamePanel.getCollisionChecker()).thenReturn(collisionChecker);
        bulletCollisionManager.handleCollision();
        verify(collisionChecker, times(1)).detectObject(bullet);
    }

    @Test
    public void collideWithMeteor1IncreasesScoreBy10() {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        obstacles.add(mock(Obstacle.class));
        Ship ship = mock(Ship.class);
        when(bullet.getShip()).thenReturn(ship);
        when(ship.getExplosionSound()).thenReturn(mock(Sound.class));
        bulletCollisionManager.collide(Obstacle.ObstacleType.METEOR_1, obstacles, 0);
        verify(ship, times(1)).increaseScore(10);
    }

    @Test
    public void collideWithMeteor2IncreasesScoreBy100AndPlaysSound() {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        obstacles.add(mock(Obstacle.class));
        Ship ship = mock(Ship.class);
        Sound meteor2VoiceSound = mock(Sound.class);
        when(bullet.getShip()).thenReturn(ship);
        when(ship.getMeteor2VoiceSound()).thenReturn(meteor2VoiceSound);
        when(ship.getExplosionSound()).thenReturn(mock(Sound.class));
        bulletCollisionManager.collide(Obstacle.ObstacleType.METEOR_2, obstacles, 0);
        verify(ship, times(1)).increaseScore(100);
        verify(meteor2VoiceSound, times(1)).play();
    }

    @Test
    public void collideSetsCollisionOnBulletAndObstacleAndPlaysSound() {
        ArrayList<Obstacle> obstacles = new ArrayList<>();
        Obstacle obstacle = mock(Obstacle.class);
        obstacles.add(obstacle);
        Ship ship = mock(Ship.class);
        Sound explosionSound = mock(Sound.class);
        when(bullet.getShip()).thenReturn(ship);
        when(ship.getExplosionSound()).thenReturn(explosionSound);
        bulletCollisionManager.collide(Obstacle.ObstacleType.METEOR_1, obstacles, 0);
        verify(obstacle, times(1)).setCollision(true);
        verify(bullet, times(1)).setCollision(true);
        verify(explosionSound, times(1)).play();
    }
}