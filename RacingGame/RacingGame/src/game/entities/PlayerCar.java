package game.entities;

import game.util.Constants;
import java.awt.*;

/**
 * PLAYER CAR
 * 
 * The car controlled by the user. Always red.
 * 
 * OOP Concepts:
 *   - INHERITANCE: extends Vehicle (gets all Vehicle fields and methods).
 *   - ENCAPSULATION: Lane management is handled internally.
 *   - POLYMORPHISM: Overrides draw() and update() from Vehicle.
 */
public class PlayerCar extends Vehicle {

    private int currentLane;     // Which lane (0, 1, 2)
    private int targetX;         // Where the car is sliding to (lane change animation)
    private boolean isSliding;   // Is car mid-lane-change?

    // Invincibility frames after a crash (so player isn't hit twice instantly)
    private int invincibleFrames;

    // Exhaust particle effect timer
    private int exhaustTimer;

    public PlayerCar() {
        // Call parent constructor: position at center lane, near bottom
        super(
            Constants.LANE_CENTERS[Constants.PLAYER_START_LANE],
            Constants.PLAYER_START_Y,
            Constants.CAR_WIDTH,
            Constants.CAR_HEIGHT,
            Constants.PLAYER_DEFAULT_SPEED,
            Color.RED
        );
        this.currentLane = Constants.PLAYER_START_LANE;
        this.targetX = x;
        this.isSliding = false;
        this.invincibleFrames = 0;
    }

    // ─── Move Left / Right ────────────────────────────────────────────────

    /**
     * Move one lane to the left (if possible).
     */
    public void moveLeft() {
        if (currentLane > 0 && !isSliding) {
            currentLane--;
            targetX = Constants.LANE_CENTERS[currentLane];
            isSliding = true;
        }
    }

    /**
     * Move one lane to the right (if possible).
     */
    public void moveRight() {
        if (currentLane < Constants.NUM_LANES - 1 && !isSliding) {
            currentLane++;
            targetX = Constants.LANE_CENTERS[currentLane];
            isSliding = true;
        }
    }

    // ─── Speed Control ────────────────────────────────────────────────────

    public void accelerate() {
        if (speed < Constants.PLAYER_MAX_SPEED) {
            speed++;
        }
    }

    public void decelerate() {
        if (speed > Constants.PLAYER_MIN_SPEED) {
            speed--;
        }
    }

    // ─── Collision Handling ───────────────────────────────────────────────

    /**
     * Called when the player crashes. Returns lives remaining.
     */
    public void crash() {
        invincibleFrames = 90; // ~1.5 seconds of invincibility
        speed = Constants.PLAYER_DEFAULT_SPEED; // Reset speed
    }

    public boolean isInvincible() {
        return invincibleFrames > 0;
    }

    // ─── Override: Update ─────────────────────────────────────────────────

    @Override
    public void update() {
        // Smooth lane-change sliding animation
        if (isSliding) {
            int dx = targetX - x;
            if (Math.abs(dx) <= 6) {
                x = targetX; // Snap to lane center
                isSliding = false;
            } else {
                x += dx / 4; // Move 25% of remaining distance each frame (easing)
            }
        }

        // Count down invincibility
        if (invincibleFrames > 0) {
            invincibleFrames--;
        }

        exhaustTimer++;
    }

    // ─── Override: Draw ──────────────────────────────────────────────────

    @Override
    public void draw(Graphics2D g2d) {
        // Blink effect when invincible
        if (invincibleFrames > 0 && (invincibleFrames / 6) % 2 == 0) {
            return; // Skip drawing = blink
        }

        // Draw exhaust smoke behind car
        drawExhaust(g2d);

        // Draw the red player car using the parent's shared helper
        Color windowColor = new Color(150, 220, 255, 200);
        Color wheelColor  = new Color(30, 30, 30);
        drawCarBody(g2d, bodyColor, windowColor, wheelColor);

        // Draw a small "PLAYER" indicator on top
        g2d.setColor(new Color(255, 255, 100));
        g2d.setFont(new Font("Arial", Font.BOLD, 8));
        g2d.drawString("YOU", x - 8, y - height / 2 - 4);
    }

    /**
     * Draw small exhaust smoke circles behind the car.
     */
    private void drawExhaust(Graphics2D g2d) {
        int baseX = x;
        int baseY = y + height / 2 + 4;
        for (int i = 0; i < 3; i++) {
            int offset = (exhaustTimer + i * 10) % 20;
            int alpha  = 180 - offset * 8;
            if (alpha < 0) alpha = 0;
            int size = 4 + i * 2;
            g2d.setColor(new Color(180, 180, 180, alpha));
            g2d.fillOval(baseX - size / 2 + (i % 2 == 0 ? -6 : 6),
                         baseY + offset, size, size);
        }
    }

    // ─── Getters ─────────────────────────────────────────────────────────

    public int getCurrentLane()     { return currentLane; }
    public boolean isSliding()      { return isSliding; }
    public int getInvincibleFrames(){ return invincibleFrames; }
}
