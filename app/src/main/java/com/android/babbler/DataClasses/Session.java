package com.android.babbler.DataClasses;

public class Session {

    private int sessionID;
    private int moderatorID;
    private String category;
    private String sessionTitle;
    private String sessionDescription;
    private String sessionDate;
    private String sessionTime;
    private int sessionDuration;



    private int maxParticipants;
    private String sessionLanguage;
    private int imageID;
    private int numOfParticipants;

    //constructor for an existing sessions
    public Session(int sessionID, int moderatorID, String category, String sessionTitle,String sessionDescription,
                   String sessionDate, String sessionTime, int sessionDuration, int maxParticipants,int numOfParticipants, String sessionLanguage,int imageID)
    {
        this.sessionID = sessionID;
        this.moderatorID = moderatorID;
        this.category = category;
        this.sessionTitle = sessionTitle;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.sessionDuration = sessionDuration;
        this.maxParticipants = maxParticipants;
        this.sessionLanguage = sessionLanguage;
        this.imageID=imageID;
        this.sessionDescription=sessionDescription;
        this.numOfParticipants=numOfParticipants;
    }

    //constructor for creating new sessions
    public Session(int moderatorID, String category, String sessionTitle,String sessionDescription,
                   String sessionDate, String sessionTime, int sessionDuration, int maxParticipants, String sessionLanguage,int imageSrc)
    {
        this.moderatorID = moderatorID;
        this.category = category;
        this.sessionTitle = sessionTitle;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.sessionDuration = sessionDuration;
        this.maxParticipants = maxParticipants;
        this.sessionLanguage = sessionLanguage;
        this.imageID=imageSrc;
        this.sessionDescription=sessionDescription;
    }

    public int getSessionID() {
        return sessionID;
    }

    public int getModeratorID() {
        return moderatorID;
    }

    public String getCategory() {
        return category;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public String getSessionLanguage() {
        return sessionLanguage;
    }
    public void setModeratorID(int moderatorID) {
        this.moderatorID = moderatorID;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setSessionLanguage(String sessionLanguage) {
        this.sessionLanguage = sessionLanguage;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageSrc(int imageID) {
        this.imageID = imageID;
    }

    public String getSessionDescription() {
        return sessionDescription;
    }

    public void setSessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
    }
    public int getNumOfParticipants() {
        return numOfParticipants;
    }

    public void setNumOfParticipants(int numOfParticipants) {
        this.numOfParticipants = numOfParticipants;
    }
    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
