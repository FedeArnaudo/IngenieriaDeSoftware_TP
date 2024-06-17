public class BulletShootingManager {
    private Bullet bullet;

    public void addToBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public void handleShooting() {
        if (bullet.isShoot()) {
            shoot();
        }
    }

    private void shoot() {
        bullet.decreaseY(bullet.getSpeed());
    }

    public void resetPosition() {
        if (!bullet.isShoot()) {
            resetToShipPosition();
        } else if (bullet.getY() < 0 || bullet.hasCollided()) {
            resetToShipPosition();
            bullet.setShoot(false);
            bullet.setCollision(false);
        }
    }

    private void resetToShipPosition() {
        bullet.setX(bullet.getShip().getX());
        bullet.setY(bullet.getShip().getY());
    }
}