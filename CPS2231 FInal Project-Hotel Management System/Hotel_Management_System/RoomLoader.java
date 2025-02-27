package Login_System;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomLoader {
    private static final String ROOM_FOLDER_PATH = "/Users/matsumatsu/eclipse-workspace/testing/src/Login_System/Room";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static List<Room> loadRooms() {
        List<Room> rooms = new ArrayList<>();
        File folder = new File(ROOM_FOLDER_PATH);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    Room room = loadRoomFromFile(file);
                    if (room != null) {
                        rooms.add(room);
                    }
                }
            }
        }
        return rooms;
    }

    private static Room loadRoomFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String roomTypeStr = parseLine(br.readLine());
            String roomStatusStr = parseLine(br.readLine());
            String remarks = parseLine(br.readLine());
            String bookedGuestStr = parseLine(br.readLine());
            String bookingStartTimeStr = parseLine(br.readLine());
            String bookingEndTimeStr = parseLine(br.readLine());

            RoomType roomType = RoomType.valueOf(roomTypeStr);
            Room.RoomStatus roomStatus = Room.RoomStatus.valueOf(roomStatusStr);
            Guest bookedGuest = bookedGuestStr.equals("null") ? null : new Guest("Guest", bookedGuestStr, "", "", "");
            Date bookingStartTime = bookingStartTimeStr.equals("null") ? null : dateFormat.parse(bookingStartTimeStr);
            Date bookingEndTime = bookingEndTimeStr.equals("null") ? null : dateFormat.parse(bookingEndTimeStr);
            String roomNumber = file.getName().replace(".txt", "");

            return new Room(roomType, bookingStartTime, bookingEndTime, bookedGuest, remarks, roomNumber, roomStatus);
        } 
        catch (IOException | java.text.ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String parseLine(String line) {
        if (line == null || !line.contains(": ")) {
            return "null";
        }
        String[] parts = line.split(": ", 2);
        return parts.length > 1 ? parts[1] : "null";
    }
}
