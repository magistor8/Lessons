package com.magistor8.noteapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.magistor8.noteapp.R;
import com.magistor8.noteapp.data.Note;
import com.magistor8.noteapp.observer.Publisher;
import com.magistor8.noteapp.observer.PublisherGetter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NoteFragment extends Fragment {

    private Publisher publisher;   // Обработчик подписок

    public static final String ARG_NOTE = "note";
    public static final String ARG_POSITION = "pos";

    private Note note;
    private int position;
    LinearLayout layout;

    public static NoteFragment newInstance(Note note, int position) {
        NoteFragment fragment = new NoteFragment();

        // Передача параметров
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        publisher = ((PublisherGetter) context).getPublisher(); // получим обработчика подписок
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_note, container, false);
        layout = view;

        if (note != null) {
            ((TextView)view.findViewById(R.id.title)).setText(note.getTitle());
            ((TextView)view.findViewById(R.id.description)).setText(note.getDescription());
            setDateToTextView(view, new Date(note.getDate()));
        }

        return (View)view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //Нажатие на дату
        LinearLayout dateAction = view.findViewById(R.id.date_picker_actions);
        dateAction.setOnClickListener(v -> {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.show(requireActivity().getSupportFragmentManager(), "datePickerFragment");
            //Лисенер для кнопки ОК
            DialogInterface.OnClickListener positiveButton = (dialog, which) -> {
                DatePicker datePicker = ((Dialog) dialog).findViewById(R.id.datePicker);
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year =  datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Date newDate = calendar.getTime();
                setDateToTextView(view, newDate);
                note.setDate(newDate.getTime());
                publisher.updateNote(note, position);
            };
            datePickerFragment.setPositiveButton(positiveButton);
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void setDateToTextView(@NonNull View view, Date newDate) {
        SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
        String date = sd.format(newDate);
        ((TextView) view.findViewById(R.id.date)).setText(date);
        sd.applyPattern("HH:mm");
        String time = sd.format(newDate);
        ((TextView) view.findViewById(R.id.time)).setText(time);
    }

    //При закрытии заметки сохраняем данные
    @Override
    public void onDestroyView() {
        note.setTitle(((EditText)layout.findViewById(R.id.title)).getText().toString());
        note.setDescription(((EditText)layout.findViewById(R.id.description)).getText().toString());
        publisher.updateNote(note, position);
        super.onDestroyView();
    }
}
