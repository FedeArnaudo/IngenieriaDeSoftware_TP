import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Objects;

public class GamePanelDrawManager {
    private GamePanel gamePanel;
    private final Font arialFont;
    private final Font courierFont;
    private int dotCounter;

    public GamePanelDrawManager() {
        this.arialFont = new Font("Arial", Font.PLAIN, 24);
        this.courierFont = new Font("Courier New", Font.PLAIN, 20);
        this.dotCounter = 1;
    }

    public void attachToGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void drawGameElements(Graphics2D graphics2D) {
        try {
            gamePanel.getTileManager().draw(graphics2D);
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to draw game elements", e);
        }

        switch (gamePanel.getCurrentState()) {
            case WELCOME:
                drawWelcomeScreen(graphics2D);
                gamePanel.getShip().drawWelcomeScreen(graphics2D);
                break;
            case GAME_OVER:
                drawGameOverScreen(graphics2D);
                break;
            case PAUSE:
                drawPauseScreen(graphics2D);
                break;
            case PLAYING:
                drawPlayingScreen(graphics2D);
                break;
        }
    }

    private void drawPlayingScreen(Graphics2D graphics2D) {
        gamePanel.getShip().draw(graphics2D);
        drawObstacles(graphics2D);
        drawScore(graphics2D);
        if (gamePanel.getShip().getBulletFired() == gamePanel.getShip().getBulletsCapacity()) {
            drawCooldownCountdown(graphics2D);
        } else {
            drawBulletsLeft(graphics2D);
        }
        drawLives(graphics2D);
    }

    public void drawObstacles(Graphics2D graphics2D) {
        drawObstaclesList(graphics2D, gamePanel.getMeteors1());
        drawObstaclesList(graphics2D, gamePanel.getMeteors2());
        drawObstaclesList(graphics2D, gamePanel.getBulletPowerUps());
        drawObstaclesList(graphics2D, gamePanel.getBasicPowerUps());
        drawObstaclesList(graphics2D, gamePanel.getSuperPowerUps());
    }

