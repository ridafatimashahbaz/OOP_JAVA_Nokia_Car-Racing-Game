package game.core;

import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

/**
 * INPUT HANDLER
 * 
 * Manages all keyboard input for the game.
 * Tracks which keys are currently pressed.
 * 
 * OOP Concepts:
 *   - IMPLEMENTS KeyListener (Java Interface) — this is interface-based polymorphism.
 *   - ENCAPSULATION: Key state is private; accessed via clean getter methods.
 *   - SINGLE RESPONSIBILITY: This class ONLY handles input, nothing else.
 * 
 * How to use:
 *   1. Create an InputHandler.
 *   2. Add it to your JPanel: panel.addKeyListener(inputHandler);
 *   3. Each frame, call inputHandler.isLeftPressed() etc. to check state.
 */
public class InputHandler implements KeyListener {

    // Tracks all keys currently held down
    // We use a Set so we can track multiple keys at once (e.g., up + right)
    private final Set<Integer> pressedKeys = new HashSet<>();

    // Track which keys were "just pressed" this frame (for single-press actions)
    private final Set<Integer> justPressed  = new HashSet<>();
    private final Set<Integer> justReleased = new HashSet<>();

    // ─── Interface Implementation ─────────────────────────────────────────
    // These three methods are REQUIRED by the KeyListener interface.

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (!pressedKeys.contains(code)) {
            justPressed.add(code);  // Only fires on the first press
        }
        pressedKeys.add(code);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        pressedKeys.remove(code);
        justReleased.add(code);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, but must be implemented (interface requirement)
    }

    // ─── Call once per frame to clear "just pressed" state ────────────────

    public void update() {
        justPressed.clear();
        justReleased.clear();
    }

    // ─── Query Methods ────────────────────────────────────────────────────

    /** Is the LEFT arrow key currently held down? */
    public boolean isLeftPressed()  { return pressedKeys.contains(KeyEvent.VK_LEFT); }

    /** Is the RIGHT arrow key currently held down? */
    public boolean isRightPressed() { return pressedKeys.contains(KeyEvent.VK_RIGHT); }

    /** Is the UP arrow key currently held down? */
    public boolean isUpPressed()    { return pressedKeys.contains(KeyEvent.VK_UP); }

    /** Is the DOWN arrow key currently held down? */
    public boolean isDownPressed()  { return pressedKeys.contains(KeyEvent.VK_DOWN); }

    /** Was ENTER just pressed this frame? */
    public boolean isEnterJustPressed() { return justPressed.contains(KeyEvent.VK_ENTER); }

    /** Was SPACE just pressed this frame? */
    public boolean isSpaceJustPressed() { return justPressed.contains(KeyEvent.VK_SPACE); }

    /** Was LEFT just pressed (single press, not held)? */
    public boolean isLeftJustPressed()  { return justPressed.contains(KeyEvent.VK_LEFT); }

    /** Was RIGHT just pressed (single press, not held)? */
    public boolean isRightJustPressed() { return justPressed.contains(KeyEvent.VK_RIGHT); }

    /** Was UP just pressed (single press, not held)? */
    public boolean isUpJustPressed()    { return justPressed.contains(KeyEvent.VK_UP); }

    /** Was DOWN just pressed (single press, not held)? */
    public boolean isDownJustPressed()  { return justPressed.contains(KeyEvent.VK_DOWN); }

    /** Is any key in pressedKeys? */
    public boolean isAnyKeyPressed()    { return !pressedKeys.isEmpty(); }

    /** Get the set of currently pressed keys (for HUD display) */
    public Set<Integer> getPressedKeys() { return pressedKeys; }
}
