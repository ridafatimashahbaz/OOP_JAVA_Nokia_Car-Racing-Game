package game.ui;

import game.util.Constants;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;

/**
 * HUD (Heads-Up Display)
 * 
 * Draws the game's on-screen information:
 * - Distance (KMs), Lives, Speed
 * - Key press buttons (show which arrow keys are pressed)
 * - Pause indicator
 * 
 * OOP Concepts:
 *   - ENCAPSULATION: All HUD drawing is in one class.
 *   - SINGLE RESPONSIBILITY: Only draws UI, no game logic.
 */
public class HUD {

    // Fonts
    private Font hudFont;
    private Font bigFont;
    private Font smallFont;

    public HUD() {
        hudFont   = new Font("Courier New", Font.BOLD, 16);
        bigFont   = new Font("Courier New", Font.BOLD, 22);
        smallFont = new Font("Courier New", Font.PLAIN, 11);
    }

    /**
     * Draw the full HUD.
     * 
     * @param g2d         Graphics context
     * @param distanceKm  Total distance traveled
     * @param lives       Remaining lives
     * @param speed       Current player speed
     * @param pressedKeys Set of currently pressed key codes (for button display)
     * @param paused      Is the game paused?
     */
    public void draw(Graphics2D g2d, long distanceKm, int lives, int speed,
                     Set<Integer> pressedKeys, boolean paused) {
        drawTopBar(g2d, distanceKm, lives);
        drawSpeedMeter(g2d, speed);
        drawControlButtons(g2d, pressedKeys);
        if (paused) drawPauseOverlay(g2d);
    }

    // ─── Top Bar (KMs and Lives) ──────────────────────────────────────────

    private void drawTopBar(Graphics2D g2d, long distanceKm, int lives) {
        // LCD-style background
        g2d.setColor(new Color(30, 40, 30));
        g2d.fillRoundRect(10, 8, Constants.WINDOW_WIDTH - 20, 38, 8, 8);

        g2d.setColor(new Color(60, 80, 50));
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRoundRect(10, 8, Constants.WINDOW_WIDTH - 20, 38, 8, 8);

        // KMS text (green LCD style)
        g2d.setFont(bigFont);
        g2d.setColor(new Color(120, 220, 80));
        g2d.drawString(String.format("KMS: %04d", distanceKm), 20, 36);

        // LIVES text (green LCD style)
        String livesText = "LIVES: " + lives;
        FontMetrics fm = g2d.getFontMetrics(bigFont);
        int textW = fm.stringWidth(livesText);
        g2d.drawString(livesText, Constants.WINDOW_WIDTH - textW - 20, 36);
    }

    // ─── Speed Meter ──────────────────────────────────────────────────────

    private void drawSpeedMeter(Graphics2D g2d, int speed) {
        int meterX = Constants.WINDOW_WIDTH - 68;
        int meterY = 56;
        int meterW = 58;
        int meterH = 14;

        // Label
        g2d.setFont(smallFont);
        g2d.setColor(new Color(160, 200, 140));
        g2d.drawString("SPEED", meterX, meterY - 2);

        // Bar background
        g2d.setColor(new Color(20, 30, 20));
        g2d.fillRoundRect(meterX, meterY, meterW, meterH, 4, 4);

        // Bar fill — color changes red→green based on speed
        float ratio = (float)(speed - Constants.PLAYER_MIN_SPEED) /
                      (Constants.PLAYER_MAX_SPEED - Constants.PLAYER_MIN_SPEED);
        Color barColor = interpolateColor(new Color(80, 200, 80), new Color(220, 60, 60), ratio);

        int fillW = (int)(meterW * ratio);
        if (fillW > 0) {
            g2d.setColor(barColor);
            g2d.fillRoundRect(meterX, meterY, fillW, meterH, 4, 4);
        }

        // Bar outline
        g2d.setColor(new Color(80, 100, 70));
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawRoundRect(meterX, meterY, meterW, meterH, 4, 4);

        // Speed number
        g2d.setColor(new Color(180, 220, 160));
        g2d.drawString(speed + "x", meterX + meterW + 4, meterY + 12);
    }

    // ─── Arrow Key Buttons ────────────────────────────────────────────────

