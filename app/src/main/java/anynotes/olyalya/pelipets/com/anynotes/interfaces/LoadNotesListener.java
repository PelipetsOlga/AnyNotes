package anynotes.olyalya.pelipets.com.anynotes.interfaces;

import java.util.List;

import anynotes.olyalya.pelipets.com.anynotes.models.Note;

/**
 * Created by Olga on 13.01.2016.
 */
public interface LoadNotesListener {
    void onLoad(List<Note> items);
}
