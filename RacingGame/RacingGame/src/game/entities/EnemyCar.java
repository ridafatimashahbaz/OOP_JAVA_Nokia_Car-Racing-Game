package game.entities;

import game.util.Constants;
import java.awt.*;
import java.util.Random;

/**
 * ENEMY CAR
 * 
 * Traffic cars that the player must dodge.
 * They move downward (toward player) at a set speed.
 * 
 * OOP Concepts:
 *   - INHERITANCE: extends Vehicle.
 *   - POLYMORPHISM: Overrides draw() with its own appearance.
 *   - Each EnemyCar is a different color for variety.
 */
public class EnemyCar extends Vehicle {

    // Pool of colors for enemy cars (not red, which is the player)
    private static final Color[] CAR_COLORS = {
        new Color(255, 165, 0),   // Orange
        new Color(0, 200, 100),   // Green
        new Color(100, 100, 255), // Blue
        new Color(255, 200, 0),   // Yellow
        new Color(180, 0, 255),   // Purple
        new Color(0, 220, 220),   // Cyan
        new Color(255, 100, 180), // Pink
        new Color(150, 150, 150)  // Gray
    };

    private static final Random random = new Random();

    private int lane;       // Which lane this car is in

    /**
     * Constructor — picks a random color and starts above the screen.
     * 
     * @param lane  Which lane (0, 1, or 2)
     * @param speed How fast this car moves down the screen
     */
    public EnemyCar(int lane, int speed) {
        super(
            Constants.LANE_CENTERS[lane],
            -Constants.CAR_HEIGHT,           // Start above screen
            Constants.CAR_WIDTH,
            Constants.CAR_HEIGHT,
            speed,
            CAR_COLORS[random.nextInt(CAR_COLORS.length)]
        );
        this.lane = lane;
    }

    // ─── Override: Update ─────────────────────────────────────────────────

    @Override
    public void update() {
        y += speed; // Move downward each frame

        // If the car has gone below the screen, mark it as dead (remove it)
        if (y > Constants.WINDOW_HEIGHT + height) {
            alive = false;
        }
    }

    // ─── Override: Draw ──────────────────────────────────────────────────

    @Override
    public void draw(Graphics2D g2d) {
        // Enemy cars face downward (toward player), so rear is on top
        // We flip the drawing so headlights face up (toward player)
        Color windowColor = new Color(100, 180, 255, 180);
        Color wheelColor  = new Color(40, 40, 40);

        // Draw facing DOWN (headlights at bottom from player's perspective)
        drawCarBodyFlipped(g2d, bodyColor, windowColor, wheelColor);
    }

    /**
     * Draw a car body facing DOWN (enemy cars come from top, facing player).
     * Same logic as parent's drawCarBody but headlights/windows are flipped.
     */
    private void drawCarBodyFlipped(Graphics2D g2d, Color body, Color window, Color wheel) {
        int left   = x - width / 2;
        int top    = y - height / 2;
        int right  = x + width / 2;
        int bottom = y + height / 2;

        // Shadow
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillRoundRect(left + 4, top + 4, width, height, 12, 12);

        // Body
        g2d.setColor(body);
        g2d.fillRoundRect(left, top, width, height, 12, 12);

        // Windshield at BOTTOM (car faces down)
        g2d.setColor(window);
        g2d.fillRoundRect(left + 5, bottom - height / 3 + 4, width - 10, height / 3 - 8, 6, 6);

        // Rear window at TOP
        g2d.setColor(window);
        g2d.fillRoundRect(left + 5, top + 4, width - 10, height / 4 - 2, 6, 6);

        // Outline
        g2d.setColor(body.darker());
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(left, top, width, height, 12, 12);

        // Wheels
        g2d.setColor(wheel);
        int ww = 8, wh = 14;
        g2d.fillRoundRect(left - 4, top + 8, ww, wh, 3, 3);
        g2d.fillRoundRect(right - 4, top + 8, ww, wh, 3, 3);
        g2d.fillRoundRect(left - 4, bottom - 8 - wh, ww, wh, 3, 3);
        g2d.fillRoundRect(right - 4, bottom - 8 - wh, ww, wh, 3, 3);

        // Headlights at bottom
        g2d.setColor(new Color(255, 255, 180));
        g2d.fillOval(left + 4, bottom - 8, 8, 5);
        g2d.fillOval(right - 12, bottom - 8, 8, 5);

        // Taillights at top
        g2d.setColor(new Color(255, 60, 60));
        g2d.fillOval(left + 4, top + 3, 8, 5);
        g2d.fillOval(right - 12, top + 3, 8, 5);
    }

    // ─── Getters ─────────────────────────────────────────────────────────

    public int getLane() { return lane; }
}
