import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{
    /**
     * Screen settings
     */
    final int originalTileSize  = 19;   //  19x19
    final int scale = 3;
    public int tileSize = originalTileSize * scale;  //  57x57
    final int maxScreenCol = 16;
    final int maxScreenRow = 16;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    KeyHandler keyHandler = new KeyHandler();
    /**
     * Ship Main
     */
    Player player = new Player(this, keyHandler);
    /**
     * Meteoritos
     */
    ArrayList<Meteor> meteors = new ArrayList<>();
    TileManager tileManager = new TileManager(this);
    CollisionChecker collisionChecker = new CollisionChecker(this);
    Thread gameThread;

    //  FPS
    final int FPS = 60;
    private double timer = 0;
    private int drawCount = 0;


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        int meteorsNumber = 6;
        for(int i = 0; i < meteorsNumber; i++){
            meteors.add(new Meteor(this));
        }
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
        double drawInterval = 1000000000/FPS;   //1ps/FPS   intervalo de refresh
        double nextDrawTime = System.nanoTime() + drawInterval;
        timer = System.nanoTime();

        while (gameThread != null){
             // 1 UPDATE:   update information such as character positions
            upDate();
             // 2 DRAW:     draw the screen with the updated information
            repaint();
            drawCount ++;
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;
                if(remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            nextDrawTime += drawInterval;
        }
    }

    public void upDate(){
        player.update();
        for (Meteor meteor: meteors){
            if(meteor.y == -tileSize){
                for(Meteor meteor1: meteors){
                    while (true){
                        if(meteor.equals(meteor1) || (!meteor.equals(meteor1) && meteor.x != meteor1.x)){
                            break;
                        }
                        else if(meteor.x == meteor1.x){
                            meteor.setDefaultValues();
                        }
                    }
                }
            }
            meteor.update();
        }
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        Graphics2D  graphics2D = (Graphics2D) graphics;

        try {
            tileManager.draw(graphics2D);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        player.draw(graphics2D);
        for(Meteor meteor: meteors){
            meteor.draw(graphics2D);
        }

        timer = System.nanoTime() - timer;
        if(timer > 1000000000){
            graphics2D.setFont(new Font("Arial", Font.PLAIN, 10));
            graphics2D.drawString("FPS: " + drawCount, 750, 30);
            graphics2D.dispose();
            drawCount = 0;
            timer = 0;
        }

    }
}
