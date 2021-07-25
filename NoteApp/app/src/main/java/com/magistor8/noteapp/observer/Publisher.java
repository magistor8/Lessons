package com.magistor8.noteapp.observer;

import com.magistor8.noteapp.data.Note;

import java.util.ArrayList;
import java.util.List;

// Обработчик подписок
public class Publisher {

    private List<Observer> observers;   // Все обозреватели

    public Publisher() {
        observers = new ArrayList<>();
    }

    // Подписать
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    // Отписать
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    // Разослать событие
    public void updateNote(Note note, int position) {
        for (Observer observer : observers) {
            observer.updateNote(note, position);
        }
    }
}

