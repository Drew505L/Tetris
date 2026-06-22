// Class Description: An algorithm that randomizes all the Tetris pieces.

package TetrisProgram;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SevenBag {

    // Declares a queue that stores the order in which pieces appear
    private final Queue<Tetromino> bag;


    // Constructor: Initializes the queue system by implementing a LinkedList from a Queue and immediately calls for the bag to be refilled
    public SevenBag() {
        bag = new LinkedList<>();
        refillBag();
    }

    // Helper Method: Gets the next piece within the 7-bag
    public Tetromino getNextPiece() {

        // Checks if the bag is empty, if so, then it refills the bag
        if (bag.isEmpty()) {
            refillBag();
            System.out.println("System: New 7-bag generated.");
        }

        // Removes and returns the first piece in the queue
        Tetromino piece = bag.poll();

        // When the piece spawns into the preview window, prints a message to the console
        System.out.println("System: " + piece.getType() + " pulled from the bag. " + bag.size() + " remains in the bag.");

        // Returns a copy of the piece so the original in the bag remains unchanged
        return piece.copy();

    }

    // Helper Method: Refills the bag
    private void refillBag() {

        // Declares a list that hold 7 types of pieces before shuffling them
        List<Tetromino> pieces = new ArrayList<>();

        // Creates all the different Tetris pieces
        pieces.add(new Tetromino(new int[][]{{0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0}}, Color.CYAN, "I"));
        pieces.add(new Tetromino(new int[][]{{1,1},{1,1}}, Color.YELLOW, "O"));
        pieces.add(new Tetromino(new int[][]{{0,1,0}, {1,1,1}, {0,0,0}}, Color.MAGENTA, "T"));
        pieces.add(new Tetromino(new int[][]{{0,0,1}, {1,1,1}, {0,0,0}}, Color.ORANGE, "L"));
        pieces.add(new Tetromino(new int[][]{{1,0,0}, {1,1,1}, {0,0,0}}, Color.BLUE, "J"));
        pieces.add(new Tetromino(new int[][]{{0,1,1}, {1,1,0}, {0,0,0}}, Color.GREEN, "S"));
        pieces.add(new Tetromino(new int[][]{{1,1,0}, {0,1,1}, {0,0,0}}, Color.RED, "Z"));

        // Shuffles the Tetris pieces
        Collections.shuffle(pieces);

        // Puts into the queue
        bag.addAll(pieces);
    }
}
