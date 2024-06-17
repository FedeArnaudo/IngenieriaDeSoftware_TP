import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import javax.sound.sampled.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AudioSystem.class, Logger.class})
public class SoundTest {
    private Sound sound;
    private Clip clip;
    private Logger logger;

    @Before
    public void setup() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        clip = mock(Clip.class);
        mockStatic(AudioSystem.class);
        when(AudioSystem.getClip()).thenReturn(clip);

        logger = mock(Logger.class);
        mockStatic(Logger.class);
        when(Logger.getLogger(Sound.class.getName())).thenReturn(logger);

        sound = new Sound("sounds/test_sound.wav");
    }

    @Test
    public void play_startsClip() {
        sound.play();
        verify(clip, times(1)).start();
    }

    @Test
    public void play_logsErrorWithInvalidFile() {
        sound = new Sound("invalid_file.wav");
        try {
            sound.play();
        } catch (Exception e) {
            verify(logger, times(1)).log(eq(Level.SEVERE), eq("Could not play sound"), isA(UnsupportedAudioFileException.class));
        }
    }

    @Test
    public void play_logsErrorWhenExceptionIsThrown() {
        sound = new Sound("invalid_file.wav");
        try {
            sound.play();
        } catch (Exception e) {
            verify(logger, times(1)).log(eq(Level.SEVERE), eq("Could not play sound"), isA(Exception.class));
        }
    }
}