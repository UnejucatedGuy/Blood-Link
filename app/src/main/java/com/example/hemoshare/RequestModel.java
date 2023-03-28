package com.example.hemoshare;

public class RequestModel {

   String bloodType,name,time;


    public RequestModel() {
        super();
    }

    public RequestModel(String bloodType, String name, String time) {
        this.bloodType = bloodType;
        this.name = name;
        this.time = time;
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
