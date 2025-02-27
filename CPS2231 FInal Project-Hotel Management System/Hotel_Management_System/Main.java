package Login_System;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "/Users/matsumatsu/eclipse-workspace/testing/src/Login_System/User.txt";
        List<User> users = UserLoader.loadUsers(filePath);
        List<Room> rooms = RoomLoader.loadRooms();

        HotelManagementSystem frame = new HotelManagementSystem(users);
        frame.setVisible(true);
    }
}
