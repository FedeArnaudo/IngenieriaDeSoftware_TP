import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private boolean leftPressed, rightPressed, upPressed, downPressed, spacePressed, enterPressed, pPressed, bulletFiredInCurrentKeyPress;

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;
            case KeyEvent.VK_SPACE:
                spacePressed = true;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = true;
                break;
            case KeyEvent.VK_P:
                pPressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
            case KeyEvent.VK_SPACE:
                spacePressed = false;
                bulletFiredInCurrentKeyPress = false;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = false;
                break;
            case KeyEvent.VK_P:
                pPressed = false;
                break;
        }
    }
    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isUpPressed() { return upPressed; }

    public boolean isDownPressed() { return downPressed; }

    public boolean isSpacePressed() {
        return spacePressed;
    }

    public boolean isEnterPressed() {
        return enterPressed;
    }

    public boolean isPPressed() {
        return pPressed;
    }

    public boolean isBulletNotFiredInCurrentKeyPress() {
        return !bulletFiredInCurrentKeyPress;
    }

    public void setEnterPressed(boolean enterPressed) {
        this.enterPressed = enterPressed;
    }

    public void setPPressed(boolean pPressed) {
        this.pPressed = pPressed;
    }

    public void setBulletFiredInCurrentKeyPress(boolean bulletFiredInCurrentKeyPress) { this.bulletFiredInCurrentKeyPress = bulletFiredInCurrentKeyPress; }
}
