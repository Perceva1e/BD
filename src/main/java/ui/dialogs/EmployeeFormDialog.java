package ui.dialogs;

import controller.EmployeeController;
import model.Employee;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EmployeeFormDialog extends JDialog {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s]{3,}$");
    private static final Pattern POSITION_PATTERN = Pattern.compile("^[A-Za-z\\s-]{3,}$");

    private final JTextField txtFullName = new JTextField(20);
    private final JTextField txtPosition = new JTextField(15);
    private final JTextField txtSalary = new JTextField(10);
    private final JTextField txtSchedule = new JTextField(30);
    private final EmployeeController controller;
    private final Runnable onSuccess;
    private final Employee editingEmployee;

    public EmployeeFormDialog(Employee employee, EmployeeController controller, Runnable onSuccess) {
        this.editingEmployee = employee;
        this.controller = controller;
        this.onSuccess = onSuccess;

        setTitle(editingEmployee == null ? "New Employee" : "Edit Employee");
        initUI();
        setupWindow();
    }

    private void initUI() {
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        fieldsPanel.add(new JLabel("Full Name* (3+ letters):"));
        fieldsPanel.add(txtFullName);
        fieldsPanel.add(new JLabel("Position* (3+ chars):"));
        fieldsPanel.add(txtPosition);
        fieldsPanel.add(new JLabel("Salary* (>0):"));
        fieldsPanel.add(txtSalary);
        fieldsPanel.add(new JLabel("Schedule (YYYY-MM-DD, comma sep):"));
        fieldsPanel.add(txtSchedule);

        if (editingEmployee != null) {
            txtFullName.setText(editingEmployee.getFullName());
            txtPosition.setText(editingEmployee.getPosition());
            txtSalary.setText(String.valueOf(editingEmployee.getSalary()));
            if (editingEmployee.getWorkSchedule() != null) {
                txtSchedule.setText(editingEmployee.getWorkSchedule().stream()
                        .map(LocalDate::toString)
                        .collect(Collectors.joining(", ")));
            }
        }

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveEmployee());

        add(fieldsPanel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
    }

    private void setupWindow() {
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        setVisible(true);
    }

    private void saveEmployee() {
        try {
            StringBuilder errors = new StringBuilder();

            String name = txtFullName.getText().trim();
            if (!NAME_PATTERN.matcher(name).matches()) {
                errors.append("• Invalid name format! (3+ letters)\n");
            }

            String position = txtPosition.getText().trim();
            if (!POSITION_PATTERN.matcher(position).matches()) {
                errors.append("• Invalid position format! (3+ chars)\n");
            }

            int salary = 0;
            try {
                salary = Integer.parseInt(txtSalary.getText().trim());
                if (salary <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                errors.append("• Salary must be positive integer\n");
            }

            List<LocalDate> schedule = Collections.emptyList();
            if (!txtSchedule.getText().isEmpty()) {
                try {
                    schedule = parseSchedule(txtSchedule.getText());
                } catch (DateTimeParseException e) {
                    errors.append("• Invalid date format in schedule\n");
                }
            }

            if (errors.length() > 0) {
                showError("Validation errors:\n" + errors);
                return;
            }

            Employee employee = new Employee();
            if (editingEmployee != null) employee.setId(editingEmployee.getId());

            employee.setFullName(name);
            employee.setPosition(position);
            employee.setSalary(salary);
            employee.setWorkSchedule(schedule);

            if (editingEmployee == null) {
                controller.addEmployee(employee);
            } else {
                controller.updateEmployee(employee);
            }

            onSuccess.run();
            dispose();
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private List<LocalDate> parseSchedule(String input) throws DateTimeParseException {
        LocalDate today = LocalDate.now();
        LocalDate prevDate = null;

        return Arrays.stream(input.split(","))
                .map(String::trim)
                .map(s -> {
                    LocalDate date = LocalDate.parse(s);
                    if (date.isBefore(today)) {
                        throw new DateTimeParseException("Date in past", s, 0);
                    }
                    if (prevDate != null && date.isBefore(prevDate)) {
                        throw new DateTimeParseException("Wrong order", s, 0);
                    }
                    return date;
                })
                .toList();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}