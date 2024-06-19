import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("My Game SpaceShips");

        GamePanelImageManager imageManager = GamePanelImageManager.getInstanceGamePanelImageManager();
        GamePanelSoundManager soundManager = GamePanelSoundManager.getInstanceGamePanelSoundManager();
        GamePanelDrawManager drawManager = GamePanelDrawManager.getInstanceGamePanelDrawManager();
        GamePanelStateManager stateManager = GamePanelStateManager.getInstanceGamePanelStateManager();

        GamePanel gamePanel = new GamePanel(imageManager, soundManager, drawManager, stateManager);
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}