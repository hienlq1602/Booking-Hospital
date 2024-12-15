package com.ndm.ptit.enitities.login;

public class LoginRequest {
    private String type;
    private String phone;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String type, String phone, String password) {
        this.type = type;
        this.phone = phone;
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}

