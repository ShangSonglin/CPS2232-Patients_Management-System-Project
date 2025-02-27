package Login_System;


import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class ForgotPasswordFrame extends JFrame {
    private List<User> users;

    public ForgotPasswordFrame(List<User> users) {
        this.users = users;

        
        setTitle("Forgot Password");

       
        setSize(400, 300);

        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        setLayout(new GridLayout(5, 2, 10, 10));

        
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameText = new JTextField();
        add(usernameLabel);
        add(usernameText);

        JLabel realNameLabel = new JLabel("Real Name:");
        JTextField realNameText = new JTextField();
        add(realNameLabel);
        add(realNameText);

        JLabel idNumberLabel = new JLabel("ID Number:");
        JTextField idNumberText = new JTextField();
        add(idNumberLabel);
        add(idNumberText);

        JLabel newPasswordLabel = new JLabel("New Password:");
        JPasswordField newPasswordText = new JPasswordField();
        add(newPasswordLabel);
        add(newPasswordText);

        
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String username = usernameText.getText();
            String realName = realNameText.getText();
            String idNumber = idNumberText.getText();
            String newPassword = new String(newPasswordText.getPassword());

            if (!isPasswordValid(newPassword)) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long and contain both letters and numbers.");
                return;
            }

            boolean updated = false;
            for (User user : users) {
                if (user instanceof Guest) {
                    Guest guest = (Guest) user;
                    if (guest.getUsername().equals(username) && guest.getRealName().equals(realName) && guest.getIdNumber().equals(idNumber)) {
                        guest.setPassword(newPassword);
                        updated = true;
                        break;
                    }
                }
            }

            if (updated) {
                saveUsersToFile();
                JOptionPane.showMessageDialog(this, "Password updated successfully!");
                this.setVisible(false);
                new HotelManagementSystem(users).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "User not found or information does not match.");
            }
        });
        add(submitButton);
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            return false;
        }
        Pattern letter = Pattern.compile("[a-zA-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        return letter.matcher(password).find() && digit.matcher(password).find();
    }

    private void saveUsersToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/matsumatsu/eclipse-workspace/testing/src/Login_System/User.txt"))) {
            for (User user : users) {
                if (user instanceof Guest) {
                    Guest guest = (Guest) user;
                    bw.write(String.format("%s,%s,%s,%s,%s%n", guest.getUserType(), guest.getUsername(), guest.getPassword(), guest.getRealName(), guest.getIdNumber()));
                } else {
                    bw.write(String.format("%s,%s,%s%n", user.getUserType(), user.getUsername(), user.getPassword()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

