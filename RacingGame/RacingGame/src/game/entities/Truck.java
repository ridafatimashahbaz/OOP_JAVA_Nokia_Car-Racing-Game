package game.entities;

import game.util.Constants;
import java.awt.*;
import java.util.Random;

/**
 * TRUCK
 * 
 * A larger, slower obstacle. Takes up more space than a car.
 * 
 * OOP Concepts:
 *   - INHERITANCE: extends Vehicle (same parent as EnemyCar).
 *   - POLYMORPHISM: Overrides draw() to draw a truck shape instead of car.
 *   - This shows how different objects can share the same base class
 *     but behave very differently.
 */
public class Truck extends Vehicle {

    private static final Color[] TRUCK_COLORS = {
        new Color(80, 80, 200),   // Dark Blue
        new Color(200, 100, 0),   // Dark Orange
        new Color(30, 130, 30),   // Dark Green
        new Color(120, 0, 120),   // Dark Purple
        new Color(160, 160, 160), // Silver
    };

    private static final Random random = new Random();

    private int lane;
    private Color cabinColor;
    private Color cargoColor;

    /**
     * @param lane  Lane (0, 1, or 2)
     * @param speed How fast this truck moves (usually slower than cars)
     */
    public Truck(int lane, int speed) {
        super(
            Constants.LANE_CENTERS[lane],
            -Constants.TRUCK_HEIGHT,       // Start above screen
            Constants.TRUCK_WIDTH,
            Constants.TRUCK_HEIGHT,
            speed,
            TRUCK_COLORS[random.nextInt(TRUCK_COLORS.length)]
        );
        this.lane = lane;

        // Cabin and cargo are different shades
        this.cabinColor = bodyColor;
        this.cargoColor = bodyColor.darker();
    }

    // ─── Override: Update ─────────────────────────────────────────────────

    @Override
    public void update() {
        y += speed; // Move downward

        if (y > Constants.WINDOW_HEIGHT + height) {
            alive = false;
        }
    }

    // ─── Override: Draw ──────────────────────────────────────────────────

    @Override
    public void draw(Graphics2D g2d) {
        int left   = x - width / 2;
        int top    = y - height / 2;
        int right  = x + width / 2;
        int bottom = y + height / 2;

        // ── Shadow ──────────────────────────────────────────────────────
        g2d.setColor(new Color(0, 0, 0, 70));
        g2d.fillRoundRect(left + 5, top + 5, width, height, 6, 6);

        // ── Cargo section (top, bigger part) ────────────────────────────
        int cargoH = (int)(height * 0.65);
        g2d.setColor(cargoColor);
        g2d.fillRect(left, top, width, cargoH);

        // Cargo stripes (horizontal lines on the trailer)
        g2d.setColor(cargoColor.darker());
        g2d.setStroke(new BasicStroke(1.0f));
        for (int iy = top + 10; iy < top + cargoH - 4; iy += 10) {
            g2d.drawLine(left + 2, iy, right - 2, iy);
        }

        // Cargo outline
        g2d.setColor(cargoColor.darker().darker());
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRect(left, top, width, cargoH);

        // ── Cabin section (bottom part) ──────────────────────────────────
        int cabinTop = top + cargoH;
        int cabinH   = height - cargoH;
        g2d.setColor(cabinColor);
        g2d.fillRoundRect(left, cabinTop, width, cabinH, 8, 8);

        // Cabin windshield
        g2d.setColor(new Color(150, 220, 255, 200));
        g2d.fillRoundRect(left + 5, cabinTop + 4, width - 10, cabinH / 2, 4, 4);

        // Cabin outline
        g2d.setColor(cabinColor.darker());
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(left, cabinTop, width, cabinH, 8, 8);

        // ── Wheels ────────────────────────────────────────────────────────
        g2d.setColor(new Color(30, 30, 30));
        int ww = 10, wh = 16;
        // Front (cabin) wheels
        g2d.fillRoundRect(left - 5, bottom - 10 - wh, ww, wh, 3, 3);
        g2d.fillRoundRect(right - 5, bottom - 10 - wh, ww, wh, 3, 3);
        // Rear (cargo) wheels
        g2d.fillRoundRect(left - 5, top + 10, ww, wh, 3, 3);
        g2d.fillRoundRect(right - 5, top + 10, ww, wh, 3, 3);

        // ── Headlights ────────────────────────────────────────────────────
        g2d.setColor(new Color(255, 255, 180));
        g2d.fillOval(left + 4, bottom - 7, 10, 6);
        g2d.fillOval(right - 14, bottom - 7, 10, 6);

        // ── Taillights ────────────────────────────────────────────────────
        g2d.setColor(new Color(255, 60, 60));
        g2d.fillOval(left + 4, top + 2, 8, 5);
        g2d.fillOval(right - 12, top + 2, 8, 5);
    }

    // ─── Getter ───────────────────────────────────────────────────────────

    public int getLane() { return lane; }
}
