package com.ndm.ptit.enitities.services;

public class ServicesResponse {
    private int result;
    private String msg;
    private Services data;

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

    public Services getData() {
        return data;
    }

    public void setData(Services data) {
        this.data = data;
    }
}
