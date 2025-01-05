package com.ndm.ptit.enitities.services;

import com.ndm.ptit.enitities.speciality.SpecialityResponse;

public class DoctorService {
    private int id;
    private String email;
    private String phone;
    private String name;
    private String description;
    private double price;
    private String role;
    private int active;
    private String avatar;
    private String create_At;
    private String update_At;
    private SpecialityResponse speciality;
    private int roomId;
    private String recoveryToken;


    private String  available_status;
    private int appointment_number;

    @Override
    public String toString() {
        return "DoctorService{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", role='" + role + '\'' +
                ", active=" + active +
                ", avatar='" + avatar + '\'' +
                ", create_At='" + create_At + '\'' +
                ", update_At='" + update_At + '\'' +
                ", speciality=" + speciality +
                ", roomId=" + roomId +
                ", recoveryToken='" + recoveryToken + '\'' +
                ", available_status='" + available_status + '\'' +
                ", appointment_number=" + appointment_number +
                '}';
    }

    public DoctorService() {
    }

    public DoctorService(int id, String email, String phone, String name, String description, double price, String role, int active, String avatar, String createAt, String updateAt, SpecialityResponse speciality, int roomId, String recoveryToken,
                         String  available_status,int appointment_number ){
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.description = description;
        this.price = price;
        this.role = role;
        this.active = active;
        this.avatar = avatar;
        this.create_At = createAt;
        this.update_At = updateAt;
        this.speciality = speciality;
        this.roomId = roomId;
        this.recoveryToken = recoveryToken;
        this.appointment_number = appointment_number;
        this.available_status = available_status;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int isActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvailable_status() {
        return available_status;
    }

    public void setAvailable_status(String available_status) {
        this.available_status = available_status;
    }

    public int getAppointment_number() {
        return appointment_number;
    }

    public void setAppointment_number(int appointment_number) {
        this.appointment_number = appointment_number;
    }

    public int getActive() {
        return active;
    }

    public String getUpdate_At() {
        return update_At;
    }

    public void setUpdate_At(String update_At) {
        this.update_At = update_At;
    }

    public String getCreate_At() {
        return create_At;
    }

    public void setCreate_At(String create_At) {
        this.create_At = create_At;
    }

    public SpecialityResponse getSpeciality() {
        return speciality;
    }

    public void setSpeciality(SpecialityResponse speciality) {
        this.speciality = speciality;
    }

    public String getCreateAt() {
        return create_At;
    }

    public void setCreateAt(String createAt) {
        this.create_At = createAt;
    }

    public String getUpdateAt() {
        return update_At;
    }

    public void setUpdateAt(String updateAt) {
        this.update_At = updateAt;
    }

    public SpecialityResponse getSpecialityId() {
        return speciality;
    }

    public void setSpecialityId(SpecialityResponse speciality) {
        this.speciality = speciality;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRecoveryToken() {
        return recoveryToken;
    }

    public void setRecoveryToken(String recoveryToken) {
        this.recoveryToken = recoveryToken;
    }
}
