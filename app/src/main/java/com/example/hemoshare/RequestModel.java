package com.example.hemoshare;

public class RequestModel {

   String bloodType,name,time,requestId;


    public RequestModel() {
        super();
    }

    public RequestModel(String bloodType, String name, String time,String requestId) {
        this.bloodType = bloodType;
        this.name = name;
        this.time = time;
        this.requestId = requestId;

    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
