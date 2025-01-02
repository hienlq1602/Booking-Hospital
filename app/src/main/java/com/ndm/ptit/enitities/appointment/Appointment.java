package com.ndm.ptit.enitities.appointment;

import com.google.gson.annotations.SerializedName;

public class Appointment {
    @SerializedName("id")
    private int id;

    @SerializedName("booking_id")
    private int bookingId;

    @SerializedName("doctor_id")
    private int doctorId;

    @SerializedName("patient_id")
    private int patientId;

    @SerializedName("patient_name")
    private String patientName;

    @SerializedName("patient_birthday")
    private String patientBirthday;

    @SerializedName("patient_reason")
    private String patientReason;

    @SerializedName("patient_phone")
    private String patientPhone;

    @SerializedName("numerical_order")
    private Integer numericalOrder;

    @SerializedName("position")
    private Integer position;

    @SerializedName("appointment_time")
    private String appointmentTime;

    @SerializedName("date")
    private String date;

    @SerializedName("status")
    private String status;

    @SerializedName("create_at")
    private String createAt;

    @SerializedName("update_at")
    private String updateAt;

    @SerializedName("room")
    private Room room;

    @SerializedName("doctor_name")
    private String doctorName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientBirthday() {
        return patientBirthday;
    }

    public void setPatientBirthday(String patientBirthday) {
        this.patientBirthday = patientBirthday;
    }

    public String getPatientReason() {
        return patientReason;
    }

    public void setPatientReason(String patientReason) {
        this.patientReason = patientReason;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public Integer getNumericalOrder() {
        return numericalOrder;
    }

    public void setNumericalOrder(Integer numericalOrder) {
        this.numericalOrder = numericalOrder;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public Room getRoom() {
        if(room == null){
            return new Room();
        }
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", bookingId=" + bookingId +
                ", doctorId=" + doctorId +
                ", patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", patientBirthday='" + patientBirthday + '\'' +
                ", patientReason='" + patientReason + '\'' +
                ", patientPhone='" + patientPhone + '\'' +
                ", numericalOrder=" + numericalOrder +
                ", position=" + position +
                ", appointmentTime='" + appointmentTime + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", createAt='" + createAt + '\'' +
                ", updateAt='" + updateAt + '\'' +
                ", room=" + room +
                ", doctorName='" + doctorName + '\'' +
                '}';
    }
}
