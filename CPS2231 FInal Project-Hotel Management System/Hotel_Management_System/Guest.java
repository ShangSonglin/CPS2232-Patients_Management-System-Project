package Login_System;

public class Guest extends User {
    private String realName;
    private String idNumber;

    public Guest(String userType, String username, String password, String realName, String idNumber) {
        super(userType, username, password);
        this.realName = realName;
        this.idNumber = idNumber;
    }

    public String getRealName() {
        return realName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "userType='" + getUserType() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", realName='" + realName + '\'' +
                ", idNumber='" + idNumber + '\'' +
                '}';
    }
}
