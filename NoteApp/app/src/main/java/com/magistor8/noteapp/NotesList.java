package com.magistor8.noteapp;

import android.os.Parcel;
import android.os.Parcelable;

public class NotesList implements Parcelable {
    private Note[] notes;

    public Note[] getNotes() {
        return notes;
    }

    public NotesList(Note[] notes) {
        this.notes = notes;
    }

    protected NotesList(Parcel in) {
        notes = in.createTypedArray(Note.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(notes, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotesList> CREATOR = new Creator<NotesList>() {
        @Override
        public NotesList createFromParcel(Parcel in) {
            return new NotesList(in);
        }

        @Override
        public NotesList[] newArray(int size) {
            return new NotesList[size];
        }
    };
}
