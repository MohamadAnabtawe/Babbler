package com.android.babbler.DataClasses;

public class Message {

    private int messageID;
    private int senderID;
    private int receiverID;
    private String subject;
    private String content;
    private String time;
    private String date;

    public Message(int messageID, int senderID, int receiverID, String subject, String content, String time, String date) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.subject = subject;
        this.content = content;
        this.time = time;
        this.date = date;
    }


    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
