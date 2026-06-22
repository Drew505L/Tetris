// Class Description: A page before the actual game mode asking the user their time limit (3, 5, or 10 minutes)

package TetrisProgram;

import javax.swing.*;
import java.awt.*;

public class ScoreAttackPreviewPage extends JFrame{

    // Constructs the GUI for the Score Attack Preview page
    private final ScoreAttackPreviewGUI scoreAttackPreviewGUI;

    public ScoreAttackPreviewPage() {

        // Constructs the JFrame and gives the name of the page
        super("Score Attack Settings");

        // Initializes a size of the page
        this.setSize(512, 600);

        // Sets the JFrame to the center
        this.setLocationRelativeTo(null);

        // Shuts down the app when the frame is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sets the layout manager
        setLayout(new BorderLayout(5,5));

        // Constructs the ScoreAttackPreviewGUI class and sets it to the center
        scoreAttackPreviewGUI = new ScoreAttackPreviewGUI();
        this.add(scoreAttackPreviewGUI, BorderLayout.CENTER);

        // Makes it so the user can't resize the JFrame
        this.setResizable(false);

        // Makes the JFrame visible to the user
        this.setVisible(true);
    }

    // Inner class within ScoreAttackPreviewPage which has the contents of the preview page
    public class ScoreAttackPreviewGUI extends JPanel {

        // Initialize timer for the game mode
        private int selectedTime = 180;

        // Constructs the elements that make up the preview page
        private final JLabel scoreattacktext = new JLabel("Score Attack Settings", SwingConstants.CENTER);
        private final JLabel directionstext = new JLabel("Directions: Get as many points as you can within the time limit.");
        private final JLabel subtitletext = new JLabel("Select Game Type", SwingConstants.CENTER);
        private final JButton shortGameButton = new JButton("Short Game");
        private final JButton normalGameButton = new JButton("Normal Game");
        private final JButton longGameButton = new JButton("Long Game");
        private final JButton playbutton = new JButton("Play");
        private final JButton backbutton = new JButton("Back");

        public ScoreAttackPreviewGUI() {

            // Calls the method that handles events
            ScoreAttackPreviewEvents();

            // Sets the layout manager for the JPanel
            setLayout(new BorderLayout());

            // Sets the theme of the page
            setBackground(UITheme.BACKGROUND);

            // Creates a main vertical container
            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setBackground(UITheme.BACKGROUND);
            content.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
            add(content);

            // Creates the top with the settings, directions, and subtitle text
            styleLabel(scoreattacktext,36,true);
            styleLabel(directionstext,14,false);
            styleLabel(subtitletext,18,false);

            // Center the text
            scoreattacktext.setAlignmentX(Component.CENTER_ALIGNMENT);
            directionstext.setAlignmentX(Component.CENTER_ALIGNMENT);
            subtitletext.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Sets up the text within the JPanel
            content.add(scoreattacktext);
            content.add(Box.createVerticalStrut(10));
            content.add(directionstext);
            content.add(Box.createVerticalStrut(10));
            content.add(subtitletext);
            content.add(Box.createVerticalStrut(10));

            // Creates an area for the game type selection
            JPanel timePanel = new JPanel();
            timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
            timePanel.setBackground(UITheme.BACKGROUND);

            // Calls the method to resize the buttons for the game types
            styleButtonSize(shortGameButton);
            styleButtonSize(normalGameButton);
            styleButtonSize(longGameButton);

            // Calls the method to stylize the buttons to the menu theme
            UITheme.styleButton(shortGameButton);
            UITheme.styleButton(normalGameButton);
            UITheme.styleButton(longGameButton);

            // Adds the game type buttons into the JPanel
            timePanel.add(shortGameButton);
            timePanel.add(Box.createVerticalStrut(10));
            timePanel.add(normalGameButton);
            timePanel.add(Box.createVerticalStrut(10));
            timePanel.add(longGameButton);

            content.add(timePanel);
            content.add(Box.createVerticalStrut(25));

            // Fixes the buttons where they are readable
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.setBackground(UITheme.BACKGROUND);

            // Calls the method to stylize the buttons
            styleButtonSize(playbutton);
            styleButtonSize(backbutton);

            // Sets up the buttons within the JPanel
            buttonPanel.add(playbutton);
            buttonPanel.add(Box.createVerticalStrut(10));
            buttonPanel.add(backbutton);
            buttonPanel.add(Box.createVerticalStrut(10));

            content.add(buttonPanel);

            // Stylize the option buttons to fit with the menu theme
            UITheme.styleButton(playbutton);
            UITheme.styleButton(backbutton);

            // Highlights the short game by default
            highlightSelection(shortGameButton);
        }

        private void ScoreAttackPreviewEvents() {

            // Starts a short game (3 mins)
            shortGameButton.addActionListener(arg0 -> {
                selectedTime = 180;
                highlightSelection(shortGameButton);
                System.out.println("System: A short game is selected.");
            });

            // Starts a normal game (5 mins)
            normalGameButton.addActionListener(arg0 -> {
                selectedTime = 300;
                highlightSelection(normalGameButton);
                System.out.println("System: A normal game is selected.");
            });

            // Starts a long game (10 mins)
            longGameButton.addActionListener(arg0 -> {
                selectedTime = 600;
                highlightSelection(longGameButton);
                System.out.println("System: A long game is selected.");
            });

            // Plays the Score Attack Game mode
            playbutton.addActionListener(arg0 -> {
//                ScoreAttackPreviewPage.this.dispose();
//                new ScoreAttackPage(selectedTime);
                System.out.println("System: Now playing the Score Attack Game mode.");

                // Fits the popup window with the theme of the main menu
                UIManager.put("OptionPane.background", Color.BLACK);
                UIManager.put("Panel.background", Color.BLACK);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 12));
                UIManager.put("Button.background", Color.DARK_GRAY);
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Button.focusPainted", false);

                // Shows popup to the user
                JOptionPane.showMessageDialog(this, "This mode will be added in a future update!", "Coming Soon",JOptionPane.INFORMATION_MESSAGE);
            });

            // Exits to the main menu
            backbutton.addActionListener(arg0 -> {
                ScoreAttackPreviewPage.this.dispose();
                new MainMenuPage();
                System.out.println("System: Exiting to the main menu.");
            });
        }

        // Helper Method: Adds a highlight effect with the game option buttons
        private void highlightSelection(JButton selected) {
            JButton[] buttons = {shortGameButton, normalGameButton, longGameButton};

            for (JButton b : buttons) {
                b.setBackground(UITheme.BUTTON_BG);
            }

            selected.setBackground(Color.GRAY);
        }

        // Helper Method: Stylized the labels
        private void styleLabel(JLabel label, int size, boolean bold) {
            label.setForeground(UITheme.TEXT);
            label.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, size));
            label.setHorizontalAlignment(SwingConstants.CENTER);
        }

        // Helper Method: Stylized the buttons size
        private void styleButtonSize(JButton button) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(200, 40));
        }
    }
}
