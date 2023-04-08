package com.example.hemoshare;

public class YourRequestModel {

   String bloodGroup,time,requestId;
   boolean isAccepted;



    public YourRequestModel() {
        super();
    }

    public YourRequestModel(String bloodGroup, String time, String requestId, boolean isAccepted) {
        this.bloodGroup = bloodGroup;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
