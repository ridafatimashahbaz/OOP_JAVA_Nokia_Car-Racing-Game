package game;

import game.core.GameWindow;

/**
 * MAIN ENTRY POINT
 * 
 * This is where the program starts. It simply creates the GameWindow.
 * 
 * OOP Concept Used: Single Responsibility - Main only starts the app.
 */
public class Main {

    public static void main(String[] args) {
        // Create and show the game window on the Event Dispatch Thread (EDT)
        // This is the correct way to start a Swing application
        javax.swing.SwingUtilities.invokeLater(() -> {
            new GameWindow();
        });
    }
}
