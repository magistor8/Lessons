package com.magistor8.noteapp.observer;

import com.magistor8.noteapp.data.Note;

public interface Observer {
    void updateNote(Note note, int position);
}
