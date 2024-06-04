package main.java;

public class SingleBulletStrategy implements ShootingStrategy {
    @Override
    public void shoot(Ship ship) {
        ship.increaseBulletFired(1);
        ship.bullets.get(ship.getBulletFired()-1).setShootFlag(true);
        ship.getSingleShootSound().play();
    }

    @Override
    public void handleShooting(Ship ship) {
        if (ship.keyHandler.getSpacePressed()) {
            if (ship.getBulletFired() < ship.getBulletsCapacity() && ship.keyHandler.isBulletNotFiredInCurrentKeyPress()) {
                shoot(ship);
                ship.keyHandler.setBulletFiredInCurrentKeyPress(true); // set the flag to true after a bullet is fired
            } else if (!(ship.getBulletFired() < ship.getBulletsCapacity()) && ship.keyHandler.isBulletNotFiredInCurrentKeyPress()){
                ship.getEmptySound().play();
                ship.keyHandler.setBulletFiredInCurrentKeyPress(true);
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