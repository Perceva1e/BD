package model;

import java.time.LocalDate;

public class Booking {
    private int id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int totalCost;
    private String status;
    private int clientId;
    private int employeeId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    public int getTotalCost() { return totalCost; }
    public void setTotalCost(int totalCost) { this.totalCost = totalCost; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    @Override
    public String toString() {
        return String.format(
                "Booking [ID=%d, Check-In=%s, Check-Out=%s, Cost=%d, Status=%s, ClientID=%d, EmployeeID=%d]",
                id, checkInDate, checkOutDate, totalCost, status, clientId, employeeId
        );
    }
}