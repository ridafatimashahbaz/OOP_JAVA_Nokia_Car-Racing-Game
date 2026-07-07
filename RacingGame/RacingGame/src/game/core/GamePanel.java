package game.core;

import game.entities.*;
import game.ui.*;
import game.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * GAME PANEL — THE HEART OF THE GAME
 * 
 * This class is the main JPanel. It contains:
 *   1. The Game Loop (update + draw every frame)
 *   2. All major game objects (player, traffic, road, HUD)
 *   3. Game state management (menu → countdown → playing → crashed → game over)
 * 
 * OOP Concepts:
 *   - EXTENDS JPanel: GamePanel IS-A JPanel (inheritance from Java library).
 *   - IMPLEMENTS Runnable: So it can run the game loop on a Thread.
 *   - COMPOSITION: GamePanel "has-a" PlayerCar, TrafficManager, HUD, etc.
 *   - POLYMORPHISM: Uses Vehicle (parent type) for enemy list.
 * 
 * GAME LOOP PATTERN:
 *   The game loop runs 60 times per second:
 *     1. processInput()  — check what keys are pressed
 *     2. update()        — move everything, check collisions
 *     3. render()        — draw everything to screen
 */
public class GamePanel extends JPanel implements Runnable {

    // ─── Game Objects (Composition) ───────────────────────────────────────
    private PlayerCar      player;
    private TrafficManager trafficManager;
    private RoadRenderer   roadRenderer;
    private HUD            hud;
    private ScreenRenderer screenRenderer;
    private InputHandler   inputHandler;

    // ─── Game State ───────────────────────────────────────────────────────
    private GameState state;
    private long      distanceKm;      // Total KMs traveled
    private int       lives;
    private int       animTick;        // General animation counter

    // ─── Countdown ────────────────────────────────────────────────────────
    private int countdownValue;        // 3, 2, 1, 0 (GO!)
    private int countdownTimer;
    private int countdownAlpha;

    // ─── Crash State ─────────────────────────────────────────────────────
    private int crashTimer;
    private int crashAlpha;

    // ─── Explosion Effects ────────────────────────────────────────────────
    private List<Explosion> explosions;

    // ─── Double Buffering ─────────────────────────────────────────────────
    // We draw to an offscreen image first, then copy to screen.
    // This prevents flickering.
    private BufferedImage offscreenBuffer;

    // ─── Game Thread ──────────────────────────────────────────────────────
    private Thread gameThread;
    private boolean running;

    // ─── Pause ────────────────────────────────────────────────────────────
    private boolean paused;

    // ─── Constructor ─────────────────────────────────────────────────────

    public GamePanel() {
        setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);         // Required to receive keyboard events

        // Create input handler and register it
        inputHandler = new InputHandler();
        addKeyListener(inputHandler);

        // Create all game subsystems
        roadRenderer   = new RoadRenderer();
        hud            = new HUD();
        screenRenderer = new ScreenRenderer();
        explosions     = new ArrayList<>();

