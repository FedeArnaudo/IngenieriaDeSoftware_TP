public class DoubleBulletStrategy implements ShootingStrategy {
    private int shootCount = 0;
    private int shootSoundCounter = 0;

    @Override
    public void shoot(Ship ship) {
        ship.increaseBulletFired(1);
        int bulletIndex = ship.getBulletFired() - 1;
        ship.getBullets().get(bulletIndex).setShoot(true);
        if (shootSoundCounter % 2 == 0) {
            ship.getDoubleShootSound().play();
        }
        shootSoundCounter++;
    }

    @Override
    public void handleShooting(Ship ship) {
        if (ship.getKeyHandler().isSpacePressed()) {
            boolean canShoot = ship.getBulletFired() < ship.getBulletsCapacity() - 1;
            boolean bulletNotFired = ship.getKeyHandler().isBulletNotFiredInCurrentKeyPress();

            if (canShoot && bulletNotFired) {
                if (shootCount == 0 || shootCount == 2) {
                    shoot(ship);
                    if (shootCount == 2) {
                        ship.getKeyHandler().setBulletFiredInCurrentKeyPress(true);
                    }
                }
                shootCount++  ;
                if (shootCount > 2) {
                    shootCount = 0; // Reset the counter
                }
            } else if (!canShoot && bulletNotFired) {
                ship.getEmptySound().play();
                ship.getKeyHandler().setBulletFiredInCurrentKeyPress(true);
            }
        }
    }

    @Override
    public void updateBullets(Ship ship) {
        for (Bullet bullet: ship.getBullets()){
            bullet.update();
        }
    }
}