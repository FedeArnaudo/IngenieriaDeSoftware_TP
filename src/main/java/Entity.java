import java.awt.*;

public abstract class Entity {
    protected GamePanel gamePanel;
    protected int x;
    protected int y;
    protected int speed;
    protected Direction direction;
    protected Rectangle solidRectangle;
    protected int solidAreaDefaultX;
    protected int solidAreaDefaultY;
    protected boolean collision = false;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public abstract void update();
    public abstract void draw(Graphics2D graphics2D);

    public abstract GamePanel getGamePanel();
    public abstract int getX();
    public abstract int getY();
    public abstract int getSpeed();
    public abstract Direction getDirection();
    public abstract Rectangle getSolidRectangle();
    public abstract boolean hasCollided();

    public abstract void setX(int x);
    public abstract void setY(int y);
    public abstract void setDirection(Direction direction);
    public abstract void setSpeed(int speed);
    public abstract void setCollision(boolean collision);
    public abstract void setSolidRectangle(Rectangle rectangle);
    public abstract void setSolidAreaDefaultX(int solidAreaDefaultX);
    public abstract void setSolidAreaDefaultY(int solidAreaDefaultY);
}