public class LaserBulletStrategy implements ShootingStrategy {
    private int shootSoundCounter = 0;
    private int emptySoundCounter = 0;

    @Override
    public void shoot(Ship ship) {
        ship.increaseBulletFired(1);
        int bulletIndex = ship.getBulletFired() - 1;
        ship.getBullets().get(bulletIndex).setShoot(true);
        if (shootSoundCounter % 3 == 0) {
            ship.getLaserShootSound().play();
        }
        shootSoundCounter++;
        emptySoundCounter = 0;
    }

    @Override
    public void handleShooting(Ship ship) {
        if (ship.getKeyHandler().isSpacePressed()) {
            if (ship.getBulletFired() < ship.getBulletsCapacity()) {
                shoot(ship);
            } else {
                shootSoundCounter = 0;
                if (emptySoundCounter % 3 == 0) {
                    ship.getEmptySound().play();
                }
                emptySoundCounter++;
            }
        }
    }

    @Override
    public void updateBullets(Ship ship)  {
        for (Bullet bullet: ship.getBullets()){
            bullet.update();
        }
    }
}