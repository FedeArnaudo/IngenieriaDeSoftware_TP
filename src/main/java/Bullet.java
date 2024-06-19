import java.awt.*;
import java.util.ArrayList;

public class Bullet extends Entity{
    private final GamePanel gamePanel;
    private final Ship ship;

    private final BulletShootingManager bulletShootingManager;
    private final BulletCollisionManager bulletCollisionManager;
    private final BulletImageManager bulletImageManager;

    private boolean shoot;

    public Bullet(GamePanel gamePanel, Ship ship, int speed, BulletShootingManager bulletShootingManager, BulletCollisionManager bulletCollisionManager, BulletImageManager bulletImageManager){

        this.gamePanel = gamePanel;
        this.ship = ship;
        this.speed = speed;

        this.bulletShootingManager = bulletShootingManager;
        this.bulletCollisionManager = bulletCollisionManager;
        this.bulletImageManager = bulletImageManager;

        addManagersToBullet();
        bulletImageManager.initializeImages();
        bulletCollisionManager.initializeBullet();
        setDefaultValues();
    }

    private void addManagersToBullet(){
        bulletShootingManager.addToBullet(this);
        bulletCollisionManager.addToBullet(this);
        bulletImageManager.addToBullet(this);
    }

    private void setDefaultValues(){
        x = ship.getX();
        y = ship.getY();
        direction = Direction.UP;
        shoot = false;
    }

    @Override
    public void update(){
        bulletShootingManager.handleShooting();
        bulletCollisionManager.handleCollision();
        bulletShootingManager.resetPosition();
    }


    public void collide(Obstacle.ObstacleType collideWithType, ArrayList<Obstacle> obstacles, int collideWithIndex){
        bulletCollisionManager.collide(collideWithType, obstacles, collideWithIndex);
    }

    @Override
    public void draw(Graphics2D graphics2D){
        bulletImageManager.drawBullet(graphics2D);
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
    public int getY() { return y; }

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

    public Ship getShip() {
        return ship;
    }

    public boolean isShoot() {
        return shoot;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    @Override
    public void setSolidRectangle(Rectangle rectangle) {
        this.solidRectangle = rectangle;
    }

    @Override
    public void setSolidAreaDefaultX(int solidAreaDefaultX) {
        this.solidAreaDefaultX = solidAreaDefaultX;
    }

    @Override
    public void setSolidAreaDefaultY(int solidAreaDefaultY) {
        this.solidAreaDefaultY = solidAreaDefaultY;
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    public void decreaseY(int decrement) { y -= decrement; }
}
