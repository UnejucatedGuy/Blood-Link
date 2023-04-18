package com.example.hemoshare.Models;

public class RequestModel {

   String bloodGroup,name,time,requestId;
   boolean isAccepted;



    public RequestModel() {
        super();
    }

    public RequestModel(String bloodGroup, String name, String time,String requestId,boolean isAccepted) {
        this.bloodGroup = bloodGroup;
        this.name = name;
        this.time = time;
        this.requestId = requestId;
        this.isAccepted = isAccepted;


    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
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
