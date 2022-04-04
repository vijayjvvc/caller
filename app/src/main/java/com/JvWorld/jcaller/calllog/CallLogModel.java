package com.JvWorld.jcaller.calllog;

public class CallLogModel {
    String phNumber, contactName, callType, callDateToday;
    int callTYpe;


    public CallLogModel() {

    }

    public CallLogModel(String phNumber, String contactName, String callType, String callDateToday, int callTYpe) {
        this.phNumber = phNumber;
        this.contactName = contactName;
        this.callType = callType;
        this.callDateToday = callDateToday;
        this.callTYpe = callTYpe;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallDateToday() {
        return callDateToday;
    }

    public void setCallDateToday(String callDateToday) {
        this.callDateToday = callDateToday;
    }

    public int getCallTYpe() {
        return callTYpe;
    }

    public void setCallTYpe(int callTYpe) {
        this.callTYpe = callTYpe;
    }
}
