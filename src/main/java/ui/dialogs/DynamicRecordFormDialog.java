package ui.dialogs;

import controller.EntityController;
import model.DynamicEntity;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DynamicRecordFormDialog extends JDialog {
    private final DynamicEntity entity;
    private final EntityController controller;
    private final Runnable refreshCallback;
    private Map<String, JTextField> fieldInputs;
    private Map<String, Object> existingRecord;

    public DynamicRecordFormDialog(Map<String, Object> record, DynamicEntity entity, EntityController controller, Runnable refreshCallback) {
        super((JFrame) null, record == null ? "Добавить запись" : "Редактировать запись", true);
        this.entity = entity;
        this.controller = controller;
        this.refreshCallback = refreshCallback;
        this.existingRecord = record;
        this.fieldInputs = new HashMap<>();
        initUI();
        if (record != null) {
            populateFields(record);
        }
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        for (Map.Entry<String, String> field : entity.getFields().entrySet()) {
            if (!field.getKey().equalsIgnoreCase("ID")) {
                inputPanel.add(new JLabel(field.getKey() + " (" + field.getValue() + "):"));
                JTextField textField = new JTextField();
                fieldInputs.put(field.getKey().toUpperCase(), textField);
                inputPanel.add(textField);
            }
        }

        add(new JScrollPane(inputPanel), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Сохранить");
        saveBtn.addActionListener(e -> saveRecord());
        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populateFields(Map<String, Object> record) {
        for (Map.Entry<String, Object> entry : record.entrySet()) {
            JTextField field = fieldInputs.get(entry.getKey());
            if (field != null) {
                field.setText(entry.getValue() != null ? entry.getValue().toString() : "");
            }
        }
    }

    private void saveRecord() {
        Map<String, Object> record = new HashMap<>();
        for (Map.Entry<String, JTextField> entry : fieldInputs.entrySet()) {
            String value = entry.getValue().getText().trim();
            if (!value.isEmpty()) {
                record.put(entry.getKey(), convertValue(value, entity.getFields().get(entry.getKey())));
            }
        }

        try {
            if (existingRecord == null) {
                controller.addRecord(entity.getName(), record);
            } else {
                System.out.println("Existing Record: " + existingRecord);
                record.put("ID", existingRecord.get("ID"));
                System.out.println("Record to update: " + record);
                controller.updateRecord(entity.getName(), record);
            }
            refreshCallback.run();
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка сохранения записи: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Object convertValue(String value, String type) {
        return switch (type) {
            case "INTEGER" -> Integer.parseInt(value);
            case "DECIMAL" -> Double.parseDouble(value);
            case "DATE" -> java.sql.Date.valueOf(value);
            default -> value;
        };
    }
}