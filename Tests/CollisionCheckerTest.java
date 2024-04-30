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
        Entity entity2 = new Meteor(gamePanel);

        boolean player = true;
        int type = 0;

        while (type == 0){
            type = collisionChecker.checkObjects(entity, player);
        }

        if(player){
            assertEquals(1, type);
        }
        else {
            assertEquals(2, type);
        }



    }
}
