package com.magistor8.noteapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.magistor8.noteapp.observer.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteListFragment extends Fragment implements Observer, FirebaseComplete {

    private static final String KEY_SAVE = "KEY_SAVE";
    private List<Note> notesArrayList = new ArrayList<>();
    private boolean isLandscape;
    private NoteAdapter adapter;
    private View generalView;
    private Firebase firebase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        firebase = new Firebase();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            fillNoteListFromInstanceState(savedInstanceState);
        }
        generalView = view;
        if(notesArrayList.isEmpty()) {
            firebase.init(this);
        } else {
            complete(notesArrayList);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    //Запускается после загрузки списка из Firebase
    @Override
    public void complete(List<Note> noteList) {
        notesArrayList = noteList;
        initList(generalView);
        initSearchButton();
        initNewNoteButton();
    }

    private void fillNoteListFromInstanceState(Bundle savedInstanceState) {
        NotesList list = savedInstanceState.getParcelable(KEY_SAVE);
        notesArrayList = new ArrayList<>(Arrays.asList(list.getNotes()));
    }

    private void initList(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);
        initRecyclerView(recyclerView);
    }

    private void initRecyclerView(RecyclerView recyclerView){

        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        adapter = new NoteAdapter(notesArrayList, this);
        recyclerView.setAdapter(adapter);

        // Установим слушателя
        adapter.SetOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Note data = notesArrayList.get(position);
                OpenNoteFragment(data, position);
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

    private void initNewNoteButton() {
        FloatingActionButton newNoteButton = getView().findViewById(R.id.add);
        newNoteButton.setOnClickListener(v -> {
            Note note = new Note("Новая заметка", "Описание");
            notesArrayList.add(note);
            adapter.notifyItemInserted(notesArrayList.size() - 1);
            OpenNoteFragment(note, notesArrayList.size() - 1);
            //Вносим иземение в firebase
            firebase.addCardData(note);
        });
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

    private void OpenNoteFragment(Note data, int position) {
        NoteFragment detail = NoteFragment.newInstance(data, position);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isLandscape) {
            fragmentTransaction.replace(R.id.noteView, detail);
        } else {
            fragmentTransaction.replace(R.id.fragment_container, detail);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Fragment noteFragment = fragmentManager.findFragmentById(R.id.noteView);
        if (item.getItemId() == R.id.action_delete) {
            int position = adapter.getMenuPosition();
            //Вносим иземение в firebase
            firebase.deleteCardData(position);
            notesArrayList.remove(position);
            adapter.notifyItemRemoved(position);
            if (isLandscape && noteFragment != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(noteFragment);
                fragmentTransaction.commit();
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void updateNote(Note note, int position) {
        //Меняем лист
        notesArrayList.set(position, note);
        //Меняем RecyclerView
        adapter.notifyItemChanged(position);
        //Вносим иземение в firebase
        firebase.updateNote(position, note);
    }
}
