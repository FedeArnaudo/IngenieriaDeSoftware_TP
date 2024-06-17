import java.util.ArrayList;

public class CollisionChecker {
    GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void detectObject(Entity entity){
        checkCollision(entity, gamePanel.getMeteors1());
        checkCollision(entity, gamePanel.getMeteors2());
        checkCollision(entity, gamePanel.getBulletPowerUps());
        checkCollision(entity, gamePanel.getBasicPowerUps());
        checkCollision(entity, gamePanel.getSuperPowerUps());
    }

    private void checkCollision(Entity entity, ArrayList<Obstacle> obstacles){
        for(Obstacle obstacle : obstacles){
            if(checkIntersect(entity, obstacle)){
                if (entity instanceof Bullet){
                    ((Bullet) entity).collide(obstacle.getObstacleType(), obstacles, obstacles.indexOf(obstacle));
                } else if (entity instanceof Ship) {
                    ((Ship) entity).collide(obstacle.getObstacleType(), obstacles, obstacles.indexOf(obstacle));
                }
            }
        }
    }

    private boolean checkIntersect(Entity entity1, Entity entity2) {
        if (entity2 == null) return false;

        // Store original positions
        int originalX1 = entity1.getSolidRectangle().x;
        int originalY1 = entity1.getSolidRectangle().y;
        int originalX2 = entity2.getSolidRectangle().x;
        int originalY2 = entity2.getSolidRectangle().y;

        // Update rectangle positions
        entity1.getSolidRectangle().setLocation(entity1.getX() + originalX1, entity1.getY() + originalY1);
        entity2.getSolidRectangle().setLocation(entity2.getX() + originalX2, entity2.getY() + originalY2);

        // Check for intersection based on direction
        boolean crashed = false;
        switch (entity1.getDirection()) {
            case UP:
                entity1.getSolidRectangle().y -= entity1.getSpeed();
                break;
            case DOWN:
                entity1.getSolidRectangle().y += entity1.getSpeed();
                break;
            case LEFT:
                entity1.getSolidRectangle().x -= entity1.getSpeed();
                break;
            case RIGHT:
                entity1.getSolidRectangle().x += entity1.getSpeed();
                break;
        }

        if (entity1.getSolidRectangle().intersects(entity2.getSolidRectangle())) {
            crashed = true;
        }

        // Reset to original positions
        entity1.getSolidRectangle().setLocation(originalX1, originalY1);
        entity2.getSolidRectangle().setLocation(originalX2, originalY2);

        return crashed;
    }
}
