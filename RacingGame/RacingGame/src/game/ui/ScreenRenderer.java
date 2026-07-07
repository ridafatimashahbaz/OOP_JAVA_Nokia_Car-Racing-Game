package game.ui;

import game.util.Constants;
import java.awt.*;

/**
 * SCREEN RENDERER
 * 
 * Draws full-screen overlays for:
 * - Main Menu
 * - Countdown (3, 2, 1, GO!)
 * - Game Over
 * - Crash message
 * 
 * OOP Concepts:
 *   - ENCAPSULATION: All screen drawing is isolated here.
 *   - SINGLE RESPONSIBILITY: Only draws screens, no game logic.
 */
public class ScreenRenderer {

    private Font titleFont;
    private Font subFont;
    private Font hintFont;

    public ScreenRenderer() {
        titleFont = new Font("Courier New", Font.BOLD, 36);
        subFont   = new Font("Courier New", Font.BOLD, 20);
        hintFont  = new Font("Courier New", Font.PLAIN, 13);
    }

    // ─── Main Menu ────────────────────────────────────────────────────────

    /**
     * Draw the main menu screen.
     * @param animTick Animation tick (for pulsing effects)
     */
    public void drawMenu(Graphics2D g2d, int animTick) {
        // Full screen dark overlay
        drawDarkOverlay(g2d, 200);

        // Title with glow effect
        drawGlowText(g2d, "RACING CAR", Constants.WINDOW_WIDTH / 2,
                     Constants.WINDOW_HEIGHT / 2 - 110,
                     titleFont, new Color(120, 240, 80), animTick);

        // Subtitle
        g2d.setFont(subFont);
        g2d.setColor(new Color(80, 180, 60));
        drawCenteredString(g2d, "NOKIA CLASSIC EDITION",
                           Constants.WINDOW_WIDTH / 2,
                           Constants.WINDOW_HEIGHT / 2 - 68);

        // Divider line
        g2d.setColor(new Color(80, 130, 60));
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawLine(60, Constants.WINDOW_HEIGHT / 2 - 48,
                     Constants.WINDOW_WIDTH - 60, Constants.WINDOW_HEIGHT / 2 - 48);

        // Instructions
        g2d.setFont(hintFont);
        g2d.setColor(new Color(160, 210, 140));
        String[] lines = {
            "← → Arrow Keys  :  Change Lane",
            "↑ ↓ Arrow Keys  :  Speed Up / Down",
            "P               :  Pause Game",
            "",
            "Dodge all traffic!",
            "Go as far as you can!"
        };
        int lineY = Constants.WINDOW_HEIGHT / 2 - 20;
        for (String line : lines) {
            drawCenteredString(g2d, line, Constants.WINDOW_WIDTH / 2, lineY);
            lineY += 22;
        }

        // Pulsing start prompt
        float pulse = (float)(Math.sin(animTick * 0.08) * 0.5 + 0.5);
        int alpha   = (int)(150 + pulse * 100);
        g2d.setFont(subFont);
        g2d.setColor(new Color(120, 240, 80, alpha));
        drawCenteredString(g2d, "PRESS ENTER TO START",
                           Constants.WINDOW_WIDTH / 2,
                           Constants.WINDOW_HEIGHT / 2 + 130);

        // Version / credits
        g2d.setFont(new Font("Courier New", Font.PLAIN, 10));
        g2d.setColor(new Color(80, 120, 70));
        drawCenteredString(g2d, "OOP Java Project  v1.0",
                           Constants.WINDOW_WIDTH / 2,
                           Constants.WINDOW_HEIGHT - 20);
    }

    // ─── Countdown ────────────────────────────────────────────────────────

