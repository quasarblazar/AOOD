import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;

public class quizScreen extends JPanel {

    private int currentIndex = 0;
    private int score        = 0;
    private ArrayList<String[]> terms = new ArrayList<>();

    private JLabel  titleLabel    = new JLabel("", SwingConstants.CENTER);
    private JLabel  questionText  = new JLabel("", SwingConstants.CENTER);
    private JLabel  answerText    = new JLabel("", SwingConstants.CENTER);
    private JLabel  progressLabel = new JLabel("", SwingConstants.CENTER);

    private JButton revealBtn   = EduQuiz.styledButton("Reveal");
    private JButton knowBtn     = EduQuiz.styledButton("Know");
    private JButton dontKnowBtn = EduQuiz.styledButton("Don't Know");
    private JPanel  buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

    public quizScreen() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        // Top bar
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);

        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(240, 245, 255));
        navBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        navBar.add(EduQuiz.navButton("Main Menu", "mainMenu"), BorderLayout.WEST);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(30, 75, 120));
        navBar.add(titleLabel, BorderLayout.CENTER);

        JButton xBtn = EduQuiz.styledButton("X");
        xBtn.addActionListener(e -> EduQuiz.show("mainMenu"));
        navBar.add(xBtn, BorderLayout.EAST);
        topSection.add(navBar, BorderLayout.NORTH);

        progressLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        progressLabel.setForeground(new Color(100, 100, 100));
        progressLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        topSection.add(progressLabel, BorderLayout.SOUTH);

        add(topSection, BorderLayout.NORTH);

        // Center: question + answer boxes
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        questionText.setFont(new Font("Arial", Font.BOLD, 20));
        questionText.setForeground(new Color(30, 75, 120));
        questionText.setOpaque(true);
        questionText.setBackground(new Color(210, 225, 245));
        questionText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 75, 120), 1),
                BorderFactory.createEmptyBorder(24, 20, 24, 20)));
        gbc.gridy = 0; gbc.insets = new Insets(20, 60, 4, 60);
        center.add(questionText, gbc);

        JLabel qTag = new JLabel("Question", SwingConstants.LEFT);
        qTag.setFont(new Font("Arial", Font.BOLD, 12));
        qTag.setForeground(new Color(80, 80, 80));
        gbc.gridy = 1; gbc.insets = new Insets(0, 64, 16, 60);
        center.add(qTag, gbc);

        answerText.setFont(new Font("Arial", Font.PLAIN, 18));
        answerText.setForeground(new Color(20, 100, 20));
        answerText.setOpaque(true);
        answerText.setBackground(new Color(225, 245, 225));
        answerText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 130, 0), 1),
                BorderFactory.createEmptyBorder(24, 20, 24, 20)));
        gbc.gridy = 2; gbc.insets = new Insets(0, 60, 4, 60);
        center.add(answerText, gbc);

        JLabel aTag = new JLabel("Answer", SwingConstants.LEFT);
        aTag.setFont(new Font("Arial", Font.BOLD, 12));
        aTag.setForeground(new Color(80, 80, 80));
        gbc.gridy = 3; gbc.insets = new Insets(0, 64, 10, 60);
        center.add(aTag, gbc);

        add(center, BorderLayout.CENTER);

        // Bottom: swapping button panel
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        revealBtn.addActionListener(e -> revealAnswer());
        knowBtn.addActionListener(e -> { score++; advance(); });
        dontKnowBtn.addActionListener(e -> advance());

        add(buttonPanel, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                startQuiz();
            }
        });
    }

    private void startQuiz() {
        String deck = studyScreen.selectedDeck;
        if (deck == null) return;
        terms = new ArrayList<>(deckManager.getTerms(deck));
        Collections.shuffle(terms);
        currentIndex = 0;
        score        = 0;
        titleLabel.setText(deck);
        statsManager.startSession(deck);
        showQuestion();
    }

    private void showQuestion() {
        String[] term = terms.get(currentIndex);
        questionText.setText(term[0]);
        answerText.setText("");
        progressLabel.setText("Question " + (currentIndex + 1) + " of " + terms.size());
        buttonPanel.removeAll();
        buttonPanel.add(revealBtn);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void revealAnswer() {
        answerText.setText(terms.get(currentIndex)[1]);
        buttonPanel.removeAll();
        buttonPanel.add(knowBtn);
        buttonPanel.add(dontKnowBtn);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void advance() {
        currentIndex++;
        if (currentIndex >= terms.size()) {
            statsManager.endSession(score, terms.size());
            EduQuiz.show("results");
        } else {
            showQuestion();
        }
    }
}