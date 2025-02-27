package Login_System;
import Login_System.User;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Admin extends User {
    private List<Room> rooms;
    private List<Guest> guests;
    private static final String ROOM_FOLDER_PATH = "/Users/matsumatsu/eclipse-workspace/testing/src/Login_System/User.txt";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Admin(String userType, String username, String password) {
        super(userType, username, password);
        this.rooms = new ArrayList<>();
        this.guests = new ArrayList<>();
    }

    public void createRoom(RoomType roomType, String roomNumber, String remarks) {
        Room room = new Room(roomType, null, null, null, remarks, roomNumber, Room.RoomStatus.vacantRoom);
        rooms.add(room);
        saveRoomToFile(room);
    }

    public void addExistingRoom(Room room) { 
        rooms.add(room);
        saveRoomToFile(room);
    }

    public void deleteRoom(String roomNumber) {
        rooms.removeIf(room -> room.getRoomNumber().equals(roomNumber));
        deleteRoomFile(roomNumber);
    }

    public void addGuest(Guest guest) {
        guests.add(guest);
    }

    public void addRoomBooking(String roomNumber, Guest guest, Date bookingStartTime, Date bookingEndTime) {
        for (Room room : rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                room.setBookedGuest(guest);
                room.setBookingStartTime(bookingStartTime);
                room.setBookingEndTime(bookingEndTime);
                room.setRoomStatus(Room.RoomStatus.Booking);
                saveRoomToFile(room);
                break;
            }
        }
    }

    private void saveRoomToFile(Room room) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ROOM_FOLDER_PATH + room.getRoomNumber() + ".txt"))) {
            bw.write("Room Type: " + room.getRoomType() + "\n");
            bw.write("Room Status: " + room.getRoomStatus() + "\n");
            bw.write("Remarks: " + room.getRemarks() + "\n");
            if (room.getRoomStatus() == Room.RoomStatus.Booking || room.getRoomStatus() == Room.RoomStatus.CheckingIn) {
                bw.write("Booked Guest: " + room.getBookedGuest().getUsername() + "\n");
                bw.write("Booking Start Time: " + dateFormat.format(room.getBookingStartTime()) + "\n");
                bw.write("Booking End Time: " + dateFormat.format(room.getBookingEndTime()) + "\n");
            } else {
                bw.write("Booked Guest: null\n");
                bw.write("Booking Start Time: null\n");
                bw.write("Booking End Time: null\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteRoomFile(String roomNumber) {
        java.io.File file = new java.io.File(ROOM_FOLDER_PATH + roomNumber + ".txt");
        if (file.exists()) {
            file.delete();
        }
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Guest> getGuests() {
        return guests;
    }
}
