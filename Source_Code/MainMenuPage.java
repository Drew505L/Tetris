// Class Description: Displays the main menu to the user, with the different modes that are available including: Classic Mode, Score Attack Mode, Line Clear Challenge, VS AI Mode, Custom Mode, and High Score.

package TetrisProgram;

import javax.swing.*;
import java.awt.*;

public class MainMenuPage extends JFrame {

    // Constructs the GUI for the Main Menu page
    private final MainMenuGUI mainMenuGUI;

    public MainMenuPage() {

        // Constructs the JFrame and gives the name of the page
        super("Main Menu");

        // Sets the size of the JFrame
        this.setSize(512, 600);

        // Sets the JFrame to the center
        this.setLocationRelativeTo(null);

        // Shuts down the app when the frame is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sets the layout manager
        setLayout(new BorderLayout(5,5));

        // Constructs the MainMenuGUI class and sets it to the center
        mainMenuGUI = new MainMenuGUI();
        this.add(mainMenuGUI, BorderLayout.CENTER);

        // Makes it so the user can't resize the JFrame
        this.setResizable(false);

        // Makes the JFrame visible to the user
        this.setVisible(true);
    }

    // Inner class within MainMenuPage which has the contents of the Main Menu
    public class MainMenuGUI extends JPanel {

        // Constructs the elements that make up the Main Menu
        private final JLabel titletext = new JLabel("Tetris");
        private final JLabel authortext = new JLabel("By Andrew Mendez V1.0.1");

        private final JButton classicButton = new JButton("Classic");
        private final JButton scoreAttackButton = new JButton("Score Attack");
        private final JButton comboChallengeButton = new JButton("Line Clearing Challenge");
        private final JButton versusAIButton = new JButton("Versus AI");
        private final JButton customButton = new JButton("Custom");
        private final JButton highScoresButton = new JButton("High Scores");
        private final JButton exitbutton = new JButton("Exit");

        public MainMenuGUI() {

            // Calls the method that handles events
            MainMenuEvents();

            // Sets the layout manager for the main JPanel
            setLayout(new BorderLayout());

            // Sets the background of the main menu to be black
            setBackground(Color.BLACK);

            // Adds the items in order into the JPanel
            // Now using structured panels instead of adding everything directly
            this.add(createTitle(), BorderLayout.NORTH);
            this.add(createButtons(), BorderLayout.CENTER);
        }

        private JPanel createTitle() {

            // Constructs a container to hold the title
            JPanel header = new JPanel();

            // Creates a vertical layout for the title and subtitle
            header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

            // Makes the header the same color as the main menu
            header.setBackground(Color.BLACK);

            // Sets the title text font, text color, and position
            titletext.setFont(new Font("Arial", Font.BOLD, 48));
            titletext.setForeground(Color.WHITE);
            titletext.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Sets the author text font, text color, and position
            authortext.setFont(new Font("Arial", Font.PLAIN, 16));
            authortext.setForeground(Color.LIGHT_GRAY);
            authortext.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Creates an empty space on the top
            header.add(Box.createVerticalStrut(40));

            // Adds the title with the author text
            header.add(titletext);
            header.add(Box.createVerticalStrut(10));
            header.add(authortext);
            header.add(Box.createVerticalStrut(20));

            return header;
        }

        private JPanel createButtons() {

            // Creates a panel
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.BLACK);

            // Defines what the grid will look like
            GridBagConstraints gbc = new GridBagConstraints();

            // Applies spacing to each button
            gbc.insets = new Insets(10, 20, 10, 20);

            // Centers the components
            gbc.anchor = GridBagConstraints.CENTER;

            // Apply styling to buttons
            styleButton(classicButton);
            styleButton(scoreAttackButton);
            styleButton(comboChallengeButton);
            styleButton(versusAIButton);
            styleButton(customButton);
            styleButton(highScoresButton);
            styleButton(exitbutton);

            // Builds the grid system into the menu
            // Row 1
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(classicButton, gbc);
            gbc.gridx = 1;
            panel.add(scoreAttackButton, gbc);

            // Row 2
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(comboChallengeButton, gbc);
            gbc.gridx = 1;
            panel.add(versusAIButton, gbc);

