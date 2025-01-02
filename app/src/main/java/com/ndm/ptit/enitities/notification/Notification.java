package com.ndm.ptit.enitities.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("record_id")
    @Expose
    private int recordId;

    @SerializedName("record_type")
    @Expose
    private String recordType;


    @SerializedName("is_read")
    @Expose
    private int isRead;

    @SerializedName("create_at")
    @Expose
    private String createAt;

    @SerializedName("update_at")
    @Expose
    private String updateAt;


    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public int getRecordId() {
        return recordId;
    }

    public String getRecordType() {
        return recordType;
    }


    public int getIsRead() {
        return isRead;
    }

    public String getCreateAt() {
        if (createAt == null || createAt.isEmpty()) {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
            return dateFormat.format(new java.util.Date());
        }
        return createAt;
    }


    public String getUpdateAt() {
        return updateAt;
    }
}
