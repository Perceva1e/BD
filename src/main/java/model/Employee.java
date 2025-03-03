package model;

import java.time.LocalDate;
import java.util.List;

public class Employee {
    private int id;
    private String fullName;
    private String position;
    private int salary;
    private List<LocalDate> workSchedule;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public int getSalary() { return salary; }
    public void setSalary(int salary) { this.salary = salary; }
    public List<LocalDate> getWorkSchedule() { return workSchedule; }
    public void setWorkSchedule(List<LocalDate> workSchedule) { this.workSchedule = workSchedule; }

    @Override
    public String toString() {
        return String.format(
                "Employee [ID=%d, Name=%s, Position=%s, Salary=%d, Schedule=%s]",
                id, fullName, position, salary,
                workSchedule != null ? workSchedule : "No schedule"
        );
    }
}