            // Row 3
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(customButton, gbc);
            gbc.gridx = 1;
            panel.add(highScoresButton, gbc);

            // Row 4 (Exit centered across both columns)
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            panel.add(exitbutton, gbc);

            // Reset grid width so it doesn't affect future components
            gbc.gridwidth = 1;

            return panel;
        }

        private void styleButton(JButton button) {

            // Removes the focus border when clicked
            button.setFocusPainted(false);

            // Sets each color of the buttons
            button.setBackground(Color.DARK_GRAY);
            button.setForeground(Color.WHITE);

            // Sets the font of the buttons
            button.setFont(new Font("Arial", Font.BOLD, 12));

            // Sets the size of the buttons
            button.setPreferredSize(new Dimension(150, 40));
        }

        private void MainMenuEvents() {

            // Takes the user to the classic mode
            classicButton.addActionListener(arg0 -> {
                System.out.println("System: Now playing the Classic Mode.");
                MainMenuPage.this.setVisible(false);
                new ClassicModePage();
            });

            // Takes the user to the score attack mode
            scoreAttackButton.addActionListener(arg0 -> {
                System.out.println("System: Opening the setting for the Score Attack Mode.");
                MainMenuPage.this.setVisible(false);
                new ScoreAttackPreviewPage();
            });

            // Takes the user to the line clearing challenge mode
            comboChallengeButton.addActionListener(arg0 -> {
                System.out.println("System: Opening the setting for the Combo Challenge Mode.");
                MainMenuPage.this.setVisible(false);
                new LineClearingChallengePreviewPage();

            });

            // Takes the user to the versus AI mode (placeholder: this will be added in a future update)
            versusAIButton.addActionListener(arg0 -> {
                System.out.println("System: Opening the settings for the Versus AI game mode.");
                MainMenuPage.this.setVisible(false);
                new VersusAIPreviewPage();
            });

            // Takes the user to the custom mode (placeholder: this will be added in a future update)
            customButton.addActionListener(arg0 -> {
                System.out.println("System: Opening the settings for the Custom Mode.");

                // Fits the popup window with the theme of the main menu
                UIManager.put("OptionPane.background", Color.BLACK);
                UIManager.put("Panel.background", Color.BLACK);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 12));
                UIManager.put("Button.background", Color.DARK_GRAY);
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Button.focusPainted", false);

                // Shows popup to the user
                JOptionPane.showMessageDialog(MainMenuPage.this, "This mode will be added in a future update!", "Coming Soon",JOptionPane.INFORMATION_MESSAGE);
            });

            // Takes the user to the high score mode
            highScoresButton.addActionListener(arg0 -> {
                System.out.println("System: Now viewing the High Scores.");
//                MainMenuPage.this.setVisible(false);
//                new HighScoresPage();

                // Fits the popup window with the theme of the main menu
                UIManager.put("OptionPane.background", Color.BLACK);
                UIManager.put("Panel.background", Color.BLACK);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 12));
                UIManager.put("Button.background", Color.DARK_GRAY);
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Button.focusPainted", false);

                // Shows popup to the user
                JOptionPane.showMessageDialog(MainMenuPage.this, "This feature will be added in a future update!", "Coming Soon",JOptionPane.INFORMATION_MESSAGE);
            });

            // Closes the program if the user selects exit
            exitbutton.addActionListener(arg0 -> {
                System.out.println("System: System needs a confirmation to exit!");

                // Fits the popup window with the theme of the main menu
                UIManager.put("OptionPane.background", Color.BLACK);
                UIManager.put("Panel.background", Color.BLACK);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 12));
                UIManager.put("Button.background", Color.DARK_GRAY);
                UIManager.put("Button.foreground", Color.WHITE);

                // Removes focus border for popup buttons
                UIManager.put("Button.focusPainted", false);

                // Popup opens to confirm exit
                int choice = JOptionPane.showConfirmDialog(MainMenuPage.this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                // Checks if the user selects "Yes"
                // If so, it closes the program
                if (choice == JOptionPane.YES_NO_OPTION) {
                    System.out.println("System: The program will now close.");
                    System.exit(0);
                }

                // If not, it aborts the termination of the program
                else {
                    System.out.println("System: System termination aborted.");
                }
            });
        }
    }
}