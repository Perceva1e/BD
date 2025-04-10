package service;

import dao.*;
import dto.*;
import model.*;
import util.DatabaseConnection;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    private final EmployeeDAO employeeDAO;
    private final ClientDAO clientDAO;
    private final BookingDAO bookingDAO;
    private final PaymentDAO paymentDAO;
    private final RoomTypeDAO roomTypeDAO;
    private final RoomDAO roomDAO;
    private final ServiceDAO serviceDAO;

    public ReportService() {
        this.employeeDAO = new EmployeeDAO();
        this.clientDAO = new ClientDAO();
        this.bookingDAO = new BookingDAO();
        this.paymentDAO = new PaymentDAO();
        this.roomTypeDAO = new RoomTypeDAO();
        this.roomDAO = new RoomDAO();
        this.serviceDAO = new ServiceDAO();
    }

    public List<Employee> getHighPaidEmployees() throws SQLException {
        return employeeDAO.getEmployeesBySalary(50000);
    }

    public List<Client> getAllClientsSorted() throws SQLException {
        return clientDAO.getAllClientsSorted();
    }

    public List<Booking> getActiveBookings() throws SQLException {
        return bookingDAO.getBookingsByStatus("Booked");
    }

    public List<BookingClientDTO> getBookingsWithClients() throws SQLException {
        return bookingDAO.getBookingsWithClients();
    }

    public List<Payment> getRecentPayments() throws SQLException {
        return paymentDAO.getPaymentsByPeriod(1.7);
    }

    public List<PaymentBookingDTO> getCreditCardPayments() throws SQLException {
        return paymentDAO.getCreditCardPayments();
    }

    public List<RoomType> getExpensiveRoomTypes() throws SQLException {
        return roomTypeDAO.getRoomTypesByPrice(1500);
    }

    public List<Room> getLargeCapacityRooms() throws SQLException {
        return roomDAO.getRoomsByGuestCapacity(2);
    }

    public List<RoomWithTypeDTO> getRoomsWithTypes() throws SQLException {
        return roomDAO.getRoomsWithTypes();
    }

    public List<Service> getLongServices() throws SQLException {
        return serviceDAO.getServicesByDuration(Duration.ofHours(1));
    }
    public List<UnusedServiceDTO> printUnusedServices() throws SQLException {
        return serviceDAO.getUnusedServices();
    }
    public List<ManagerBookingDTO> printManagerBookings() throws SQLException {
        return bookingDAO.getManagerBookings();
    }
    public List<ServiceUsageDTO> printServiceUsage() throws SQLException {
        return serviceDAO.getServiceUsageWithDates();
    }
    public List<Employee> getHighAveragePaidEmployees() throws SQLException {
        return employeeDAO.getHighPaidEmployees();
    }

    public List<Client> getClientsWithLuxuryBookings() throws SQLException {
        return clientDAO.getClientsWithLuxuryBookings();
    }
    public List<Booking> getAboveAverageBookings() throws SQLException {
        return bookingDAO.getAboveAverageBookings();
    }

    public List<PaymentStatsDTO> getPaymentStats() throws SQLException {
        return paymentDAO.getPaymentStats();
    }
    public List<RoomTypeCostDTO> getRoomTypeCostStats() throws SQLException {
        return roomTypeDAO.getMinMaxCostByCategory();
    }

    public List<Room> getRoomsAboveAverageArea() throws SQLException {
        return roomDAO.getRoomsAboveAverageArea();
    }

    public List<Room> getRoomsWithAmenities() throws SQLException {
        return roomDAO.getRoomsWithAmenities("\\yKitchen\\y", "\\yJacuzzi\\y");
    }

    public List<ServiceCostDTO> getServiceCostByCategory() throws SQLException {
        return serviceDAO.getServiceCostByCategory();
    }

    public List<ClientSpendingDTO> getClientSpendingAboveAverage() throws SQLException {
        return bookingDAO.getClientSpendingAboveAverage();
    }

    public List<PaymentTypeCountDTO> getPaymentTypeCounts() throws SQLException {
        return paymentDAO.getPaymentTypeCounts();
    }

    public List<Room> getRoomsWithKitchenOrJacuzzi() throws SQLException {
        return roomDAO.getRoomsWithKitchenOrJacuzzi();
    }
    public List<Object[]> executeRawQuery(String sql) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Object[]> results = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                results.add(row);
            }
            return results;
        }
    }
}