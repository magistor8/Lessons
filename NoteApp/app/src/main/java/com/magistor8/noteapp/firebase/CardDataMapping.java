package com.magistor8.noteapp.firebase;

import com.magistor8.noteapp.data.Note;

import java.util.HashMap;
import java.util.Map;

public class CardDataMapping {

    public static class Fields{
        public final static String DATE = "date";
        public final static String TITLE = "title";
        public final static String DESCRIPTION = "description";
    }

    public static Note toNoteData(String id, Map<String, Object> doc) {
        Note answer = new Note(((String)doc.get(Fields.TITLE)), ((String)doc.get(Fields.DESCRIPTION)), ((long)doc.get(Fields.DATE)));
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDocument(Note note){
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.TITLE, note.getTitle());
        answer.put(Fields.DESCRIPTION, note.getDescription());
        answer.put(Fields.DATE, note.getDate());
        return answer;
    }
}