package Login_System;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserLoader {
    public static List<User> loadUsers(String filePath) {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    users.add(new Guest(parts[0], parts[1], parts[2], parts[3], parts[4]));
                } else if (parts.length == 3) {
                    users.add(new Admin(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

//    public static void main(String[] args) {
//        String filePath = "/Users/matsumatsu/eclipse-workspace/testing/src/Login_System/User.txt";
//        List<User> users = loadUsers(filePath);
//        for (User user : users) {
//            System.out.println(user);
//        }
//    }
}
