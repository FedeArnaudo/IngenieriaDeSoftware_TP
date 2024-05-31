package test.java;

import main.java.GamePanel;
import main.java.ScoreboardNumber;
import main.java.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScoreboardNumberTest {
    private GamePanel gamePanel;
    private Ship ship;
    private ScoreboardNumber scoreboardNumber;

    @BeforeEach
    public void setUp() {
        gamePanel = mock(GamePanel.class);
        ship = mock(Ship.class);
        scoreboardNumber = new ScoreboardNumber(gamePanel, ship, 1);
    }

    @Test
    public void positionIsSetCorrectly() {
        int expectedX = gamePanel.getScreenWidth() - gamePanel.getTileSize() - (gamePanel.getTileSize() * scoreboardNumber.getPlace());
        assertEquals(expectedX, scoreboardNumber.getX());
        assertEquals(0, scoreboardNumber.getY());
    }

    @Test
    public void scoreIsExtractedCorrectly() {
        when(ship.getScore()).thenReturn(123456);
        scoreboardNumber.update();
        assertEquals(5, ScoreboardNumber.transformScoreToArray(ship.getScore())[scoreboardNumber.getPlace()]);
    }

    @Test
    public void scoreIsPaddedWithZeros() {
        when(ship.getScore()).thenReturn(123);
        scoreboardNumber.update();
        assertEquals(0, ScoreboardNumber.transformScoreToArray(ship.getScore())[3]);
        assertEquals(0, ScoreboardNumber.transformScoreToArray(ship.getScore())[4]);
        assertEquals(0, ScoreboardNumber.transformScoreToArray(ship.getScore())[5]);
    }
}