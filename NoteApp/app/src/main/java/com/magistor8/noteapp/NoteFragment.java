package com.magistor8.noteapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteFragment extends Fragment {

    public static final String ARG_INDEX = "index";

    private Note note;
    LinearLayout layout;

    public static NoteFragment newInstance(Note note) {
        NoteFragment fragment = new NoteFragment();

        // Передача параметров
        Bundle args = new Bundle();
        args.putParcelable(ARG_INDEX, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_note, container, false);
        layout = view;

        if (note != null) {
            int count = view.getChildCount();
            View v1 = null;
            View v2 = null;
            View v3 = null;
            for(int j =0; j <count; j++) {
                v1 = view.getChildAt(j);
                if (v1 instanceof EditText) {
                    ((EditText) v1).setText(note.getDescription());
                    continue;
                }
                v2 = ((LinearLayout) v1).getChildAt(0);
                ((EditText) v2).setText(note.getTitle());
                v2 = ((LinearLayout) v1).getChildAt(1);
                v3 = ((LinearLayout) v2).getChildAt(0);
                SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
                String date = sd.format(new Date(note.getDate() * 1000));
                ((TextView) v3).setText(date);
                v3 = ((LinearLayout) v2).getChildAt(1);
                sd.applyPattern("HH:mm");
                String time = sd.format(new Date(note.getDate() * 1000));
                ((TextView) v3).setText(time);
            }
        }

        return (View)view;
    }
}
