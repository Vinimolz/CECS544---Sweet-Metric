package gui;

import model.Language;
import javax.swing.*;
import java.awt.*;

public class LanguageDialog extends JDialog{
    private Language language;           // reference to the model
    private ButtonGroup buttonGroup;     // ensures only one selection at a time
    private String tempSelection = null; // tracks selection before hitting Done

    public LanguageDialog(JFrame parent, Language language) {
        super(parent, "Select Language", true); // true = modal dialog
        this.language = language;

        buildUI();

        pack();
        setLocationRelativeTo(parent); // centers over main window
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // --- Top label ---
        JLabel label = new JLabel("Select one language");
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        add(label, BorderLayout.NORTH);

        // --- Radio buttons panel ---
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        radioPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        buttonGroup = new ButtonGroup();

        for (String lang : Language.LANGUAGES) {
            JRadioButton radioButton = new JRadioButton(lang);

            // If this language was previously selected, restore it
            if (lang.equals(language.getSelectedLanguage())) {
                radioButton.setSelected(true);
                tempSelection = lang;
            }

            radioButton.addActionListener(e -> tempSelection = lang);
            buttonGroup.add(radioButton);
            radioPanel.add(radioButton);
        }

        add(radioPanel, BorderLayout.CENTER);

        // --- Done button ---
        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            language.setSelectedLanguage(tempSelection); // save to model
            dispose(); // close the dialog
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(doneButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
