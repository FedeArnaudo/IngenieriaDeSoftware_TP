public class ShipPowerUpManager {
    private Ship ship;
    private int powerUpCounter;

    private static final int POWER_UP_DURATION = 30 * GamePanel.getFps();
    private static final int SUPER_POWER_UP_BULLET_CAPACITY = 1000;
    private static final int DEFAULT_BULLET_CAPACITY = 100;
    private static final int COLLISION_DEBOUNCE = 10;

    public ShipPowerUpManager() {
        this.powerUpCounter = 0;
    }

    public void attachToShip(Ship ship) {
        this.ship = ship;
    }

    // Initializes the ship with super power-up bullet capacity
    public void initializePowerUp() {
        ship.setSuperPowerUpBulletCapacity(SUPER_POWER_UP_BULLET_CAPACITY);
    }

    // Updates the ship's power-up status based on the current power-up type and counter
    public void updatePowerUpStatus() {
        if (ship.hasCollided() && ship.getCollisionDebounce() == COLLISION_DEBOUNCE) {
            handleCollision(ship.getCollidedWith());
        } else if (isPowerUpActive(ship.getCurrentPowerUp())) {
            powerUpCounter++;
            if (powerUpCounter >= POWER_UP_DURATION) {
                handlePowerUpExpiry();
            }
        }
    }

    // Handles collision with different power-ups
    private void handleCollision(Obstacle.ObstacleType collisionType) {
        switch (collisionType) {
            case BASIC_POWER_UP:
                ship.setLastPowerUp(ship.getCurrentPowerUp());
                if (ship.getLastPowerUp().equals(Ship.PowerUpType.NONE)) {
                    ship.setCurrentPowerUp(Ship.PowerUpType.BASIC);
                    powerUpCounter = 0;  // Reset counter when a new power-up is picked up
                }
                break;

            case SUPER_POWER_UP:
                ship.setLastPowerUp(ship.getCurrentPowerUp());
                if (!ship.getCurrentPowerUp().equals(Ship.PowerUpType.SUPER)) {
                    ship.setCurrentPowerUp(Ship.PowerUpType.SUPER);
                    powerUpCounter = 0;  // Reset counter when a new power-up is picked up
                }
                break;

            // Handle other collision types if necessary
        }
    }

    // Checks if the power-up is active
    private boolean isPowerUpActive(Ship.PowerUpType powerUpType) {
        return powerUpType == Ship.PowerUpType.BASIC || powerUpType == Ship.PowerUpType.SUPER;
    }

    // Handles the expiry of the current power-up
    private void handlePowerUpExpiry() {
        resetPowerUp();
        resetShootingStrategy();
    }

    // Resets the ship's values to default
    public void resetPowerUp() {
        ship.setLastPowerUp(ship.getCurrentPowerUp());
        ship.setCurrentPowerUp(Ship.PowerUpType.NONE);
        ship.setSpeed(ship.getDefaultSpeed());
        ship.setBulletFired(0);
        ship.setBulletsCapacity(DEFAULT_BULLET_CAPACITY);
    }

    // Resets the ship's shooting strategy to single bullet
    private void resetShootingStrategy() {
        ship.setShootingStrategy(new SingleBulletStrategy());
    }
}