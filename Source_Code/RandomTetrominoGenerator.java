// Class Description: Unlike a 7-Bag, this is a true random Tetris piece generator (Might use in a later update)

package TetrisProgram;

import java.awt.*;
import java.util.Random;

public class RandomTetrominoGenerator {

    // Initialize the randomness of spawning the pieces
    private static final Random random = new Random();

    public static Tetromino getRandomPiece() {

        // Initialize the amount of Tetris pieces
        int choice = random.nextInt(7);

        return switch (choice) {

            // I piece
            case 0 -> new Tetromino(new int[][] {{1,1,1,1}}, Color.CYAN, "I");

            // O piece
            case 1 -> new Tetromino(new int[][]{{1,1}, {1,1}}, Color.YELLOW, "O");

            // T piece
            case 2 -> new Tetromino(new int[][]{{0,1,0}, {1,1,1}}, Color.MAGENTA, "T");

            // L piece
            case 3 -> new Tetromino(new int[][]{{1,0}, {1,0}, {1,1}}, Color.ORANGE, "L");

            // J piece
            case 4 -> new Tetromino(new int[][]{{0,1}, {0,1}, {1,1}}, Color.BLUE, "J");

            // S piece
            case 5 -> new Tetromino(new int[][]{{0,1,1}, {1,1,0}}, Color.GREEN, "S");

            // Z piece
            default -> new Tetromino(new int[][]{{1,1,0}, {0,1,1}}, Color.RED, "Z");
        };
    }
}
