package CPS2232_finalproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, createAccountButton;
    private JComboBox<String> userTypeComboBox;
    private LoginManager loginManager;
    private HashMap<String, String> familyToPatientMap;

    public LoginFrame() {
        loginManager = new LoginManager();
        familyToPatientMap = new HashMap<>();

        // Initialize the mapping between family members and patients
        familyToPatientMap.put("family1", "P001");
        familyToPatientMap.put("family2", "P002");

        // Set the window title and layout
        setTitle("Login Interface");
        setLayout(new GridLayout(5, 2, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel userTypeLabel = new JLabel("User Type:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        userTypeComboBox = new JComboBox<>(new String[]{"doctor", "family"});
        loginButton = new JButton("Login");
        createAccountButton = new JButton("Create Account");

        // Add event listeners
        loginButton.addActionListener(new LoginActionListener());
        createAccountButton.addActionListener(new CreateAccountActionListener());

        // Add components to the window
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(userTypeLabel);
        add(userTypeComboBox);
        add(loginButton);
        add(createAccountButton);

        // Set window size and visibility
        setSize(400, 250);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String userType = (String) userTypeComboBox.getSelectedItem();

            String result = loginManager.login(username, password);
            if (result != null && result.equals(userType)) {
                JOptionPane.showMessageDialog(null, "Login successful! User Type: " + userType);

                // Hide the current login window
                dispose();

                // Navigate to the corresponding interface
                if ("doctor".equals(userType)) {
                    DoctorFrame.showDoctorFrame(); // Launch the doctor interface
                } else {
                    FamilyFrame.showFamilyFrame(username); // Launch the family member interface
                }
            } else {
                JOptionPane.showMessageDialog(null, "Login failed! Please check your username, password, or user type.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class CreateAccountActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JComboBox<String> userTypeComboBox = new JComboBox<>(new String[]{"doctor", "family"});

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Username:"));
            panel.add(usernameField);
            panel.add(new JLabel("Password:"));
            panel.add(passwordField);
            panel.add(new JLabel("User Type:"));
            panel.add(userTypeComboBox);

            int result = JOptionPane.showConfirmDialog(null, panel, "Create New Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String userType = (String) userTypeComboBox.getSelectedItem();

                if (loginManager.createUser(username, password, userType)) {
                    JOptionPane.showMessageDialog(null, "Account created successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Account creation failed. The username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}

class LoginManager {
    private List<User> users;
    private static final String FILE_PATH = "/Users/matsumatsu/eclipse-workspace/testing/src/CPS2232_finalproject/User.txt";

    public LoginManager() {
        users = new ArrayList<>();
        loadUsersFromFile();
    }

    public String login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user.getUserType();
            }
        }
        return null;
    }

    public boolean createUser(String username, String password, String userType) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }

        User newUser = new User(username, password, userType);
        users.add(newUser);
        saveUsersToFile();
        return true;
    }

    private void loadUsersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    users.add(new User(parts[1], parts[2], parts[0]));
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to load user data: " + e.getMessage());
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                bw.write(String.format("%s,%s,%s%n", user.getUserType(), user.getUsername(), user.getPassword()));
            }
        } catch (IOException e) {
            System.out.println("Unable to save user data: " + e.getMessage());
        }
    }
}

class User {
    private String username;
    private String password;
    private String userType;

    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }
}