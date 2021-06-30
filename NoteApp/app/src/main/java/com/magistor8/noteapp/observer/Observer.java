package com.magistor8.noteapp.observer;

import com.magistor8.noteapp.Note;

public interface Observer {
    void updateNote(Note note, int position);
}
