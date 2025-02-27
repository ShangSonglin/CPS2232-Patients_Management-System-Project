package Login_System;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BookingRoomFrame extends JFrame {
    private Admin admin;
    private List<Room> rooms;
    private List<User> users;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public BookingRoomFrame(Admin admin, List<Room> rooms, List<User> users) {
        this.admin = admin;
        this.rooms = rooms;
        this.users = users;

        setTitle("Booking Management");

        setSize(600, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel availableRoomsPanel = new JPanel();
        availableRoomsPanel.setLayout(new GridLayout(rooms.size(), 1, 10, 10));

        JPanel bookedRoomsPanel = new JPanel();
        bookedRoomsPanel.setLayout(new GridLayout(rooms.size(), 1, 10, 10));

        JLabel availableRoomsLabel = new JLabel("Available Rooms", SwingConstants.CENTER);
        availableRoomsLabel.setFont(new Font("Serif", Font.BOLD, 20));
        availableRoomsPanel.add(availableRoomsLabel);

        JLabel bookedRoomsLabel = new JLabel("Booked Rooms", SwingConstants.CENTER);
        bookedRoomsLabel.setFont(new Font("Serif", Font.BOLD, 20));
        bookedRoomsPanel.add(bookedRoomsLabel);

        for (Room room : rooms) {
            if (room.getRoomStatus() == Room.RoomStatus.vacantRoom) {
                JButton roomButton = new JButton(room.getRoomNumber() + ": " + room.getRoomType());
                roomButton.addActionListener(e -> showBookingDialog(room));
                availableRoomsPanel.add(roomButton);
            } else {
                JLabel bookedRoomLabel = new JLabel(room.getRoomNumber() + ": " + room.getRoomType() + " (Booked by " + room.getBookedGuest().getRealName() + ")");
                bookedRoomsPanel.add(bookedRoomLabel);
            }
        }

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.add(availableRoomsPanel);
        centerPanel.add(bookedRoomsPanel);
        add(centerPanel, BorderLayout.CENTER);

        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> {
            this.setVisible(false);
            new AdminDashboardFrame(admin, rooms, users).setVisible(true);
        });
        add(returnButton, BorderLayout.SOUTH);
    }

    private void showBookingDialog(Room room) {
        JDialog dialog = new JDialog(this, "Book Room", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(7, 2, 10, 10));

        JLabel guestNameLabel = new JLabel("Guest Name:");
        JTextField guestNameText = new JTextField();
        dialog.add(guestNameLabel);
        dialog.add(guestNameText);

        JLabel guestIdLabel = new JLabel("Guest ID:");
        JTextField guestIdText = new JTextField();
        dialog.add(guestIdLabel);
        dialog.add(guestIdText);

        JLabel bookingStartDateLabel = new JLabel("Booking Start Date (yyyy-MM-dd):");
        JTextField bookingStartDateText = new JTextField();
        dialog.add(bookingStartDateLabel);
        dialog.add(bookingStartDateText);

        JLabel bookingEndDateLabel = new JLabel("Booking End Date (yyyy-MM-dd):");
        JTextField bookingEndDateText = new JTextField();
        dialog.add(bookingEndDateLabel);
        dialog.add(bookingEndDateText);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            String guestName = guestNameText.getText();
            String guestId = guestIdText.getText();
            String bookingStartDateStr = bookingStartDateText.getText();
            String bookingEndDateStr = bookingEndDateText.getText();

            try {
                Date bookingStartDate = dateFormat.parse(bookingStartDateStr);
                Date bookingEndDate = dateFormat.parse(bookingEndDateStr);

                Guest guest = new Guest("Guest", guestName, "", guestName, guestId);
                admin.addGuest(guest);
                admin.addRoomBooking(room.getRoomNumber(), guest, bookingStartDate, bookingEndDate);
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Room booked successfully!");
                this.setVisible(false);
                new BookingRoomFrame(admin, rooms, users).setVisible(true);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
            }
        });
        dialog.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }
}
