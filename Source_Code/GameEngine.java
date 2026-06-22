// Class Description: Handles timer (for other game-modes), moves pieces with gravity, piece rotation, and hold mechanic.

package TetrisProgram;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;

public class GameEngine {

    // Declares a variable for the game board class
    private final GameBoard board;

    // Declares a variable for the score calculator class
    private final ScoreCalculator scoreCalculator;

    // Declares a variable for the current tetris piece on the grid
    private Tetromino currentPiece;

    // Declares a variable for the held piece
    private Tetromino heldPiece;

    // Declares a variable to check if you can hold a piece
    private boolean canHold = true;

    // Declares a variable for the seven bag
    private final SevenBag bag;

    // Declares a variable for the preview window
    private final Queue<Tetromino> previewQueue;

    // Declares a variable for the fall speed of the tetris pieces
    private double fallSpeed;

    // Declares a variable to check the game state
    private boolean paused = false;

    // Initialize a time for the game
    private Timer gameTimer;

    // Initialize a checker for the game over animation
    private boolean gameOver = false;

    // Declares and initialize a variable to delay the pieces being placed on the grid
    private long lockStartTime = -1;
    private static final long LOCK_DELAY = 500;

    // Declares a temp variable to calculate how many points are awarded to the player using soft drop
    private int softDropPointsThisPiece = 0;

    // Declares a variable for the game frame
    private final JFrame gameFrame;

    // Declares a variable to keep track if the piece was rotated
    private boolean lastMoveWasRotation = false;

    // Declares a variable to keep track of combos
    private int combo = -1;

    // Declares a variable to keep track of back to backs
    private boolean backToBack = false;

    public GameEngine(JFrame gameFrame, GameBoard board, ScoreCalculator scoreCalculator) {
        this.gameFrame = gameFrame;
        this.board = board;
        this.scoreCalculator = scoreCalculator;

        bag = new SevenBag();
        previewQueue = new LinkedList<>();

        // Initialize pieces
        currentPiece = bag.getNextPiece();
        previewQueue.add(bag.getNextPiece());

        // Initialize fall speed
        this.fallSpeed = Math.pow(0.8 - ((scoreCalculator.getLevel() - 1) * 0.007), scoreCalculator.getLevel() - 1);

        // Level change listener
        scoreCalculator.setLevelChangeListener(newLevel -> updateFallSpeed());
    }

    // Helper Method: Checks if the game is paused
    public boolean isPaused() {
        return paused;
    }

    // Helper Method: Starts the game engine when is called
    public void start() {

        // Declares a variable to store the millions of the falling pieces in milliseconds
        int speed = 500;
        gameTimer = new Timer(speed, e -> update());
        gameTimer.start();
    }


