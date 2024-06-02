package main.java;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private boolean leftPressed, rightPressed, spacePressed, enterPressed, pPressed, wPressed, bulletFiredInCurrentKeyPress;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_LEFT){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_RIGHT){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_SPACE){
            spacePressed = true;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        if(code == KeyEvent.VK_P){
            pPressed = true;
        }
        if(code == KeyEvent.VK_W){
            wPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_LEFT){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_RIGHT){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_SPACE){
            spacePressed = false;
            bulletFiredInCurrentKeyPress = false;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = false;
        }
        if(code == KeyEvent.VK_P){
            pPressed = false;
        }
        if(code == KeyEvent.VK_W){
            wPressed = false;
        }
    }

    public boolean  getLeftPressed() {
        return leftPressed;
    }

    public boolean  getRightPressed() {
        return rightPressed;
    }

    public boolean  getSpacePressed() {
        return spacePressed;
    }

    public boolean  getEnterPressed() {
        return enterPressed;
    }

    public boolean  getPPressed() {
        return pPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public void setSpacePressed(boolean spacePressed) {
        this.spacePressed = spacePressed;
    }

    public void setEnterPressed(boolean enterPressed) {
        this.enterPressed = enterPressed;
    }

    public void setPPressed(boolean pPressed) {
        this.pPressed = pPressed;
    }
}
