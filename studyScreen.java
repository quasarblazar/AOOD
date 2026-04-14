import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class studyScreen extends JPanel {

    static String selectedDeck = null;
    private JPanel deckList = new JPanel();

    public studyScreen() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        add(EduQuiz.topBar("Study", "Main Menu", "mainMenu"), BorderLayout.NORTH);

        deckList.setLayout(new BoxLayout(deckList, BoxLayout.Y_AXIS));
        deckList.setOpaque(false);
        deckList.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JScrollPane scroll = new JScrollPane(deckList);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton beginBtn = EduQuiz.styledButton("Begin Quiz");
        beginBtn.addActionListener(e -> {
            if (selectedDeck == null) {
                JOptionPane.showMessageDialog(null, "Please select a deck first.");
            } else if (deckManager.getTerms(selectedDeck).isEmpty()) {
                JOptionPane.showMessageDialog(null, "No questions in this deck.");
            } else {
                EduQuiz.show("quiz");
            }
        });
        bottom.add(beginBtn);
        add(bottom, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshList();
            }
        });
    }

    private void refreshList() {
        deckList.removeAll();
        selectedDeck = null;
        if (deckManager.decks.isEmpty()) {
            JLabel empty = new JLabel("No Decks");
            empty.setFont(new Font("Arial", Font.PLAIN, 22));
            empty.setForeground(new Color(150, 150, 150));
            empty.setAlignmentX(CENTER_ALIGNMENT);
            empty.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            deckList.add(empty);
        } else {
            for (String name : deckManager.decks) {
                deckList.add(makeDeckLabel(name));
            }
        }
        deckList.revalidate();
        deckList.repaint();
    }

    private JLabel makeDeckLabel(String name) {
        JLabel label = new JLabel(name);
        label.setFont(new Font("Arial", Font.PLAIN, 26));
        label.setForeground(new Color(30, 75, 120));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        label.setAlignmentX(LEFT_ALIGNMENT);

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectedDeck = name;
                for (Component c : deckList.getComponents()) {
                    c.setForeground(new Color(30, 75, 120));
                }
                label.setForeground(new Color(0, 150, 0));
            }
        });
        return label;
    }
}