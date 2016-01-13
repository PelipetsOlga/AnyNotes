package anynotes.olyalya.pelipets.com.anynotes.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import anynotes.olyalya.pelipets.com.anynotes.interfaces.LoadNotesListener;
import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.storage.NotesRepository;

public class NotesService extends Service {

    public NotesService() {
    }

    public static class NotesWorker extends Binder {
        private final Executor executor = Executors.newSingleThreadExecutor();

        public void loadAll(final NotesRepository repository, final LoadNotesListener listener) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    List<Note> allNotes = repository.loadAll();
                    listener.onLoad(allNotes);
                }
            });
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
       return new NotesWorker();
    }
}
