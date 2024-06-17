import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TileTest {

    @Test
    public void testSetAndGetImage() {
        BufferedImage mockImage = Mockito.mock(BufferedImage.class);
        Tile tile = new Tile();
        tile.setImage(mockImage);
        assertEquals(mockImage, tile.getImage());
    }
}