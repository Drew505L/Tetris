// Class Description: Display a normal game of Tetris that implements the different mechanics.

package TetrisProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


public class ClassicModePage extends JFrame {

    // Defines the Tetris grid
    private final int ROWS = 20;
    private final int COLUMNS = 10;
    private final int BLOCK_SIZE = 30;

    // Adds fixed widths for the side panels
    private final int LEFT_PANEL_WIDTH = 150;
    private final int RIGHT_PANEL_WIDTH = 180;

    // Tracks key press states for rendering the key visualizer
    private boolean leftPressed, rightPressed, upPressed, downPressed;
    private boolean zPressed, cPressed, spacePressed;

    public ClassicModePage() {

        // Constructs the JFrame and gives the name of the page
        super("Classic Mode");

        // Sets the layout manager to split into 3 sections
        setLayout(new BorderLayout());

        // Adds each panel to its respective position
        GridPanel gridPanel = new GridPanel();
        RightPanel rightPanel = new RightPanel(gridPanel.getEngine(), gridPanel.getScoreCalculator());

        gridPanel.getScoreCalculator().setScoreEventListener(
                (text, points) -> rightPanel.showPopup(text, points)
        );

        this.add(new LeftPanel(gridPanel.getEngine()), BorderLayout.WEST);
        this.add(gridPanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);

        // Hooks up the controls class to the grid Panel
        new Controls(gridPanel, this);

        // Shuts down the app when the frame is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Makes it so the user can't resize the JFrame
        this.setResizable(false);

        // Adjusts the window to fit the panel size
        this.pack();

        // Sets the JFrame to the center
        this.setLocationRelativeTo(null);

        // Makes the JFrame visible to the user
        this.setVisible(true);
    }

