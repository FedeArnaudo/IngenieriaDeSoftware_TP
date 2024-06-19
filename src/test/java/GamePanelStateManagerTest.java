import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class GamePanelStateManagerTest {
    private GamePanelStateManager gamePanelStateManager;
    private GamePanel gamePanel;
    private KeyHandler keyHandler;
    private Ship ship;

    @BeforeEach
    public void setup() {
        gamePanel = mock(GamePanel.class);
        keyHandler = mock(KeyHandler.class);
        ship = mock(Ship.class);
        when(gamePanel.getKeyHandler()).thenReturn(keyHandler);
        when(gamePanel.getShip()).thenReturn(ship);
        gamePanelStateManager = GamePanelStateManager.getInstanceGamePanelStateManager();
        gamePanelStateManager.attachToGamePanel(gamePanel);
    }

    @Test
    public void initializeStateSetsCorrectState() {
        gamePanelStateManager.initializeState();
        verify(gamePanel).setLastState(GamePanel.GameState.NONE);
        verify(gamePanel).setCurrentState(GamePanel.GameState.WELCOME);
        verify(gamePanel).setStartTransitionCompleted(false);
        verify(gamePanel).setLoseTransitionCompleted(false);
        verify(gamePanel).setRestartTransitionCompleted(false);
    }

    @Test
    public void updateStateHandlesWelcomeStateWhenEnterPressed() {
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.WELCOME);
        when(keyHandler.isEnterPressed()).thenReturn(true);
        gamePanelStateManager.updateState();
        verify(gamePanel).setCurrentState(GamePanel.GameState.PLAYING);
    }

    @Test
    public void updateStateDoesNotHandlesWelcomeStateWhenEnterNotPressed() {
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.WELCOME);
        when(keyHandler.isEnterPressed()).thenReturn(false);
        gamePanelStateManager.updateState();
        verify(gamePanel, never()).setCurrentState(GamePanel.GameState.PLAYING);
    }

    @Test
    public void updateStateHandlesPlayingStateWithPause() {
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.PLAYING);
        when(keyHandler.isPPressed()).thenReturn(true);
        gamePanelStateManager.updateState();
        verify(gamePanel).setCurrentState(GamePanel.GameState.PAUSE);
    }

    @Test
    public void updateStateHandlesPlayingStateWithGameOver() {
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.PLAYING);
        when(ship.getLives()).thenReturn(0);
        gamePanelStateManager.updateState();
        verify(gamePanel).setCurrentState(GamePanel.GameState.GAME_OVER);
    }

    @Test
    public void updateStateHandlesPauseStateWhenPPressed() {
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.PAUSE);
        when(keyHandler.isPPressed()).thenReturn(true);
        gamePanelStateManager.updateState();
        verify(gamePanel).setCurrentState(GamePanel.GameState.PLAYING);
    }

    @Test
    public void updateStateDoesNotHandlesPauseStateWhenPNotPressed() {
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.PAUSE);
        when(keyHandler.isPPressed()).thenReturn(false);
        gamePanelStateManager.updateState();
        verify(gamePanel, never()).setCurrentState(GamePanel.GameState.PLAYING);
    }

    @Test
    public void updateStateHandlesGameOverStateWhenEnterPressed() {
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.GAME_OVER);
        when(keyHandler.isEnterPressed()).thenReturn(true);
        gamePanelStateManager.updateState();
        verify(gamePanel).setCurrentState(GamePanel.GameState.PLAYING);
    }

    @Test
    public void updateStateDoesNotHandlesGameOverStateWhenEnterNotPressed() {
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.GAME_OVER);
        when(keyHandler.isEnterPressed()).thenReturn(false);
        gamePanelStateManager.updateState();
        verify(gamePanel, never()).setCurrentState(GamePanel.GameState.PLAYING);
    }

    @Test
    public void updateStateHandlesPlayingStateWithNoTransitions() {
        when(gamePanel.getCurrentState()).thenReturn(GamePanel.GameState.PLAYING);
        when(keyHandler.isPPressed()).thenReturn(false);
        when(ship.getLives()).thenReturn(1);
        gamePanelStateManager.updateState();
        verify(gamePanel).getMeteors1();
    }
}