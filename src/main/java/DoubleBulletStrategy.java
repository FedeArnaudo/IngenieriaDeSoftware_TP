package main.java;

public class DoubleBulletStrategy implements ShootingStrategy {
    private int shootCount = 0;

    @Override
    public void shoot(Ship ship) {
        if (ship.getBulletFired() < ship.getBulletsCapacity() - 1) {
            ship.increaseBulletFired(1);
            ship.bullets.get(ship.getBulletFired()-1).setShootFlag(true);
        }
    }

    @Override
    public void handleShooting(Ship ship) {
        if (ship.keyHandler.getSpacePressed() && ship.getBulletFired() < ship.getBulletsCapacity() - 1 && !ship.keyHandler.getBulletFiredInCurrentKeyPress()) {
            if (shootCount == 0 || shootCount == 2) {
                shoot(ship);
                if (shootCount==2){
                    ship.keyHandler.setBulletFiredInCurrentKeyPress(true); // set the flag to true after a bullet is fired
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