package com.ndm.ptit.enitities.login;

public class Patient {
    private Long id;
    private String email;
    private String phone;
    private String name;
    private int gender;
    private String birthday;
    private String address;
    private String avatar;
    private String create_at;
    private String update_at;
    private String password;

    public Patient() {
    }

    public Patient(Long id, String email, String phone, String name, int gender, String birthday,
                   String address, String avatar, String createAt, String updateAt) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.avatar = avatar;
        this.create_at = createAt;
        this.update_at = updateAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone != null ? phone : "";
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name != null ? name : "";
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
        return birthday != null ? birthday : "";
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address != null ? address : "";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar != null ? avatar : "";
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreateAt() {
        return create_at != null ? create_at : "";
    }

    public void setCreateAt(String createAt) {
        this.create_at = createAt;
    }

    public String getUpdateAt() {
        return update_at != null ? update_at : "";
    }

    public void setUpdateAt(String updateAt) {
        this.update_at = updateAt;
    }

    public String getPassword() {
        return password != null ? password : "";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", email='" + (email != null ? email : "") + '\'' +
                ", phone='" + (phone != null ? phone : "") + '\'' +
                ", name='" + (name != null ? name : "") + '\'' +
                ", gender=" + gender +
                ", birthday='" + (birthday != null ? birthday : "") + '\'' +
                ", address='" + (address != null ? address : "") + '\'' +
                ", avatar='" + (avatar != null ? avatar : "") + '\'' +
                ", createAt='" + (create_at != null ? create_at : "") + '\'' +
                ", updateAt='" + (update_at != null ? update_at : "") + '\'' +
                ", password='" + (password != null ? password : "") + '\'' +
                '}';
    }
}
