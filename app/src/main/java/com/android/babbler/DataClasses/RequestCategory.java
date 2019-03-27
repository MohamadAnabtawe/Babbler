package com.android.babbler.DataClasses;

public class RequestCategory {

    private int rID;
    private int uID;
    private String category;

    public RequestCategory(int rID, int uID, String category) {
        this.rID = rID;
        this.uID = uID;
        this.category = category;
    }
    public int getrID() {
        return rID;
    }

    public void setrID(int rID) {
        this.rID = rID;
    }

    public int getuID() {
        return uID;
    }

    public void setuID(int uID) {
        this.uID = uID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
