package ui.dialogs;

import controller.EmployeeController;
import model.Employee;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeFormDialog extends JDialog {
    private final JTextField txtFullName = new JTextField(20);
    private final JTextField txtPosition = new JTextField(15);
    private final JTextField txtSalary = new JTextField(10);
    private final JTextField txtSchedule = new JTextField(30);

    public EmployeeFormDialog(Employee employee, EmployeeController controller, Runnable onSuccess) {
        setTitle(employee == null ? "New Employee" : "Edit Employee");
        setLayout(new BorderLayout());
        setModal(true);

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        fieldsPanel.add(new JLabel("Full Name:"));
        fieldsPanel.add(txtFullName);
        fieldsPanel.add(new JLabel("Position:"));
        fieldsPanel.add(txtPosition);
        fieldsPanel.add(new JLabel("Salary:"));
        fieldsPanel.add(txtSalary);
        fieldsPanel.add(new JLabel("Schedule (YYYY-MM-DD, comma sep):"));
        fieldsPanel.add(txtSchedule);

        if (employee != null) {
            txtFullName.setText(employee.getFullName());
            txtPosition.setText(employee.getPosition());
            txtSalary.setText(String.valueOf(employee.getSalary()));
            if (employee.getWorkSchedule() != null) {
                txtSchedule.setText(employee.getWorkSchedule().stream()
                        .map(LocalDate::toString)
                        .collect(Collectors.joining(", ")));
            }
        }

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveEmployee(employee, controller, onSuccess));

        add(fieldsPanel, BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void saveEmployee(Employee employee, EmployeeController controller, Runnable onSuccess) {
        try {
            Employee newEmployee = new Employee();
            if (employee != null) newEmployee.setId(employee.getId());

            newEmployee.setFullName(txtFullName.getText());
            newEmployee.setPosition(txtPosition.getText());
            newEmployee.setSalary(Integer.parseInt(txtSalary.getText()));

            if (!txtSchedule.getText().isEmpty()) {
                List<LocalDate> schedule = Arrays.stream(txtSchedule.getText().split(","))
                        .map(String::trim)
                        .map(LocalDate::parse)
                        .toList();
                newEmployee.setWorkSchedule(schedule);
            }

            if (employee == null) {
                controller.addEmployee(newEmployee);
            } else {
                controller.updateEmployee(newEmployee);
            }

            onSuccess.run();
            dispose();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary format!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}