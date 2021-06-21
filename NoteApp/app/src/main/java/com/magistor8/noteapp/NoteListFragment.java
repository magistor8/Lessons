package com.magistor8.noteapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteListFragment extends Fragment {

    private static final String KEY_SAVE = "KEY_SAVE";

    private List<Note> notesArrayList = new ArrayList<>();

    private boolean isLandscape;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Промежуточный метод для демонстрации
        createNoteListFromRes();

//        restoreNotes(savedInstanceState);
//        initButtonNew();
        super.onViewCreated(view, savedInstanceState);
    }

    private void createNoteListFromRes() {
        if (notesArrayList.isEmpty()) {
            String[] Titles = getResources().getStringArray(R.array.Title);
            String[] Descriptions = getResources().getStringArray(R.array.Description);
            int[] Dates = getResources().getIntArray(R.array.Date);
            for (int i = 0; i < Titles.length; i++) {
                notesArrayList.add(new Note(Titles[i], Descriptions[i], Dates[i]));
            }
        }
        showNotes();
    }

//    private void initButtonNew() {
//        FloatingActionButton addNoteButton = getView().findViewById(R.id.add);
//        addNoteButton.setOnClickListener(v -> {
//            //Создаем новую заметку
//            Note newNote = new Note("Title", "Description");
//            //Добавляем ее в лист
//            notesArrayList.add(newNote);
//            //Открываем ее
//            OpenNoteFragment(newNote);
//        });
//    }

    // Сохранение данных
    @Override
    public void onSaveInstanceState(@NonNull Bundle instanceState) {
        if (!notesArrayList.isEmpty()) {
            NotesList list = new NotesList(notesArrayList.toArray(new Note[0]));
            instanceState.putParcelable(KEY_SAVE, list);
        }
        super.onSaveInstanceState(instanceState);
    }

    // Восстановление данных
    @Override
    public void onViewStateRestored(Bundle instanceState) {
        super.onViewStateRestored(instanceState);
    }

//    private void restoreNotes(Bundle instanceState) {
//        if (!notesArrayList.isEmpty()) {
//            showNotes();
//            return;
//        }
//        if (instanceState != null) {
//            NotesList notesList = instanceState.getParcelable(KEY_SAVE);
//            if (notesList != null) {
//                notesArrayList = Arrays.asList(notesList.getNotes());
//                showNotes();
//            }
//        }
//    }

    private void showNotes() {
        //Получаем лист заметок
        LinearLayout notesListView = getView().findViewById(R.id.notesList);
        //Надуватель
        LayoutInflater inflater = getLayoutInflater();
        //Создаем список заметок
        for (int i = 0; i < notesArrayList.size(); i++) {
            //Надуваем новый эелемент
            LinearLayout note = (LinearLayout) inflater.inflate(R.layout.note, null);
            //Заполняем
            fillNote(i, note);
            //Вешаем лисенер
            addListenerToNote(i, note);
            //Показываем в списке
            notesListView.addView(note);
        }
    }

    private void addListenerToNote(int i, LinearLayout note) {
        note.setOnClickListener(v -> {
            Note data = notesArrayList.get(i);
            OpenNoteFragment(data);
        });
    }

    private void OpenNoteFragment(Note data) {
        NoteFragment detail = NoteFragment.newInstance(data);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isLandscape) {
            fragmentTransaction.replace(R.id.noteView, detail);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, detail);
        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void fillNote(int i, LinearLayout note) {
        int count = note.getChildCount();
        View v1 = null;
        View v2 = null;
        for(int j =0; j <count; j++) {
            v1 = note.getChildAt(j);
            if (v1 instanceof TextView) {
                ((TextView) v1).setText(notesArrayList.get(i).getTitle());
                continue;
            }
            v2 = ((LinearLayout) v1).getChildAt(0);
            SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
            String date = sd.format(new Date(notesArrayList.get(i).getDate() * 1000));
            ((TextView) v2).setText(date);
            v2 = ((LinearLayout) v1).getChildAt(1);
            sd.applyPattern("HH:mm");
            String time = sd.format(new Date(notesArrayList.get(i).getDate() * 1000));
            ((TextView) v2).setText(time);
        }
    }
}
