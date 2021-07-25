package com.magistor8.noteapp.firebase;

import com.magistor8.noteapp.data.Note;

import java.util.List;

public interface FirebaseComplete {
    void complete(List<Note> noteList);
}
