package anynotes.olyalya.pelipets.com.anynotes.storage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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
        return insert(note, false);
    }

    public boolean insert(Note note, boolean loadedFromServer) {
        if (note == null) return false;
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.CREATING, note.getCreating());
        if (loadedFromServer) {
            cv.put(DBSchema.LAST_SAVING, note.getLastSaving());
        } else {
            cv.put(DBSchema.LAST_SAVING, Calendar.getInstance().getTimeInMillis());
        }
        if (!TextUtils.isEmpty(note.getObjectId())) {
            cv.put(DBSchema.OBJECT_ID, note.getObjectId());
        }
        if (!TextUtils.isEmpty(note.getOwnerId())) {
            cv.put(DBSchema.OWNER_ID, note.getOwnerId());
        }
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

    public void setAlarm(Note note) {
        NoteUtils.log("set first Alarm creating=" + note.getCreating() + ", time=" + note.getAlarm());
        PendingIntent pendingIntent = getPendingIntent(note);
        NoteUtils.log("setAlarm pendingIntent = " + pendingIntent);
        am.cancel(pendingIntent);
        try {
            Date date = dateFormat.parse(note.getAlarm());
            if (date.after(new Date())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    NoteUtils.log("set not repeat Alarm creating=" + note.getCreating() + ", time=" + note.getAlarm());
                    am.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
                } else {
                    NoteUtils.log("set not repeat Alarm creating=" + note.getCreating() + ", time=" + note.getAlarm());
                    am.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void reSetAlarm(Note note) {
        try {
            Date oldAlarmDate = dateFormat.parse(note.getAlarm());
            long newAlarmMillis = oldAlarmDate.getTime() + note.getRepeat();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(newAlarmMillis);
            Date newAlarmDate = calendar.getTime();
            String newAlarmNote = dateFormat.format(newAlarmDate);
            note.setAlarm(newAlarmNote);
            updateByCreating(note);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private PendingIntent getPendingIntent(Note note) {
        Intent intent = new Intent(context, TimeNotification.class);
        intent.putExtra(Constants.EXTRA_NOTE, note);
        return PendingIntent.getBroadcast(context, (int) note.getCreating(),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public boolean updateByCreating(Note note) {
        return updateByCreating(note, false);
    }

    public boolean updateByCreating(Note note, boolean loadedFromServer) {
        Note oldNote = findByCreating(note.getCreating());
        deleteAlarm(oldNote);

        if (note == null) return false;
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.CREATING, note.getCreating());
        if (loadedFromServer) {
            cv.put(DBSchema.LAST_SAVING, note.getLastSaving());
        } else {
            cv.put(DBSchema.LAST_SAVING, Calendar.getInstance().getTimeInMillis());
        }
        cv.put(DBSchema.STATUS, note.getStatus());
        if (!TextUtils.isEmpty(note.getObjectId())) {
            cv.put(DBSchema.OBJECT_ID, note.getObjectId());
        }
        if (!TextUtils.isEmpty(note.getOwnerId())) {
            cv.put(DBSchema.OWNER_ID, note.getOwnerId());
        }
        cv.put(DBSchema.TITLE, note.getTitle());
        if (note.getStatus() == Constants.STATUS_DELETED
                || note.getStatus() == Constants.STATUS_DRAFT_DELETED) {
            cv.put(DBSchema.ALARM, "");
            cv.put(DBSchema.REPEAT, 0);
        } else {
            cv.put(DBSchema.ALARM, note.getAlarm());
            cv.put(DBSchema.REPEAT, note.getRepeat());
        }
        cv.put(DBSchema.TEXT, note.getText());

        int update = db.update(DBSchema.TABLE, cv, DBSchema.CREATING + "=" + note.getCreating(), null);
        Log.d(TAG, "updateByCreating note " + update);
        if (note.getAlarm() != null && !TextUtils.isEmpty(note.getAlarm())) {
            setAlarm(note);
        }
        return true;
    }

    private void deleteAlarm(Note oldNote) {
        if (oldNote != null) {
            NoteUtils.log("delete Alarm creating=" + oldNote.getCreating());
            String oldAlarm = oldNote.getAlarm();
            if (oldAlarm != null && !TextUtils.isEmpty(oldAlarm.trim())) {
                PendingIntent oldPendingIntent = getPendingIntent(oldNote);
                oldPendingIntent.cancel();
            }
        }
    }

    public Note findByCreating(long creating) {
        Cursor cursor = db.query(DBSchema.TABLE, null, DBSchema.CREATING + "=" + creating,
                null, null, null, null);
        Note note = null;
        if (cursor.moveToFirst()) {
            note = new Note();
            note.setId(cursor.getLong(cursor.getColumnIndex(DBSchema.ID)));
            String objectId = cursor.getString(cursor.getColumnIndex(DBSchema.OBJECT_ID));
            if (!TextUtils.isEmpty(objectId)) {
                note.setObjectId(objectId);
            }
            String ownerId = cursor.getString(cursor.getColumnIndex(DBSchema.OWNER_ID));
            if (!TextUtils.isEmpty(ownerId)) {
                note.setOwnerId(ownerId);
            }
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
            case Constants.MODE_SORT_SEARCH:
                SharedPreferences preferences = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
                String keyword = preferences.getString(Constants.PREF_SEARCH, "");
                selections = DBSchema.TEXT + " like \"%" + keyword + "%\" OR " + DBSchema.TITLE + " like \"%" + keyword + "%\"";
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
                String objectId = cursor.getString(cursor.getColumnIndex(DBSchema.OBJECT_ID));
                if (!TextUtils.isEmpty(objectId)) {
                    note.setObjectId(objectId);
                }
                String ownerId = cursor.getString(cursor.getColumnIndex(DBSchema.OWNER_ID));
                if (!TextUtils.isEmpty(ownerId)) {
                    note.setOwnerId(ownerId);
                }
                note.setCreating(cursor.getLong(cursor.getColumnIndex(DBSchema.CREATING)));
                note.setLastSaving(cursor.getLong(cursor.getColumnIndex(DBSchema.LAST_SAVING)));
                note.setStatus(cursor.getInt(cursor.getColumnIndex(DBSchema.STATUS)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(DBSchema.TITLE)));
                note.setAlarm(alarm);
                note.setRepeat(cursor.getLong(cursor.getColumnIndex(DBSchema.REPEAT)));
                note.setText(cursor.getString(cursor.getColumnIndex(DBSchema.TEXT)));
                if (modeSort == Constants.MODE_SORT_ALARMS) {
                    if (alarm != null && !TextUtils.isEmpty(alarm)) {
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

    public List<Note> loadFreshNotes(long lastSynch) {
        String selections = DBSchema.LAST_SAVING + ">=" + lastSynch;
        Cursor cursor = db.query(DBSchema.TABLE, null, selections, null, null, null, null);
        List<Note> items = new ArrayList<Note>();
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getLong(cursor.getColumnIndex(DBSchema.ID)));
                String objectId = cursor.getString(cursor.getColumnIndex(DBSchema.OBJECT_ID));
                if (!TextUtils.isEmpty(objectId)) {
                    note.setObjectId(objectId);
                }
                String ownerId = cursor.getString(cursor.getColumnIndex(DBSchema.OWNER_ID));
                if (!TextUtils.isEmpty(ownerId)) {
                    note.setOwnerId(ownerId);
                }
                note.setCreating(cursor.getLong(cursor.getColumnIndex(DBSchema.CREATING)));
                note.setLastSaving(cursor.getLong(cursor.getColumnIndex(DBSchema.LAST_SAVING)));
                note.setStatus(cursor.getInt(cursor.getColumnIndex(DBSchema.STATUS)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(DBSchema.TITLE)));
                note.setAlarm(cursor.getString(cursor.getColumnIndex(DBSchema.ALARM)));
                note.setRepeat(cursor.getLong(cursor.getColumnIndex(DBSchema.REPEAT)));
                note.setText(cursor.getString(cursor.getColumnIndex(DBSchema.TEXT)));
                items.add(note);
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "loadAll fo ssynch cursor.size=" + cursor.getCount() + ", items.size=" + items.size());
        return items;
    }

    public void insertOrUpdateLoadedNote(Note loadedNote) {
        if (loadedNote.getObjectId() == null) return;
        Note oldSuchNoteByObjectId = findByObjectId(loadedNote.getObjectId());
        if (oldSuchNoteByObjectId == null) {
            Note oldSuchNoteByCreating = findByCreating(loadedNote.getCreating());
            if (oldSuchNoteByCreating == null) {
                insert(loadedNote, true);
            } else {
                updateByObjectId(loadedNote, true);
            }
        } else {
            updateByObjectId(loadedNote, true);
        }
    }

    public Note findByObjectId(String objectId) {
        Cursor cursor = db.query(DBSchema.TABLE, null, DBSchema.OBJECT_ID + "='" + objectId + "'",
                null, null, null, null);
        Note note = null;
        if (cursor.moveToFirst()) {
            note = new Note();
            note.setId(cursor.getLong(cursor.getColumnIndex(DBSchema.ID)));
            note.setObjectId(cursor.getString(cursor.getColumnIndex(DBSchema.OBJECT_ID)));
            note.setOwnerId(cursor.getString(cursor.getColumnIndex(DBSchema.OWNER_ID)));
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

    public boolean updateByObjectId(Note note) {
        return updateByObjectId(note, false);
    }

    public boolean updateByObjectId(Note note, boolean loadedFromServer) {
        if (note == null) return false;

        Note oldNote = findByObjectId(note.getObjectId());
        deleteAlarm(oldNote);

        ContentValues cv = new ContentValues();
        cv.put(DBSchema.CREATING, note.getCreating());
        if (loadedFromServer) {
            if (oldNote != null && oldNote.getLastSaving() > note.getLastSaving()) {
                cv.put(DBSchema.LAST_SAVING, oldNote.getLastSaving());
            } else {
                cv.put(DBSchema.LAST_SAVING, note.getLastSaving());
            }
            cv.put(DBSchema.LAST_SAVING, note.getLastSaving());
        } else {
            cv.put(DBSchema.LAST_SAVING, Calendar.getInstance().getTimeInMillis());
        }
        cv.put(DBSchema.STATUS, note.getStatus());
        if (!TextUtils.isEmpty(note.getObjectId())) {
            cv.put(DBSchema.OBJECT_ID, note.getObjectId());
        }
        if (!TextUtils.isEmpty(note.getOwnerId())) {
            cv.put(DBSchema.OWNER_ID, note.getOwnerId());
        }
        cv.put(DBSchema.TITLE, note.getTitle());
        if (note.getStatus() == Constants.STATUS_DELETED
                || note.getStatus() == Constants.STATUS_DRAFT_DELETED) {
            cv.put(DBSchema.ALARM, "");
            cv.put(DBSchema.REPEAT, 0);
        } else {
            cv.put(DBSchema.ALARM, note.getAlarm());
            cv.put(DBSchema.REPEAT, note.getRepeat());
        }
        cv.put(DBSchema.TEXT, note.getText());

        int update = db.update(DBSchema.TABLE, cv, DBSchema.OBJECT_ID + "='" + note.getObjectId() + "'", null);
        Log.d(TAG, "updateByObjectId note " + update);
        if (note.getAlarm() != null && !TextUtils.isEmpty(note.getAlarm())) {
            setAlarm(note);
        }
        return true;
    }


    public void writeObjectIdAndOwnerId(Note response) {
        ContentValues cv = new ContentValues();
        cv.put(DBSchema.OBJECT_ID, response.getObjectId());
        cv.put(DBSchema.OWNER_ID, response.getOwnerId());
        int update = db.update(DBSchema.TABLE, cv, DBSchema.CREATING + "=" + response.getCreating(), null);
        Log.d(TAG, "updateByObjectId note " + update);
    }

    public List<Note> loadAllNotesWithAlarms() {
        List<Note> items = new ArrayList<Note>();
        NoteUtils.log(" public List<Note> loadAllNotesWithAlarms()");
        Cursor cursor = db.query(DBSchema.TABLE, null, null, null, null, null, null);
        int k = 0;
        NoteUtils.log(" cursor=" + cursor);
        NoteUtils.log(" cursor_size=" + cursor.getCount());
        if (cursor.moveToFirst()) {
            NoteUtils.log(" cursor moved to first");
            do {
                String alarm = cursor.getString(cursor.getColumnIndex(DBSchema.ALARM));
                if (alarm != null && !TextUtils.isEmpty(alarm)) {
                    Note note = new Note();
                    note.setId(cursor.getLong(cursor.getColumnIndex(DBSchema.ID)));
                    String objectId = cursor.getString(cursor.getColumnIndex(DBSchema.OBJECT_ID));
                    if (!TextUtils.isEmpty(objectId)) {
                        note.setObjectId(objectId);
                    }
                    String ownerId = cursor.getString(cursor.getColumnIndex(DBSchema.OWNER_ID));
                    if (!TextUtils.isEmpty(ownerId)) {
                        note.setOwnerId(ownerId);
                    }
                    note.setCreating(cursor.getLong(cursor.getColumnIndex(DBSchema.CREATING)));
                    note.setLastSaving(cursor.getLong(cursor.getColumnIndex(DBSchema.LAST_SAVING)));
                    note.setStatus(cursor.getInt(cursor.getColumnIndex(DBSchema.STATUS)));
                    note.setTitle(cursor.getString(cursor.getColumnIndex(DBSchema.TITLE)));
                    note.setAlarm(alarm);
                    note.setRepeat(cursor.getLong(cursor.getColumnIndex(DBSchema.REPEAT)));
                    note.setText(cursor.getString(cursor.getColumnIndex(DBSchema.TEXT)));
                    items.add(note);
                    NoteUtils.log(" note with alarm #" + (++k));
                }
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "loadAll fo ssynch cursor.size=" + cursor.getCount() + ", items.size=" + items.size());
        return items;
    }
}