    /**
     * Draw the arrow key indicators at the bottom of the screen.
     * Buttons "press in" (darker) when the key is held down.
     * This gives visual feedback for which keys are active.
     */
    private void drawControlButtons(Graphics2D g2d, Set<Integer> pressedKeys) {
        int btnSize = 38;
        int btnY    = Constants.WINDOW_HEIGHT - 56;

        // Left cluster: UP and DOWN (speed control)
        int upDownX = 24;
        drawArrowButton(g2d, upDownX, btnY - btnSize - 4, btnSize, "▲",
                        pressedKeys.contains(KeyEvent.VK_UP));
        drawArrowButton(g2d, upDownX, btnY, btnSize, "▼",
                        pressedKeys.contains(KeyEvent.VK_DOWN));

        // Right cluster: LEFT and RIGHT (lane change)
        int leftRightY = btnY - btnSize / 2 + 2;
        int lrX        = Constants.WINDOW_WIDTH / 2 + 20;
        drawArrowButton(g2d, lrX, leftRightY, btnSize, "◀",
                        pressedKeys.contains(KeyEvent.VK_LEFT));
        drawArrowButton(g2d, lrX + btnSize + 6, leftRightY, btnSize, "▶",
                        pressedKeys.contains(KeyEvent.VK_RIGHT));

        // Labels
        g2d.setFont(smallFont);
        g2d.setColor(new Color(140, 180, 130));
        g2d.drawString("SPEED", upDownX, btnY - btnSize - 10);
        g2d.drawString("LANE", lrX + 4, leftRightY - 6);

        // Pause hint
        g2d.setColor(new Color(100, 130, 100));
        g2d.setFont(new Font("Courier New", Font.PLAIN, 10));
        g2d.drawString("[P] PAUSE", Constants.WINDOW_WIDTH / 2 - 28,
                       Constants.WINDOW_HEIGHT - 6);
    }

    /**
     * Draw a single arrow button.
     * @param pressed If true, button appears pressed in (darker).
     */
    private void drawArrowButton(Graphics2D g2d, int bx, int by,
                                  int size, String arrow, boolean pressed) {
        // Button shadow (only when NOT pressed)
        if (!pressed) {
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRoundRect(bx + 3, by + 3, size, size, 8, 8);
        }

        // Button body
        Color btnColor = pressed
            ? new Color(40, 70, 40)   // Pressed: dark
            : new Color(60, 100, 60); // Normal: brighter
        g2d.setColor(btnColor);
        g2d.fillRoundRect(bx + (pressed ? 2 : 0),
                          by + (pressed ? 2 : 0), size, size, 8, 8);

        // Button border
        g2d.setColor(new Color(100, 150, 80));
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRoundRect(bx + (pressed ? 2 : 0),
                          by + (pressed ? 2 : 0), size, size, 8, 8);

        // Arrow symbol
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(pressed ? new Color(120, 200, 100) : new Color(180, 240, 160));
        FontMetrics fm = g2d.getFontMetrics();
        int arrowX = bx + (pressed ? 2 : 0) + (size - fm.stringWidth(arrow)) / 2;
        int arrowY = by + (pressed ? 2 : 0) + (size + fm.getAscent()) / 2 - 4;
        g2d.drawString(arrow, arrowX, arrowY);
    }

    // ─── Pause Overlay ────────────────────────────────────────────────────

    private void drawPauseOverlay(Graphics2D g2d) {
        // Semi-transparent dark overlay
        g2d.setColor(new Color(0, 0, 0, 140));
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        g2d.setFont(new Font("Courier New", Font.BOLD, 32));
        g2d.setColor(new Color(120, 220, 80));
        String msg = "PAUSED";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(msg, (Constants.WINDOW_WIDTH - fm.stringWidth(msg)) / 2,
                       Constants.WINDOW_HEIGHT / 2 - 10);

        g2d.setFont(smallFont);
        g2d.setColor(new Color(160, 200, 140));
        String sub = "Press P to continue";
        fm = g2d.getFontMetrics();
        g2d.drawString(sub, (Constants.WINDOW_WIDTH - fm.stringWidth(sub)) / 2,
                       Constants.WINDOW_HEIGHT / 2 + 20);
    }

    // ─── Utility ─────────────────────────────────────────────────────────

    /** Linearly interpolate between two colors. */
    private Color interpolateColor(Color a, Color b, float t) {
        if (t < 0) t = 0;
        if (t > 1) t = 1;
        int r = (int)(a.getRed()   + (b.getRed()   - a.getRed())   * t);
        int g = (int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl= (int)(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t);
        return new Color(r, g, bl);
    }
}
