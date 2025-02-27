package Login_System;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HotelManagementSystem extends JFrame {
    private List<User> users;
    private List<Room> rooms;

    public HotelManagementSystem(List<User> users) {
        this.users = users;
        this.rooms = RoomLoader.loadRooms();

        
        setTitle("Hotel Management System");

        setSize(400, 300);
       

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Hotel Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

       
        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField(20);
        panel.add(userLabel);
        panel.add(userText);
    
        
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField(20);
        panel.add(passwordLabel);
        panel.add(passwordText);

        
        add(panel, BorderLayout.CENTER);

       
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

      
        JButton loginButton = new JButton("Login");
        buttonPanel.add(loginButton);
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            
            boolean authenticated = authenticate(username, password);
            if (authenticated) {
                User loggedInUser = getUserByUsername(username);
                if (loggedInUser instanceof Admin) {
                    this.setVisible(false);
                    new AdminDashboardFrame((Admin) loggedInUser, rooms, users).setVisible(true);
                } 
                else {
                    JOptionPane.showMessageDialog(null, "Login successful");
                  
                }
            } 
           
            else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
            }
        });
       

        
        JButton registerButton = new JButton("Register");
        buttonPanel.add(registerButton);
        registerButton.addActionListener(e -> {
            this.setVisible(false);
            new GuestRegistrationFrame(users).setVisible(true);
        });
        

       
        JButton forgotPasswordButton = new JButton("Forgot Password");
        buttonPanel.add(forgotPasswordButton);
        forgotPasswordButton.addActionListener(e -> {
            this.setVisible(false);
            new ForgotPasswordFrame(users).setVisible(true);
        });
        add(buttonPanel, BorderLayout.SOUTH);
    }

    
    private boolean authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
   
    
}
