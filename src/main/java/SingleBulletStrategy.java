public class SingleBulletStrategy implements ShootingStrategy {
    @Override
    public void shoot(Ship ship) {
        ship.increaseBulletFired(1);
        int bulletIndex = ship.getBulletFired() - 1;
        ship.getBullets().get(bulletIndex).setShoot(true);
        ship.getSingleShootSound().play();
    }

    @Override
    public void handleShooting(Ship ship) {
        if (ship.getKeyHandler().isSpacePressed()) {
            boolean canShoot = ship.getBulletFired() < ship.getBulletsCapacity();
            boolean bulletNotFired = ship.getKeyHandler().isBulletNotFiredInCurrentKeyPress();

            if (canShoot && bulletNotFired) {
                shoot(ship);
                ship.getKeyHandler().setBulletFiredInCurrentKeyPress(true);
            } else if (!canShoot && bulletNotFired) {
                ship.getEmptySound().play();
                ship.getKeyHandler().setBulletFiredInCurrentKeyPress(true);
            }
        }
    }

    @Override
    public void updateBullets(Ship ship) {
        for (Bullet bullet : ship.getBullets()) {
            bullet.update();
        }
    }
}
