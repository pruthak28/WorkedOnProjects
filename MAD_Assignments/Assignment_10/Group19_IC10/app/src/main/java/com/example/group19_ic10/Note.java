package com.example.group19_ic10;

public class Note {
    public String id;
    public String uId;
    public String txt;

    public Note(String id, String userId, String text) {
        this.id = id;
        this.uId = userId;
        this.txt = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return "Hey " +  uId + "!!!";
    }

    public void setUserId(String userId) {
        this.uId = userId;
    }

    public String getText() {
        return txt ;
    }

    public void setText(String text) {
        this.txt = text;
    }


}
