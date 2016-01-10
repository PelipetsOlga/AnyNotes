package anynotes.olyalya.pelipets.com.anynotes.application;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;

import anynotes.olyalya.pelipets.com.anynotes.storage.DAOSession;

/**
 * Created by Olga on 26.12.2015.
 */
public class NotesApplication extends Application {
    private DAOSession daoSession;
    private AlarmManager am;

    @Override
    public void onCreate() {
        super.onCreate();
        daoSession = new DAOSession(this);
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    public DAOSession getDaoSession() {
        return daoSession;
    }

    public AlarmManager getAlarmManager() {
        return am;
    }
}
