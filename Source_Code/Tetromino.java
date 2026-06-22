// Class Description: Stores all the different tetris pieces that are used in the program.

package TetrisProgram;

import java.awt.*;

public class Tetromino {

    // Initialize the shape of the pieces (1 = filled, 0 = empty)
    private int[][] shape;

    // Initialize the position of the shape on the grid
    private int row;
    private int column;

    // Initialize the color of the piece
    private final Color color;

    // Initialize the type of piece
    private final String type;

    // Initialize the state of the rotation of a piece
    private int rotationState = 0;

    // Stores the original shape of the Tetris piece when using a held piece
    private final int[][] originalShape;

    // Constructor
    public Tetromino(int[][] shape, Color color, String type) {
        this.shape = shape;
        this.color = color;
        this.type = type;

        // Deep copy for original shape
        this.originalShape = new int[shape.length][shape[0].length];
        for (int i = 0; i < shape.length; i++) {
            System.arraycopy(shape[i], 0, originalShape[i], 0, shape[i].length);
        }

        // Sets default spawn row and column on the board
        this.row = 0;
        this.column = 3;
    }

    // Getters
    public int[][] getShape() {
        return shape;
    }
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
    public Color getColor() {
        return color;
    }
    public String getType() {
        return type;
    }
    public int getRotationState() {
        return rotationState;
    }

    // Setter
    public void setPosition(int row, int col) {
        this.row = row;
        this.column = col;
    }

    // Movement
    public void moveDown() {
        row++;
    }
    public void moveLeft() {
        column--;
    }
    public void moveRight() {
        column++;
    }

    // Clockwise rotation
    public void clockwiseRotation() {

        // Gets the current dimensions
        int rows = shape.length;
        int columns = shape[0].length;

        // Creates a new rotated array for the peaces
        int [][] rotated = new int[columns][rows];

        // Rotates the shape
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                rotated[c][rows - 1 - r] = shape[r][c];
            }
        }

        // Replaces the old shape with the new shape
        shape = rotated;
    }

    // Implements SRS
    public void rotateClockwise() {
        clockwiseRotation();
        rotationState = (rotationState + 1) % 4;
    }

    public void rotateCounterClockwise() {
        for (int i = 0; i < 3; i++) {
            clockwiseRotation();
        }
        rotationState = (rotationState + 3) % 4;
    }

    // Resets the piece to its default spawn state
    public void resetState() {
        this.row = 0;
        this.column = 3;
        this.rotationState = 0;

        // Restore original shape
        int[][] newShape = new int[originalShape.length][originalShape[0].length];
        for (int i = 0; i < originalShape.length; i++) {
            System.arraycopy(originalShape[i], 0, newShape[i], 0, originalShape[i].length);
        }

        this.shape = newShape;
    }

    // Creates a deep copy of the current Tetromino object
    public Tetromino copy() {
        int[][] newShape = new int[shape.length][shape[0].length];

        for (int i = 0; i < shape.length; i++) {
            System.arraycopy(shape[i], 0, newShape[i], 0, shape[i].length);
        }

        Tetromino copy = new Tetromino(newShape, color, type);

        copy.rotationState = this.rotationState;
        copy.row = this.row;
        copy.column = this.column;

        return copy;
    }
}