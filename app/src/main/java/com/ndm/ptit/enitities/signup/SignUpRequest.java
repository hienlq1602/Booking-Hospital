package com.ndm.ptit.enitities.signup;

import java.util.Date;

public class SignUpRequest {

    private String type;
    private String email;
    private String phone;
    private String password;
    private String passwordConfirm;
    private String name;
    private String gender;
    private String birthday;
    private String address;
    private String avatar;
    private String  createAt;
    private String  updateAt;

    public SignUpRequest() {
    }

    public SignUpRequest(String type,String phone, String password, String passwordConfirm,
                         String createAt, String updateAt) {
        this.type="patient";
        this.email = "user@gmail.com";
        this.phone = phone;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = "User";
        this.gender = null;
        this.birthday = "Unknown";
        this.address = "Unknown";
        this.avatar = null;
        this.createAt = createAt;
        this.updateAt =updateAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
}
