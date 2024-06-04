package main.java;

public class LaserBulletStrategy implements ShootingStrategy {
    @Override
    public void shoot(Ship ship) {
        ship.increaseBulletFired(1);
        ship.bullets.get(ship.getBulletFired()-1).setShootFlag(true);
        ship.getLaserShootSound().play();
    }

    @Override
    public void handleShooting(Ship ship) {
        if (ship.keyHandler.getSpacePressed()) {
            if (ship.getBulletFired() < ship.getBulletsCapacity()) {
                shoot(ship);
            } else if (!(ship.getBulletFired() < ship.getBulletsCapacity())) {
                ship.getEmptySound().play();
            }
        }
    }

    @Override
    public void updateBullets(Ship ship)  {
        for (Bullet bullet: ship.bullets){
            bullet.update();
        }
    }
}