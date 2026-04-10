import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class statisticsScreen extends JPanel {

    // Value labels — updated live on componentShown
    private JLabel decksVal      = makeVal("0");
    private JLabel timeVal       = makeVal("0s");
    private JLabel questionsVal  = makeVal("0");
    private JLabel studiedVal    = makeVal("0");
    private JLabel lastScoreVal  = makeVal("—");

    public statisticsScreen() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        add(EduQuiz.topBar("Statistics", "Main Menu", "mainMenu"), BorderLayout.NORTH);

        // Grid of label : value pairs
        JPanel grid = new JPanel(new GridLayout(5, 2, 20, 20));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        grid.add(makeKey("Total Decks"));       grid.add(decksVal);
        grid.add(makeKey("Time Studied"));      grid.add(timeVal);
        grid.add(makeKey("Questions Answered")); grid.add(questionsVal);
        grid.add(makeKey("Decks Studied"));     grid.add(studiedVal);
        grid.add(makeKey("Last Quiz Score"));   grid.add(lastScoreVal);

        add(grid, BorderLayout.CENTER);

        // Refresh every time the screen is shown
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refresh();
            }
        });
    }

    private void refresh() {
        decksVal.setText(String.valueOf(deckManager.decks.size()));
        timeVal.setText(statsManager.totalTimeMs == 0
                ? "0s"
                : statsManager.formatTime(statsManager.totalTimeMs));
        questionsVal.setText(String.valueOf(statsManager.questionsAnswered));
        studiedVal.setText(String.valueOf(statsManager.decksStudied.size()));

        if (statsManager.lastTotal == 0) {
            lastScoreVal.setText("—");
        } else {
            int pct = (int) Math.round((statsManager.lastScore * 100.0) / statsManager.lastTotal);
            lastScoreVal.setText(statsManager.lastScore + " / " + statsManager.lastTotal
                    + "  (" + pct + "%)");
        }
    }

    private static JLabel makeKey(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.RIGHT);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(new Color(30, 75, 120));
        return lbl;
    }

    private static JLabel makeVal(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.LEFT);
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        return lbl;
    }
}