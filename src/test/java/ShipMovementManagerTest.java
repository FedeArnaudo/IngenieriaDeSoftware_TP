import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class ShipMovementManagerTest {
    private Ship ship;
    private KeyHandler keyHandler;
    private ShipMovementManager shipMovementManager;
    private final int screenWidth = 800;
    private final int screenHeight = 600;

    @BeforeEach
    public void setup() {
        ship = mock(Ship.class);
        keyHandler = mock(KeyHandler.class);
        GamePanel gamePanel = mock(GamePanel.class);
        when(gamePanel.getScreenWidth()).thenReturn(screenWidth);
        when(gamePanel.getScreenHeight()).thenReturn(screenHeight);
        when(gamePanel.getTileSize()).thenReturn(57);
        when(ship.getKeyHandler()).thenReturn(keyHandler);
        when(ship.getGamePanel()).thenReturn(gamePanel);
        when(ship.getSpeed()).thenReturn(5);
        shipMovementManager = new ShipMovementManager();
        shipMovementManager.attachToShip(ship);
    }

    @Test
    public void handleMovementWhenLeftPressedMovesLeftIfCanMove() {
        when(keyHandler.isLeftPressed()).thenReturn(true);
        when(ship.getX()).thenReturn(10);
        shipMovementManager.handleMovement();
        verify(ship).setDirection(Entity.Direction.LEFT);
    }

    @Test
    public void handleMovementWhenLeftPressedDoesNotMoveLeftIfCantMove() {
        when(keyHandler.isLeftPressed()).thenReturn(true);
        when(ship.getX()).thenReturn(0);
        shipMovementManager.handleMovement();
        verify(ship, never()).setDirection(Entity.Direction.LEFT);
    }

    @Test
    public void handleMovementWhenRightPressedMovesRightIfCanMove() {
        when(keyHandler.isRightPressed()).thenReturn(true);
        when(ship.getX()).thenReturn(0);
        shipMovementManager.handleMovement();
        verify(ship).setDirection(Entity.Direction.RIGHT);
    }

    @Test
    public void handleMovementWhenRightPressedDoesNotMoveRightIfCantMove() {
        when(keyHandler.isRightPressed()).thenReturn(true);
        when(ship.getX()).thenReturn(screenWidth);
        shipMovementManager.handleMovement();
        verify(ship,never()).setDirection(Entity.Direction.RIGHT);
    }

    @Test
    public void handleMovementWhenUpPressedMovesUpIfCanMove() {
        when(keyHandler.isUpPressed()).thenReturn(true);
        when(ship.getY()).thenReturn(10);
        shipMovementManager.handleMovement();
        verify(ship).setDirection(Entity.Direction.UP);
    }

    @Test
    public void handleMovementWhenUpPressedDoesNotMoveUpIfCantMove() {
        when(keyHandler.isUpPressed()).thenReturn(true);
        when(ship.getY()).thenReturn(0);
        shipMovementManager.handleMovement();
        verify(ship,never()).setDirection(Entity.Direction.UP);
    }

    @Test
    public void handleMovementWhenDownPressedMovesDownIfCanMove() {
        when(keyHandler.isDownPressed()).thenReturn(true);
        when(ship.getY()).thenReturn(0);
        shipMovementManager.handleMovement();
        verify(ship).setDirection(Entity.Direction.DOWN);
    }

    @Test
    public void handleMovementWhenDownPressedDoesNotMoveDownIfCantMove() {
        when(keyHandler.isDownPressed()).thenReturn(true);
        when(ship.getY()).thenReturn(screenHeight);
        shipMovementManager.handleMovement();
        verify(ship, never()).setDirection(Entity.Direction.DOWN);
    }

    @Test
    public void handleMovementWhenNoKeyIsPressedDoesNotMove() {
        shipMovementManager.handleMovement();
        verify(ship, never()).setDirection(any());
    }
}