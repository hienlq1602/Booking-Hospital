package com.ndm.ptit.enitities.login;

public class LoginRespone {
    private int result;
    private String msg;
    private String accessToken;
    private Patient data;


    public LoginRespone() {
    }

    public LoginRespone(int result, String msg, String accessToken, Patient data,String avatar) {
        this.result = result;
        this.msg = msg;
        this.accessToken = accessToken;
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Patient getData() {
        return data;
    }

    public void setData(Patient data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginRespone{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", data=" + data +
                '}';
    }
}
