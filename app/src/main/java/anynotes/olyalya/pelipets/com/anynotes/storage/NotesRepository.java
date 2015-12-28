package anynotes.olyalya.pelipets.com.anynotes.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

/**
 * Created by Olga on 26.12.2015.
 */
public class NotesRepository {
    private final SQLiteDatabase db;
    String TAG = NotesRepository.class.getSimpleName();

    public NotesRepository(SQLiteDatabase db) {
        this.db = db;
    }

    public boolean insert(Note note) {
        if (note==null)return false;
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.CREATING, note.getCreating());
        cv.put(DBSchema.LAST_SAVING, note.getLastSaving());
        cv.put(DBSchema.STATUS, note.getStatus());
        cv.put(DBSchema.TITLE, note.getTitle());
        cv.put(DBSchema.TEXT, note.getText());

        long insert = db.insert(DBSchema.TABLE, null, cv);
        NoteUtils.log("insert to DB note " + insert);
        return true;
    }

    public boolean update(Note note) {
        if (note==null)return false;
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.CREATING, note.getCreating());
        cv.put(DBSchema.LAST_SAVING, note.getLastSaving());
        cv.put(DBSchema.STATUS, note.getStatus());
        cv.put(DBSchema.TITLE, note.getTitle());
        cv.put(DBSchema.TEXT, note.getText());

        int update = db.update(DBSchema.TABLE, cv, DBSchema.ID + "=" + note.getId(), null);
        Log.d(TAG, "update note " + update);
        return true;
    }

    public boolean delete(Note note) {
        if (note==null)return false;
        int delete = db.delete(DBSchema.TABLE, DBSchema.ID + "=" + note.getId(), null);
        Log.d(TAG, "delete note" + delete);
        return true;
    }

    public Note findById(long id) {
        Cursor cursor = db.query(DBSchema.TABLE, null, DBSchema.ID + "=" + id,
                null, null, null, null);
        Note note = null;
        if (cursor.moveToFirst()) {
            note=new Note();
            note.setId(cursor.getLong(cursor.getColumnIndex(DBSchema.ID)));
            note.setCreating(cursor.getLong(cursor.getColumnIndex(DBSchema.CREATING)));
            note.setLastSaving(cursor.getLong(cursor.getColumnIndex(DBSchema.LAST_SAVING)));
            note.setStatus(cursor.getInt(cursor.getColumnIndex(DBSchema.STATUS)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(DBSchema.TITLE)));
            note.setText(cursor.getString(cursor.getColumnIndex(DBSchema.TEXT)));
        }
        return note;
    }

    public Note findByKey(long creating) {
        Cursor cursor = db.query(DBSchema.TABLE, null, DBSchema.CREATING + "="+creating,
                null, null, null, null);
        Note note = null;
        if (cursor.moveToFirst()) {
            note=new Note();
            note.setId(cursor.getLong(cursor.getColumnIndex(DBSchema.ID)));
            note.setCreating(cursor.getLong(cursor.getColumnIndex(DBSchema.CREATING)));
            note.setLastSaving(cursor.getLong(cursor.getColumnIndex(DBSchema.LAST_SAVING)));
            note.setStatus(cursor.getInt(cursor.getColumnIndex(DBSchema.STATUS)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(DBSchema.TITLE)));
            note.setText(cursor.getString(cursor.getColumnIndex(DBSchema.TEXT)));
        }
        return note;
    }

    public List<Note> loadAll() {
        Cursor cursor = db.query(DBSchema.TABLE, null, null, null, null, null, null);
        List<Note> items = new ArrayList<Note>();
        if (cursor.moveToFirst()) {
            do {
                Note note=new Note();
                note.setId(cursor.getLong(cursor.getColumnIndex(DBSchema.ID)));
                note.setCreating(cursor.getLong(cursor.getColumnIndex(DBSchema.CREATING)));
                note.setLastSaving(cursor.getLong(cursor.getColumnIndex(DBSchema.LAST_SAVING)));
                note.setStatus(cursor.getInt(cursor.getColumnIndex(DBSchema.STATUS)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(DBSchema.TITLE)));
                note.setText(cursor.getString(cursor.getColumnIndex(DBSchema.TEXT)));
                items.add(note);
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "loadAll cursor.size=" + cursor.getCount() + ", items.size=" + items.size());
        return items;
    }

}

