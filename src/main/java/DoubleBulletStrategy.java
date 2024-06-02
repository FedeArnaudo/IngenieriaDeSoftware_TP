package main.java;

public class DoubleBulletStrategy implements ShootingStrategy {
    private int shootCount = 0;

    @Override
    public void shoot(Ship ship) {
        if (ship.bulletFired < ship.bulletsCapacity - 1) {
            ship.bulletFired += 2;
            ship.bullets.get(ship.bulletFired-1).shootFlag = true;
        }
    }

    @Override
    public void handleShooting(Ship ship) {
        if (ship.keyHandler.spacePressed && ship.bulletFired < ship.bulletsCapacity - 1 && !ship.keyHandler.bulletFiredInCurrentKeyPress) {
            if (shootCount == 0 || shootCount == 2) {
                shoot(ship);
                if (shootCount==2){
                    ship.keyHandler.bulletFiredInCurrentKeyPress = true; // set the flag to true after a bullet is fired
                }
            }
            shootCount++;
            if (shootCount > 2) {
                shootCount = 0; // reset the counter
            }
        }
    }

    @Override
    public void updateBullets(Ship ship) {
        for (Bullet bullet: ship.bullets){
            bullet.update();
        }
    }
}