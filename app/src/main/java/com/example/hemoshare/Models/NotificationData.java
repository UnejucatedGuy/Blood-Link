package com.example.hemoshare.Models;

public class NotificationData {
    private String title;
    private String message;
    private String requestId;


    public NotificationData(String title, String message,String requestId) {
        this.title = title;
        this.message = message;
        this.requestId = requestId;

    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
