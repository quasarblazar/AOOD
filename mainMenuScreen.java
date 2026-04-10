import javax.swing.*;
import java.awt.*;

public class mainMenuScreen extends JPanel {

    public mainMenuScreen() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("EduQuiz", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(new Color(30, 75, 120));
        title.setBorder(BorderFactory.createEmptyBorder(60, 0, 40, 0));
        add(title, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(4, 1, 0, 16));
        buttons.setOpaque(false);
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 250, 80, 250));

        buttons.add(EduQuiz.navButton("Study", "study"));
        buttons.add(EduQuiz.navButton("Create / Edit Deck", "deckOptions"));
        buttons.add(EduQuiz.navButton("Statistics", "stats")); 

        JButton exit = EduQuiz.styledButton("Exit");
        exit.addActionListener(e -> System.exit(0));
        buttons.add(exit);

        add(buttons, BorderLayout.CENTER);
    }
}