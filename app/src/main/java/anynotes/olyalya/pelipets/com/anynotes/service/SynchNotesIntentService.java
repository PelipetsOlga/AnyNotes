package anynotes.olyalya.pelipets.com.anynotes.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.Calendar;
import java.util.List;

import anynotes.olyalya.pelipets.com.anynotes.application.NotesApplication;
import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.storage.NotesRepository;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class SynchNotesIntentService extends IntentService {
    private NotesApplication application;
    private NotesRepository repository;
    private boolean isLogined;
    private String login;
    private String password;

    public SynchNotesIntentService() {
        super("anynotes_coding");
        NoteUtils.log("SynchNotesIntentService(anynotes_coding)");
    }

    public SynchNotesIntentService(String name) {
        super(name);
        NoteUtils.log("SynchNotesIntentService(String" + name + ")");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NoteUtils.log("onHandleIntent");

        isLogined = intent.getBooleanExtra(Constants.PREF_IS_LOGINED, false);
        login = intent.getStringExtra(Constants.PREF_LOGIN);
        password = intent.getStringExtra(Constants.PREF_PASSWORD);

        if (isLogined && !TextUtils.isEmpty(login) && !TextUtils.isEmpty(password)) {
            application = NotesApplication.getInstanceApplication();
            repository = application.getDaoSession().getRepository();
            saveAllDataToServer();
        }
    }

    private void saveAllDataToServer() {
        if (!NoteUtils.isConnected(application)) {
            NoteUtils.log("SynchNotesIntentService not Net");
            return;
        }
        NoteUtils.log("SynchNotesIntentService there is Net");

        final long lastSynch = readLastSynchDate();
        NoteUtils.log("lastSynch read " + lastSynch);
        final String whereClause = "lastSaving > " + lastSynch;

        Backendless.UserService.login(login, password, new AsyncCallback<BackendlessUser>() {

            public void handleResponse(BackendlessUser user) {
                NoteUtils.log("response success hidden login" + user);

                BackendlessDataQuery dataQuery = new BackendlessDataQuery();
                dataQuery.setWhereClause(whereClause);
                Backendless.Persistence.of(Note.class).find(dataQuery,
                        new AsyncCallback<BackendlessCollection<Note>>() {
                            @Override
                            public void handleResponse(BackendlessCollection<Note> response) {
                                for (Note note : response.getData()) {
                                    repository.insertOrUpdateLoadedNote(note);
                                }
                                saveCurrentSynchDate();
                                List<Note> freshNotes = repository.loadFreshNotes(lastSynch);
                                final boolean[] perfectSaving = {true};
                                try {
                                    for (Note note : freshNotes) {
                                        Backendless.Persistence.save(note, new AsyncCallback<Note>() {
                                            public void handleResponse(Note response) {
                                                repository.writeObjectId(response);
                                            }

                                            public void handleFault(BackendlessFault fault) {
                                                perfectSaving[0] = false;
                                            }
                                        });
                                    }

                                } catch (Exception e) {
                                    NoteUtils.log("error in handleResponce in Find");
                                } finally {
                                    if (perfectSaving[0]) {
                                        saveCurrentSynchDate();
                                    }
                                }
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                NoteUtils.log("response query failure" + fault);
                            }
                        });
            }

            public void handleFault(BackendlessFault fault) {
                NoteUtils.log("response hidden login failure" + fault);
            }
        }, true);
    }

    private long readLastSynchDate() {
        SharedPreferences pref = application.getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        return pref.getLong(Constants.PREF_LAST_SYNCH, 0);
    }

    private void saveCurrentSynchDate() {
        SharedPreferences pref = application.getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        long newLastSynch = Calendar.getInstance().getTimeInMillis();
        ed.putLong(Constants.PREF_LAST_SYNCH, newLastSynch);
        ed.commit();
        NoteUtils.log("lastSynch write " + newLastSynch);
    }
}
