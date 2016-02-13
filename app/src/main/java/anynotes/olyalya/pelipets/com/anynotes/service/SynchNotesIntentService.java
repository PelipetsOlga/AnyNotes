package anynotes.olyalya.pelipets.com.anynotes.service;

import android.app.IntentService;
import android.content.Intent;

import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class SynchNotesIntentService extends IntentService {

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
        try {
            Thread.sleep(5000);
            NoteUtils.log("onHandleIntent after sleep");
            Thread.sleep(5000);
            NoteUtils.log("onHandleIntent after sleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
