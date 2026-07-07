package game.entities;

import game.util.Constants;
import java.awt.*;

/**
 * ABSTRACT BASE CLASS: Vehicle
 * 
 * This is the parent class for ALL vehicles in the game (PlayerCar, EnemyCar, Truck).
 * 
 * OOP Concepts Demonstrated:
 *   - ABSTRACTION: Vehicle defines common structure; subclasses fill in details.
 *   - ENCAPSULATION: Fields are private/protected; accessed through getters/setters.
 *   - INHERITANCE: PlayerCar, EnemyCar, Truck all extend this class.
 *   - POLYMORPHISM: draw() is abstract — each subclass draws itself differently.
 */
public abstract class Vehicle {

    // ─── Protected Fields (accessible to subclasses) ─────────────────────
    protected int x;          // Center X position
    protected int y;          // Center Y position
    protected int width;
    protected int height;
    protected int speed;      // Pixels per frame (positive = moving down)
    protected Color bodyColor;
    protected boolean alive;  // False = should be removed

    // ─── Constructor ──────────────────────────────────────────────────────
    public Vehicle(int x, int y, int width, int height, int speed, Color bodyColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.bodyColor = bodyColor;
        this.alive = true;
    }

    // ─── Abstract Methods (MUST be implemented by subclasses) ─────────────

    /**
     * Each vehicle draws itself. This is POLYMORPHISM in action:
     * the same method name, but different behavior per subclass.
     */
    public abstract void draw(Graphics2D g2d);

    /**
     * Each vehicle updates its own movement logic.
     */
    public abstract void update();

    // ─── Concrete Methods (shared logic for all vehicles) ─────────────────

    /**
     * Returns a Rectangle for collision detection.
     * Hitbox is slightly smaller than the visual for fairness.
     */
    public Rectangle getHitbox() {
        int margin = 6;
        return new Rectangle(
            x - width / 2 + margin,
            y - height / 2 + margin,
            width - margin * 2,
            height - margin * 2
        );
    }

    /**
     * Check if this vehicle collides with another.
     * OOP: We pass in a Vehicle (not PlayerCar specifically) → polymorphism.
     */
    public boolean collidesWith(Vehicle other) {
        return this.getHitbox().intersects(other.getHitbox());
    }

    // ─── Getters & Setters (Encapsulation) ────────────────────────────────

    public int getX()           { return x; }
    public int getY()           { return y; }
    public int getWidth()       { return width; }
    public int getHeight()      { return height; }
    public int getSpeed()       { return speed; }
    public boolean isAlive()    { return alive; }
    public Color getBodyColor() { return bodyColor; }

    public void setX(int x)         { this.x = x; }
    public void setY(int y)         { this.y = y; }
    public void setSpeed(int speed) { this.speed = speed; }
    public void setAlive(boolean alive) { this.alive = alive; }

    /**
     * Helper: draw a car body shape (used by subclasses for shared drawing code).
     * This shows code reuse through inheritance.
     */
    protected void drawCarBody(Graphics2D g2d, Color body, Color window, Color wheel) {
        int left   = x - width / 2;
        int top    = y - height / 2;
        int right  = x + width / 2;
        int bottom = y + height / 2;

        // Shadow
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillRoundRect(left + 4, top + 4, width, height, 12, 12);

        // Car body
        g2d.setColor(body);
        g2d.fillRoundRect(left, top, width, height, 12, 12);

        // Windshield (top)
        g2d.setColor(window);
        g2d.fillRoundRect(left + 5, top + 8, width - 10, height / 3 - 4, 6, 6);

        // Rear window (bottom)
        g2d.setColor(window);
        g2d.fillRoundRect(left + 5, top + height / 2 + 4, width - 10, height / 4 - 2, 6, 6);

        // Body outline
        g2d.setColor(body.darker());
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(left, top, width, height, 12, 12);

        // Wheels
        g2d.setColor(wheel);
        int ww = 8, wh = 14;
        // Front left
        g2d.fillRoundRect(left - 4, top + 8, ww, wh, 3, 3);
        // Front right
        g2d.fillRoundRect(right - 4, top + 8, ww, wh, 3, 3);
        // Rear left
        g2d.fillRoundRect(left - 4, bottom - 8 - wh, ww, wh, 3, 3);
        // Rear right
        g2d.fillRoundRect(right - 4, bottom - 8 - wh, ww, wh, 3, 3);

        // Headlights
        g2d.setColor(new Color(255, 255, 180));
        g2d.fillOval(left + 4, top + 3, 8, 5);
        g2d.fillOval(right - 12, top + 3, 8, 5);

        // Taillights
        g2d.setColor(new Color(255, 60, 60));
        g2d.fillOval(left + 4, bottom - 7, 8, 5);
        g2d.fillOval(right - 12, bottom - 7, 8, 5);
    }
}
