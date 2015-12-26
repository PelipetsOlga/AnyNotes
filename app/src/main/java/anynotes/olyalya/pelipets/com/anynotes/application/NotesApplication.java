package anynotes.olyalya.pelipets.com.anynotes.application;

import android.app.Application;

import anynotes.olyalya.pelipets.com.anynotes.storage.DAOSession;

/**
 * Created by Olga on 26.12.2015.
 */
public class NotesApplication extends Application {
    private DAOSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        daoSession = new DAOSession(this);
    }

    public DAOSession getDaoSession() {
        return daoSession;
    }
}
