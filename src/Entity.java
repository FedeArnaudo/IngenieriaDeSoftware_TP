import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int x, y;
    public int speed;

    public BufferedImage ship1, ship2, ship3, ship4, ship5;
    public BufferedImage meteor1, meteor2, meteor3;
    public String direction;
    public Rectangle solidRectangle;
    public int solidAreaDefaultX;
    public int solidAreaDefaultY;
    public boolean collisionOn = false;

}
