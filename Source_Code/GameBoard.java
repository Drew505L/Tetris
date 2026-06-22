// Class Description: Outlines the grid of the game, which can detect collisions with other blocks and whether the game is over.

package TetrisProgram;

import java.awt.*;

public class GameBoard extends Component {

    // Declare variables for grid along with the color of the pieces
    private final int ROWS;
    private final int COLUMNS;
    private final Color[][] grid;

    // Declares variables for the game over animation
    private boolean gameOverAnimation = false;
    private int redRow = -1;


    public GameBoard(int rows, int columns) {
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.grid = new Color[rows][columns];
    }

    // Check collision
    public boolean isValidPosition(Tetromino piece, int newRow, int newCol) {

        int[][] shape = piece.getShape();

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {

                if (shape[r][c] == 1) {

                    int row = newRow + r;
                    int col = newCol + c;

                    // Out of bounds
                    if (col < 0 || col >= COLUMNS || row >= ROWS) {
                        return false;
                    }

                    // Collision
                    if (row >= 0 && grid[row][col] != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // Lock piece into grid
    public void placePiece(Tetromino piece) {

        int[][] shape = piece.getShape();

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {

                if (shape[r][c] == 1) {
                    int row = piece.getRow() + r;
                    int col = piece.getColumn() + c;

                    if (row >= 0 && row < ROWS && col >= 0 && col < COLUMNS) {
                        grid[row][col] = piece.getColor();
                    }
                }
            }
        }
    }

    // Detects line clearing and how many lines have been cleared
    public int clearLines() {
        int linesCleared = 0;

        for (int row = 0; row < ROWS; row++) {
            boolean full = true;

            // Check if row is full
            for (int col = 0; col < COLUMNS; col++) {
                if (grid[row][col] == null) {
                    full = false;
                    break;
                }
            }

            // If full, clear it
            if (full) {
                removeLine(row);
                linesCleared++;
            }
        }

        return linesCleared;
    }

    // Shift the rows above the cleared line(s) down
    private void removeLine(int rowToRemove) {

        // Move all rows above down
        for (int row = rowToRemove; row > 0; row--) {
            for (int col = 0; col < COLUMNS; col++) {
                grid[row][col] = grid[row - 1][col];
            }
        }

        // Clear the top row
        for (int col = 0; col < COLUMNS; col++) {
            grid[0][col] = null;
        }
    }

    // Detect game over
    public boolean isGameOver(Tetromino piece) {
        return !isValidPosition(piece, piece.getRow(), piece.getColumn());
    }

    // Starts the game over animation by coloring the bottom pieces red and rising to the top of the grid
    public void startGameOverAnimation() {
        gameOverAnimation = true;
        redRow = ROWS - 1;
    }

    public boolean isGameOverAnimation() {
        return gameOverAnimation;
    }

    public void decreaseRedRow() {
        redRow--;
    }

    // Getter
    public Color[][] getGrid() {
        return grid;
    }
    public int getRedRow() {
        return redRow;
    }
}