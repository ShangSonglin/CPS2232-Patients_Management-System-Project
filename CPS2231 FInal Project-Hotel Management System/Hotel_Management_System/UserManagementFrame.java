package Login_System;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class UserManagementFrame extends JFrame {
    private Admin admin;
    private List<User> users;

    public UserManagementFrame(Admin admin, List<User> users) {
        this.admin = admin;
        this.users = users;

        setTitle("User Management");

        setSize(400, 400);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> showAddUserDialog());
        panel.add(addUserButton);

        JButton manageUsersButton = new JButton("Modify/Delete User");
        manageUsersButton.addActionListener(e -> showManageUsersDialog());
        panel.add(manageUsersButton);

        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> {
            this.setVisible(false);
            new AdminDashboardFrame(admin, null, users).setVisible(true);
        });
        panel.add(returnButton);

        add(panel, BorderLayout.CENTER);
    }

    private void showAddUserDialog() {
        JDialog dialog = new JDialog(this, "Add User", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel userTypeLabel = new JLabel("User Type:");
        JComboBox<String> userTypeComboBox = new JComboBox<>(new String[] {"Admin", "Guest"});
        dialog.add(userTypeLabel);
        dialog.add(userTypeComboBox);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameText = new JTextField();
        dialog.add(usernameLabel);
        dialog.add(usernameText);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField();
        dialog.add(passwordLabel);
        dialog.add(passwordText);

        JLabel realNameLabel = new JLabel("Real Name:");
        JTextField realNameText = new JTextField();
        dialog.add(realNameLabel);
        dialog.add(realNameText);

        JLabel idNumberLabel = new JLabel("ID Number:");
        JTextField idNumberText = new JTextField();
        dialog.add(idNumberLabel);
        dialog.add(idNumberText);

        userTypeComboBox.addActionListener(e -> {
            if (userTypeComboBox.getSelectedItem().equals("Admin")) {
                realNameText.setEnabled(false);
                idNumberText.setEnabled(false);
            } else {
                realNameText.setEnabled(true);
                idNumberText.setEnabled(true);
            }
        });

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            String userType = (String) userTypeComboBox.getSelectedItem();
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());
            String realName = realNameText.getText();
            String idNumber = idNumberText.getText();

            if (isPasswordValid(password)) {
                User newUser;
                if ("Admin".equals(userType)) {
                    newUser = new Admin(userType, username, password);
                } else {
                    newUser = new Guest(userType, username, password, realName, idNumber);
                }
                users.add(newUser);
                saveUserToFile(newUser);
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "User created successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Passwords must be at least 6 characters long and contain letters and numbers.");
            }
        });
        dialog.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }

    private void showManageUsersDialog() {
        JDialog dialog = new JDialog(this, "Modify/Delete User", true);
        dialog.setSize(300, 400);
        dialog.setLayout(new GridLayout(users.size() + 1, 1, 10, 10));

        for (User user : users) {
            JButton userButton = new JButton(user instanceof Guest ? ((Guest) user).getRealName() : user.getUsername());
            userButton.addActionListener(e -> showEditUserDialog(user));
            dialog.add(userButton);
        }

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }

    private void showEditUserDialog(User user) {
        JDialog dialog = new JDialog(this, "Edit user", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameText = new JTextField(user.getUsername());
        dialog.add(usernameLabel);
        dialog.add(usernameText);

        JLabel passwordLabel = new JLabel("New Password:");
        JPasswordField passwordText = new JPasswordField();
        dialog.add(passwordLabel);
        dialog.add(passwordText);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());

            if (isPasswordValid(password)) {
                user.setUsername(username);
                user.setPassword(password);
                saveUsersToFile();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "The user information is updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Passwords must be at least 6 characters long and contain letters and numbers.");
            }
        });
        dialog.add(confirmButton);

        JButton deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this, "Confirm deletion of user " + user.getUsername() + "？", "Confirm", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                users.remove(user);
                saveUsersToFile();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "User deletes successfully");
            }
        });
        dialog.add(deleteButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            return false;
        }
        Pattern letter = Pattern.compile("[a-zA-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        return letter.matcher(password).find() && digit.matcher(password).find();
    }

    private void saveUserToFile(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/matsumatsu/eclipse-workspace/testing/src/Login_System/User.txt", true))) {
            if (user instanceof Guest) {
                Guest guest = (Guest) user;
                bw.write(String.format("%s,%s,%s,%s,%s%n", guest.getUserType(), guest.getUsername(), guest.getPassword(), guest.getRealName(), guest.getIdNumber()));
            } else if (user instanceof Admin) {
                bw.write(String.format("%s,%s,%s%n", user.getUserType(), user.getUsername(), user.getPassword()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/matsumatsu/eclipse-workspace/testing/src/Login_System/User.txt"))) {
            for (User user : users) {
                if (user instanceof Guest) {
                    Guest guest = (Guest) user;
                    bw.write(String.format("%s,%s,%s,%s,%s%n", guest.getUserType(), guest.getUsername(), guest.getPassword(), guest.getRealName(), guest.getIdNumber()));
                } 
                else if (user instanceof Admin) {
                    bw.write(String.format("%s,%s,%s%n", user.getUserType(), user.getUsername(), user.getPassword()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
