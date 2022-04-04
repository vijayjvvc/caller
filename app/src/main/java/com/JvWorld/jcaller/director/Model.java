package com.JvWorld.jcaller.director;

public class Model {
    String callType,date,duration;

    public Model(String callType, String date, String duration) {
        this.callType = callType;
        this.date = date;
        this.duration = duration;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
