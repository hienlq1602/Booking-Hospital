package com.ndm.ptit.enitities.appointment;

public class AppointmentRequest {
    private int size;
    private int page;

    public AppointmentRequest() {
    }

    public AppointmentRequest(int size, int page) {
        this.size = size;
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