    private void drawObstaclesList(Graphics2D graphics2D, Iterable<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(graphics2D);
        }
    }

    public void drawWelcomeScreen(Graphics2D graphics2D) {
        String word = "spaceships";
        int startX = (gamePanel.getScreenWidth() - word.length() * gamePanel.getLetterImages().get('a').getWidth() * 2) / 2 - 60;
        int y = (int) (gamePanel.getScreenHeight() * 0.25);

        drawFloatingText(graphics2D, "spaceships", gamePanel.getLetterImages(), startX, y, gamePanel.getFloatTime());
        drawCenteredText(graphics2D, "PRESS ENTER TO PLAY", new Font("Courier New", Font.PLAIN, 20), (int) (gamePanel.getScreenHeight() * 0.85));
        drawInstructions(graphics2D);
    }

    public void drawPauseScreen(Graphics2D graphics2D) {
        graphics2D.setColor(Color.decode("#000422"));
        graphics2D.fillRect(0, 0, gamePanel.getScreenWidth(), gamePanel.getScreenHeight());

        String word = "pause";
        int startX = (gamePanel.getScreenWidth() - word.length() * gamePanel.getLetterImages().get('a').getWidth() * 2) / 2;
        int y = (int) (gamePanel.getScreenHeight() * 0.25);

        drawFloatingText(graphics2D, "pause", gamePanel.getLetterImages(), startX, y, gamePanel.getFloatTime());
        drawCenteredText(graphics2D, "PRESS P TO RESUME", new Font("Courier New", Font.PLAIN, 20), (int) (gamePanel.getScreenHeight() * 0.85));
        drawInstructions(graphics2D);
    }

    public void drawGameOverScreen(Graphics2D graphics2D) {
        String title = "game over";
        int startX = calculateCenteredTextX(title, gamePanel.getLetterImages(), 2);
        int y = (int) (gamePanel.getScreenHeight() * 0.25);

        drawFloatingText(graphics2D, title, gamePanel.getLetterImages(), startX, y, gamePanel.getFloatTime());
        drawCenteredText(graphics2D, "PRESS ENTER TO RESTART", courierFont, (int) (gamePanel.getScreenHeight() * 0.85));

        drawFinalScore(graphics2D);
    }

    private void drawFinalScore(Graphics2D graphics2D) {
        String scoreText = String.format("%06d", gamePanel.getShip().getFinalScore());
        int totalWidth = calculateTextWidth(scoreText, gamePanel.getNumberImages(), 2);
        int x = (gamePanel.getScreenWidth() - totalWidth) / 2;
        int y = (int) (gamePanel.getScreenHeight() * 0.65);

        drawCenteredText(graphics2D, "YOUR FINAL SCORE:", courierFont, y - 30);

        drawNumberImages(graphics2D, scoreText, x, y, 2);
    }

    public void drawScore(Graphics2D graphics2D) {
        String scoreString = String.format("%06d", gamePanel.getShip().getScore());
        int padding = 20;
        int x = gamePanel.getScreenWidth() - calculateTextWidth(scoreString, gamePanel.getNumberImages(), 2) - padding;
        drawNumberImages(graphics2D, scoreString, x, padding, 2);

        if (gamePanel.getScoreZoom() > 1.0) {
            gamePanel.setScoreZoom(gamePanel.getScoreZoom() - 0.015);
        }
    }

    public void drawBulletsLeft(Graphics2D graphics2D) {
        int bulletsLeft = gamePanel.getShip().getBulletsCapacity() - gamePanel.getShip().getBulletFired();
        String bulletsLeftString = String.format("%d", bulletsLeft);
        int x = calculateRightAlignedTextX(bulletsLeftString, arialFont, 40);
        int y = calculateTextY(arialFont, 2, 20);

        graphics2D.setFont(arialFont);
        graphics2D.setColor(bulletsLeft <= gamePanel.getShip().getBulletsCapacity() * 0.1 ? Color.RED : Color.WHITE);
        graphics2D.drawString(bulletsLeftString, x, y);

        drawImageRightOfText(graphics2D, bulletsLeftString, x, y, gamePanel.getBulletScoreboardImage(), arialFont);
    }

    public void drawLives(Graphics2D graphics2D) {
        int padding = 30;
        int x = padding;

        for (int i = 0; i < gamePanel.getShip().getLives(); i++) {
            drawImageWithPadding(graphics2D, gamePanel.getLiveImage(), x, padding, 10);
            x += gamePanel.getLiveImage().getWidth() + padding;
        }
    }

    public void drawCooldownCountdown(Graphics2D graphics2D) {
        String cooldownString = String.format("%d", (gamePanel.getShip().getCooldownCounter() / 60) + 1);
        int x = calculateRightAlignedTextX(cooldownString, arialFont, 40);
        int y = calculateTextY(arialFont, 2, 20);

        graphics2D.setFont(arialFont);
        graphics2D.setColor(Color.BLUE);
        graphics2D.drawString(cooldownString, x, y);

        drawCoolingText(graphics2D, y);

        drawImageRightOfText(graphics2D, cooldownString, x, y, gamePanel.getCollingIconImage(), arialFont);

        if (gamePanel.getShip().getCooldownCounter() % 20 == 0) {
            dotCounter = (dotCounter % 3) + 1;
        }
    }

    private void drawCoolingText(Graphics2D graphics2D, int y) {
        String coolingText = "COOLING";
        String dots = repeat(".", dotCounter);
        int x = calculateRightAlignedTextX(coolingText + dots, arialFont, 80);

        graphics2D.setFont(arialFont);
        graphics2D.setColor(Color.BLUE);
        graphics2D.drawString(coolingText + dots, x, y);
    }


    private String repeat(String str, int times) {
        if (str == null || times <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(str.length() * times);
        for (int i = 0; i < times; i++) {
            builder.append(str);
        }
        return builder.toString();
    }

    public void drawFloatingText(Graphics2D graphics2D, String text, Map<Character, BufferedImage> letterImages, int startX, int y, double floatTime) {
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            BufferedImage img = letterImages.get(ch);
            if (img != null) {
                int floatOffset = (int) (Math.sin(floatTime + i) * 5);
                graphics2D.drawImage(img, startX + i * img.getWidth() * 2, y + floatOffset, img.getWidth() * 2, img.getHeight() * 2, null);
            }
        }
    }

    private void drawCenteredText(Graphics2D graphics2D, String text, Font font, int y) {
        graphics2D.setFont(font);
        FontMetrics metrics = graphics2D.getFontMetrics(font);
        int x = (gamePanel.getScreenWidth() - metrics.stringWidth(text)) / 2;
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(text, x, y);
    }

    public void drawInstructions(Graphics2D graphics2D) {
        int y = 450; // Adjust this as needed
        int padding = 5; // Decrease this value to bring the images closer together
        int totalWidth = gamePanel.getKeyUpImage().getWidth() + gamePanel.getKeyDownImage().getWidth() +
                gamePanel.getKeyRightImage().getWidth() + gamePanel.getKeyLeftImage().getWidth() + padding * 2;
        int x = (gamePanel.getScreenWidth() - totalWidth) / 4; // Center the images
        int spaceX = ((gamePanel.getScreenWidth() - gamePanel.getKeySpaceImage().getWidth()) / 4) * 3; // Center the space key

        // Draw the keys
        graphics2D.drawImage(gamePanel.getKeyUpImage(), x + gamePanel.getKeyUpImage().getWidth() + padding, y, null);
        graphics2D.drawImage(gamePanel.getKeyLeftImage(), x, y + gamePanel.getKeyLeftImage().getHeight() + padding, null);
        graphics2D.drawImage(gamePanel.getKeyDownImage(), x + gamePanel.getKeyDownImage().getWidth() + padding, y + gamePanel.getKeyDownImage().getHeight() + padding, null);
        graphics2D.drawImage(gamePanel.getKeyRightImage(), x + gamePanel.getKeyRightImage().getWidth() * 2 + padding * 2, y + gamePanel.getKeyRightImage().getHeight() + padding, null);
        graphics2D.drawImage(gamePanel.getKeySpaceImage(), spaceX, y + gamePanel.getKeySpaceImage().getHeight() + padding, null);

        // Set the font and color for the text
        graphics2D.setFont(new Font("Courier New", Font.PLAIN, 20));
        graphics2D.setColor(Color.WHITE);

        // Draw "MOVE" and "SHOOT" text
        String moveText = "MOVE";
        int moveTextX = x + gamePanel.getKeyDownImage().getWidth() + padding;
        int moveTextY = y + gamePanel.getKeyDownImage().getHeight() * 3;
        graphics2D.drawString(moveText, moveTextX, moveTextY);

        String shootText = "SHOOT";
        int shootTextWidth = graphics2D.getFontMetrics().stringWidth(shootText);
        int shootTextX = spaceX + (gamePanel.getKeySpaceImage().getWidth() - shootTextWidth) / 2;
        int shootTextY = y + gamePanel.getKeyDownImage().getHeight() * 3;
        graphics2D.drawString(shootText, shootTextX, shootTextY);
    }

    private int calculateCenteredTextX(String text, Map<Character, BufferedImage> letterImages, int sizeMultiplier) {
        int totalWidth = text.chars().mapToObj(c -> letterImages.get((char) c))
                .filter(Objects::nonNull)
                .mapToInt(img -> img.getWidth() * sizeMultiplier)
                .sum();
        return (gamePanel.getScreenWidth() - totalWidth) / 2;
    }

    private int calculateRightAlignedTextX(String text, Font font, int padding) {
        FontMetrics metrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics().getFontMetrics(font);
        return gamePanel.getScreenWidth() - metrics.stringWidth(text) - padding;
    }

    private int calculateTextY(Font font, int lineCount, int padding) {
        FontMetrics metrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics().getFontMetrics(font);
        return padding + metrics.getHeight() * lineCount + metrics.getHeight() / 2;
    }

    private void drawImageRightOfText(Graphics2D graphics2D, String text, int textX, int textY, BufferedImage image, Font font) {
        FontMetrics metrics = graphics2D.getFontMetrics(font);
        int imageX = textX + metrics.stringWidth(text) + 5; // Adjust as needed
        int imageY = textY - image.getHeight(); // Adjust as needed
        graphics2D.drawImage(image, imageX, imageY, null);
    }

    private void drawImageWithPadding(Graphics2D graphics2D, BufferedImage image, int x, int y, int padding) {
        int imageWidth = image.getWidth() + padding;
        int imageHeight = image.getHeight() + padding;
        graphics2D.drawImage(image, x, y, imageWidth, imageHeight, null);
    }

    private void fillBackground(Graphics2D graphics2D, Color color) {
        graphics2D.setColor(color);
        graphics2D.fillRect(0, 0, gamePanel.getScreenWidth(), gamePanel.getScreenHeight());
    }

    private int calculateTextWidth(String text, Map<Integer, BufferedImage> numberImages, int sizeMultiplier) {
        return text.chars()
                .mapToObj(c -> numberImages.get(Character.getNumericValue(c)))
                .filter(Objects::nonNull)
                .mapToInt(img -> img.getWidth() * sizeMultiplier)
                .sum();
    }

    private void drawNumberImages(Graphics2D graphics2D, String text, int startX, int y, int sizeMultiplier) {
        for (char c : text.toCharArray()) {
            int number = Character.getNumericValue(c);
            BufferedImage numberImage = gamePanel.getNumberImages().get(number);
            if (numberImage != null) {
                graphics2D.drawImage(numberImage, startX, y, numberImage.getWidth() * sizeMultiplier, numberImage.getHeight() * sizeMultiplier, null);
                startX += numberImage.getWidth() * sizeMultiplier;
            }
        }
    }
}
