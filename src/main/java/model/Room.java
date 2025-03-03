package model;

public class Room {
    private int id;
    private int area;
    private int cost;
    private int maxGuests;
    private String amenities;
    private int roomTypeId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getArea() { return area; }
    public void setArea(int area) { this.area = area; }
    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
    public int getMaxGuests() { return maxGuests; }
    public void setMaxGuests(int maxGuests) { this.maxGuests = maxGuests; }
    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }
    public int getRoomTypeId() { return roomTypeId; }
    public void setRoomTypeId(int roomTypeId) { this.roomTypeId = roomTypeId; }

    @Override
    public String toString() {
        return String.format(
                "Room [ID=%d, Area=%d, Cost=%d, MaxGuests=%d, Amenities=%s, RoomTypeID=%d]",
                id, area, cost, maxGuests, amenities, roomTypeId
        );
    }
}