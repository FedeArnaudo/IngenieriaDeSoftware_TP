package main.java;

import java.awt.*;

public abstract class Entity {
    protected int x;
    protected int y;
    protected int speed;
    protected String direction;
    protected Rectangle solidRectangle;
    protected int solidAreaDefaultX;
    protected int solidAreaDefaultY;
    protected boolean collisionOn = false;

    public abstract int getX();
    public abstract int getY();
    public abstract int getSpeed();
    public abstract void update();
    public abstract void draw(Graphics2D graphics2D);
}