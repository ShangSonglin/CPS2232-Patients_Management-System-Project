package CPS2232_finalproject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;

class FamilyFrame extends JFrame {
    private JTextArea reminderArea;
    private JTextField contactField;  // Emergency contact input field
    private JButton saveContactButton;

    public FamilyFrame(String username) {
        setTitle("Family Interface");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Information display area
        reminderArea = new JTextArea(15, 30);
        reminderArea.setEditable(false);

        // Back to login button
        JButton backButton = new JButton("Return to Login Page");
        backButton.addActionListener(e -> {
            dispose(); // Close the current window
            new LoginFrame(); // Return to the login page
        });

        // Emergency contact input area
        JPanel contactPanel = new JPanel(new GridLayout(2, 1));
        contactPanel.add(new JLabel("Set Emergency Contact:"));
        contactField = new JTextField();
        saveContactButton = new JButton("Save Contact Information");
        contactPanel.add(contactField);
        contactPanel.add(saveContactButton);

        // Save emergency contact functionality
        saveContactButton.addActionListener(e -> saveEmergencyContact(username));

        // Add components to the window
        add(backButton, BorderLayout.SOUTH);
        add(contactPanel, BorderLayout.NORTH);
        add(new JScrollPane(reminderArea), BorderLayout.CENTER);

        // Load patient information
        displayPatientInfo(username);

        // Set window properties
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Display medication reminders and follow-up appointments for the patient
     * based on the family member's username (patient name)
     */
    
    private void displayPatientInfo(String patientName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/matsumatsu/eclipse-workspace/testing/src/CPS2232_finalproject/PatientInfomation.txt"))) {
            String line;
            String latestRecord = null; // 用于保存最新匹配记录
            while ((line = reader.readLine()) != null) {
                if (line.contains("Name: " + patientName)) {
                    latestRecord = line; // 更新最新记录
                }
            }
            // 显示结果
            if (latestRecord != null) {
                reminderArea.setText(latestRecord); // 显示最新的记录
            } else {
                reminderArea.setText("No patient information found for " + patientName + "!");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load patient information file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Save the emergency contact information to a file
     */
    private void saveEmergencyContact(String username) {
        String contact = contactField.getText().trim();
        if (contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an emergency contact!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/matsumatsu/eclipse-workspace/testing/src/CPS2232_finalproject/emergencyContact.txt", true))) { // Append mode
            writer.write(username + "," + contact + "\n");
            JOptionPane.showMessageDialog(this, "Emergency contact information saved!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save emergency contact information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void showFamilyFrame(String username) {
        SwingUtilities.invokeLater(() -> new FamilyFrame(username));
    }
}