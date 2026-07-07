package game.entities;

import java.awt.*;
import java.util.Random;

/**
 * EXPLOSION
 * 
 * Visual explosion effect when the player crashes.
 * Not a vehicle, but still an entity in the game world.
 * 
 * OOP Concepts:
 *   - ENCAPSULATION: All explosion state managed internally.
 *   - Uses an array of Particle inner objects (composition).
 */
public class Explosion {

    // ─── Inner Class: Particle ────────────────────────────────────────────
    // This is a "nested class" — Particle only makes sense inside Explosion

    private static class Particle {
        float x, y;      // Position
        float vx, vy;    // Velocity
        float alpha;      // Transparency (1.0 = solid, 0.0 = invisible)
        Color color;
        int size;

        Particle(float x, float y, Random random) {
            this.x = x;
            this.y = y;
            // Random direction
            double angle = random.nextDouble() * Math.PI * 2;
            float spd = 2 + random.nextFloat() * 5;
            this.vx    = (float)(Math.cos(angle) * spd);
            this.vy    = (float)(Math.sin(angle) * spd);
            this.alpha = 1.0f;
            this.size  = 4 + random.nextInt(8);

            // Colors: fire palette
            int type = random.nextInt(3);
            if (type == 0)      this.color = new Color(255, 60, 20);   // Red
            else if (type == 1) this.color = new Color(255, 180, 0);   // Orange
            else                this.color = new Color(255, 255, 100); // Yellow
        }

        void update() {
            x     += vx;
            y     += vy;
            vy    += 0.15f;   // Gravity
            alpha -= 0.03f;   // Fade out
            size  = Math.max(1, size - 1);
        }

        boolean isDead() { return alpha <= 0; }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(
                color.getRed(), color.getGreen(), color.getBlue(),
                Math.max(0, (int)(alpha * 255))
            ));
            g2d.fillOval((int)x - size / 2, (int)y - size / 2, size, size);
        }
    }

    // ─── Explosion Fields ─────────────────────────────────────────────────

    private Particle[] particles;
    private boolean done;
    private static final Random random = new Random();

    public Explosion(int x, int y) {
        particles = new Particle[40];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(x, y, random);
        }
        done = false;
    }

    public void update() {
        boolean anyAlive = false;
        for (Particle p : particles) {
            if (!p.isDead()) {
                p.update();
                anyAlive = true;
            }
        }
        if (!anyAlive) done = true;
    }

    public void draw(Graphics2D g2d) {
        for (Particle p : particles) {
            if (!p.isDead()) {
                p.draw(g2d);
            }
        }
    }

    public boolean isDone() { return done; }
}
