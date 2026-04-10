import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.ArrayList;

public class deckOptionsScreen extends JPanel {

    static String selectedDeck = null;
    private JPanel deckList = new JPanel();

    public deckOptionsScreen() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        add(EduQuiz.topBar("Create and Edit", "Main Menu", "mainMenu"), BorderLayout.NORTH);

        deckList.setLayout(new BoxLayout(deckList, BoxLayout.Y_AXIS));
        deckList.setOpaque(false);
        deckList.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JScrollPane scroll = new JScrollPane(deckList);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(1, 5, 10, 0));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 40, 30, 40));
        bottom.add(EduQuiz.navButton("Create New", "create"));

        // --- Import ---
        JButton importBtn = EduQuiz.styledButton("Import New");
        importBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Import Deck");
            chooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String deckName = file.getName().replaceFirst("\\.txt$", "");
                if (deckManager.decks.contains(deckName)) {
                    int overwrite = JOptionPane.showConfirmDialog(null,
                            "A deck named \"" + deckName + "\" already exists. Overwrite it?",
                            "Deck Exists", JOptionPane.YES_NO_OPTION);
                    if (overwrite != JOptionPane.YES_OPTION) return;
                    deckManager.removeDeck(deckName);
                }
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    deckManager.addDeck(deckName);
                    // Read entire file and split on | to get individual terms
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    String[] entries = sb.toString().split("\\|");
                    int skipped = 0;
                    for (String entry : entries) {
                        entry = entry.trim();
                        if (entry.isEmpty()) continue;
                        String[] parts = entry.split("~", 2);
                        if (parts.length != 2) { skipped++; continue; }
                        String question = parts[0].trim();
                        String answer   = parts[1].trim();
                        if (question.isEmpty() || answer.isEmpty()) { skipped++; continue; }
                        deckManager.addTerm(deckName, question, answer);
                    }
                    String msg = "Imported \"" + deckName + "\" with "
                            + deckManager.getTerms(deckName).size() + " terms.";
                    if (skipped > 0) msg += "\n(" + skipped + " malformed entries skipped)";
                    JOptionPane.showMessageDialog(null, msg);
                    refreshList();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error reading file: " + ex.getMessage());
                }
            }
        });
        bottom.add(importBtn);

        // --- Export ---
        JButton exportBtn = EduQuiz.styledButton("Export");
        exportBtn.addActionListener(e -> {
            if (selectedDeck == null) {
                JOptionPane.showMessageDialog(null, "Please select a deck first.");
                return;
            }
            ArrayList<String[]> terms = deckManager.getTerms(selectedDeck);
            if (terms.isEmpty()) {
                JOptionPane.showMessageDialog(null, "This deck has no terms to export.");
                return;
            }
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Export Deck");
            chooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
            chooser.setSelectedFile(new File(selectedDeck + ".txt"));
            int result = chooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".txt")) {
                    file = new File(file.getAbsolutePath() + ".txt");
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (int i = 0; i < terms.size(); i++) {
                        writer.write(terms.get(i)[0] + "~" + terms.get(i)[1]);
                        if (i < terms.size() - 1) writer.write("|");
                    }
                    JOptionPane.showMessageDialog(null,
                            "Exported \"" + selectedDeck + "\" to:\n" + file.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage());
                }
            }
        });
        bottom.add(exportBtn);

        JButton editBtn = EduQuiz.styledButton("Edit Deck");
        editBtn.addActionListener(e -> {
            if (selectedDeck == null) {
                JOptionPane.showMessageDialog(null, "Please select a deck first.");
            } else {
                EduQuiz.show("edit");
            }
        });
        bottom.add(editBtn);

        JButton deleteBtn = EduQuiz.styledButton("Delete Deck");
        deleteBtn.addActionListener(e -> {
            if (selectedDeck == null) {
                JOptionPane.showMessageDialog(null, "Please select a deck first.");
            } else {
                deckManager.removeDeck(selectedDeck);
                selectedDeck = null;
                refreshList();
            }
        });
        bottom.add(deleteBtn);

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
        for (String name : deckManager.decks) {
            deckList.add(makeDeckLabel(name));
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