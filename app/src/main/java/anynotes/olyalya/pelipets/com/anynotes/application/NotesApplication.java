package anynotes.olyalya.pelipets.com.anynotes.application;

import android.app.Application;

import com.backendless.Backendless;

import anynotes.olyalya.pelipets.com.anynotes.storage.DAOSession;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;

/**
 * Created by Olga on 26.12.2015.
 */
public class NotesApplication extends Application {
    private static NotesApplication instanceApplication;
    private DAOSession daoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        daoSession = new DAOSession(this);
        instanceApplication=this;

        Backendless.initApp(this, Constants.BAAS_APP_ID,
                Constants.BAAS_APP_SECRET, Constants.BAAS_APP_VERSION);
    }

    public DAOSession getDaoSession() {
        return daoSession;
    }

    public static NotesApplication getInstanceApplication() {
        return instanceApplication;
    }
}
