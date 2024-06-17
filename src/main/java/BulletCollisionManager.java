import java.awt.*;
import java.util.ArrayList;

public class BulletCollisionManager {
    private Bullet bullet;

    public void addToBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public void initializeBullet() {
        bullet.setCollision(false);
        Rectangle solidRect = new Rectangle(24, 18, bullet.getGamePanel().getTileSize() - 48, bullet.getGamePanel().getTileSize() - 36);
        bullet.setSolidRectangle(solidRect);
        bullet.setSolidAreaDefaultX(solidRect.x);
        bullet.setSolidAreaDefaultY(solidRect.y);
    }

    public void handleCollision() {
        if (bullet.isShoot()) {
            bullet.getGamePanel().getCollisionChecker().detectObject(bullet);
        }
    }

    public void collide(Obstacle.ObstacleType collideWithType, ArrayList<Obstacle> obstacles, int collideWithIndex){
        switch (collideWithType){
            case METEOR_1:
                bullet.getShip().increaseScore(10);
                break;

            case METEOR_2:
                bullet.getShip().increaseScore(100);
                bullet.getShip().getMeteor2VoiceSound().play();
                break;
        }

        obstacles.get(collideWithIndex).setCollision(true);
        bullet.setCollision(true);
        bullet.getShip().getExplosionSound().play();
    }
}
