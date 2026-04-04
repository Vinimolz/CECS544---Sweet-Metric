package gui;

import model.Project;
import javax.swing.*;
import java.awt.*;

public class NewProjectDialog extends JDialog {

    private JTextField projectNameField;
    private JTextField productNameField;
    private JTextField creatorField;
    private JTextArea  commentsArea;

    private Project project = null; // will be set when user clicks OK

    public NewProjectDialog(JFrame parent) {
        super(parent, "New Project", true);
        buildUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // --- Title label ---
        JLabel titleLabel = new JLabel("CECS 544 Metrics Suite New Project");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // --- Form panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Project Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Project Name:"), gbc);
        gbc.gridx = 1;
        projectNameField = new JTextField(20);
        formPanel.add(projectNameField, gbc);

        // Product Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        productNameField = new JTextField(20);
        formPanel.add(productNameField, gbc);

        // Creator
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Creator:"), gbc);
        gbc.gridx = 1;
        creatorField = new JTextField(20);
        formPanel.add(creatorField, gbc);

        // Comments
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Comments:"), gbc);
        gbc.gridx = 1;
        commentsArea = new JTextArea(4, 20);
        commentsArea.setLineWrap(true);
        formPanel.add(new JScrollPane(commentsArea), gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- OK and Cancel buttons ---
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            // Require at least a project name
            if (projectNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Project Name is required.",
                        "Missing Field",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Create the project model object
            project = new Project(
                    projectNameField.getText().trim(),
                    productNameField.getText().trim(),
                    creatorField.getText().trim(),
                    commentsArea.getText().trim()
            );
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // MainFrame calls this after dialog closes to get the result
    // Returns null if user cancelled
    public Project getProject() {
        return project;
    }
}