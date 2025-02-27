package Login_System;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RoomManagementFrame extends JFrame {
    private Admin admin;
    private List<Room> rooms;
    private List<User> users;

    public RoomManagementFrame(Admin admin, List<Room> rooms, List<User> users) {
        this.admin = admin;
        this.rooms = rooms;
        this.users = users;

      
        setTitle("Room Management");

        
        setSize(400, 300);

       
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       
        setLayout(new GridLayout(3, 1, 10, 10));

       
        JButton addRoomButton = new JButton("Add Room");
        addRoomButton.addActionListener(e -> showAddRoomDialog());
        add(addRoomButton);

       
        JButton deleteRoomButton = new JButton("Delete Room");
        deleteRoomButton.addActionListener(e -> showDeleteRoomDialog());
        add(deleteRoomButton);

        
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> {
            this.setVisible(false);
            new AdminDashboardFrame(admin, rooms, users).setVisible(true);
        });
        add(returnButton);
    }

    private void showAddRoomDialog() {
        JDialog dialog = new JDialog(this, "Add Room", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel roomNumberLabel = new JLabel("Room Number:");
        JTextField roomNumberText = new JTextField();
        dialog.add(roomNumberLabel);
        dialog.add(roomNumberText);

        JLabel roomTypeLabel = new JLabel("Room Type:");
        JComboBox<RoomType> roomTypeComboBox = new JComboBox<>(RoomType.values());
        dialog.add(roomTypeLabel);
        dialog.add(roomTypeComboBox);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            String roomNumber = roomNumberText.getText();
            RoomType roomType = (RoomType) roomTypeComboBox.getSelectedItem();
            admin.createRoom(roomType, roomNumber, ""); 
            rooms.add(new Room(roomType, null, null, null, "", roomNumber, Room.RoomStatus.vacantRoom));
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Room created successfully");
        });
        dialog.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }

    private void showDeleteRoomDialog() {
        JDialog dialog = new JDialog(this, "Delete Room", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(rooms.size() + 1, 1, 10, 10));

        for (Room room : rooms) {
            JButton roomButton = new JButton(room.getRoomNumber());
            roomButton.addActionListener(e -> {
                int response = JOptionPane.showConfirmDialog(this, "Confirm room deletion " + room.getRoomNumber() + " ？", "Confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    admin.deleteRoom(room.getRoomNumber());
                    rooms.remove(room);
                    JOptionPane.showMessageDialog(this, "Room deletes successfully");
                    dialog.dispose();
                }
            });
            dialog.add(roomButton);
        }

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }
}