        // Initialize game to MENU state
        initGame();
        state = GameState.MENU;
    }

    /**
     * Initialize (or reset) all game variables.
     * Called at start and whenever the player restarts.
     */
    private void initGame() {
        player         = new PlayerCar();
        trafficManager = new TrafficManager();
        distanceKm     = 0;
        lives          = Constants.INITIAL_LIVES;
        animTick       = 0;
        paused         = false;
        explosions.clear();
    }

    // ─── Start Game Loop ─────────────────────────────────────────────────

    /**
     * Called by GameWindow after the window is visible.
     * Starts the game thread.
     */
    public void startGame() {
        running    = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // ─── Runnable Interface ───────────────────────────────────────────────

    /**
     * THE GAME LOOP.
     * This method runs forever (while running == true) on its own thread.
     * Each iteration is one "frame".
     */
    @Override
    public void run() {
        // Create the offscreen buffer once
        offscreenBuffer = new BufferedImage(
            Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT,
            BufferedImage.TYPE_INT_ARGB
        );

        while (running) {
            long frameStart = System.currentTimeMillis();

            // ── The Three Steps of Every Game Frame ──────────────────────
            processInput();   // Step 1: Check keyboard
            update();         // Step 2: Update game logic
            render();         // Step 3: Draw everything
            // ─────────────────────────────────────────────────────────────

            // Wait for remainder of frame time (to maintain 60 FPS)
            long elapsed = System.currentTimeMillis() - frameStart;
            long sleep   = Constants.FRAME_TIME_MS - elapsed;
            if (sleep > 0) {
                try { Thread.sleep(sleep); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }
    }

    // ─── Step 1: Process Input ───────────────────────────────────────────

    /**
     * Read keyboard input and act on it based on current game state.
     */
    private void processInput() {
        // Always: pause/unpause with P
        if (isKeyJustPressed(java.awt.event.KeyEvent.VK_P) && state == GameState.PLAYING) {
            paused = !paused;
        }

        switch (state) {
            case MENU:
                if (inputHandler.isEnterJustPressed()) {
                    startCountdown();
                }
                break;

            case PLAYING:
                if (!paused) {
                    // Lane changes (single key press, not held)
                    if (inputHandler.isLeftJustPressed())  player.moveLeft();
                    if (inputHandler.isRightJustPressed()) player.moveRight();

                    // Speed changes (held keys — continuously adjust)
                    if (inputHandler.isUpPressed())   player.accelerate();
                    if (inputHandler.isDownPressed())  player.decelerate();
                }
                break;

            case GAME_OVER:
                if (inputHandler.isEnterJustPressed()) {
                    initGame();
                    startCountdown();
                }
                break;

            case CRASHED:
                // No input during crash animation
                break;

            default:
                break;
        }

        // Update the input handler (clears "just pressed" state)
        inputHandler.update();
    }

    /**
     * Shortcut to check if a specific key was just pressed this frame.
     */
    private boolean isKeyJustPressed(int keyCode) {
        return inputHandler.getPressedKeys().contains(keyCode)
               && !paused; // handled separately
    }

    // ─── Countdown Logic ────────────────────────────────────────────────

    private void startCountdown() {
        state          = GameState.COUNTDOWN;
        countdownValue = 3;
        countdownTimer = 0;
        countdownAlpha = 255;
    }

    // ─── Step 2: Update ──────────────────────────────────────────────────

    /**
     * Update all game logic for this frame.
     */
    private void update() {
        animTick++;

        switch (state) {
            case MENU:
                roadRenderer.update(3); // Slow scroll on menu
                break;

            case COUNTDOWN:
                updateCountdown();
                roadRenderer.update(2);
                break;

            case PLAYING:
                if (!paused) updatePlaying();
                break;

            case CRASHED:
                updateCrashed();
                break;

            case GAME_OVER:
                // Just animate background
                roadRenderer.update(2);
                break;
        }

        // Update explosions (always, even between states)
        explosions.removeIf(ex -> {
            ex.update();
            return ex.isDone();
        });
    }

    /**
     * Update countdown: tick the timer, change number, then start game.
     */
    private void updateCountdown() {
        countdownTimer++;
        // Fade out alpha
        countdownAlpha = Math.max(0, 255 - (countdownTimer % 60) * 5);

        if (countdownTimer >= 60) { // One second per number
            countdownTimer = 0;
            countdownValue--;
            if (countdownValue < 0) {
                state = GameState.PLAYING; // Start!
            }
        }
    }

    /**
     * Core gameplay update: move player, spawn traffic, check collisions, score.
     */
    private void updatePlaying() {
        // Update road scroll
        roadRenderer.update(player.getSpeed());

        // Update player
        player.update();

        // Update traffic
        trafficManager.update(player.getSpeed(), distanceKm);

        // Add distance (faster speed = more distance per frame)
        distanceKm += player.getSpeed() / 3;
        if (player.getSpeed() > 0) distanceKm++; // Minimum 1 per frame

        // Check collision
        if (!player.isInvincible() && trafficManager.checkCollision(player)) {
            handleCrash();
        }
    }

    /**
     * Handle a crash event.
     */
    private void handleCrash() {
        lives--;

        // Spawn explosion at player position
        explosions.add(new Explosion(player.getX(), player.getY()));

        if (lives <= 0) {
            state = GameState.GAME_OVER;
        } else {
            state      = GameState.CRASHED;
            crashTimer = 0;
            crashAlpha = 255;
            player.crash(); // Player resets speed and becomes invincible
        }
    }

    /**
     * Update the brief crash pause state.
     */
    private void updateCrashed() {
        crashTimer++;
        crashAlpha = Math.max(0, 255 - crashTimer * 4);

        // After ~1.5 seconds, resume playing
        if (crashTimer >= 90) {
            state = GameState.PLAYING;
        }
    }

    // ─── Step 3: Render ──────────────────────────────────────────────────

    /**
     * Draw everything to the offscreen buffer, then paint to screen.
     * Using double-buffering prevents flickering.
     */
    private void render() {
        // Get Graphics2D from the offscreen buffer
        Graphics2D g2d = offscreenBuffer.createGraphics();

        // Enable antialiasing for smooth graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // ── Draw the road and background (always visible) ──────────────
        roadRenderer.draw(g2d);

        // ── Draw based on state ────────────────────────────────────────
        switch (state) {
            case MENU:
                screenRenderer.drawMenu(g2d, animTick);
                break;

            case COUNTDOWN:
                // Draw traffic and player during countdown for style
                trafficManager.drawAll(g2d);
                player.draw(g2d);
                // Then draw countdown overlay
                screenRenderer.drawCountdown(g2d, countdownValue, countdownAlpha);
                break;

            case PLAYING:
            case CRASHED:
                // Draw all traffic vehicles
                trafficManager.drawAll(g2d);

                // Draw player
                player.draw(g2d);

                // Draw explosions
                for (Explosion ex : explosions) {
                    ex.draw(g2d);
                }

                // Draw HUD (always on top of gameplay)
                hud.draw(g2d, distanceKm, lives, player.getSpeed(),
                         inputHandler.getPressedKeys(), paused);

                // Draw crash message if in crashed state
                if (state == GameState.CRASHED) {
                    screenRenderer.drawCrashMessage(g2d, crashAlpha);
                }
                break;

            case GAME_OVER:
                screenRenderer.drawGameOver(g2d, distanceKm, animTick);
                break;
        }

        g2d.dispose(); // Always release graphics resources!

        // Copy the buffer to the actual screen
        Graphics screenG = getGraphics();
        if (screenG != null) {
            screenG.drawImage(offscreenBuffer, 0, 0, null);
            screenG.dispose();
        }
    }
}
