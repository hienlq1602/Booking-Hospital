package com.ndm.ptit.enitities.services;

import java.util.List;

public class DoctorServiceResponse {
    private int result;
    private String msg;
    private int  quantity;
    private List<DoctorService> data;


    public DoctorServiceResponse() {
    }

    public DoctorServiceResponse(int result, String msg, int quantity, List<DoctorService> data) {
        this.result = result;
        this.msg = msg;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<DoctorService> getData() {
        return data;
    }

    public void setData(List<DoctorService> data) {
        this.data = data;
    }
}
