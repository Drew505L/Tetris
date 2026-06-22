// Class description: Allows the UI theme to be reused in different pages

package TetrisProgram;

import javax.swing.*;
import java.awt.*;

public class UITheme {

    public static final Color BACKGROUND = Color.BLACK;
    public static final Color BUTTON_BG = Color.DARK_GRAY;
    public static final Color TEXT = Color.WHITE;

    public static void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(BUTTON_BG);
        button.setForeground(TEXT);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(150, 40));
    }
}