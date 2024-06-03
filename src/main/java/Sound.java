package main.java;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    private final Clip clip;

    public Sound(String soundFileName) {
        try {
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("Could not play sound", e);
        }
    }

    public void play() {
        clip.setFramePosition(0); // rewind to the beginning
        clip.start(); // Start playing
    }
}