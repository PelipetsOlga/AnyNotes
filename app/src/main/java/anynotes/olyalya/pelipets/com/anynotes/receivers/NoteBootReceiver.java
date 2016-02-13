package anynotes.olyalya.pelipets.com.anynotes.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import anynotes.olyalya.pelipets.com.anynotes.application.NotesApplication;
import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.storage.NotesRepository;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class NoteBootReceiver extends BroadcastReceiver {
    private static final String TAG = NoteBootReceiver.class.getName();
    private NotesRepository repository;
    private List<Note> notesWithAlarm;

    @Override
    public void onReceive(Context context, Intent intent) {
        NoteUtils.log("onReceive NoteBootReceiver");
        Thread alarmsSetThread = new Thread(new Runnable() {
            @Override
            public void run() {
                repository = NotesApplication.getInstanceApplication().getDaoSession().getRepository();
                NoteUtils.log("NoteBootReceiver repository=" + repository);
                notesWithAlarm = repository.loadAllNotesWithAlarms();
                NoteUtils.log("NoteBootReceiver notesWithAlarm=" + notesWithAlarm);
                for (Note note : notesWithAlarm) {
                    repository.setAlarm(note);
                    NoteUtils.log("NoteBootReceiver setAlarm = " + note.getAlarm());
                }
            }
        });
        alarmsSetThread.start();
    }
}
