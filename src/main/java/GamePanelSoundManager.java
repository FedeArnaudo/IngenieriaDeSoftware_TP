import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class GamePanelSoundManager {
    private GamePanel gamePanel;
    Clip backgroundMusic;
    Clip pauseMusic;

    public void attachToGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void loadGamePanelSounds() {
        gamePanel.setWelcomeScreenVoiceSound(new Sound("sounds/welcome_to_spaceships.wav"));
        gamePanel.setStartGameSound(new Sound("sounds/start_game.wav"));
        gamePanel.setStartGameVoiceSound(new Sound("sounds/start_game_voice.wav"));
        gamePanel.setGameOverSound(new Sound("sounds/game_over.wav"));
        gamePanel.setGameOverVoiceSound(new Sound("sounds/game_over_voice.wav"));
    }

    public void startBackgroundMusic() {
        backgroundMusic = playMusic("music/welcome_background.wav");
        gamePanel.getWelcomeScreenVoiceSound().play();
        pauseMusic = null;
    }

    public void updateMusic() {
        GamePanel.GameState currentState = gamePanel.getLastState();

        switch (currentState) {
            case WELCOME:
                handleWelcomeStateSounds();
                break;
            case PLAYING:
                handlePlayingStateSounds();
                break;
            case PAUSE:
                handlePauseStateSounds();
                break;
            case GAME_OVER:
                handleGameOverStateSounds();
                break;
        }
    }

    private void handleWelcomeStateSounds() {
        if (gamePanel.isStartTransitionNotCompleted()) {
            playTransitionSounds(GamePanel.GameState.WELCOME,"music/game_background.wav");
            gamePanel.setStartTransitionCompleted(true);
        }
    }

    private void handlePlayingStateSounds() {
        if (gamePanel.getKeyHandler().isPPressed() && !gamePanel.getCurrentState().equals(GamePanel.GameState.GAME_OVER)) {
            pauseMusic = playNewMusic("music/pause_background.wav");
            gamePanel.getKeyHandler().setPPressed(false);
        } else if (gamePanel.getShip().getLives() <= 0 && gamePanel.isLoseTransitionNotCompleted()) {
            playTransitionSounds(GamePanel.GameState.PLAYING, "music/game_over_background.wav");
            gamePanel.setLoseTransitionCompleted(true);
        }
    }

    private void handlePauseStateSounds() {
        if (gamePanel.getKeyHandler().isPPressed()) {
            resumePlayingMusic();
            gamePanel.getKeyHandler().setPPressed(false);
        }
    }

    private void handleGameOverStateSounds() {
        if (gamePanel.isRestartTransitionNotCompleted()) {
            playTransitionSounds(GamePanel.GameState.GAME_OVER, "music/game_background.wav");
            gamePanel.setRestartTransitionCompleted(true);
        }
    }

    private void playTransitionSounds(GamePanel.GameState transitionFrom, String newMusicPath) {
        switch (transitionFrom) {
            case WELCOME:

            case GAME_OVER:
                gamePanel.getStartGameSound().play();
                gamePanel.getStartGameVoiceSound().play();
                break;

            case PLAYING:
                gamePanel.getGameOverSound().play();
                gamePanel.getGameOverVoiceSound().play();
                break;
        }

        backgroundMusic = playNewMusic(newMusicPath);
    }

    public Clip playMusic(String filePath) {
        try {
            URL url = getClass().getClassLoader().getResource(filePath);
            if (url == null) {
                throw new RuntimeException("Resource not found: " + filePath);
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("Could not play music", e);
        }
    }

    public Clip playNewMusic(String newMusicPath) {
        backgroundMusic.stop();

        if (!gamePanel.getCurrentState().equals(GamePanel.GameState.PAUSE)) {
            backgroundMusic.close();
        }

        return playMusic(newMusicPath);
    }

    public void resumePlayingMusic() {
        if (pauseMusic != null) {
            pauseMusic.stop();
            backgroundMusic.start();
        }
    }

}
