// Class Description: Displays the results after the user gets a game over for the classic mode, includes an option to save their score.

package TetrisProgram;

import javax.swing.*;
import java.awt.*;

public class ClassicModeResultsPage extends JFrame {

    // Declares a variable for the score calculator class
    private final ScoreCalculator scoreCalculator;

    // Constructs the GUI for the Classic Mode's Results page
    private final ClassicModeResultsGUI classicModeResultsGUI;

    public ClassicModeResultsPage(ScoreCalculator scoreCalculator) {

        // Constructs the JFrame and gives the name of the page
        super("Results");

        // Initializes a variable for score calculator
        this.scoreCalculator = scoreCalculator;

        // Initialize the size of the page
        this.setSize(512, 600);

        // Sets the JFrame to the center
        this.setLocationRelativeTo(null);

        // Shuts down the app when the frame is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sets the layout manager
        setLayout(new BorderLayout(5, 5));

        // Constructs the ClassicModeResultsGUI class and sets it the center
        classicModeResultsGUI = new ClassicModeResultsGUI();
        this.add(classicModeResultsGUI, BorderLayout.CENTER);

        // Makes it so the user can't resize the JFrame
        this.setResizable(false);

        // Makes the JFrame visible to the user
        this.setVisible(true);
    }

    // Inner class within ClassicModeResultsPage which has the contents of the results screen
    public class ClassicModeResultsGUI extends JPanel {

        // Constructs the elements that make up the results screen
        private final JLabel gameovertext = new JLabel("Game Over!", SwingConstants.CENTER);
        private final JLabel resultstext = new JLabel("Results", SwingConstants.CENTER);
        private final JButton playagainbutton = new JButton("Play Again");
        private final JButton savescorebutton = new JButton("Save Score");
        private final JButton exittomainmenubutton = new JButton("Exit");

        public ClassicModeResultsGUI() {

            // Calls the method that handles events
            ClassicModeResultsEvents();

            // Sets the layout manager for the main JPanel
            setLayout(new BorderLayout());

            // Sets the theme of the page
            setBackground(UITheme.BACKGROUND);

            // Creates a main vertical container
            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setBackground(UITheme.BACKGROUND);
            content.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
            add(content);

            // Creates the top with the game over and results text
            styleLabel(gameovertext, 36, true);
            styleLabel(resultstext, 22, true);

            // Centers the results text
            gameovertext.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultstext.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Sets up the text within the JPanel
            content.add(gameovertext);
            content.add(Box.createVerticalStrut(10));
            content.add(resultstext);
            content.add(Box.createVerticalStrut(30));

            // Groups the stats in a readable manner
            JPanel statsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            statsPanel.setBackground(UITheme.BACKGROUND);

            statsPanel.add(createStatLabel("Score"));
            statsPanel.add(createStatValue(String.valueOf(scoreCalculator.getScore())));

            statsPanel.add(createStatLabel("Lines"));
            statsPanel.add(createStatValue(String.valueOf(scoreCalculator.getLinesCleared())));

            statsPanel.add(createStatLabel("Level"));
            statsPanel.add(createStatValue(String.valueOf(scoreCalculator.getLevel())));

            content.add(statsPanel);
            content.add(Box.createVerticalStrut(30));

            // Fixes the buttons where they are readable
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.setBackground(UITheme.BACKGROUND);

            // Calls the method to stylize the buttons
            styleButtonSize(playagainbutton);
            styleButtonSize(savescorebutton);
            styleButtonSize(exittomainmenubutton);

            // Sets up the buttons within the JPanel
            buttonPanel.add(playagainbutton);
            buttonPanel.add(Box.createVerticalStrut(10));
            buttonPanel.add(savescorebutton);
            buttonPanel.add(Box.createVerticalStrut(10));
            buttonPanel.add(exittomainmenubutton);

            content.add(buttonPanel);

            UITheme.styleButton(playagainbutton);
            UITheme.styleButton(savescorebutton);
            UITheme.styleButton(exittomainmenubutton);
        }

        private void ClassicModeResultsEvents() {

            // Plays the Classic Mode again
            playagainbutton.addActionListener(arg0 -> {
                ClassicModeResultsPage.this.dispose();
                new ClassicModePage();
                System.out.println("System: Replaying Classic Mode.");
            });

            // Saves the stats of the game
            savescorebutton.addActionListener(arg0 -> {
                System.out.println("System: Saving score.");

                // Fits the popup window with the theme of the main menu
                UIManager.put("OptionPane.background", Color.BLACK);
                UIManager.put("Panel.background", Color.BLACK);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 12));
                UIManager.put("Button.background", Color.DARK_GRAY);
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Button.focusPainted", false);

                // Shows popup to the user
                JOptionPane.showMessageDialog(this, "This feature will be added in a future update!", "Coming Soon",JOptionPane.INFORMATION_MESSAGE);
            });

            // Returns to the Main Menu
            exittomainmenubutton.addActionListener(arg0 -> {
                ClassicModeResultsPage.this.dispose();
                new MainMenuPage();
                System.out.println("System: Returning to the Main Menu.");
            });
        }
    }

    // Helper Method: Stylized the labels
    private void styleLabel(JLabel label, int size, boolean bold) {
        label.setForeground(UITheme.TEXT);
        label.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, size));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    // Helper Method: Creates the labels for the stats
    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text + ":");
        label.setForeground(UITheme.TEXT);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }

    // Helper Method: Creates the values of the stats
    private JLabel createStatValue(String value) {
        JLabel label = new JLabel(value);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    // Helper Method: Stylized the buttons
    private void styleButtonSize(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
    }
}