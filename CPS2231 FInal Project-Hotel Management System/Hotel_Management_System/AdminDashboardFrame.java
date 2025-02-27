package Login_System;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminDashboardFrame extends JFrame {
    private List<Room> rooms;
    private List<User> users;
    private Admin admin;

    public AdminDashboardFrame(Admin admin, List<Room> rooms, List<User> users) {
        this.admin = admin;
        this.rooms = rooms;
        this.users = users;

        
        setTitle("Admin Dashboard");

       
        setSize(500, 400);

     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridLayout(4, 1, 10, 10));

        
        JButton manageRoomsButton = new JButton("Room Management ");
        manageRoomsButton.addActionListener(e -> manageRooms());
        add(manageRoomsButton);

       
        JButton manageUsersButton = new JButton("User Management");
        manageUsersButton.addActionListener(e -> manageUsers());
        add(manageUsersButton);

       
        JButton bookRoomButton = new JButton("Room Booking");
        bookRoomButton.addActionListener(e -> bookRoom());
        add(bookRoomButton);

   
        JButton logoutButton = new JButton("Log out");
        logoutButton.addActionListener(e -> logout());
        add(logoutButton);
    }

    private void manageRooms() {
        this.setVisible(false);
        new RoomManagementFrame(admin, rooms, users).setVisible(true);
    }

    private void manageUsers() {
        this.setVisible(false);
        new UserManagementFrame(admin, users).setVisible(true);
    }

    private void bookRoom() {
        this.setVisible(false);
        new BookingRoomFrame(admin, rooms, users).setVisible(true);
    }

    private void logout() {
        this.setVisible(false);
        new HotelManagementSystem(users).setVisible(true);
    }
}
