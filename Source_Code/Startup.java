// Class Description: Entry point of the program and starts at the main menu.

package TetrisProgram;

import javax.swing.SwingUtilities;

public class Startup {

    public static void main(String[] args) {

        // Attempts to start the system
        try {
            System.out.println("System: System startup was successful.");

            // Executes the program with an Event-dispatching thread to ensure thread safety
            SwingUtilities.invokeLater(MainMenuPage::new);

        }

        // Catches any errors when at startup
        catch (Exception e) {
            System.out.println("System: An error has occurred!");
            e.printStackTrace();
        }
    }
}
