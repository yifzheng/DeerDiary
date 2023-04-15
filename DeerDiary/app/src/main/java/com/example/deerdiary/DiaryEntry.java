package com.example.deerdiary;

public class DiaryEntry {
    public String id, userId, title, content, dateTime;

    public  DiaryEntry(){
    }

    public DiaryEntry(String userId, String title, String content, String dateTime){
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getDateTime(){
        return dateTime;
    }

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }
}
