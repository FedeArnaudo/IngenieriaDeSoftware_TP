import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class TileManager {
    GamePanel gamePanel;
    private final ArrayList<Tile> tiles;
    private int countDraw;
    private int imagesNumb;

    public TileManager(GamePanel gamePanel){
        Random random = new Random();
        tiles = new ArrayList<>();
        countDraw = 0;
        imagesNumb = 66;
        this.gamePanel = gamePanel;
        //tiles = new Tile[12];

        getTileImage();
    }

    public void getTileImage(){
        try {
            for(int i = 0; i < imagesNumb; i ++){
                Tile tile = new Tile();
                tile.image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/space3/fila-" + (i+1) + "-columna-1.png")));
                tiles.add(tile);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D graphics2D) throws InterruptedException {
        for(int i = 0; i < imagesNumb; i ++){
            graphics2D.drawImage(tiles.get(i).image, 0, (gamePanel.originalTileSize * i), gamePanel.screenWidth, gamePanel.originalTileSize, null);

            if (countDraw == 4 && i == (imagesNumb - 1)){
                Tile tile = tiles.get(i);
                ArrayList<Tile> tiles1 = new ArrayList<>();
                tiles1.add(tile);
                tiles1.addAll(tiles.subList(0,i));
                tiles.clear();
                tiles.addAll(tiles1);
                countDraw = -1;
            }
        }
        countDraw ++;
    }
}
