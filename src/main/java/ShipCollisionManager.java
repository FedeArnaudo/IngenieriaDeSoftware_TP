import java.awt.*;
import java.util.ArrayList;

public class ShipCollisionManager {
    private Ship ship;
    private static final Rectangle DEFAULT_SOLID_RECTANGLE = new Rectangle(3,5,52,40);

    public void attachToShip(Ship ship) {
        this.ship = ship;
    }

    public void initializeCollision() {
        ship.setCollision(false);
        ship.setCollisionDebounce(0);

        ship.setSolidRectangle(DEFAULT_SOLID_RECTANGLE);
        ship.setSolidAreaDefaultX(ship.getSolidRectangle().x);
        ship.setSolidAreaDefaultY(ship.getSolidRectangle().y);
    }

    public void handleCollision() {
        if (!ship.hasCollided()) {
            ship.getGamePanel().getCollisionChecker().detectObject(ship);
        }

        if (ship.hasCollided() && ship.getCollisionDebounce() <= 10){
            ship.increaseCollisionDebounce();
        }
        else if (ship.hasCollided()){
            ship.setCollision(false);
            ship.setCollisionDebounce(0);
        }
    }

    public void collide(Obstacle.ObstacleType collideWithType, ArrayList<Obstacle> obstacles, int collideWithIndex){
        ship.setCollidedWith(collideWithType);
        ship.setCollision(true);
        obstacles.get(collideWithIndex).setCollision(true);
    }
}
