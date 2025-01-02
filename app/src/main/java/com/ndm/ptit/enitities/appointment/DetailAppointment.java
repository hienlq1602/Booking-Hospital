package com.ndm.ptit.enitities.appointment;

import java.util.List;

public class DetailAppointment {
    private int result;
    private String msg;
    private Appointment data;
    private List<Appointment_queue> appointment_queue;

    public DetailAppointment() {
    }

    public DetailAppointment(int result, String msg, Appointment data, List<Appointment_queue> appointment_queue) {
        this.result = result;
        this.msg = msg;
        this.data = data;
        this.appointment_queue = appointment_queue;
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

    public Appointment getData() {
        return data;
    }

    public void setData(Appointment data) {
        this.data = data;
    }

    public List<Appointment_queue> getAppointment_queue() {
        return appointment_queue;
    }

    public void setAppointment_queue(List<Appointment_queue> appointment_queue) {
        this.appointment_queue = appointment_queue;
    }
}
