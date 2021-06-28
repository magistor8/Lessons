package com.magistor8.noteapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        createNoteListFromRes();
        initList(view);
        initSearchButton();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initList(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);
        initRecyclerView(recyclerView);
    }

    private void initRecyclerView(RecyclerView recyclerView){

        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        NoteAdapter adapter = new NoteAdapter(notesArrayList);
        recyclerView.setAdapter(adapter);

        // Установим слушателя
        adapter.SetOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Note data = notesArrayList.get(position);
                OpenNoteFragment(data);
            }
        });
    }


    private void initSearchButton() {
        SearchView search = getView().findViewById(R.id.searchText); // поиск пункта
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // реагирует на конец ввода поиска
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                return true;
            }
            // реагирует на нажатие каждой клавиши
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
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
    }

    // Сохранение данных
    @Override
    public void onSaveInstanceState(@NonNull Bundle instanceState) {
        if (!notesArrayList.isEmpty()) {
            NotesList list = new NotesList(notesArrayList.toArray(new Note[0]));
            instanceState.putParcelable(KEY_SAVE, list);
        }
        super.onSaveInstanceState(instanceState);
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
}
