public class ShipBulletManager {
    private Ship ship;
    private final int bulletSpeed;
    private final int cooldownTime;

    public ShipBulletManager(int bulletSpeed, int cooldownTime) {
        this.bulletSpeed = bulletSpeed;
        this.cooldownTime = cooldownTime;
    }

    public void attachToShip(Ship ship) {
        this.ship = ship;
    }

    public void initializeCooldown() {
        ship.setCooldownCounter(cooldownTime * GamePanel.getFps());
    }

    public void initializeBullets() {
        for(int i = 0; i < ship.getSuperPowerUpBulletCapacity(); i++){
            BulletShootingManager bulletShootingManager = new BulletShootingManager();
            BulletCollisionManager bulletCollisionManager = new BulletCollisionManager();
            BulletImageManager bulletImageManager = new BulletImageManager();

            Bullet bullet = new Bullet(ship.getGamePanel(), ship, bulletSpeed, bulletShootingManager, bulletCollisionManager, bulletImageManager);

            ship.getBullets().add(bullet);
        }
        ship.setBulletFired(0);
    }

    public void updateCooldown() {
        if(ship.getBulletFired() == ship.getBulletsCapacity()){
            ship.decreaseCooldownCounter();
        }

        if(ship.getCooldownCounter() == 0){
            ship.setBulletFired(50);

            ship.setCooldownCounter(cooldownTime * GamePanel.getFps());
        }
    }
}