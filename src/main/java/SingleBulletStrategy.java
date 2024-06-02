package main.java;

public class SingleBulletStrategy implements ShootingStrategy {
    @Override
    public void shoot(Ship ship) {
        if (ship.bulletFired < ship.bulletsCapacity) {
            ship.bulletFired++;
            ship.bullets.get(ship.bulletFired-1).shootFlag = true;
        }
    }

    @Override
    public void handleShooting(Ship ship) {
        if (ship.keyHandler.getSpacePressed() && ship.bulletFired < ship.bulletsCapacity && !ship.keyHandler.getBulletFiredInCurrentKeyPress()) {
            shoot(ship);
            ship.keyHandler.setBulletFiredInCurrentKeyPress(true); // set the flag to true after a bullet is fired
        }
    }

    @Override
    public void updateBullets(Ship ship) {
        for (Bullet bullet: ship.bullets){
            bullet.update();
        }
    }
}