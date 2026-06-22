// Class Description: Displays before the VS AI game mode where the user can change the AI difficulty and chooses between Marathon Mode or Score Attack Mode.

package TetrisProgram;

import javax.swing.*;
import java.awt.*;

public class VersusAIPreviewPage extends JFrame {

    // Constructs the GUI for the Versus AI Preview page
    private final VersusAIPreviewPageGUI versusAIPreviewPageGUI;

    public VersusAIPreviewPage() {

        // Constructs the JFrame and gives the name of the page
        super("Score Attack Settings");

        // Initializes a size of the page
        this.setSize(512, 600);

        // Sets the JFrame to the center
        this.setLocationRelativeTo(null);

        // Shuts down the app when the frame is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sets the layout manager
        setLayout(new BorderLayout(5, 5));

        // Constructs the VersusAIPreviewPageGUI class and sets it to the center
        versusAIPreviewPageGUI = new VersusAIPreviewPageGUI();
        this.add(versusAIPreviewPageGUI, BorderLayout.CENTER);

        // Makes it so the user can't resize the JFrame
        this.setResizable(false);

        // Makes the JFrame visible to the user
        this.setVisible(true);
    }

    // Inner class within VersusAIPreviewPageGUI which has the contents of the preview page
    public class VersusAIPreviewPageGUI extends JPanel {

        // Constructs the elements that make up the preview page
        private final JLabel versusAItext = new JLabel("Versus Settings", SwingConstants.CENTER);
        private final JLabel directionstext = new JLabel("Directions: Beat the AI in a 1v1.");
        private final JLabel subtitletext = new JLabel("Select Difficulty", SwingConstants.CENTER);
        private final JButton easyGameButton = new JButton("Easy Game");
        private final JButton normalGameButton = new JButton("Normal Game");
        private final JButton hardGameButton = new JButton("Hard Game");
        private final JButton playbutton = new JButton("Play");
        private final JButton backbutton = new JButton("Back");

        public VersusAIPreviewPageGUI() {

            // Calls the method that handles events
            VersusAIPreviewPageEvents();

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
            styleLabel(versusAItext, 26, true);
            styleLabel(directionstext, 12, false);
            styleLabel(subtitletext, 18, false);

            // Center the text
            versusAItext.setAlignmentX(Component.CENTER_ALIGNMENT);
            directionstext.setAlignmentX(Component.CENTER_ALIGNMENT);
            subtitletext.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Sets up the text within the JPanel
            content.add(versusAItext);
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
            styleButtonSize(easyGameButton);
            styleButtonSize(normalGameButton);
            styleButtonSize(hardGameButton);

            // Calls the method to stylize the buttons to the menu theme
            UITheme.styleButton(easyGameButton);
            UITheme.styleButton(normalGameButton);
            UITheme.styleButton(hardGameButton);

            // Adds the game type buttons into the JPanel
            timePanel.add(easyGameButton);
            timePanel.add(Box.createVerticalStrut(10));
            timePanel.add(normalGameButton);
            timePanel.add(Box.createVerticalStrut(10));
            timePanel.add(hardGameButton);

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

            // Highlights the slow game by default
            highlightSelection(easyGameButton);
        }

        private void VersusAIPreviewPageEvents() {

            // Starts a slow game
            easyGameButton.addActionListener(arg -> {
                highlightSelection(easyGameButton);
                System.out.println("System: A easy game is selected.");
            });

            // Starts a normal game
            normalGameButton.addActionListener(arg -> {
                highlightSelection(normalGameButton);
                System.out.println("System: A normal game is selected.");
            });

            // Starts a fast game
            hardGameButton.addActionListener(arg -> {
                highlightSelection(hardGameButton);
                System.out.println("System: A hard game is selected.");
            });

            // Plays the Line Clearing Challenge Game Mode
            playbutton.addActionListener(arg0 -> {
//                VersusAIPreviewPage.this.dispose();
//                new VersusAIPage();
                System.out.println("System: Now playing the Versus AI Game mode.");

                // Fits the popup window with the theme of the main menu
                UIManager.put("OptionPane.background", Color.BLACK);
                UIManager.put("Panel.background", Color.BLACK);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 12));
                UIManager.put("Button.background", Color.DARK_GRAY);
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Button.focusPainted", false);

                // Shows popup to the user
                JOptionPane.showMessageDialog(this, "This mode will be added in a future update!", "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
            });

            // Exits to the main menu
            backbutton.addActionListener(arg0 -> {
                VersusAIPreviewPage.this.dispose();
                new MainMenuPage();
                System.out.println("System: Exiting to the main menu.");
            });
        }

        // Helper Method: Adds a highlight effect with the game option buttons
        private void highlightSelection(JButton selected) {
            JButton[] buttons = {easyGameButton, normalGameButton, hardGameButton};

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
