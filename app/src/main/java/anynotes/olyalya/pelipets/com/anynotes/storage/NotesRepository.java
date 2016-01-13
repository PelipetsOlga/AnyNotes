package anynotes.olyalya.pelipets.com.anynotes.storage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.receivers.TimeNotification;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

/**
 * Created by Olga on 26.12.2015.
 */
public class NotesRepository {
    private final SQLiteDatabase db;
    private final Context context;
    private final AlarmManager am;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.FORMAT_ALARM);

    String TAG = NotesRepository.class.getSimpleName();
    private int modeSort = Constants.MODE_SORT_ALL;
    private int modeOrdered = Constants.MODE_ORDERED_UNSORTED;

    public void setModeOrdered(int modeOrdered) {
        this.modeOrdered = modeOrdered;
    }

    public void setModeSort(int modeSort) {
        this.modeSort = modeSort;
    }

    public NotesRepository(SQLiteDatabase db, Context context) {
        this.db = db;
        this.context = context;
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public boolean insert(Note note) {
        if (note == null) return false;
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.CREATING, note.getCreating());
        cv.put(DBSchema.LAST_SAVING, note.getLastSaving());
        cv.put(DBSchema.STATUS, note.getStatus());
        cv.put(DBSchema.TITLE, note.getTitle());
        cv.put(DBSchema.ALARM, note.getAlarm());
        cv.put(DBSchema.REPEAT, note.getRepeat());
        cv.put(DBSchema.TEXT, note.getText());

        long insert = db.insert(DBSchema.TABLE, null, cv);
        NoteUtils.log("insert to DB note " + insert);
        if (note.getAlarm() != null && !TextUtils.isEmpty(note.getAlarm())) {
            setAlarm(note);
        }
        return true;
    }

    private void setAlarm(Note note) {
        PendingIntent pendingIntent = getPendingIntent(note);
        am.cancel(pendingIntent);
        try {
            Date date = dateFormat.parse(note.getAlarm());
            am.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private PendingIntent getPendingIntent(Note note) {
        Intent intent = new Intent(context, TimeNotification.class);
        intent.putExtra(Constants.EXTRA_CREATING, note.getCreating());
        intent.putExtra(Constants.EXTRA_NOTE_TITLE, note.getTitle());
        intent.putExtra(Constants.EXTRA_NOTE_CONTENT, note.getText());
        intent.putExtra(Constants.EXTRA_REPEAT, note.getRepeat());
        return PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public boolean update(Note note) {
        Note oldNote = findByKey(note.getCreating());
        deleteAlarm(oldNote);

        if (note == null) return false;
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.CREATING, note.getCreating());
        cv.put(DBSchema.LAST_SAVING, Calendar.getInstance().getTimeInMillis());
        cv.put(DBSchema.STATUS, note.getStatus());
        cv.put(DBSchema.TITLE, note.getTitle());
        cv.put(DBSchema.ALARM, note.getAlarm());
        cv.put(DBSchema.REPEAT, note.getRepeat());
        cv.put(DBSchema.TEXT, note.getText());

        int update = db.update(DBSchema.TABLE, cv, DBSchema.CREATING + "=" + note.getCreating(), null);
        Log.d(TAG, "update note " + update);
        if (note.getAlarm() != null && !TextUtils.isEmpty(note.getAlarm())) {
            setAlarm(note);
        }
        return true;
    }

    private void deleteAlarm(Note oldNote) {
        if (oldNote != null) {
            String oldAlarm = oldNote.getAlarm();
            if (oldAlarm != null && !TextUtils.isEmpty(oldAlarm.trim())) {
                PendingIntent oldPendingIntent = getPendingIntent(oldNote);
                oldPendingIntent.cancel();
            }
        }
    }

    public boolean delete(Note note) {
        if (note == null) return false;
        int delete = db.delete(DBSchema.TABLE, DBSchema.ID + "=" + note.getId(), null);
        Log.d(TAG, "delete note" + delete);
        deleteAlarm(note);
        return true;
    }

    public Note findById(long id) {
        Cursor cursor = db.query(DBSchema.TABLE, null, DBSchema.ID + "=" + id,
                null, null, null, null);
        Note note = null;
        if (cursor.moveToFirst()) {
            note = new Note();
            note.setId(cursor.getLong(cursor.getColumnIndex(DBSchema.ID)));
            note.setCreating(cursor.getLong(cursor.getColumnIndex(DBSchema.CREATING)));
            note.setLastSaving(cursor.getLong(cursor.getColumnIndex(DBSchema.LAST_SAVING)));
            note.setStatus(cursor.getInt(cursor.getColumnIndex(DBSchema.STATUS)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(DBSchema.TITLE)));
            note.setAlarm(cursor.getString(cursor.getColumnIndex(DBSchema.ALARM)));
            note.setRepeat(cursor.getLong(cursor.getColumnIndex(DBSchema.REPEAT)));
            note.setText(cursor.getString(cursor.getColumnIndex(DBSchema.TEXT)));
        }
        return note;
    }

    public Note findByKey(long creating) {
        Cursor cursor = db.query(DBSchema.TABLE, null, DBSchema.CREATING + "=" + creating,
                null, null, null, null);
        Note note = null;
        if (cursor.moveToFirst()) {
            note = new Note();
            note.setId(cursor.getLong(cursor.getColumnIndex(DBSchema.ID)));
            note.setCreating(cursor.getLong(cursor.getColumnIndex(DBSchema.CREATING)));
            note.setLastSaving(cursor.getLong(cursor.getColumnIndex(DBSchema.LAST_SAVING)));
            note.setStatus(cursor.getInt(cursor.getColumnIndex(DBSchema.STATUS)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(DBSchema.TITLE)));
            note.setAlarm(cursor.getString(cursor.getColumnIndex(DBSchema.ALARM)));
            note.setRepeat(cursor.getLong(cursor.getColumnIndex(DBSchema.REPEAT)));
            note.setText(cursor.getString(cursor.getColumnIndex(DBSchema.TEXT)));
        }
        return note;
    }

    public List<Note> loadAll() {
        //TODO
        String selections = null;
        switch (modeSort) {
            case Constants.MODE_SORT_ALL:
            case Constants.MODE_SORT_ALARMS:
                selections = DBSchema.STATUS + "=" + Constants.STATUS_ACTUAL + " OR " + DBSchema.STATUS + "=" + Constants.STATUS_IMPORTANT;
                break;
            case Constants.MODE_SORT_IMPORTANTS:
                selections = DBSchema.STATUS + "=" + Constants.STATUS_IMPORTANT;
                break;
            case Constants.MODE_SORT_ACTUALS:
                selections = DBSchema.STATUS + "=" + Constants.STATUS_ACTUAL;
                break;
            case Constants.MODE_SORT_DRAFTS:
                selections = DBSchema.STATUS + "=" + Constants.STATUS_DRAFT;
                break;
            case Constants.MODE_SORT_DELETED:
                selections = DBSchema.STATUS + "=" + Constants.STATUS_DELETED;
                break;
        }

        String ordered = null;
        switch (modeOrdered) {
            case Constants.MODE_ORDERED_UNSORTED:
                ordered = DBSchema.CREATING + " ASC";
                break;
            case Constants.MODE_ORDERED_SORT_ALPHA_ASC:
                ordered = DBSchema.TITLE + " ASC";
                break;
            case Constants.MODE_ORDERED_SORT_ALPHA_DESC:
                ordered = DBSchema.TITLE + " DESC";
                break;
            case Constants.MODE_ORDERED_SORT_DATE_ASC:
                ordered = DBSchema.LAST_SAVING + " ASC";
                break;
            case Constants.MODE_ORDERED_SORT_DATE_DESC:
                ordered = DBSchema.LAST_SAVING + " DESC";
                break;
        }

        Cursor cursor = db.query(DBSchema.TABLE, null, selections, null, null, null, ordered);
        List<Note> items = new ArrayList<Note>();
        if (cursor.moveToFirst()) {
            do {
                String alarm = cursor.getString(cursor.getColumnIndex(DBSchema.ALARM));
                Note note = new Note();
                note.setId(cursor.getLong(cursor.getColumnIndex(DBSchema.ID)));
                note.setCreating(cursor.getLong(cursor.getColumnIndex(DBSchema.CREATING)));
                note.setLastSaving(cursor.getLong(cursor.getColumnIndex(DBSchema.LAST_SAVING)));
                note.setStatus(cursor.getInt(cursor.getColumnIndex(DBSchema.STATUS)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(DBSchema.TITLE)));
                note.setAlarm(alarm);
                note.setRepeat(cursor.getLong(cursor.getColumnIndex(DBSchema.REPEAT)));
                note.setText(cursor.getString(cursor.getColumnIndex(DBSchema.TEXT)));
                if (modeSort == Constants.MODE_SORT_ALARMS) {
                    if (alarm!=null && !TextUtils.isEmpty(alarm)) {
                        items.add(note);
                    }
                } else {
                    items.add(note);
                }
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "loadAll cursor.size=" + cursor.getCount() + ", items.size=" + items.size());
        return items;
    }

}

