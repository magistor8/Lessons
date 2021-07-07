package com.magistor8.noteapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Firebase {

    private static final String CARDS_COLLECTION = "notes";
    private static final String TAG = "[Firebase]";

    // База данных Firestore
    private FirebaseFirestore store = FirebaseFirestore.getInstance();

    // Коллекция документов
    private CollectionReference collection = store.collection(CARDS_COLLECTION);

    // Загружаемый список карточек
    private List<Note> notesArrayList  = new ArrayList<>();

    protected void init (FirebaseComplete firebaseComplete) {
        // Получить всю коллекцию, отсортированную по полю «Дата»
        collection.orderBy(CardDataMapping.Fields.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    // При удачном считывании данных загрузим список карточек
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            notesArrayList = new ArrayList<Note>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> doc = document.getData();
                                String id = document.getId();
                                Note newNote = CardDataMapping.toNoteData(id, doc);
                                notesArrayList.add(newNote);
                            }
                            firebaseComplete.complete(notesArrayList);
                            Log.d(TAG, "success " + notesArrayList.size() + " qnt");
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "get failed with ", e);
                    }
                });
    }

    protected void updateNote(int position, Note note) {
        String id = note.getId();
        // Изменить документ по идентификатору
        collection.document(id).set(CardDataMapping.toDocument(note));
    }

    protected void addCardData(final Note note) {
        // Добавить документ
        collection.add(CardDataMapping.toDocument(note))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        note.setId(documentReference.getId());
                }
        });
    }

    protected void deleteCardData(int position) {
        // Удалить документ с определённым идентификатором
        collection.document(notesArrayList.get(position).getId()).delete();
    }

    protected int getSize() {
        return notesArrayList.size();
    }

}