    // Helper Method: Pauses the game state
    public void pause() {
        paused = true;
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    // Helper Method: Resumes game state
    public void resume() {
        paused = false;
        if (gameTimer != null) {
            gameTimer.start();
        }
    }


    public int softDrop() {

        lastMoveWasRotation = false;

        int cells = 0;
        if (canMoveDown()) {
            currentPiece.moveDown();
            cells++;

            // Adds the total score and also keeps track of how many cells were used on the piece
            softDropPointsThisPiece += 1;
            lockStartTime = -1;
        }
        return cells;
    }

    public int hardDrop() {
        int cells = 0;
        while (canMoveDown()) {
            currentPiece.moveDown();
            cells++;
        }
        lockPiece();
        return cells;
    }


    // Updates the game state
    private void update() {
        if (canMoveDown()) {
            currentPiece.moveDown();
            lockStartTime = -1;
        }
        else {
            if (lockStartTime == -1) {
                lockStartTime = System.currentTimeMillis();
            }
            else {
                long elapsed = System.currentTimeMillis() - lockStartTime;

                if (elapsed >= LOCK_DELAY) {
                    lockPiece();
                    lockStartTime = -1;
                }
            }
        }
    }


    private void updateFallSpeed() {
        double fallSpeedSeconds = Math.pow(0.8 - ((scoreCalculator.getLevel() - 1) * 0.007), scoreCalculator.getLevel() - 1);

        int interval = (int) (fallSpeedSeconds * 1000);

        if (gameTimer != null) {
            gameTimer.setDelay(interval);
            gameTimer.setInitialDelay(interval);
        }

    }

    // Helper Method: Locks pieces in place within the grid
    private void lockPiece() {

        // Step 1: Place piece FIRST
        board.placePiece(currentPiece);

        // Step 2: Detect T-spin before clearing
        boolean isTSpin = isTSpin();

        // Step 3: Clear lines after placement
        int cleared = board.clearLines();

        // Step 4: Combo logic
        if (cleared > 0) {
            combo++;
        }
        else {
            combo = -1;
        }

        // Step 5: Back-to-back logic
        boolean isB2BAction = (cleared == 4) || (isTSpin && cleared > 0);

        if (isB2BAction) {
            if (backToBack) {
                scoreCalculator.applyBackToBackBonus();
            }
            backToBack = true;
        }
        else if (cleared > 0) {
            backToBack = false;
        }

        // Step 6: Scoring
        if (isTSpin) {
            scoreCalculator.addTSpin(cleared);
        }
        else {
            scoreCalculator.addLineClear(cleared);
        }

        // Step 7: Combo bonus
        if (combo > 0) {
            scoreCalculator.addCombo(combo);
        }

        // Step 8: Reset rotation flag
        lastMoveWasRotation = false;

        // Step 9: Soft drop score message
        if (softDropPointsThisPiece > 0) {
            System.out.println("System: Soft drop used. (+ " + softDropPointsThisPiece + " pts)");
            softDropPointsThisPiece = 0;
        }

        // Step 10: Update fall speed
        updateFallSpeed();

        // Step 11: Reset hold
        canHold = true;

        // Step 12: Spawn next piece
        currentPiece = previewQueue.poll();
        previewQueue.add(bag.getNextPiece());

        // Step 13: Game over check
        if (board.isGameOver(currentPiece)) {
            gameOver();
            System.out.println("System: Final Score = " + scoreCalculator.getScore() + " | Lines Cleared = " + scoreCalculator.getLinesCleared() + " | Final Level = " + scoreCalculator.getLevel());
            return;
        }

        // Step 14: Spawn message
        System.out.println("System: " + currentPiece.getType() + " has spawned on the grid.");
    }

    // Checks if the current piece can move down without collision or going out of bounds
    public boolean canMoveDown() {
        return board.isValidPosition(currentPiece, currentPiece.getRow() + 1, currentPiece.getColumn());
    }

    // Moves the pieces to the left
    public void moveLeft() {

        lastMoveWasRotation = false;

        if (board.isValidPosition(currentPiece, currentPiece.getRow(), currentPiece.getColumn() - 1)) {
            currentPiece.moveLeft();
            lockStartTime = -1;
        }
    }

    // Moves the pieces to the right
    public void moveRight() {

        lastMoveWasRotation = false;

        if (board.isValidPosition(currentPiece, currentPiece.getRow(), currentPiece.getColumn() + 1)) {
            currentPiece.moveRight();
            lockStartTime = -1;
        }
    }

    // Rotates the piece clockwise
    public void rotateClockwise() {

        lockStartTime = -1;

        if (currentPiece.getType().equals("O")) return;

        int oldState = currentPiece.getRotationState();
        int newState = (oldState + 1) % 4;

        // Save original position
        int originalRow = currentPiece.getRow();
        int originalCol = currentPiece.getColumn();

        // Apply rotation
        currentPiece.rotateClockwise();

        int[][] kicks = getKickData(oldState, newState, true);

        for (int[] kick : kicks) {
            int testRow = originalRow - kick[1];
            int testCol = originalCol + kick[0];

            if (board.isValidPosition(currentPiece, testRow, testCol)) {
                currentPiece.setPosition(testRow, testCol);
                lastMoveWasRotation = true;
                return;
            }
        }

        // If all fail, revert everything
        currentPiece.rotateCounterClockwise();
        currentPiece.setPosition(originalRow, originalCol);
    }

    // Rotates the piece counterclockwise
    public void rotateCounterClockwise() {

        lockStartTime = -1;

        if (currentPiece.getType().equals("O")) {
            return;
        }

        int oldState = currentPiece.getRotationState();
        int newState = (oldState + 3) % 4;

        int originalRow = currentPiece.getRow();
        int originalCol = currentPiece.getColumn();

        currentPiece.rotateCounterClockwise();

        int[][] kicks = getKickData(oldState, newState, false);

        for (int[] kick : kicks) {
            int testRow = originalRow - kick[1];
            int testCol = originalCol + kick[0];

            if (board.isValidPosition(currentPiece, testRow, testCol)) {
                currentPiece.setPosition(testRow, testCol);
                lastMoveWasRotation = true;
                return;
            }
        }

        // Revert if all fail
        currentPiece.rotateClockwise();
        currentPiece.setPosition(originalRow, originalCol);
    }

    // Helper Method: Recognizes T-spins
    private boolean isTSpin() {

        if (!currentPiece.getType().equals("T")) return false;
        if (!lastMoveWasRotation) return false;

        int pivotRow = currentPiece.getRow() + 1;
        int pivotCol = currentPiece.getColumn() + 1;

        int occupiedCorners = 0;

        int[][] corners = {
                {pivotRow - 1, pivotCol - 1},
                {pivotRow - 1, pivotCol + 1},
                {pivotRow + 1, pivotCol - 1},
                {pivotRow + 1, pivotCol + 1}
        };

        for (int[] c : corners) {

            int r = c[0];
            int col = c[1];

            // Treat out-of-bounds as filled (correct behavior)
            if (r < 0 || r >= board.getGrid().length ||
                    col < 0 || col >= board.getGrid()[0].length ||
                    board.getGrid()[r][col] != null) {

                occupiedCorners++;
            }
        }

        return occupiedCorners >= 3;
    }

    // Helper Method: Implements the hold pieces
    public void hold() {

        if (!canHold) {
            return;
        }

        // Checks if there is a held piece, if so, then swap the pieces
        // If not, adds the piece into the HOLD box
        if (heldPiece == null) {
            heldPiece = currentPiece.copy();
            currentPiece = previewQueue.poll();
            previewQueue.add(bag.getNextPiece());
        }
        else {
            Tetromino temp = currentPiece;
            currentPiece = heldPiece.copy();
            currentPiece.resetState();
            heldPiece = temp;
        }

        // Prints the hold mechanic being used
        System.out.println("System: Hold used.");

        // Previous if statement but now prints the message
        if (heldPiece == null) {
            System.out.println("System: " + currentPiece.getType() + " moved to HOLD.");
        }
        else {
            System.out.println("System: " + currentPiece.getType() + " deployed from HOLD.");
            System.out.println("System: " + heldPiece.getType() + " moved to HOLD.");
        }

        // Disables holding the piece until a new piece spawns
        canHold = false;
    }

    // Implements SRS Wall kick data
    private int[][] getKickData(int from, int to, boolean clockwise) {

        if (!currentPiece.getType().equals("I")) {
            return getJLSTZKickData(from, to);
        }
        else {
            return getIKickData(from, to);
        }
    }

    // Gets the data from the J, L, S, T, Z pieces
    private int[][] getJLSTZKickData(int from, int to) {

        // 0 -> 1
        if (from == 0 && to == 1)
            return new int[][]{{0,0}, {-1,0}, {-1,1}, {0,-2}, {-1,-2}};

        // 1 -> 0
        if (from == 1 && to == 0)
            return new int[][]{{0,0}, {1,0}, {1,-1}, {0,2}, {1,2}};

        // 1 -> 2
        if (from == 1 && to == 2)
            return new int[][]{{0,0}, {1,0}, {1,-1}, {0,2}, {1,2}};

        // 2 -> 1
        if (from == 2 && to == 1)
            return new int[][]{{0,0}, {-1,0}, {-1,1}, {0,-2}, {-1,-2}};

        // 2 -> 3
        if (from == 2 && to == 3)
            return new int[][]{{0,0}, {1,0}, {1,1}, {0,-2}, {1,-2}};

        // 3 -> 2
        if (from == 3 && to == 2)
            return new int[][]{{0,0}, {-1,0}, {-1,-1}, {0,2}, {-1,2}};

        // 3 -> 0
        if (from == 3 && to == 0)
            return new int[][]{{0,0}, {-1,0}, {-1,-1}, {0,2}, {-1,2}};

        // 0 -> 3
        if (from == 0 && to == 3)
            return new int[][]{{0,0}, {1,0}, {1,1}, {0,-2}, {1,-2}};

        return new int[][]{{0,0}};
    }

    // Gets the data from the I piece
    private int[][] getIKickData(int from, int to) {

        if (from == 0 && to == 1)
            return new int[][]{{0,0}, {-2,0}, {1,0}, {-2,-1}, {1,2}};

        if (from == 1 && to == 0)
            return new int[][]{{0,0}, {2,0}, {-1,0}, {2,1}, {-1,-2}};

        if (from == 1 && to == 2)
            return new int[][]{{0,0}, {-1,0}, {2,0}, {-1,2}, {2,-1}};

        if (from == 2 && to == 1)
            return new int[][]{{0,0}, {1,0}, {-2,0}, {1,-2}, {-2,1}};

        if (from == 2 && to == 3)
            return new int[][]{{0,0}, {2,0}, {-1,0}, {2,1}, {-1,-2}};

        if (from == 3 && to == 2)
            return new int[][]{{0,0}, {-2,0}, {1,0}, {-2,-1}, {1,2}};

        if (from == 3 && to == 0)
            return new int[][]{{0,0}, {1,0}, {-2,0}, {1,-2}, {-2,1}};

        if (from == 0 && to == 3)
            return new int[][]{{0,0}, {-1,0}, {2,0}, {-1,2}, {2,-1}};

        return new int[][]{{0,0}};
    }


    // Helper Method: When a game over is reached, it stops the game and opens the results screen
    private void gameOver() {
        System.out.println("System: Game Over!");

        gameOver = true;

        if (gameTimer != null) {
            gameTimer.stop();
        }

        // Starts the animation
        board.startGameOverAnimation();
        Timer animationTimer = new Timer(40, null);
        animationTimer.addActionListener(e -> {
            board.decreaseRedRow();
            gameFrame.repaint();
            if (board.getRedRow() < 0) {
                animationTimer.stop();
                SwingUtilities.invokeLater(() -> {
                    new ClassicModeResultsPage(scoreCalculator);
                    gameFrame.dispose();
                });
            }
        });

        // Starts the game over animation
        animationTimer.start();
    }

    // Getters
    public Tetromino getCurrentPiece() {
        return currentPiece;
    }
    public Queue<Tetromino> getPreviewQueue() {
        return previewQueue;
    }
    public Tetromino getHeldPiece() {
        return heldPiece;
    }
    public int getGhostRow() {
        int ghostRow = currentPiece.getRow();

        while (board.isValidPosition(currentPiece, ghostRow + 1, currentPiece.getColumn())) {
            ghostRow++;
        }

        return ghostRow;
    }
    public boolean isGameOver() {
        return gameOver;
    }
}