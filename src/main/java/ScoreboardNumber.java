package main.java;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ScoreboardNumber extends Entity{
    GamePanel gamePanel;
    Ship ship;

    private final ArrayList<BufferedImage> bufferedImages;
    private final Random random;

    private int place;
    private int number;

    public ScoreboardNumber(GamePanel gamePanel, Ship ship, int place){
        this.gamePanel = gamePanel;
        this.place = place;
        this.ship = ship;

        number = 0;
        random = new Random();
        bufferedImages = new ArrayList<>();

        setPosition();
        getNumberImage();
    }

    public ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }

    public void setPosition(){
        y = 0;
        x = gamePanel.getScreenWidth() - gamePanel.getTileSize() - (gamePanel.getTileSize() * place);
    }

    public void getNumberImage() {
        try {
            BufferedImage number0 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/numbers/number_0.png")));
            BufferedImage number1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/numbers/number_1.png")));
            BufferedImage number2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/numbers/number_2.png")));
            BufferedImage number3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/numbers/number_3.png")));
            BufferedImage number4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/numbers/number_4.png")));
            BufferedImage number5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/numbers/number_5.png")));
            BufferedImage number6 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/numbers/number_6.png")));
            BufferedImage number7 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/numbers/number_7.png")));
            BufferedImage number8 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/numbers/number_8.png")));
            BufferedImage number9 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/numbers/number_9.png")));

            bufferedImages.add(number0);
            bufferedImages.add(number1);
            bufferedImages.add(number2);
            bufferedImages.add(number3);
            bufferedImages.add(number4);
            bufferedImages.add(number5);
            bufferedImages.add(number6);
            bufferedImages.add(number7);
            bufferedImages.add(number8);
            bufferedImages.add(number9);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(){
        extractScorePlaceNumber();
    }

    private void extractScorePlaceNumber(){
        int score_temp = ship.getScore();
        int[] scoreArray = transformScoreToArray(score_temp);
        number = scoreArray[place];
    }

    public static int[] transformScoreToArray(int score) {
        String scoreStr = String.valueOf(score);
        int[] result = new int[6];

        // Fill the array with digits from the score starting from the right
        int strIndex = scoreStr.length() - 1;
        for (int i = 0; i < result.length; i++) {
            if (strIndex >= 0) {
                result[i] = Character.getNumericValue(scoreStr.charAt(strIndex));
                strIndex--;
            } else {
                result[i] = 0; // pad with zeros if score has fewer than 6 digits
            }
        }

        return result;
    }

    @Override
    public void draw(Graphics2D graphics2D){
        BufferedImage bufferedImage = bufferedImages.get(number);
        graphics2D.drawImage(bufferedImage, x, y, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() { return y; }

    @Override
    public int getSpeed() {
        return speed;
    }

    public int getPlace(){
        return place;
    }
}