package main.java;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TileManager {
    GamePanel gamePanel;
    private final ArrayList<Tile> tiles;
    private int countDraw;
    private final int IMAGES_NUMB = 66;

    public TileManager(GamePanel gamePanel){
        tiles = new ArrayList<>();
        countDraw = 0;
        this.gamePanel = gamePanel;

        getTileImage();
    }

    public void getTileImage(){
        try {
            for(int i = 0; i < IMAGES_NUMB; i ++){
                Tile tile = new Tile();
                tile.image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/space3/fila-" + (i+1) + "-columna-1.png")));
                tiles.add(tile);
            }
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics2D graphics2D) {
        for(int i = 0; i < IMAGES_NUMB; i ++){
            graphics2D.drawImage(tiles.get(i).image, 0, (GamePanel.getOriginalTileSize() * i), gamePanel.getScreenWidth(), GamePanel.getOriginalTileSize(), null);

            if (countDraw == 4 && i == (IMAGES_NUMB - 1)){
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
