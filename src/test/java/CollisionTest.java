package test.java;

import main.java.CollisionChecker;
import main.java.Entity;
import main.java.GamePanel;
import main.java.Meteor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.util.ArrayList;

import static org.mockito.Mockito.when;

public class CollisionTest {
    private GamePanel gamePanel;
    private Meteor meteor;
    private Entity entity;
    private CollisionChecker collisionChecker;

    @BeforeEach
    void setUp() {
        // Crea un mock de GamePanel
        gamePanel = Mockito.mock(GamePanel.class);
        meteor = Mockito.mock(Meteor.class);
        entity = Mockito.mock(Entity.class);

        // Simula la lista de meteoros
        ArrayList<Meteor> meteors = new ArrayList<>();
        meteors.add(meteor);
        when(gamePanel.getMeteors()).thenReturn(meteors);

        // Configura los rectángulos sólidos
        when(meteor.getSolidRectangle()).thenReturn(new Rectangle(18, 36, 20, 20));
        when(entity.getSolidRectangle()).thenReturn(new Rectangle(3, 5, 52, 40));

        collisionChecker = new CollisionChecker(gamePanel);
    }

    @Test
    void testDetectObjectCollision() {
        // Simula las coordenadas de las entidades
        when(meteor.getX()).thenReturn(0);
        when(meteor.getY()).thenReturn(850);
        when(entity.getX()).thenReturn(0);
        when(entity.getY()).thenReturn(850);
        when(entity.getDirection()).thenReturn("up");

        // Verifica que detecta la colisión
        Entity result = collisionChecker.detectObject(entity);
        Assertions.assertEquals(meteor, result);
    }
    @Test
    void testDetectObjectNoCollision(){
        // Simula las coordenadas de las entidades
        when(meteor.getX()).thenReturn(0);
        when(meteor.getY()).thenReturn(0);
        when(entity.getX()).thenReturn(57*4);
        when(entity.getY()).thenReturn(850);
        when(entity.getDirection()).thenReturn("up");

        // Verifica que detecta la colisión
        Entity result = collisionChecker.detectObject(entity);
        Assertions.assertNull(result);
    }
    @Test
    void testCheckIntersectTrueCollision(){
        // Simula las coordenadas de las entidades
        when(meteor.getX()).thenReturn(0);
        when(meteor.getY()).thenReturn(850);
        when(entity.getX()).thenReturn(0);
        when(entity.getY()).thenReturn(850);
        when(entity.getDirection()).thenReturn("up");

        // Verifica que detecta la colisión
        boolean result = collisionChecker.checkIntersect(entity, meteor);
        Assertions.assertTrue(result);
    }
    @Test
    void testCheckIntersectFalseCollision(){
        // Simula las coordenadas de las entidades
        when(meteor.getX()).thenReturn(4*57);
        when(meteor.getY()).thenReturn(850);
        when(entity.getX()).thenReturn(0);
        when(entity.getY()).thenReturn(850);
        when(entity.getDirection()).thenReturn("up");

        // Verifica que detecta la colisión
        boolean result = collisionChecker.checkIntersect(entity, meteor);
        Assertions.assertFalse(result);
    }
}
