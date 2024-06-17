import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("SpaceShips");

        GamePanelImageManager imageManager = new GamePanelImageManager();
        GamePanelSoundManager soundManager = new GamePanelSoundManager();
        GamePanelDrawManager drawManager = new GamePanelDrawManager();
        GamePanelStateManager stateManager = new GamePanelStateManager();
        GamePanel gamePanel = new GamePanel(imageManager, soundManager, drawManager, stateManager);
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}