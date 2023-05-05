package com.example.deerdiary;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DiaryEntry implements Parcelable {
    public String id, userId, title, content, dateTime;

    public  DiaryEntry(){
    }

    public DiaryEntry(String userId, String title, String content, String dateTime){
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
    }

    // deserialize from parcel
    protected DiaryEntry(Parcel in) {
        super();
        id = in.readString();
        userId = in.readString();
        title = in.readString();
        content = in.readString();
        dateTime = in.readString();
    }

    // create DiaryEntry objects from parcel
    public static final Creator<DiaryEntry> CREATOR = new Creator<DiaryEntry>() {
        @Override
        public DiaryEntry createFromParcel(Parcel in) {
            return new DiaryEntry(in);
        }

        @Override
        public DiaryEntry[] newArray(int size) {
            return new DiaryEntry[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    // writing to parcel
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(dateTime);
    }
}
