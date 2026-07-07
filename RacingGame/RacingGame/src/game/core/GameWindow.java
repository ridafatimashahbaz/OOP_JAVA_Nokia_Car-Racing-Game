package game.core;

import javax.swing.*;
import java.awt.*;

/**
 * GAME WINDOW
 * 
 * This class creates the main application window (JFrame).
 * It holds the GamePanel where all drawing/logic happens.
 * 
 * OOP Concept Used:
 *   - Encapsulation: Window setup details are hidden inside this class.
 *   - Composition: GameWindow "has-a" GamePanel.
 */
public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Nokia Racing Car - OOP Java Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Create the main game panel and add it
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        pack(); // Size window to fit gamePanel's preferred size
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);

        // Start the game loop AFTER window is visible
        gamePanel.startGame();
    }
}
