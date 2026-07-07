package game.core;

import game.entities.*;
import game.util.Constants;
import java.util.*;

/**
 * TRAFFIC MANAGER
 * 
 * Responsible for creating and managing all enemy vehicles.
 * Spawns cars and trucks, adjusts difficulty over time.
 * 
 * OOP Concepts:
 *   - ENCAPSULATION: All traffic logic is in one place, hidden from GamePanel.
 *   - COMPOSITION: TrafficManager "has-a" list of Vehicles.
 *   - POLYMORPHISM: The vehicles list holds both EnemyCar AND Truck objects.
 *     When we call vehicle.update() or vehicle.draw(), Java automatically
 *     calls the correct version for each subclass. THIS IS POLYMORPHISM!
 */
public class TrafficManager {

    // ─── Vehicle List ─────────────────────────────────────────────────────
    // We store Vehicle (parent type) but it holds EnemyCar and Truck objects.
    // This is "programming to the interface" — a core OOP principle.
    private List<Vehicle> vehicles;

    // ─── Spawn Settings ───────────────────────────────────────────────────
    private int spawnTimer;
    private int spawnInterval;    // Frames between spawns
    private int baseEnemySpeed;   // Base speed for traffic

    // Track which lanes were recently used to avoid cluster spawning
    private int lastSpawnLane;
    private static final Random random = new Random();

    // For overtake tracking
    private List<Vehicle> passedVehicles;

    public TrafficManager() {
        vehicles       = new ArrayList<>();
        passedVehicles = new ArrayList<>();
        spawnTimer     = 0;
        spawnInterval  = 80;   // Start with spawning every 80 frames
        baseEnemySpeed = 3;
        lastSpawnLane  = -1;
    }

    // ─── Update ──────────────────────────────────────────────────────────

    /**
     * Update all vehicles and handle spawning.
     * @param playerSpeed  Player's current speed (affects relative traffic speed)
     * @param distanceKm   Total distance traveled (for difficulty scaling)
     * @return Number of vehicles the player has just overtaken (for bonus score)
     */
    public int update(int playerSpeed, long distanceKm) {
        // Increase difficulty every DIFFICULTY_INTERVAL km
        updateDifficulty(distanceKm);

        // Spawn new vehicles
        spawnTimer++;
        if (spawnTimer >= spawnInterval) {
            spawnVehicle(playerSpeed);
            spawnTimer = 0;
        }

        // Update all vehicles (POLYMORPHISM: calls EnemyCar.update() or Truck.update())
        int overtakeCount = 0;
        Iterator<Vehicle> it = vehicles.iterator();
        while (it.hasNext()) {
            Vehicle v = it.next();
            v.update();

            if (!v.isAlive()) {
                it.remove(); // Remove vehicles that went off screen
            }
        }

        return overtakeCount;
    }

    /**
     * Scale difficulty based on distance traveled.
     */
    private void updateDifficulty(long distanceKm) {
        int level = (int)(distanceKm / Constants.DIFFICULTY_INTERVAL);
        // Cap at level 8
        if (level > 8) level = 8;

        // Decrease spawn interval (faster spawning)
        spawnInterval  = Math.max(35, 80 - level * 6);
        // Increase enemy speed
        baseEnemySpeed = Math.min(8, 3 + level);
    }

    /**
     * Spawn a new vehicle in a random lane.
     * Randomly decides: car or truck?
     */
    private void spawnVehicle(int playerSpeed) {
        // Pick a lane different from the last spawn
        int lane;
        do {
            lane = random.nextInt(Constants.NUM_LANES);
        } while (lane == lastSpawnLane && Constants.NUM_LANES > 1);
        lastSpawnLane = lane;

        // Speed of this vehicle (relative to player but independent)
        int speed = baseEnemySpeed + random.nextInt(3);

        // 25% chance of spawning a truck instead of a car
        Vehicle newVehicle;
        if (random.nextInt(4) == 0) {
            newVehicle = new Truck(lane, Math.max(1, speed - 1)); // Trucks are slower
        } else {
            newVehicle = new EnemyCar(lane, speed);
        }

        // Only add if there's enough gap (no overlap with existing vehicles in same lane)
        if (isSafeTospawn(newVehicle, lane)) {
            vehicles.add(newVehicle);
        }
    }

    /**
     * Check if there's enough vertical space in this lane to spawn.
     */
    private boolean isSafeTospawn(Vehicle newV, int lane) {
        for (Vehicle v : vehicles) {
            // Check if same lane and too close to top of screen
            int vLane = (v instanceof EnemyCar) ? ((EnemyCar) v).getLane()
                      : (v instanceof Truck)     ? ((Truck) v).getLane()
                      : -1;
            if (vLane == lane && v.getY() < 100) {
                return false; // Too close to previous car in this lane
            }
        }
        return true;
    }

    // ─── Draw ────────────────────────────────────────────────────────────

    /**
     * Draw all vehicles.
     * POLYMORPHISM: draw() calls EnemyCar.draw() or Truck.draw() automatically!
     */
    public void drawAll(java.awt.Graphics2D g2d) {
        for (Vehicle v : vehicles) {
            v.draw(g2d); // Java picks the right draw() for each subclass
        }
    }

    // ─── Collision Check ─────────────────────────────────────────────────

    /**
     * Check if the player collides with any traffic vehicle.
     * @param player The player's car
     * @return true if collision detected
     */
    public boolean checkCollision(game.entities.PlayerCar player) {
        for (Vehicle v : vehicles) {
            if (player.collidesWith(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reset all traffic (used when player respawns or game restarts).
     */
    public void reset() {
        vehicles.clear();
        spawnTimer     = 0;
        spawnInterval  = 80;
        baseEnemySpeed = 3;
        lastSpawnLane  = -1;
    }

    // ─── Getters ─────────────────────────────────────────────────────────

    public List<Vehicle> getVehicles() { return vehicles; }
    public int getVehicleCount()       { return vehicles.size(); }
}
