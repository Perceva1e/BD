package ui.dialogs;

import model.DynamicEntity;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EntityFormDialog extends JDialog {
    private DynamicEntity entity;
    private JTextField nameField;
    private JPanel fieldsPanel;
    private Map<JTextField, JComboBox<String>> fieldInputs;

    public EntityFormDialog(JFrame parent) {
        super(parent, "Создание новой сущности", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        fieldInputs = new HashMap<>();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        inputPanel.add(new JLabel("Имя сущности:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        fieldsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        JButton addFieldBtn = new JButton("Добавить поле");
        addFieldBtn.addActionListener(e -> addFieldRow());
        inputPanel.add(addFieldBtn);
        inputPanel.add(new JLabel("Поля:"));
        inputPanel.add(fieldsPanel);

        add(new JScrollPane(inputPanel), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Сохранить");
        saveBtn.addActionListener(e -> saveEntity());
        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        addFieldRow();
    }

    private void addFieldRow() {
        JTextField fieldName = new JTextField();
        JComboBox<String> fieldType = new JComboBox<>(new String[]{"VARCHAR", "INTEGER", "DATE", "DECIMAL"});
        fieldsPanel.add(fieldName);
        fieldsPanel.add(fieldType);
        fieldInputs.put(fieldName, fieldType);
        fieldsPanel.revalidate();
        fieldsPanel.repaint();
    }

    private void saveEntity() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите имя сущности.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        entity = new DynamicEntity(name);
        for (Map.Entry<JTextField, JComboBox<String>> entry : fieldInputs.entrySet()) {
            String fieldName = entry.getKey().getText().trim();
            String fieldType = (String) entry.getValue().getSelectedItem();
            if (fieldName.equalsIgnoreCase("ID")) {
                JOptionPane.showMessageDialog(this, "Поле с именем 'ID' недопустимо, так как оно добавляется автоматически.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!fieldName.isEmpty()) {
                entity.addField(fieldName, fieldType);
            }
        }

        if (entity.getFields().size() <= 1) { // Только ID
            JOptionPane.showMessageDialog(this, "Добавьте хотя бы одно поле.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dispose();
    }

    public DynamicEntity getEntity() {
        return entity;
    }
}