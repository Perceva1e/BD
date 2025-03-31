package ui.dialogs;

import model.SavedQuery;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class QueryEditorDialog extends JDialog {
    private final JTextField txtName = new JTextField(25);
    private final JTextArea txtSQL = new JTextArea(10, 40);
    private boolean saved = false;

    public QueryEditorDialog(JFrame parent) {
        super(parent, "Query Editor", true);
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        inputPanel.add(createLabeledField("Query Name:", txtName));
        inputPanel.add(createLabeledField("SQL Query:", new JScrollPane(txtSQL)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveQuery());

        buttonPanel.add(btnSave);
        buttonPanel.add(new JButton(new AbstractAction("Cancel") {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }));

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(getParent());
    }

    private JPanel createLabeledField(String label, Component field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel(label), BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void saveQuery() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Query name is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtSQL.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "SQL query cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        saved = true;
        dispose();
    }

    public SavedQuery getQuery() {
        if (!saved) return null;
        SavedQuery query = new SavedQuery();
        query.setName(txtName.getText().trim());
        query.setSql(txtSQL.getText().trim());
        return query;
    }
}