package anynotes.olyalya.pelipets.com.anynotes.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Olga on 26.12.2015.
 */
public class DAOSession {

    private NotesRepository repository;

    public DAOSession(Context context) {
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        repository = new NotesRepository(db);
    }

    public NotesRepository getRepository() {
        return repository;
    }
}
