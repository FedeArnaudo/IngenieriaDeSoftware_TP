import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Clip;

import static org.mockito.Mockito.*;

public class GamePanelSoundManagerTest {
    private GamePanel gamePanel;
    private GamePanelSoundManager gamePanelSoundManager;
    private KeyHandler keyHandler;
    private Sound sound;

    @BeforeEach
    public void setup() {
        gamePanel = mock(GamePanel.class);
        keyHandler = mock(KeyHandler.class);
        sound = mock(Sound.class);
        gamePanelSoundManager = new GamePanelSoundManager();
        gamePanelSoundManager.attachToGamePanel(gamePanel);
    }

    @Test
    public void loadGamePanelSounds_setsSoundsOnGamePanel() {
        gamePanelSoundManager.loadGamePanelSounds();

        verify(gamePanel).setWelcomeScreenVoiceSound(any(Sound.class));
        verify(gamePanel).setStartGameSound(any(Sound.class));
        verify(gamePanel).setStartGameVoiceSound(any(Sound.class));
        verify(gamePanel).setGameOverSound(any(Sound.class));
        verify(gamePanel).setGameOverVoiceSound(any(Sound.class));
    }

    @Test
    public void startBackgroundMusic_startsBackgroundMusicAndPlaysWelcomeVoiceSound() {
        when(gamePanel.getWelcomeScreenVoiceSound()).thenReturn(sound);

        gamePanelSoundManager.startBackgroundMusic();

        verify(sound).play();
    }

    @Test
    public void updateMusic_handlesWelcomeStateSoundsWhenStateIsWelcome() {
        when(gamePanel.getLastState()).thenReturn(GamePanel.GameState.WELCOME);

        gamePanelSoundManager.updateMusic();

        verify(gamePanel).isStartTransitionNotCompleted();
    }

    @Test
    public void updateMusic_handlesPauseStateSoundsWhenStateIsPause() {
        when(gamePanel.getLastState()).thenReturn(GamePanel.GameState.PAUSE);
        when(gamePanel.getKeyHandler()).thenReturn(keyHandler);

        gamePanelSoundManager.updateMusic();

        verify(keyHandler).isPPressed();
    }

    @Test
    public void updateMusic_handlesGameOverStateSoundsWhenStateIsGameOver() {
        when(gamePanel.getLastState()).thenReturn(GamePanel.GameState.GAME_OVER);

        gamePanelSoundManager.updateMusic();

        verify(gamePanel).isRestartTransitionNotCompleted();
    }


    @Test
    public void resumePlayingMusic_resumesBackgroundMusicWhenPauseMusicExists() {
        gamePanelSoundManager.pauseMusic = mock(Clip.class);
        gamePanelSoundManager.backgroundMusic = mock(Clip.class);

        gamePanelSoundManager.resumePlayingMusic();

        verify(gamePanelSoundManager.pauseMusic).stop();
        verify(gamePanelSoundManager.backgroundMusic).start();
    }
}