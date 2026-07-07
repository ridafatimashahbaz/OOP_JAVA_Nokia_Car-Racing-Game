package game.ui;

import game.util.Constants;
import java.awt.*;
import java.awt.geom.*;

/**
 * ROAD RENDERER
 * 
 * Handles all drawing of the road, background scenery, and lane markings.
 * The road stripe animation creates the illusion of movement.
 * 
 * OOP Concepts:
 *   - ENCAPSULATION: All road-drawing logic is in one class.
 *   - SINGLE RESPONSIBILITY: This class only draws the road, nothing else.
 *   - The scrollOffset simulates car movement (road moves down, car stays still).
 */
public class RoadRenderer {

    private float scrollOffset;   // How far the road has scrolled (pixels)
    private float scrollSpeed;    // How fast road scrolls = player's visual speed

    public RoadRenderer() {
        scrollOffset = 0;
        scrollSpeed  = Constants.PLAYER_DEFAULT_SPEED;
    }

    /**
     * Update the scroll position each frame.
     * @param playerSpeed Current player speed
     */
    public void update(int playerSpeed) {
        scrollSpeed   = playerSpeed * 1.5f;
        scrollOffset += scrollSpeed;
        // Keep offset within one full stripe period
        float totalPeriod = Constants.STRIPE_HEIGHT + Constants.STRIPE_GAP;
        if (scrollOffset >= totalPeriod) {
            scrollOffset -= totalPeriod;
        }
    }

    /**
     * Draw everything: sky, buildings, road, and lane markings.
     * Call this first, before drawing any vehicles.
     */
    public void draw(Graphics2D g2d) {
        drawBackground(g2d);
        drawRoad(g2d);
        drawLaneMarkings(g2d);
        drawRoadEdges(g2d);
    }

    // ─── Private Drawing Methods ──────────────────────────────────────────

    /**
     * Draw the sky and background scenery (trees / buildings).
     */
    private void drawBackground(Graphics2D g2d) {
        // Sky gradient
        GradientPaint sky = new GradientPaint(
            0, 0, new Color(20, 30, 60),
            0, Constants.WINDOW_HEIGHT, new Color(40, 50, 80)
        );
        g2d.setPaint(sky);
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // Left scenery (trees)
        drawSceneryLeft(g2d);

        // Right scenery (trees)
        drawSceneryRight(g2d);
    }

    /**
     * Draw trees / bushes on the left side of the road.
     */
    private void drawSceneryLeft(Graphics2D g2d) {
        int sideW = Constants.ROAD_LEFT;  // Width of left side = 80

        // Ground
        g2d.setColor(new Color(30, 80, 30));
        g2d.fillRect(0, 0, sideW, Constants.WINDOW_HEIGHT);

        // Simple scrolling trees
        float treePeriod = 120;
        for (int ty = (int)(-scrollOffset % treePeriod) - (int)treePeriod;
             ty < Constants.WINDOW_HEIGHT + (int)treePeriod;
             ty += (int)treePeriod) {

            drawTree(g2d, sideW / 2 - 10, ty + 30);
            drawTree(g2d, sideW / 2 + 15, ty + 80);
        }
    }

    /**
     * Draw trees on the right side of the road.
     */
    private void drawSceneryRight(Graphics2D g2d) {
        int startX = Constants.ROAD_RIGHT;
        int sideW  = Constants.WINDOW_WIDTH - startX; // = 80

        // Ground
        g2d.setColor(new Color(30, 80, 30));
        g2d.fillRect(startX, 0, sideW, Constants.WINDOW_HEIGHT);

        float treePeriod = 120;
        for (int ty = (int)(-scrollOffset % treePeriod) - (int)treePeriod;
             ty < Constants.WINDOW_HEIGHT + (int)treePeriod;
             ty += (int)treePeriod) {

            drawTree(g2d, startX + sideW / 2 - 5, ty + 60);
            drawTree(g2d, startX + sideW / 2 + 10, ty + 20);
        }
    }

    /**
     * Draw a simple tree at a position.
     */
    private void drawTree(Graphics2D g2d, int tx, int ty) {
        // Trunk
        g2d.setColor(new Color(100, 60, 20));
        g2d.fillRect(tx - 3, ty, 6, 18);

        // Canopy (triangle-ish)
        g2d.setColor(new Color(20, 120, 20));
        int[] xp = { tx, tx - 14, tx + 14 };
        int[] yp = { ty - 20, ty + 2, ty + 2 };
        g2d.fillPolygon(xp, yp, 3);

        // Highlight
        g2d.setColor(new Color(60, 160, 60));
        int[] xh = { tx - 2, tx - 8, tx + 4 };
        int[] yh = { ty - 18, ty, ty };
        g2d.fillPolygon(xh, yh, 3);
    }

    /**
     * Draw the dark asphalt road surface.
     */
    private void drawRoad(Graphics2D g2d) {
        // Road base (dark asphalt)
        g2d.setColor(new Color(50, 50, 55));
        g2d.fillRect(Constants.ROAD_LEFT, 0,
                     Constants.ROAD_WIDTH, Constants.WINDOW_HEIGHT);

        // Slight gradient to give depth
        GradientPaint roadShade = new GradientPaint(
            Constants.ROAD_LEFT, 0, new Color(45, 45, 50),
            Constants.ROAD_RIGHT, 0, new Color(60, 60, 65)
        );
        g2d.setPaint(roadShade);
        g2d.fillRect(Constants.ROAD_LEFT, 0,
                     Constants.ROAD_WIDTH, Constants.WINDOW_HEIGHT);
    }

    /**
     * Draw animated dashed lane markings.
     * The scrollOffset makes the stripes appear to move downward.
     */
    private void drawLaneMarkings(Graphics2D g2d) {
        g2d.setColor(new Color(220, 220, 100)); // Yellow-white dashes
        g2d.setStroke(new BasicStroke(3.0f));

        int totalPeriod = Constants.STRIPE_HEIGHT + Constants.STRIPE_GAP;

        // Draw dashes for each lane divider (2 dividers for 3 lanes)
        for (int lane = 1; lane < Constants.NUM_LANES; lane++) {
            int divX = Constants.ROAD_LEFT + lane * Constants.LANE_WIDTH;

            // Draw stripes from above screen to below, offset by scroll
            for (int sy = (int)scrollOffset - totalPeriod;
                 sy < Constants.WINDOW_HEIGHT + totalPeriod;
                 sy += totalPeriod) {

                g2d.drawLine(divX, sy, divX, sy + Constants.STRIPE_HEIGHT);
            }
        }
    }

    /**
     * Draw yellow border lines on the edges of the road.
     */
    private void drawRoadEdges(Graphics2D g2d) {
        g2d.setColor(new Color(255, 220, 50));
        g2d.setStroke(new BasicStroke(4.0f));
        g2d.drawLine(Constants.ROAD_LEFT,  0, Constants.ROAD_LEFT,  Constants.WINDOW_HEIGHT);
        g2d.drawLine(Constants.ROAD_RIGHT, 0, Constants.ROAD_RIGHT, Constants.WINDOW_HEIGHT);
    }

    // ─── Getter ───────────────────────────────────────────────────────────

    public float getScrollOffset() { return scrollOffset; }
}
