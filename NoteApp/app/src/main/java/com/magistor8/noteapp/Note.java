package com.magistor8.noteapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Note implements Parcelable{

    private String title;
    private String description;
    private long date;

    public long getDate() {
        return date;
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
        date = new Date().getTime();
    }

    public Note(String title, String description, int Date) {
        this.title = title;
        this.description = description;
        date = (long) Date;
    }

    protected Note(Parcel in) {
        title = in.readString();
        description = in.readString();
        date = in.readLong();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(date);
    }
}
