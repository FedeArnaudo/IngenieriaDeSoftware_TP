import java.awt.*;
import java.security.SecureRandom;
import java.util.EnumMap;
import java.util.Map;

public class Obstacle extends Entity{
    private final GamePanel gamePanel;

    public enum ObstacleType {
        METEOR_1,
        METEOR_2,
        BULLET_POWER_UP,
        BASIC_POWER_UP,
        SUPER_POWER_UP
    }

    private final ObstacleImageManager obstacleImageManager;
    private final ObstacleMovementManager obstacleMovementManager;

    private final ObstacleType obstacleType;
    private final SecureRandom random ;

    // Map to store initial Y positions for different obstacle types
    private static final Map<ObstacleType, Integer> initialYMap = new EnumMap<>(ObstacleType.class);

    static {
        initialYMap.put(ObstacleType.METEOR_1, -15);
        initialYMap.put(ObstacleType.METEOR_2, -20);
        initialYMap.put(ObstacleType.BULLET_POWER_UP, -30);
        initialYMap.put(ObstacleType.BASIC_POWER_UP, -50);
        initialYMap.put(ObstacleType.SUPER_POWER_UP, -100);
    }


    public Obstacle(GamePanel gamePanel, ObstacleType obstacleType, int speed, ObstacleImageManager obstacleImageManager, ObstacleMovementManager obstacleMovementManager) {
        this.gamePanel = gamePanel;
        this.obstacleType = obstacleType;
        this.speed = speed;

        this.obstacleImageManager = obstacleImageManager;
        this.obstacleMovementManager = obstacleMovementManager;

        random = new SecureRandom();
        collision = false;
        solidRectangle = new Rectangle(18, 36, 20, 20);
        solidAreaDefaultX = solidRectangle.x;
        solidAreaDefaultY = solidRectangle.y;

        addManagers();
        setDefaultValues();
    }

    private  void addManagers() {
        obstacleImageManager.attachToObstacle(this);
        obstacleMovementManager.attachToObstacle(this);
    }

    public void setDefaultValues() {
        x = random.nextInt(GamePanel.getMaxScreenCol()) * gamePanel.getTileSize();
        y = initialYMap.get(obstacleType) * gamePanel.getTileSize();

        collision = false;
        direction = Direction.DOWN;
    }

    @Override
    public void update(){
       obstacleMovementManager.handleMovement();
    }

    @Override
    public void draw(Graphics2D graphics2D){
        obstacleImageManager.draw(graphics2D);
    }

    @Override
    public GamePanel getGamePanel() {
        return gamePanel;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public Rectangle getSolidRectangle() {
        return solidRectangle;
    }

    @Override
    public boolean hasCollided() {
        return collision;
    }

    public ObstacleType getObstacleType() { return obstacleType; }

    @Override
    public void setX(int x) { this.x = x; }

    @Override
    public void setY(int y) { this.y = y; }

    @Override
    public void setDirection(Direction direction) { this.direction = direction; }

    @Override
    public void setSpeed(int speed) { this.speed = speed; }

    @Override
    public void setCollision(boolean collision) { this.collision = collision; }

    @Override
    public void setSolidRectangle(Rectangle rectangle) { this.solidRectangle = rectangle; }

    @Override
    public void setSolidAreaDefaultX(int solidAreaDefaultX) { this.solidAreaDefaultX = solidAreaDefaultX; }

    @Override
    public void setSolidAreaDefaultY(int solidAreaDefaultY) { this.solidAreaDefaultY = solidAreaDefaultY; }

    public void increaseY(int increase){ this.y += increase; }
}
