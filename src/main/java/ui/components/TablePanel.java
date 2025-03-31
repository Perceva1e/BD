package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class TablePanel extends JPanel {
    protected JTable table;
    protected JButton btnAdd;
    protected JButton btnEdit;
    protected JButton btnDelete;

    public TablePanel(String title) {
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        // Table
        table = new JTable();
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Toolbar
        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAdd = createToolButton("Add", e -> onAdd());
        btnEdit = createToolButton("Edit", e -> onEdit());
        btnDelete = createToolButton("Delete", e -> onDelete());

        toolPanel.add(btnAdd);
        toolPanel.add(btnEdit);
        toolPanel.add(btnDelete);
        add(toolPanel, BorderLayout.SOUTH);
    }

    private JButton createToolButton(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFocusable(false);
        btn.setMargin(new Insets(5, 15, 5, 15));
        btn.addActionListener(listener);
        return btn;
    }

    protected abstract void onAdd();
    protected abstract void onEdit();
    protected abstract void onDelete();

    public void refreshTable() {
        table.updateUI();
    }
}