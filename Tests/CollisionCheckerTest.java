import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

public class CollisionCheckerTest {
    JFrame window;
    GamePanel gamePanel;
    @BeforeEach
    public void iniciar(){
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("My Game SpaceShips");

        gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }

    @Test
    public void collisionProve(){
        CollisionChecker collisionChecker = new CollisionChecker(gamePanel);
        Entity entity = gamePanel.player;

        boolean collision = false;

        while (!collision){
            collision = collisionChecker.checkObjects(entity);
        }

        assertTrue(collision);

    }

    @Test
    public void meteorNullCollision(){
        CollisionChecker collisionChecker = new CollisionChecker(gamePanel);
        Entity entity = gamePanel.player;
        Meteor meteor = null;
        gamePanel.meteors.add(meteor);

        boolean collision = collisionChecker.checkObjects(entity);

        assertFalse(collision);
    }
}
