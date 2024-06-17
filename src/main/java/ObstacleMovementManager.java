public class ObstacleMovementManager {
    private Obstacle obstacle;
    private int explosionAnimationCounter;

    private static final double METEOR_1_RESET_POSITION_FACTOR = 1.0;
    private static final double METEOR_2_RESET_POSITION_FACTOR = 10.0;
    private static final double BULLET_POWER_UP_RESET_POSITION_FACTOR = 20.0;
    private static final double BASIC_POWER_UP_RESET_POSITION_FACTOR = 35.0;
    private static final double SUPER_POWER_UP_RESET_POSITION_FACTOR = 75.0;

    public ObstacleMovementManager() {
        explosionAnimationCounter = 0;
    }

    public void attachToObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    public void handleMovement() {
        moveDown();
        checkPositionAndReset();
    }

    private void moveDown() {
        obstacle.increaseY(obstacle.getSpeed());
    }

    private void checkPositionAndReset() {
        double screenHeight = obstacle.getGamePanel().getScreenHeight();
        double resetPositionFactor = getResetPositionFactor(obstacle.getObstacleType());

        if (resetPositionFactor != -1 && (obstacle.getY() > screenHeight * resetPositionFactor || obstacle.hasCollided())) {
            handleCollisionOrReset(screenHeight * resetPositionFactor);
        }
    }

    private double getResetPositionFactor(Obstacle.ObstacleType obstacleType) {
        switch (obstacleType) {
            case METEOR_1:
                return METEOR_1_RESET_POSITION_FACTOR;
            case METEOR_2:
                return METEOR_2_RESET_POSITION_FACTOR;
            case BULLET_POWER_UP:
                return BULLET_POWER_UP_RESET_POSITION_FACTOR;
            case BASIC_POWER_UP:
                return BASIC_POWER_UP_RESET_POSITION_FACTOR;
            case SUPER_POWER_UP:
                return SUPER_POWER_UP_RESET_POSITION_FACTOR;
            default:
                return -1;
        }
    }

    private void handleCollisionOrReset(double resetPosition) {
        if (obstacle.hasCollided()) {
            explosionAnimationCounter++;
            if (explosionAnimationCounter > 10) {
                resetObstacle();
            }
        } else if (obstacle.getY() > resetPosition) {
            resetObstacle();
            obstacle.setCollision(false);
        }
    }

    private void resetObstacle() {
        obstacle.setDefaultValues();
        explosionAnimationCounter = 0;
    }
}
