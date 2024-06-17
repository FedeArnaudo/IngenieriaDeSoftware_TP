import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import java.awt.*;

public class TileManagerTest {
    private Graphics2D graphics2D;
    private TileManager tileManager;

    @BeforeEach
    public void setup() {
        GamePanel gamePanel = mock(GamePanel.class);
        graphics2D = mock(Graphics2D.class);

        when(gamePanel.getScreenWidth()).thenReturn(800);
        tileManager = new TileManager(gamePanel);
    }

    @Test
    public void drawsTilesCorrectly() throws InterruptedException {
        tileManager.draw(graphics2D);
        verify(graphics2D, times(66)).drawImage(any(), eq(0), anyInt(), eq(800), anyInt(), isNull());
    }

    @Test
    public void reordersTilesAfterFourDraws() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            tileManager.draw(graphics2D);
        }
        verify(graphics2D, times(330)).drawImage(any(), eq(0), anyInt(), eq(800), anyInt(), isNull());
    }
}