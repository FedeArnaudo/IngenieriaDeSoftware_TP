public class ShipStatsManager {
    private Ship ship;
    private int aliveCounter;
    private final int initialLives;
    private final int defaultSpeed;
    private final int defaultBulletsCapacity;

    private static final int MAX_SCORE = 999999;
    private static final int SCORE_INCREMENT_INTERVAL = 30;
    private static final int COLLISION_DEBOUNCE = 10;

    public ShipStatsManager(int initialLives, int defaultSpeed, int defaultBulletsCapacity) {
        this.aliveCounter = 0;
        this.initialLives = initialLives;
        this.defaultSpeed = defaultSpeed;
        this.defaultBulletsCapacity = defaultBulletsCapacity;
    }

    public void attachToShip(Ship ship) {
        this.ship = ship;
    }

    public void initializeStats() {
        ship.setLives(initialLives);
        ship.setSpeed(defaultSpeed);
        ship.setDefaultSpeed(defaultSpeed);
        ship.setBulletsCapacity(defaultBulletsCapacity);
        ship.setScore(0);
        ship.setFinalScore(0);
    }

    public void updateStats() {
        if (ship.hasCollided() && ship.getCollisionDebounce() == COLLISION_DEBOUNCE) {
            handleCollision(ship.getCollidedWith());
        }

        incrementScoreIfStillAlive();
        checkScoreLimit();
    }

    private void handleCollision(Obstacle.ObstacleType collisionType) {
        switch (collisionType) {
            case METEOR_1:
                ship.decreaseLives(1);
                break;

            case METEOR_2:
                ship.decreaseLives(2);
                break;

            case BULLET_POWER_UP:
                ship.setBulletFired(0);
                break;

            case BASIC_POWER_UP:
                if (ship.getLastPowerUp().equals(Ship.PowerUpType.NONE)) {
                    handleBasicPowerUp();
                }
                break;

            case SUPER_POWER_UP:
                if (!ship.getLastPowerUp().equals(Ship.PowerUpType.SUPER)) {
                    handleSuperPowerUp();
                }
                break;
        }
    }

    private void handleBasicPowerUp() {
        ship.setBulletsCapacity(ship.getBulletsCapacity() * 2);
        ship.setBulletFired(0);
        ship.setShootingStrategy(new DoubleBulletStrategy());
    }

    private void handleSuperPowerUp() {
        ship.setLives(7);
        ship.setSpeed(ship.getDefaultSpeed() + 3);
        ship.setBulletsCapacity(ship.getSuperPowerUpBulletCapacity());
        ship.setBulletFired(0);
        ship.setShootingStrategy(new LaserBulletStrategy());
    }

    private void incrementScoreIfStillAlive() {
        if (aliveCounter == SCORE_INCREMENT_INTERVAL){
            ship.increaseScore(1);
            aliveCounter = 0;
        }
        else {
            aliveCounter++;
        }
    }

    private void checkScoreLimit() {
        if (ship.getScore() > MAX_SCORE || ship.getLives() == 0) {
            ship.setFinalScore(ship.getScore());
            ship.setScore(0);
        }
    }
}