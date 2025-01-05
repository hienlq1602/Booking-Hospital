package com.ndm.ptit.enitities.booking;

import com.ndm.ptit.enitities.Doctor;

import java.util.Date;

public class Booking {
    private int id;
    private String bookingName;
    private String bookingPhone;
    private String name;
    private int gender;
    private String birthday;
    private String address;
    private String reason;
    private String appointment_time;
    private String status;
    private String createAt;
    private String updateAt;
    private Service service;

    // Constructor mặc định
    public Booking() {
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookingName() {
        return bookingName;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
    }

    public String getBookingPhone() {
        return bookingPhone;
    }

    public void setBookingPhone(String bookingPhone) {
        this.bookingPhone = bookingPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAppointmentTime() {
        return appointment_time;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointment_time = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    // Class Service bên trong Booking
    public static class Service {
        private int id;
        private String name;

        private String image;

        public Service() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return image;
        }

        public void setAvatar(String avatar) {
            this.image = avatar;
        }

        @Override
        public String toString() {
            return "Service{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", avatar='" + image + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingName='" + bookingName + '\'' +
                ", bookingPhone='" + bookingPhone + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", reason='" + reason + '\'' +
                ", appointment_Time='" + appointment_time + '\'' +
                ", status='" + status + '\'' +
                ", createAt='" + createAt + '\'' +
                ", updateAt='" + updateAt + '\'' +
                ", service=" + service.toString() +
                '}';
    }
}
