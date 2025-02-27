package Login_System;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class GuestRegistrationFrame extends JFrame {
    private List<User> users;

    public GuestRegistrationFrame(List<User> users) {
        this.users = users;

        // Set title
        setTitle("Guest Registration");

        // Set window size
        setSize(400, 400);

        // Set the default shutdown operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set window layout
        setLayout(new GridLayout(6, 2, 10, 10));

        // Create form labels and text fields
        JLabel userTypeLabel = new JLabel("User Type:");
        JTextField userTypeText = new JTextField("Guest");
        userTypeText.setEnabled(false); // The userType cannot be changed.
        add(userTypeLabel);
        add(userTypeText);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameText = new JTextField();
        add(usernameLabel);
        add(usernameText);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField();
        add(passwordLabel);
        add(passwordText);
        
        JLabel realNameLabel = new JLabel("Real Name:");
        JTextField realNameText = new JTextField();
        add(realNameLabel);
        add(realNameText);

        JLabel idNumberLabel = new JLabel("ID Number:");
        JTextField idNumberText = new JTextField();
        add(idNumberLabel);
        add(idNumberText);

       
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());
            String realName = realNameText.getText();
            String idNumber = idNumberText.getText();

            if (isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(this, "Username is already taken.");
                return;
            }

            if (isPasswordValid(password)) {
                Guest newGuest = new Guest("Guest", username, password, realName, idNumber);
                users.add(newGuest);
                saveUserToFile(newGuest);
                JOptionPane.showMessageDialog(this, "Registration successful!");
                this.setVisible(false);
                new HotelManagementSystem(users).setVisible(true);
            } 
            else {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long and contain both letters and numbers.");
            }
        });
        add(registerButton);
    }

    private boolean isUsernameTaken(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            return false;
        }
        Pattern letter = Pattern.compile("[a-zA-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        return letter.matcher(password).find() && digit.matcher(password).find();
    }

    private void saveUserToFile(Guest guest) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/matsumatsu/eclipse-workspace/testing/src/Login_System/User.txt",true))) {
            bw.write(String.format("%s,%s,%s,%s,%s%n", guest.getUserType(), guest.getUsername(), guest.getPassword(), guest.getRealName(), guest.getIdNumber()));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
