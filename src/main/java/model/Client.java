package model;

import java.time.LocalDate;

public class Client {
    private int id;
    private String fullName;
    private String phone;
    private String passportNumber;
    private LocalDate birthDate;

    public Client() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    @Override
    public String toString() {
        return String.format("Client [ID=%d, Name=%s, Phone=%s, Passport=%s, BirthDate=%s]",
                id, fullName, phone, passportNumber, birthDate);
    }
}