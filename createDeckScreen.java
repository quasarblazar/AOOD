import javax.swing.*;
import java.awt.*;

public class createDeckScreen extends JPanel {

    public createDeckScreen() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        add(EduQuiz.topBar("Create New Deck", "Main Menu", "mainMenu"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel prompt = new JLabel("Name your new deck:");
        prompt.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(prompt, gbc);

        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        form.add(nameField, gbc);

        JButton save = EduQuiz.styledButton("Save as New");
        save.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                deckManager.addDeck(name);
                nameField.setText("");
                EduQuiz.show("deckOptions");
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a deck name.");
            }
        });
        gbc.gridy = 2;
        form.add(save, gbc);

        add(form, BorderLayout.CENTER);
    }
}