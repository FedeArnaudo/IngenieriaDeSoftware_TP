public class ShipMovementManager {
    private Ship ship;
    private KeyHandler keyHandler;
    private int screenWidth;
    private int screenHeight;
    private int tileSize;

    public void attachToShip(Ship ship) {
        this.ship = ship;
        this.keyHandler = ship.getKeyHandler();
        GamePanel gamePanel = ship.getGamePanel();
        this.screenWidth = gamePanel.getScreenWidth();
        this.screenHeight = gamePanel.getScreenHeight();
        this.tileSize = gamePanel.getTileSize();
    }

    public void handleMovement() {
        // Handle movement based on key press and direction
        handleDirectionalMovement(Entity.Direction.LEFT, keyHandler.isLeftPressed());
        handleDirectionalMovement(Entity.Direction.RIGHT, keyHandler.isRightPressed());
        handleDirectionalMovement(Entity.Direction.UP, keyHandler.isUpPressed());
        handleDirectionalMovement(Entity.Direction.DOWN, keyHandler.isDownPressed());
    }

    private void handleDirectionalMovement(Entity.Direction direction, boolean isPressed) {
        if (isPressed && canMove(direction)) {
            move(direction);
        }
    }

    private boolean canMove(Entity.Direction direction) {
        int x = ship.getX();
        int y = ship.getY();
        int speed = ship.getSpeed();

        switch (direction) {
            case LEFT:
                return x - speed >= 0;
            case RIGHT:
                return x + speed < screenWidth - tileSize;
            case UP:
                return y - speed >= 0;
            case DOWN:
                return y + speed < screenHeight - tileSize;
            default:
                return false;
        }
    }

    private void move(Entity.Direction direction) {
        ship.setDirection(direction);
        int speed = ship.getSpeed();

        switch (direction) {
            case LEFT:
                ship.decreaseX(speed);
                break;
            case RIGHT:
                ship.increaseX(speed);
                break;
            case UP:
                ship.decreaseY(speed);
                break;
            case DOWN:
                ship.increaseY(speed);
                break;
        }
    }
}