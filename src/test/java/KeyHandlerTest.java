import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Component;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class KeyHandlerTest {
    private KeyHandler keyHandler;
    private Component component;

    @BeforeEach
    public void setup() {
        keyHandler = new KeyHandler();
        component = mock(Component.class);
    }

    @Test
    public void whenLeftKeyPressedThenIsLeftPressedReturnsTrue() {
        keyHandler.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED));
        assertTrue(keyHandler.isLeftPressed());
    }

    @Test
    public void whenLeftKeyReleasedThenIsLeftPressedReturnsFalse() {
        keyHandler.keyReleased(new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED));
        assertFalse(keyHandler.isLeftPressed());
    }

    @Test
    public void whenSpaceKeyPressedThenIsSpacePressedReturnsTrue() {
        keyHandler.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, KeyEvent.CHAR_UNDEFINED));
        assertTrue(keyHandler.isSpacePressed());
    }

    @Test
    public void whenSpaceKeyReleasedThenIsSpacePressedReturnsFalse() {
        keyHandler.keyReleased(new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_SPACE, KeyEvent.CHAR_UNDEFINED));
        assertFalse(keyHandler.isSpacePressed());
    }

    @Test
    public void whenEnterKeyPressedThenIsEnterPressedReturnsTrue() {
        keyHandler.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED));
        assertTrue(keyHandler.isEnterPressed());
    }

    @Test
    public void whenEnterKeyReleasedThenIsEnterPressedReturnsFalse() {
        keyHandler.keyReleased(new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED));
        assertFalse(keyHandler.isEnterPressed());
    }

    @Test
    public void whenPKeyPressedThenIsPPressedReturnsTrue() {
        keyHandler.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_P, KeyEvent.CHAR_UNDEFINED));
        assertTrue(keyHandler.isPPressed());
    }

    @Test
    public void whenPKeyReleasedThenIsPPressedReturnsFalse() {
        keyHandler.keyReleased(new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_P, KeyEvent.CHAR_UNDEFINED));
        assertFalse(keyHandler.isPPressed());
    }

    @Test
    public void whenBulletFiredInCurrentKeyPressSetToTrueThenIsBulletNotFiredInCurrentKeyPressReturnsFalse() {
        keyHandler.setBulletFiredInCurrentKeyPress(true);
        assertFalse(keyHandler.isBulletNotFiredInCurrentKeyPress());
    }

    @Test
    public void whenBulletFiredInCurrentKeyPressSetToFalseThenIsBulletNotFiredInCurrentKeyPressReturnsTrue() {
        keyHandler.setBulletFiredInCurrentKeyPress(false);
        assertTrue(keyHandler.isBulletNotFiredInCurrentKeyPress());
    }

    @Test
    public void whenRightKeyPressedThenIsRightPressedReturnsTrue() {
        keyHandler.keyPressed(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED));
        assertTrue(keyHandler.isRightPressed());
    }

    @Test
    public void whenRightKeyReleasedThenIsRightPressedReturnsFalse() {
        keyHandler.keyReleased(new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED));
        assertFalse(keyHandler.isRightPressed());
    }

    @Test
    public void whenEnterPressedSetToTrueThenIsEnterPressedReturnsTrue() {
        keyHandler.setEnterPressed(true);
        assertTrue(keyHandler.isEnterPressed());
    }

    @Test
    public void whenEnterPressedSetToFalseThenIsEnterPressedReturnsFalse() {
        keyHandler.setEnterPressed(false);
        assertFalse(keyHandler.isEnterPressed());
    }

    @Test
    public void whenPPressedSetToTrueThenIsPPressedReturnsTrue() {
        keyHandler.setPPressed(true);
        assertTrue(keyHandler.isPPressed());
    }

    @Test
    public void whenPPressedSetToFalseThenIsPPressedReturnsFalse() {
        keyHandler.setPPressed(false);
        assertFalse(keyHandler.isPPressed());
    }

    @Test
    public void testGetLeftPressed() {
        assertFalse(keyHandler.isLeftPressed());
    }

    @Test
    public void testGetRightPressed() {
        assertFalse(keyHandler.isRightPressed());
    }

    @Test
    public void testGetUpPressed() {
        assertFalse(keyHandler.isUpPressed());
    }

    @Test
    public void testGetDownPressed() {
        assertFalse(keyHandler.isDownPressed());
    }

    @Test
    public void testGetSpacePressed() {
        assertFalse(keyHandler.isSpacePressed());
    }

    @Test
    public void testGetEnterPressed() {
        assertFalse(keyHandler.isEnterPressed());
    }

    @Test
    public void testGetPPressed() {
        assertFalse(keyHandler.isPPressed());
    }

    @Test
    public void testGetBulletNotFiredInCurrentKeyPress() {
        assertTrue(keyHandler.isBulletNotFiredInCurrentKeyPress());
    }

    @Test
    public void testSetAndGetEnterPressed() {
        keyHandler.setEnterPressed(true);
        assertTrue(keyHandler.isEnterPressed());
        keyHandler.setEnterPressed(false);
        assertFalse(keyHandler.isEnterPressed());
    }

    @Test
    public void testSetAndGetPPressed() {
        keyHandler.setPPressed(true);
        assertTrue(keyHandler.isPPressed());
        keyHandler.setPPressed(false);
        assertFalse(keyHandler.isPPressed());
    }

    @Test
    public void testSetAndGetBulletFiredInCurrentKeyPress() {
        keyHandler.setBulletFiredInCurrentKeyPress(true);
        assertFalse(keyHandler.isBulletNotFiredInCurrentKeyPress());
        keyHandler.setBulletFiredInCurrentKeyPress(false);
        assertTrue(keyHandler.isBulletNotFiredInCurrentKeyPress());
    }

    @Test
    public void testKeyTyped() {
        KeyEvent e = new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a');
        keyHandler.keyTyped(e);
        // Since keyTyped doesn't do anything, there's nothing to assert here.
        // The test will pass as long as no exceptions are thrown.
    }
}