    /**
     * Draw the countdown before the game starts.
     * @param count 3, 2, 1, or 0 (0 = "GO!")
     * @param alpha Transparency of the number (for fade animation)
     */
    public void drawCountdown(Graphics2D g2d, int count, int alpha) {
        drawDarkOverlay(g2d, 120);

        Font countFont = new Font("Courier New", Font.BOLD, 100);
        g2d.setFont(countFont);

        String text = (count == 0) ? "GO!" : String.valueOf(count);
        Color color = (count == 0) ? new Color(120, 240, 80, alpha)
                                   : new Color(240, 200, 60, alpha);

        // Glow
        for (int r = 12; r > 0; r -= 4) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(),
                                   Math.max(0, alpha / 8)));
            drawCenteredString(g2d, text,
                Constants.WINDOW_WIDTH / 2 + r / 2, Constants.WINDOW_HEIGHT / 2 + 30);
        }

        g2d.setColor(color);
        drawCenteredString(g2d, text, Constants.WINDOW_WIDTH / 2,
                           Constants.WINDOW_HEIGHT / 2 + 30);
    }

    // ─── Crash Message ────────────────────────────────────────────────────

    /**
     * Draw the "CRASH!" message briefly after collision.
     * @param alpha Transparency (fade out)
     */
    public void drawCrashMessage(Graphics2D g2d, int alpha) {
        Font crashFont = new Font("Courier New", Font.BOLD, 50);
        g2d.setFont(crashFont);
        g2d.setColor(new Color(255, 60, 30, alpha));
        drawCenteredString(g2d, "CRASH!",
                           Constants.WINDOW_WIDTH / 2,
                           Constants.WINDOW_HEIGHT / 2);

        g2d.setFont(hintFont);
        g2d.setColor(new Color(220, 180, 80, alpha));
        drawCenteredString(g2d, "Respawning...",
                           Constants.WINDOW_WIDTH / 2,
                           Constants.WINDOW_HEIGHT / 2 + 40);
    }

    // ─── Game Over ────────────────────────────────────────────────────────

    /**
     * Draw the Game Over screen.
     * @param finalDistance Distance the player traveled
     * @param animTick For pulsing effects
     */
    public void drawGameOver(Graphics2D g2d, long finalDistance, int animTick) {
        drawDarkOverlay(g2d, 210);

        // Game Over title
        drawGlowText(g2d, "GAME OVER", Constants.WINDOW_WIDTH / 2,
                     Constants.WINDOW_HEIGHT / 2 - 100,
                     new Font("Courier New", Font.BOLD, 40),
                     new Color(220, 60, 40), animTick);

        // Final score
        g2d.setFont(subFont);
        g2d.setColor(new Color(200, 210, 160));
        drawCenteredString(g2d, "DISTANCE TRAVELED",
                           Constants.WINDOW_WIDTH / 2,
                           Constants.WINDOW_HEIGHT / 2 - 30);

        g2d.setFont(new Font("Courier New", Font.BOLD, 44));
        g2d.setColor(new Color(120, 240, 80));
        drawCenteredString(g2d, finalDistance + " KM",
                           Constants.WINDOW_WIDTH / 2,
                           Constants.WINDOW_HEIGHT / 2 + 20);

        // Grade
        String grade = getGrade(finalDistance);
        g2d.setFont(subFont);
        g2d.setColor(new Color(255, 200, 60));
        drawCenteredString(g2d, "GRADE: " + grade,
                           Constants.WINDOW_WIDTH / 2,
                           Constants.WINDOW_HEIGHT / 2 + 60);

        // Pulsing restart prompt
        float pulse = (float)(Math.sin(animTick * 0.08) * 0.5 + 0.5);
        int pAlpha  = (int)(150 + pulse * 100);
        g2d.setFont(hintFont);
        g2d.setColor(new Color(160, 220, 140, pAlpha));
        drawCenteredString(g2d, "Press ENTER to play again",
                           Constants.WINDOW_WIDTH / 2,
                           Constants.WINDOW_HEIGHT / 2 + 110);
    }

    // ─── Utilities ────────────────────────────────────────────────────────

    private void drawDarkOverlay(Graphics2D g2d, int alpha) {
        g2d.setColor(new Color(0, 0, 0, alpha));
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
    }

    /**
     * Draw text centered at a given X position.
     */
    private void drawCenteredString(Graphics2D g2d, String text, int cx, int y) {
        FontMetrics fm = g2d.getFontMetrics();
        int textX = cx - fm.stringWidth(text) / 2;
        g2d.drawString(text, textX, y);
    }

    /**
     * Draw text with a green glow halo behind it.
     */
    private void drawGlowText(Graphics2D g2d, String text, int cx, int y,
                              Font font, Color color, int animTick) {
        g2d.setFont(font);
        // Draw glow layers (offset slightly, very transparent)
        for (int r = 10; r > 0; r -= 3) {
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
            drawCenteredString(g2d, text, cx, y + r / 2);
        }
        g2d.setColor(color);
        drawCenteredString(g2d, text, cx, y);
    }

    /**
     * Assign a letter grade based on distance traveled.
     */
    private String getGrade(long km) {
        if (km >= 2000) return "S  ★★★";
        if (km >= 1000) return "A+ ★★";
        if (km >= 500)  return "A  ★";
        if (km >= 200)  return "B";
        if (km >= 100)  return "C";
        return "D  Keep Practicing!";
    }
}
