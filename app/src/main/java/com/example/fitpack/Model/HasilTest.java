package com.example.fitpack.Model;

public class HasilTest {

    private String userID;
    private String namePublisher;
    private String currentDate;
    private String currentTime;

    public HasilTest() {
    }

    public HasilTest(String userID, String namePublisher, String currentDate, String currentTime) {
        this.userID = userID;
        this.namePublisher = namePublisher;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNamePublisher() {
        return namePublisher;
    }

    public void setNamePublisher(String namePublisher) {
        this.namePublisher = namePublisher;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
