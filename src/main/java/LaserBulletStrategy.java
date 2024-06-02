package main.java;

public class LaserBulletStrategy implements ShootingStrategy {
    @Override
    public void shoot(Ship ship) {
        if (ship.bulletFired < ship.bulletsCapacity) {
            ship.bulletFired++;
            ship.bullets.get(ship.bulletFired-1).shootFlag = true;
        }
    }

    @Override
    public void handleShooting(Ship ship) {
        if (ship.keyHandler.spacePressed && ship.bulletFired < ship.bulletsCapacity) {
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