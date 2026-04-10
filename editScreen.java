import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class editScreen extends JPanel {

    private int currentIndex = 0;
    private boolean isNewTerm = false; // true when editing an unsaved blank

    private JTextField questionField = new JTextField();
    private JTextField answerField   = new JTextField();
    private JLabel     indexLabel    = new JLabel("", SwingConstants.CENTER);

    public editScreen() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        add(EduQuiz.topBar("Edit Deck", "Create and Edit", "deckOptions"), BorderLayout.NORTH);

        // ── Center: question / answer fields + index counter ──────────────
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        fields.setBorder(BorderFactory.createEmptyBorder(30, 80, 10, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JLabel qLabel = new JLabel("Question:");
        qLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        fields.add(qLabel, gbc);

        questionField.setFont(new Font("Arial", Font.PLAIN, 14));
        questionField.setPreferredSize(new Dimension(380, 32));
        gbc.gridx = 1; gbc.weightx = 1.0;
        fields.add(questionField, gbc);

        JLabel aLabel = new JLabel("Answer:");
        aLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        fields.add(aLabel, gbc);

        answerField.setFont(new Font("Arial", Font.PLAIN, 14));
        answerField.setPreferredSize(new Dimension(380, 32));
        gbc.gridx = 1; gbc.weightx = 1.0;
        fields.add(answerField, gbc);

        // "Term X of Y" counter
        indexLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        indexLabel.setForeground(new Color(100, 100, 100));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        fields.add(indexLabel, gbc);

        add(fields, BorderLayout.CENTER);

        // ── Bottom: Prev | Save Term | Delete Term | Add | Next ───────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        JButton prevBtn = EduQuiz.styledButton("< Prev");
        prevBtn.addActionListener(e -> navigate(-1));

        JButton saveBtn = EduQuiz.styledButton("Save Term");
        saveBtn.addActionListener(e -> saveTerm());

        JButton deleteBtn = EduQuiz.styledButton("Delete Term");
        deleteBtn.addActionListener(e -> deleteTerm());

        JButton addBtn = EduQuiz.styledButton("Add");
        addBtn.addActionListener(e -> startNewTerm());

        JButton nextBtn = EduQuiz.styledButton("Next >");
        nextBtn.addActionListener(e -> navigate(1));

        bottom.add(prevBtn);
        bottom.add(saveBtn);
        bottom.add(deleteBtn);
        bottom.add(addBtn);
        bottom.add(nextBtn);
        add(bottom, BorderLayout.SOUTH);

        // Reset to the first term (or a blank) whenever this screen is shown
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                currentIndex = 0;
                isNewTerm    = false;
                loadCurrent();
            }
        });
    }

    // ── Load helpers ────────────────────────────────────────────────────────

    /** Populate fields from currentIndex, or show blank if deck is empty. */
    private void loadCurrent() {
        ArrayList<String[]> list = getTerms();
        if (list == null) return;

        if (list.isEmpty()) {
            // Fresh/empty deck — open a ready-to-fill blank term
            showBlank("New term — fill in and press Save Term");
            isNewTerm = true;
        } else {
            String[] term = list.get(currentIndex);
            questionField.setText(term[0]);
            answerField.setText(term[1]);
            updateCounter(list.size());
        }
    }

    private void showBlank(String counterText) {
        questionField.setText("");
        answerField.setText("");
        indexLabel.setText(counterText);
    }

    private void updateCounter(int total) {
        if (isNewTerm) {
            indexLabel.setText("New term " + (total + 1));
        } else {
            indexLabel.setText("Term " + (currentIndex + 1) + " of " + total);
        }
    }

    // ── Button actions ──────────────────────────────────────────────────────

    /** Save the current fields — adds a new term or updates an existing one. */
    private void saveTerm() {
        String deck = deckOptionsScreen.selectedDeck;
        if (deck == null) return;

        String q = questionField.getText().trim();
        String a = answerField.getText().trim();
        if (q.isEmpty() || a.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in both Question and Answer.");
            return;
        }

        if (isNewTerm) {
            deckManager.addTerm(deck, q, a);
            currentIndex = deckManager.getTerms(deck).size() - 1;
            isNewTerm = false;
        } else {
            deckManager.editTerm(deck, currentIndex, q, a);
        }
        updateCounter(deckManager.getTerms(deck).size());
        JOptionPane.showMessageDialog(null, "Term saved!");
    }

    /** Delete the current term; land on the nearest remaining term or a blank. */
    private void deleteTerm() {
        String deck = deckOptionsScreen.selectedDeck;
        if (deck == null) return;
        ArrayList<String[]> list = getTerms();
        if (list == null) return;

        if (isNewTerm) {
            // Nothing persisted yet — just go back to what was there
            isNewTerm = false;
            if (list.isEmpty()) {
                showBlank("New term — fill in and press Save Term");
                isNewTerm = true;
            } else {
                currentIndex = Math.max(0, list.size() - 1);
                loadCurrent();
            }
            return;
        }

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No terms to delete.");
            return;
        }

        deckManager.removeTerm(deck, currentIndex);
        list = deckManager.getTerms(deck);
        if (list.isEmpty()) {
            currentIndex = 0;
            showBlank("New term — fill in and press Save Term");
            isNewTerm = true;
        } else {
            currentIndex = Math.min(currentIndex, list.size() - 1);
            loadCurrent();
        }
    }

    /** Toggle to a fresh blank term; saving it will append it to the deck. */
    private void startNewTerm() {
        ArrayList<String[]> list = getTerms();
        if (list == null) return;
        isNewTerm = true;
        showBlank("New term " + (list.size() + 1));
    }

    /** Move Prev / Next through existing saved terms. */
    private void navigate(int direction) {
        ArrayList<String[]> list = getTerms();
        if (list == null || list.isEmpty()) return;

        // If on a new blank, navigating discards it silently and returns to list
        isNewTerm    = false;
        currentIndex = (currentIndex + direction + list.size()) % list.size();
        loadCurrent();
    }


    private ArrayList<String[]> getTerms() {
        String deck = deckOptionsScreen.selectedDeck;
        if (deck == null) {
            indexLabel.setText("No deck selected.");
            return null;
        }
        return deckManager.getTerms(deck);
    }
}