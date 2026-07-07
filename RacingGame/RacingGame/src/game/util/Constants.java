package game.util;

/**
 * GAME CONSTANTS
 * 
 * All constants used across the game are stored here.
 * This avoids "magic numbers" scattered in the code.
 * 
 * OOP Concept Used:
 *   - Utility class with static final fields (constants).
 *   - Single place to change game balance values.
 */
public class Constants {

    // ─── Window & Road Layout ────────────────────────────────────────────
    public static final int WINDOW_WIDTH    = 400;
    public static final int WINDOW_HEIGHT   = 650;

    public static final int ROAD_LEFT       = 80;   // X where road starts
    public static final int ROAD_RIGHT      = 320;  // X where road ends
    public static final int ROAD_WIDTH      = ROAD_RIGHT - ROAD_LEFT; // 240

    // ─── Lanes (3 lanes) ─────────────────────────────────────────────────
    public static final int NUM_LANES       = 3;
    public static final int LANE_WIDTH      = ROAD_WIDTH / NUM_LANES; // 80

    // Center X of each lane
    public static final int[] LANE_CENTERS = {
        ROAD_LEFT + LANE_WIDTH / 2,           // Lane 0 center = 120
        ROAD_LEFT + LANE_WIDTH + LANE_WIDTH / 2, // Lane 1 center = 200
        ROAD_LEFT + 2 * LANE_WIDTH + LANE_WIDTH / 2 // Lane 2 center = 280
    };

    // ─── Car Dimensions ──────────────────────────────────────────────────
    public static final int CAR_WIDTH       = 36;
    public static final int CAR_HEIGHT      = 60;

    public static final int TRUCK_WIDTH     = 40;
    public static final int TRUCK_HEIGHT    = 80;

    // ─── Player Settings ─────────────────────────────────────────────────
    public static final int PLAYER_START_LANE   = 1;    // Middle lane
    public static final int PLAYER_START_Y      = 520;  // Near bottom
    public static final int PLAYER_MIN_SPEED    = 2;
    public static final int PLAYER_MAX_SPEED    = 12;
    public static final int PLAYER_DEFAULT_SPEED = 5;

    // ─── Game Settings ───────────────────────────────────────────────────
    public static final int INITIAL_LIVES       = 3;
    public static final int MAX_LIVES           = 7;
    public static final int TARGET_FPS          = 60;
    public static final long FRAME_TIME_MS      = 1000 / TARGET_FPS;

    // How many km before we speed up traffic
    public static final int DIFFICULTY_INTERVAL = 200;

    // ─── Scoring ─────────────────────────────────────────────────────────
    public static final int KM_PER_FRAME        = 1; // distance added each frame
    public static final int OVERTAKE_BONUS      = 10; // bonus km for overtaking

    // ─── Road Stripe Animation ───────────────────────────────────────────
    public static final int STRIPE_HEIGHT       = 40;
    public static final int STRIPE_GAP          = 40;
    public static final int STRIPE_WIDTH        = 6;
}
