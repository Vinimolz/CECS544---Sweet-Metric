package gui;

import model.FunctionPointData;
import javax.swing.*;
import java.awt.*;

public class VAFDialog extends JDialog {

    private FunctionPointData data;
    private JComboBox<Integer>[] comboBoxes;

    private static final String[] VAF_QUESTIONS = {
            "Does the system require reliable backup and recovery processes?",
            "Are specialized data communications required to transfer information to or from the application?",
            "Are there distributed processing functions?",
            "Is performance critical?",
            "Will the system run in an existing, heavily utilized operational environment?",
            "Does the system require online data entry?",
            "Does the online data entry require the input transaction to be built over multiple screens or operations?",
            "Are the internal logical files updated online?",
            "Are the input, output, files or inquiries complex?",
            "Is the internal processing complex?",
            "Is the code designed to be reusable?",
            "Are conversion and installation included in the design?",
            "Is the system designed for multiple installations in different organizations?",
            "Is the application designed to facilitate change and for ease of use by the user?"
    };

    @SuppressWarnings("unchecked")
    public VAFDialog(JFrame parent, FunctionPointData data) {
        super(parent, "Value Adjustment Factors", true);
        this.data = data;
        this.comboBoxes = new JComboBox[14];
        buildUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // --- Title label ---
        JLabel titleLabel = new JLabel(
                "Assign a value from 0 to 5 for each of the following Value Adjustment Factors:");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        add(titleLabel, BorderLayout.NORTH);

        // --- Questions panel ---
        JPanel questionsPanel = new JPanel(new GridBagLayout());
        questionsPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);

        for (int i = 0; i < VAF_QUESTIONS.length; i++) {
            // Question label
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            questionsPanel.add(new JLabel(VAF_QUESTIONS[i]), gbc);

            // Combo box (0-5)
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            comboBoxes[i] = new JComboBox<>(
                    new Integer[]{0, 1, 2, 3, 4, 5});

            // Restore previous value per requirement 4.15
            comboBoxes[i].setSelectedItem(data.getVafValues()[i]);
            questionsPanel.add(comboBoxes[i], gbc);
        }

        // Wrap in scroll pane in case window is small
        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        add(scrollPane, BorderLayout.CENTER);

        // --- Done and Cancel buttons ---
        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            // Save all VAF values to model (requirement 4.16)
            for (int i = 0; i < 14; i++) {
                data.setVafValue(i, (Integer) comboBoxes[i].getSelectedItem());
            }
            dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose()); // discard changes

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(doneButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}