import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class resultsScreen extends JPanel {

    private JLabel scoreLabel  = new JLabel("", SwingConstants.CENTER);
    private JLabel detailLabel = new JLabel("", SwingConstants.CENTER);

    public resultsScreen() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        add(EduQuiz.topBar("Results", "Main Menu", "mainMenu"), BorderLayout.NORTH);

        // Center: big score display
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 52));
        scoreLabel.setForeground(new Color(30, 75, 120));
        gbc.gridy = 0; gbc.insets = new Insets(40, 60, 10, 60);
        center.add(scoreLabel, gbc);

        detailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        detailLabel.setForeground(new Color(80, 80, 80));
        gbc.gridy = 1; gbc.insets = new Insets(0, 60, 40, 60);
        center.add(detailLabel, gbc);

        add(center, BorderLayout.CENTER);

        // Bottom: Restart and Main Menu
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton restartBtn = EduQuiz.styledButton("Restart");
        restartBtn.addActionListener(e -> EduQuiz.show("quiz"));

        JButton menuBtn = EduQuiz.styledButton("Main Menu");
        menuBtn.addActionListener(e -> EduQuiz.show("mainMenu"));

        bottom.add(restartBtn);
        bottom.add(menuBtn);
        add(bottom, BorderLayout.SOUTH);

        // Refresh score every time this screen is shown
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                int score = statsManager.lastScore;
                int total = statsManager.lastTotal;
                int pct   = total == 0 ? 0 : (int) Math.round((score * 100.0) / total);
                scoreLabel.setText("You got " + pct + "%");
                detailLabel.setText(score + " correct out of " + total);
            }
        });
    }
}