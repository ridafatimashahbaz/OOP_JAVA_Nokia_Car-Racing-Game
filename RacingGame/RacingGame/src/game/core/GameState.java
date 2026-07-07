package game.core;

/**
 * GAME STATE ENUM
 * 
 * Represents which "screen" or "mode" the game is currently in.
 * 
 * OOP Concept:
 *   - ENUM: A special class with a fixed set of named constants.
 *   - This replaces magic numbers like: state = 0 (menu), state = 1 (playing)
 *     with readable names: GameState.MENU, GameState.PLAYING
 * 
 * The GamePanel uses this to decide what to draw and update each frame.
 */
public enum GameState {

    /** The main menu / start screen */
    MENU,

    /** Countdown before game starts (3... 2... 1... GO!) */
    COUNTDOWN,

    /** Game is actively running */
    PLAYING,

    /** Player just crashed, brief pause */
    CRASHED,

    /** Game over — no lives left */
    GAME_OVER,

    /** Player paused the game */
    PAUSED
}
