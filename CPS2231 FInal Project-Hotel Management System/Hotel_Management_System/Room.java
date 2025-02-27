package Login_System;

import java.util.Date;

public class Room {
    public enum RoomStatus {
    	vacantRoom,Booking,CheckingIn;
    }

    private RoomType roomType;
    private Date bookingStartTime;
    private Date bookingEndTime;
    private Guest bookedGuest;
    private String remarks;
    private String roomNumber;
    private RoomStatus roomStatus;

    public Room(RoomType roomType, Date bookingStartTime, Date bookingEndTime, Guest bookedGuest, String remarks, String roomNumber, RoomStatus roomStatus) {
        this.roomType = roomType;
        this.bookingStartTime = bookingStartTime;
        this.bookingEndTime = bookingEndTime;
        this.bookedGuest = bookedGuest;
        this.remarks = remarks;
        this.roomNumber = roomNumber;
        this.roomStatus = roomStatus;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Date getBookingStartTime() {
        return bookingStartTime;
    }

    public void setBookingStartTime(Date bookingStartTime) {
        this.bookingStartTime = bookingStartTime;
    }

    public Date getBookingEndTime() {
        return bookingEndTime;
    }

    public void setBookingEndTime(Date bookingEndTime) {
        this.bookingEndTime = bookingEndTime;
    }

    public Guest getBookedGuest() {
        return bookedGuest;
    }

    public void setBookedGuest(Guest bookedGuest) {
        this.bookedGuest = bookedGuest;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomType=" + roomType +
                ", bookingStartTime=" + bookingStartTime +
                ", bookingEndTime=" + bookingEndTime +
                ", bookedGuest=" + bookedGuest +
                ", remarks='" + remarks + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomStatus=" + roomStatus +
                '}';
    }
}