    // Setter methods for key visualizer
    public void setKeyState(int keyCode, boolean pressed) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT -> leftPressed = pressed;
            case KeyEvent.VK_RIGHT -> rightPressed = pressed;
            case KeyEvent.VK_UP -> upPressed = pressed;
            case KeyEvent.VK_DOWN -> downPressed = pressed;
            case KeyEvent.VK_Z -> zPressed = pressed;
            case KeyEvent.VK_C -> cPressed = pressed;
            case KeyEvent.VK_SPACE -> spacePressed = pressed;
        }

        // Refreshes visualizer
        repaint();
    }

    // Panel for the HOLD section (left side)
    public class LeftPanel extends JPanel {

        // Declares the Game Engine to implement the HOLD mechanic
        private GameEngine engine;

        public LeftPanel(GameEngine engine) {

            // Sets the size of the left panel
            setPreferredSize(new Dimension(LEFT_PANEL_WIDTH, ROWS * BLOCK_SIZE));

            // Sets the background color
            setBackground(Color.BLACK);

            this.engine = engine;

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Sets drawing color
            g.setColor(Color.WHITE);

            // Label for the HOLD box
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("HOLD", 40, 40);

            // Draws the HOLD box
            g.drawRect(25, 60, 100, 100);

            // Section for the key visualizer
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("CONTROLS", 25, 200);

            // Arrow keys layout
            drawKey(g, 60, 220, "↑", upPressed);
            drawKey(g, 30, 250, "←", leftPressed);
            drawKey(g, 60, 250, "↓", downPressed);
            drawKey(g, 90, 250, "→", rightPressed);

            // Other keys
            drawKey(g, 30, 300, "Z", zPressed);
            drawKey(g, 70, 300, "C", cPressed);

            // Space bar
            if (spacePressed) {
                g.setColor(Color.WHITE);
                g.fillRect(30, 340, 90, 30);
                g.setColor(Color.BLACK);
            }
            else {
                g.setColor(Color.WHITE);
                g.drawRect(30, 340, 90, 30);
            }

            g.drawString("SPACE", 45, 360);

            // Renders the hold piece
            Tetromino held = engine.getHeldPiece();

            if (held != null) {
                drawMiniPieceCentered(g, held, 25, 60, 100, 100);
            }
        }

        // Helper Method: Illustrate the key visualizer
        private void drawKey(Graphics g, int x, int y, String label, boolean pressed) {

            // Checks if the keys are pressed
            if (pressed) {
                g.setColor(Color.WHITE);
                g.fillRect(x, y, 30, 30);
                g.setColor(Color.BLACK);
            }
            else {
                g.setColor(Color.WHITE);
                g.drawRect(x,y,30,30);
            }

            // Draws the labels for the key visualizer
            FontMetrics fm = g.getFontMetrics();
            int textX = x + (30 - fm.stringWidth(label)) / 2;
            int textY = y + ((30 - fm.getHeight()) / 2) + fm.getAscent();

            g.drawString(label, textX, textY);

            // Ensures visual consistency
            g.setColor(Color.WHITE);
        }

        // Helper method: Draws a mini version of the piece for the hold box
        private void drawMiniPieceCentered(Graphics g, Tetromino piece, int boxX, int boxY, int boxWidth, int boxHeight) {

            int blockSize = 15;
            int[][] shape = piece.getShape();

            int pieceWidth = shape[0].length * blockSize;
            int pieceHeight = shape.length * blockSize;

            int startX = boxX + (boxWidth - pieceWidth) / 2;
            int startY = boxY + (boxHeight - pieceHeight) / 2;

            for (int r = 0; r < shape.length; r++) {
                for (int c = 0; c < shape[r].length; c++) {

                    if (shape[r][c] == 1) {

                        int x = startX + c * blockSize;
                        int y = startY + r * blockSize;

                        g.setColor(piece.getColor());
                        g.fillRect(x, y, blockSize, blockSize);

                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, blockSize, blockSize);
                    }
                }
            }
        }
    }

    // Panel for the Tetris grid (center)
    public class GridPanel extends JPanel {

        // Initialize countdown variables
        private int countdown = 3;
        private boolean countdownActive = true;


        // Declares variables for the GameBoard and GameEngine classes
        private final GameBoard board;
        private final GameEngine engine;

        // Declares and initialize score calculator
        private final ScoreCalculator scoreCalculator = new ScoreCalculator();

        public GridPanel() {
            setPreferredSize(new Dimension(COLUMNS * BLOCK_SIZE, ROWS * BLOCK_SIZE));
            setBackground(Color.BLACK);
            setFocusable(true);

            board = new GameBoard(ROWS, COLUMNS);

            engine = new GameEngine(ClassicModePage.this, board, scoreCalculator);


            startCountdownBeforeResume(() -> {
                engine.start();
                System.out.println("System: The game has started.");
            });

            SwingUtilities.invokeLater(this::requestFocusInWindow);
            new Timer(16, e -> repaint()).start();
        }

        // Getters
        public GameEngine getEngine() {
            return engine;
        }
        public boolean isCountdownActive() {
            return countdownActive;
        }
        public ScoreCalculator getScoreCalculator() {
            return scoreCalculator;
        }

        // Helper Method: Checks if the game state is paused
        public void togglePause() {
            if (engine.isPaused()) {
                showPauseMenu(false);
            }
            else {
                engine.pause();
                showPauseMenu(true);
            }
        }

        // Helper Method: Displays the pause menu contents
        private void showPauseMenu(boolean paused) {

            System.out.println("System: Game paused.");

            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Paused", true);
            dialog.setSize(300, 250);
            dialog.setLocationRelativeTo(this);

            JPanel panel = new JPanel();
            panel.setBackground(Color.BLACK);
            panel.setLayout(new GridLayout(4, 1, 10, 10));

            JLabel title = new JLabel("PAUSED", SwingConstants.CENTER);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Arial", Font.BOLD, 20));

            JButton resume = createStyledButton("Resume");
            JButton controls = createStyledButton("Controls");
            JButton exit = createStyledButton("Exit");

            resume.addActionListener(e -> {
                System.out.println("System: Game resuming...");
                dialog.dispose();
                startCountdownBeforeResume(() -> {
                    engine.resume();
                    requestFocusInWindow();
                });
            });

            controls.addActionListener(e -> {
                showControls();
            });

            exit.addActionListener(e -> {
                dialog.dispose();
                exitToMenu();
            });

            panel.add(title);
            panel.add(resume);
            panel.add(controls);
            panel.add(exit);

            dialog.add(panel);
            dialog.setVisible(true);
        }

        // Helper Method: Creates the theme for the buttons
        private JButton createStyledButton(String text) {
            JButton button = new JButton(text);
            button.setFocusPainted(false);
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            return button;
        }

        // Helper Method: Displays the controls page
        private void showControls() {

            System.out.println("System: Showing the controls.");

            String message = """
            Controls:

            ← / → : Move piece
            ↑ : Rotate clockwise
            Z : Rotate counterclockwise
            ↓ : Soft drop
            SPACE : Hard drop
            C : Hold piece
            P : Pause
            """;

            javax.swing.JOptionPane.showMessageDialog(this, message, "Controls", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

        // Helper Method: Exits the game to the main menu
        private void exitToMenu() {
            javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
            new MainMenuPage();
            System.out.println("System: Exiting to the Main Menu.");
        }

        // Draws the grid
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw a piece
            Tetromino currentPiece = engine.getCurrentPiece();

            // Draws each block
            for (int row = 0; row < ROWS; row++) {
                for (int column = 0; column < COLUMNS; column++) {

                    // Determines the block color
                    Color[][] grid = board.getGrid();
                    Color color;

                    // Checks if the game is over
                    if (board.isGameOverAnimation() && row >= board.getRedRow()) {
                        color = Color.RED;
                    }
                    else if (grid[row][column] != null) {
                        color = grid[row][column];
                    }
                    else {
                        color = Color.DARK_GRAY.darker();
                    }

                    // Sets color
                    g.setColor(color);

                    // Draws block
                    g.fillRect(column * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

                    // Draws block border
                    g.setColor(Color.BLACK);
                    g.drawRect(column * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

                }
            }

            // Draws a ghost piece
            if (currentPiece != null && !countdownActive) {

                int ghostRow = engine.getGhostRow();

                // Skip if already on ground (prevents overlap flicker)
                if (ghostRow != currentPiece.getRow()) {

                    int[][] shape = currentPiece.getShape();

                    Color ghostColor = new Color(
                            currentPiece.getColor().getRed(),
                            currentPiece.getColor().getGreen(),
                            currentPiece.getColor().getBlue(),
                            80
                    );

                    for (int r = 0; r < shape.length; r++) {
                        for (int c = 0; c < shape[r].length; c++) {

                            if (shape[r][c] == 1) {

                                int x = (currentPiece.getColumn() + c) * BLOCK_SIZE;
                                int y = (ghostRow + r) * BLOCK_SIZE;

                                g.setColor(ghostColor);
                                g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);

                                g.setColor(Color.BLACK);
                                g.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                            }
                        }
                    }
                }
            }

            // Draws the current Tetris piece
            currentPiece = engine.getCurrentPiece();
            if (currentPiece != null && !countdownActive) {

                int[][] shape = currentPiece.getShape();

                for (int r = 0; r < shape.length; r++) {
                    for (int c = 0; c < shape[r].length; c++) {
                        if (shape[r][c] == 1) {

                            int x = (currentPiece.getColumn() + c) * BLOCK_SIZE;
                            int y = (currentPiece.getRow() + r) * BLOCK_SIZE;

                            // Checks if the game is doing the animation and turns the current piece red
                            // Otherwise use the normal piece color
                            if (board.isGameOverAnimation()) {
                                g.setColor(Color.RED);
                            }
                            else {
                                g.setColor(currentPiece.getColor());
                            }
                            g.fillRect(x,y,BLOCK_SIZE,BLOCK_SIZE);

                            g.setColor(Color.BLACK);
                            g.drawRect(x,y,BLOCK_SIZE,BLOCK_SIZE);
                        }
                    }
                }
            }

            // Draws the countdown overlay whenever a countdown is active
            if (countdownActive) {
                String text = (countdown > 0) ? String.valueOf(countdown) : "GO!";

                // Change color plus size when GO! appears
                if (countdown > 0) {
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 60));
                }
                else {
                    g.setColor(Color.GREEN);
                    g.setFont(new Font("Arial", Font.BOLD, 70));
                }

                // Centers the text
                FontMetrics fm = g.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = getHeight() / 2;

                g.drawString(text, x, y);
            }

            // Grey scales the grid when the countdown is active
            if (countdownActive) {
                g.setColor(new Color(0,0,0,100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }

        // Helper Method: A countdown when the game starts and pause menu
        public void startCountdownBeforeResume(Runnable onCountdownFinished) {

            // reset countdown and disables input and piece disability
            countdown = 3;
            countdownActive = true;

            // Refresh immediately
            repaint();

            Timer timer = new Timer(1000, null);

            timer.addActionListener(e -> {
                countdown--;

                if (countdown == 0) {
                    ((Timer)e.getSource()).stop();

                    // Short "GO!" flash
                    Timer goTimer = new Timer(400, ev -> {

                        // Re-enables input and piece visibility and game engine
                        countdownActive = false;
                        onCountdownFinished.run();
                        ((Timer) ev.getSource()).stop();
                        repaint();
                    });
                    goTimer.setRepeats(false);
                    goTimer.start();
                }

                repaint();
            });

            timer.setInitialDelay(1000);
            timer.start();
        }

        // These methods handle controls
        // Move the piece to the left
        public void movePieceLeft() {
            engine.moveLeft();
            repaint();
        }

        // Moves the piece to the right
        public void movePieceRight() {
            engine.moveRight();
            repaint();
        }

        // Rotates the pieces clockwise
        public void rotatePieceClockwise() {
            engine.rotateClockwise();
            repaint();
        }

        // Rotates the pieces counterclockwise
        public void rotatePieceCounterClockwise() {
            engine.rotateCounterClockwise();
            repaint();
        }


        // Helper Method: Soft drop
        public void softDrop() {
            int movedCells = engine.softDrop();
            scoreCalculator.addSoftDrop(movedCells);
            repaint();
        }

        // Example: hard drop
        public void hardDrop() {
            int cellsDropped = engine.hardDrop(); // make hardDrop() return cells dropped
            scoreCalculator.addHardDrop(cellsDropped);
            repaint();
        }

        // After clearing lines
        public void onLinesCleared(int lines, boolean tSpin) {
            if (tSpin) {
                scoreCalculator.addTSpin(lines);
            }
            else {
                scoreCalculator.addLineClear(lines);
            }
            repaint();
        }

        // Hold pieces
        public void holdPiece() {
            engine.hold();
            repaint();
        }

        // Getter
        public GameEngine getGameEngine() {
            return engine;
        }
    }

    // Panel for NEXT and STATS (right side)
    public class RightPanel extends JPanel {

        private GameEngine engine;
        private final ScoreCalculator scoreCalculator;

        // Declare variables for the popup messages
        private String popupText = "";
        private int popupPoints = 0;
        private long popupStartTime = 0;

        public RightPanel(GameEngine engine, ScoreCalculator scoreCalculator) {

            this.engine = engine;
            this.scoreCalculator = scoreCalculator;

            // Sets the size of the right panel
            setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, ROWS * BLOCK_SIZE));

            // Sets the background color
            setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Sets drawing color
            g.setColor(Color.WHITE);

            // Draws the NEXT label and box
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("NEXT", 50, 40);
            g.drawRect(40, 60, 100, 100);

            // Draws the outer box for the STATS section
            g.drawRect(20, 200, 140, 180);

            // Establishes the title of the section
            g.drawString("STATS", 55, 220);

            // Establishes the labels of the section
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("SCORE", 40, 250);
            g.drawString("LINES", 40, 300);
            g.drawString("LEVEL", 40, 350);

            // Establishes the values under the labels
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString(String.valueOf(scoreCalculator.getScore()), 40, 270);
            g.drawString(String.valueOf(scoreCalculator.getLinesCleared()), 40, 320);
            g.drawString(String.valueOf(scoreCalculator.getLevel()), 40, 370);

            Tetromino nextPiece = engine.getPreviewQueue().peek();

            if (nextPiece != null) {
                drawMiniPieceCentered(g, nextPiece, 40, 60, 100, 100);
            }

            // Displays the action in the right panel
            long elapsed = System.currentTimeMillis() - popupStartTime;

            if (elapsed < 1500) {

                int alpha = (int)(255 * (1 - (elapsed / 1500.0)));

                Graphics2D g2 = (Graphics2D) g;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));

                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 16));

                g.drawString(popupText, 30, 420);
                g.drawString("+" + popupPoints, 30, 450);

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        }

        // Helper method to draw mini pieces for the NEXT box
        private void drawMiniPieceCentered(Graphics g, Tetromino piece, int boxX, int boxY, int boxWidth, int boxHeight) {

            int blockSize = 15;
            int[][] shape = piece.getShape();

            int pieceWidth = shape[0].length * blockSize;
            int pieceHeight = shape.length * blockSize;

            // Center calculation
            int startX = boxX + (boxWidth - pieceWidth) / 2;
            int startY = boxY + (boxHeight - pieceHeight) / 2;

            for (int r = 0; r < shape.length; r++) {
                for (int c = 0; c < shape[r].length; c++) {

                    if (shape[r][c] == 1) {

                        int x = startX + c * blockSize;
                        int y = startY + r * blockSize;

                        g.setColor(piece.getColor());
                        g.fillRect(x, y, blockSize, blockSize);

                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, blockSize, blockSize);
                    }
                }
            }
        }

        // Helper Method: Displays the text based on the user's actions
        public void showPopup(String text, int points) {
            if (text == null || text.isEmpty() || points <= 0) {
                return;
            }

            this.popupText = text;
            this.popupPoints = points;
            this.popupStartTime = System.currentTimeMillis();
        }
    }
}