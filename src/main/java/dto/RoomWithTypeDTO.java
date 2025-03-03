package dto;

public class RoomWithTypeDTO {
    private final int roomId;
    private final int area;
    private final String roomType;
    private final int costPerNight;

    public RoomWithTypeDTO(int roomId, int area, String roomType, int costPerNight) {
        this.roomId = roomId;
        this.area = area;
        this.roomType = roomType;
        this.costPerNight = costPerNight;
    }

    @Override
    public String toString() {
        return String.format("Room #%d | Area: %d m? | Type: %s | Nightly Cost: %d",
                roomId, area, roomType, costPerNight);
    }
}