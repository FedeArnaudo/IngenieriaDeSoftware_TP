package main.java;

public class LaserBulletStrategy implements ShootingStrategy {
    @Override
    public void shoot(Ship ship) {
        if (ship.getBulletFired() < ship.getBulletsCapacity()) {
            ship.increaseBulletFired(1);
            ship.bullets.get(ship.getBulletFired()-1).setShootFlag(true);
            ship.getShootSound().play();
        }
    }

    @Override
    public void handleShooting(Ship ship) {
        if (ship.keyHandler.getSpacePressed() && ship.getBulletFired() < ship.getBulletsCapacity()) {
            shoot(ship);
        }
    }

    @Override
    public void updateBullets(Ship ship)  {
        for (Bullet bullet: ship.bullets){
            bullet.update();
        }
    }
}