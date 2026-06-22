// Class Description: Stores the controls for the program, this program will use keyboard controls.

package TetrisProgram;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controls extends KeyAdapter {

    // Declares the grid variable that displays and manages the Tetris grid, which all piece movements will be performed
    private final ClassicModePage.GridPanel grid;

    // Declares the page variable, which is used for the key visualizer
    private final ClassicModePage page;

    // Constructor: Initializes reference to the grid and page, and sets up keyboard inputs
    public Controls(ClassicModePage.GridPanel grid, ClassicModePage page) {
        this.grid = grid;
        this.page = page;

        // Sets up the keyboard input into the grid
        grid.addKeyListener(this);
        grid.setFocusable(true);
        grid.requestFocusInWindow();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        // Declares a variable to read key inputs
        int key = e.getKeyCode();

        // Check if the countdown or game over is occurring
        // If so, block any input
        if (grid.isCountdownActive() || grid.getGameEngine().isGameOver()) {
            return;
        }

        // Update key visualizer
        page.setKeyState(key, true);

        // Executes the corresponding Tetris action based on which key was pressed
        switch (key) {
            case KeyEvent.VK_LEFT -> grid.movePieceLeft();
            case KeyEvent.VK_RIGHT -> grid.movePieceRight();
            case KeyEvent.VK_UP -> grid.rotatePieceClockwise();
            case KeyEvent.VK_Z -> grid.rotatePieceCounterClockwise();
            case KeyEvent.VK_DOWN -> grid.softDrop();
            case KeyEvent.VK_SPACE -> grid.hardDrop();
            case KeyEvent.VK_C -> grid.holdPiece();
            case KeyEvent.VK_P -> grid.togglePause();
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {

        // Keeps track when a key is released for the key visualizer
        page.setKeyState(e.getKeyCode(), false);
    }
}