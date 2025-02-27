package CPS2232_finalproject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class DoctorFrame extends JFrame {
    private JTextArea patientInfoArea;
    private JTextField patientIdField, patientNameField, patientAgeField, patientGenderField;
    private JTextField medicationField, medicationTimeField, appointmentField;
    private JButton addPatientButton, queryPatientButton, addMedicationButton, addAppointmentButton, saveToFileButton, addBackButton;
    private HashMap<String, Patient> patientDatabase;

    public DoctorFrame() {
        // Initialize patient database
        patientDatabase = new HashMap<>();

        // Window settings
        setTitle("Doctor Interface");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Top input area
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        patientIdField = new JTextField();
        patientNameField = new JTextField();
        patientAgeField = new JTextField();
        patientGenderField = new JTextField();
        medicationField = new JTextField();
        medicationTimeField = new JTextField();
        appointmentField = new JTextField();

        inputPanel.add(new JLabel("Patient ID:"));
        inputPanel.add(patientIdField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(patientNameField);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(patientAgeField);
        inputPanel.add(new JLabel("Gender:"));
        inputPanel.add(patientGenderField);
        inputPanel.add(new JLabel("Medication:"));
        inputPanel.add(medicationField);
        inputPanel.add(new JLabel("Medication Time:"));
        inputPanel.add(medicationTimeField);
        inputPanel.add(new JLabel("Follow-Up Time:"));
        inputPanel.add(appointmentField);

        // Middle information display area
        patientInfoArea = new JTextArea(10, 40);
        patientInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(patientInfoArea);

        // Bottom button area
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        addPatientButton = new JButton("Add Patient");
        queryPatientButton = new JButton("Query Patient");
        addMedicationButton = new JButton("Add Medication Reminder");
        addAppointmentButton = new JButton("Add Follow-Up Time");
        saveToFileButton = new JButton("Save to File");
        addBackButton = new JButton("Logout");

        buttonPanel.add(addPatientButton);
        buttonPanel.add(queryPatientButton);
        buttonPanel.add(addMedicationButton);
        buttonPanel.add(addAppointmentButton);
        buttonPanel.add(saveToFileButton);
        buttonPanel.add(addBackButton);

        // Bind functions to buttons
        addPatientButton.addActionListener(e -> addPatient());
        queryPatientButton.addActionListener(e -> queryPatient());
        addMedicationButton.addActionListener(e -> addMedication());
        addAppointmentButton.addActionListener(e -> addAppointment());
        saveToFileButton.addActionListener(e -> saveToFile());
        addBackButton.addActionListener(e -> returnToLogin());

        // Assemble interface
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Window settings
        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Add patient information
    private void addPatient() {
        try {
            String id = patientIdField.getText().trim();
            String name = patientNameField.getText().trim();
            int age = Integer.parseInt(patientAgeField.getText().trim());
            String gender = patientGenderField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please ensure all fields are filled in!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Patient patient = new Patient(id, name, age, gender);
            patientDatabase.put(id, patient);
            patientInfoArea.append("Added Patient: " + patient + "\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Query patient information
    private void queryPatient() {
        String id = patientIdField.getText().trim();
        String name = patientNameField.getText().trim();
        String latestRecord = null;
        String emergencyContact = null;

        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/matsumatsu/eclipse-workspace/testing/src/CPS2232_finalproject/PatientInfomation.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if ((id.isEmpty() || line.contains("Patient ID: " + id)) &&
                    (name.isEmpty() || line.contains("Name: " + name))) {
                    latestRecord = line; // Save the latest matching record
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load patient information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Retrieve emergency contact
        if (latestRecord != null) {
            String patientName = extractName(latestRecord);
            emergencyContact = getEmergencyContact(patientName);
        }

        // Display query result
        if (latestRecord != null) {
            patientInfoArea.setText("Latest Record Found:\n" + latestRecord +
                    (emergencyContact != null ? "\nEmergency Contact: " + emergencyContact : "\nNo Emergency Contact Found"));
        } else {
            patientInfoArea.setText("No matching patient information found.");
        }
    }

    // Extract name from the record
    private String extractName(String record) {
        int nameIndex = record.indexOf("Name: ");
        if (nameIndex != -1) {
            int endIndex = record.indexOf(",", nameIndex);
            return record.substring(nameIndex + 6, endIndex).trim();
        }
        return null;
    }

    // Get emergency contact
    private String getEmergencyContact(String patientName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("/Users/matsumatsu/eclipse-workspace/testing/src/CPS2232_finalproject/emergencyContact.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].trim().equals(patientName)) {
                    return parts[1].trim(); // Return emergency contact
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load emergency contact: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    // Add medication reminder
    private void addMedication() {
        String id = patientIdField.getText().trim();
        String medication = medicationField.getText().trim();
        String time = medicationTimeField.getText().trim();

        Patient patient = patientDatabase.getOrDefault(id, new Patient(id));
        patient.setMedication(medication, time);
        patientDatabase.put(id, patient);

        patientInfoArea.append("Updated Medication Reminder: " + patient + "\n");
    }

    // Add follow-up appointment time
    private void addAppointment() {
        String id = patientIdField.getText().trim();
        String appointment = appointmentField.getText().trim();

        Patient patient = patientDatabase.getOrDefault(id, new Patient(id));
        patient.setAppointment(appointment);
        patientDatabase.put(id, patient);

        patientInfoArea.append("Updated Follow-Up Time: " + patient + "\n");
    }

    // Save patient information to file
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/matsumatsu/eclipse-workspace/testing/src/CPS2232_finalproject/PatientInfomation.txt", true))) {
            for (Patient patient : patientDatabase.values()) {
                writer.write(patient.toString() + "\n");
            }
            JOptionPane.showMessageDialog(this, "Patient information saved to file!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Return to login interface
    private void returnToLogin() {
        dispose();
        new LoginFrame();
    }

    public static void showDoctorFrame() {
        SwingUtilities.invokeLater(DoctorFrame::new);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}

class Patient {
    private String id, name, gender, medication, medicationTime, appointmentTime;
    private int age;

    public Patient(String id, String name, int age, String gender) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.medication = "None";
        this.medicationTime = "None";
        this.appointmentTime = "None";
    }

    public Patient(String id) {
        this(id, "Unknown", 0, "Unknown");
    }

    public void setMedication(String medication, String medicationTime) {
        this.medication = medication;
        this.medicationTime = medicationTime;
    }

    public void setAppointment(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    @Override
    public String toString() {
        return String.format("Patient ID: %s, Name: %s, Age: %d, Gender: %s, Medication: %s, Medication Time: %s, Follow-Up Time: %s",
                id, name, age, gender, medication, medicationTime, appointmentTime);
    }
}