import javax.swing.*;
import java.awt.*;

public class EduQuiz {

    static JFrame frame = new JFrame("EduQuiz");
    static CardLayout cardLayout = new CardLayout();
    static JPanel mainPanel = new JPanel(cardLayout);

    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);

        mainPanel.add(new mainMenuScreen(),    "mainMenu");
        mainPanel.add(new deckOptionsScreen(), "deckOptions");
        mainPanel.add(new createDeckScreen(),  "create");
        mainPanel.add(new editScreen(),        "edit");
        mainPanel.add(new studyScreen(),       "study");
        mainPanel.add(new quizScreen(),        "quiz");
        mainPanel.add(new resultsScreen(),     "results");
        mainPanel.add(new statisticsScreen(),  "stats");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    static void show(String screen) {
        cardLayout.show(mainPanel, screen);
    }

    static JButton navButton(String label, String destination) {
        JButton btn = styledButton(label);
        btn.addActionListener(e -> EduQuiz.show(destination));
        return btn;
    }

    static JButton styledButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(new Color(30, 75, 120));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    static JLabel titleLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
        lbl.setForeground(new Color(30, 75, 120));
        lbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        return lbl;
    }

    static JPanel topBar(String title, String backLabel, String backDest) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(240, 245, 255));
        bar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bar.add(navButton(backLabel, backDest), BorderLayout.WEST);
        bar.add(titleLabel(title), BorderLayout.CENTER);
        JButton x = styledButton("X");
        x.addActionListener(e -> EduQuiz.show("mainMenu"));
        bar.add(x, BorderLayout.EAST);
        return bar;
    }
}