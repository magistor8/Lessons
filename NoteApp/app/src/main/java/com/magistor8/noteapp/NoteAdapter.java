package com.magistor8.noteapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> notesArrayList;
    private OnItemClickListener itemClickListener;  // Слушатель будет устанавливаться извне

    // Передаём в конструктор источник данных
    public NoteAdapter(List<Note> dataSource) {
        this.notesArrayList = dataSource;
    }

    // Создать новый элемент пользовательского интерфейса
    // Запускается менеджером
    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Создаём новый элемент пользовательского интерфейса
        // Через Inflater
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note, viewGroup, false);
        // Здесь можно установить всякие параметры
        return new ViewHolder(v);
    }

    // Заменить данные в пользовательском интерфейсе
    // Вызывается менеджером
    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder viewHolder, int i) {
        // Вынести на экран, используя ViewHolder
        viewHolder.getTitle().setText(notesArrayList.get(i).getTitle());
        SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
        String date = sd.format(new Date(notesArrayList.get(i).getDate() * 1000));
        viewHolder.getData().setText(date);
        sd.applyPattern("HH:mm");
        String time = sd.format(new Date(notesArrayList.get(i).getDate() * 1000));
        viewHolder.getTime().setText(time);
    }

    // Вернуть размер данных, вызывается менеджером
    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    // Сеттер слушателя нажатий
    public void SetOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    // Интерфейс для обработки нажатий, как в ListView
    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    // Этот класс хранит связь между данными и элементами View
    // Сложные данные могут потребовать несколько View на один пункт списка
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Title;
        private TextView Data;
        private TextView Time;

        public ViewHolder(@NonNull View NoteItem) {
            super(NoteItem);
            Title = (TextView) NoteItem.findViewById(R.id.title);
            Data = (TextView) NoteItem.findViewById(R.id.data);
            Time = (TextView) NoteItem.findViewById(R.id.time);

            // Обработчик нажатий на этом ViewHolder
            NoteItem.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }

        public TextView getTitle() {
            return Title;
        }

        public TextView getData() {
            return Data;
        }

        public TextView getTime() {
            return Time;
        }
    }
}