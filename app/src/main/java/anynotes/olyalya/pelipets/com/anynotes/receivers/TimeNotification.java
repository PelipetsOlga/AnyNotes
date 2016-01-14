package anynotes.olyalya.pelipets.com.anynotes.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.activities.NoteActivity;
import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class TimeNotification extends BroadcastReceiver {
    private String ringtone;
    private boolean vibro;

    public TimeNotification() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        loadSettings(context);

        long noteCreating = intent.getLongExtra(Constants.EXTRA_CREATING, 0);
        String noteTitle = intent.getStringExtra(Constants.EXTRA_NOTE_TITLE);
        int noteStatus = intent.getIntExtra(Constants.EXTRA_STATUS, Constants.STATUS_ACTUAL);
        long noteLastSaving = intent.getLongExtra(Constants.EXTRA_LASTSAVING, 0);
        String noteText = intent.getStringExtra(Constants.EXTRA_NOTE_CONTENT);
        String noteAlarm = intent.getStringExtra(Constants.EXTRA_TIME_DATE);
        long noteRepeat = intent.getIntExtra(Constants.EXTRA_REPEAT, 0);

        NoteUtils.log("TimeNotification onReceive() creating=" + noteCreating);

        Note note = new Note();
        note.setCreating(noteCreating);
        note.setTitle(noteTitle);
        note.setText(noteText);
        note.setStatus(noteStatus);
        note.setLastSaving(noteLastSaving);
        note.setAlarm(noteAlarm);
        note.setRepeat(noteRepeat);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(noteTitle);
        builder.setContentText(noteAlarm);
        builder.setAutoCancel(true);

        Intent intentNotification = new Intent(context, NoteActivity.class);
        intentNotification.putExtra(Constants.EXTRA_CREATING, noteCreating);
        intentNotification.putExtra(Constants.EXTRA_NOTE_TITLE, noteTitle);
        intentNotification.putExtra(Constants.EXTRA_TIME_DATE, noteAlarm);
        intentNotification.putExtra(Constants.EXTRA_STATUS, noteStatus);
        intentNotification.putExtra(Constants.EXTRA_NOTE_CONTENT, noteText);
        intentNotification.putExtra(Constants.EXTRA_REPEAT, noteRepeat);
        intentNotification.putExtra(Constants.EXTRA_OPEN_CURRENT_NOTE,true);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intentNotification, 0);

        builder.setContentIntent(pi);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }
        if (notification != null) {
            if (vibro) {
                long[] vibrate = {500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500};
                notification.vibrate = vibrate;
            }

            if (TextUtils.isEmpty(ringtone.trim())) {
                notification.defaults |= Notification.DEFAULT_SOUND;
            } else {
                int ringId = context.getResources().getIdentifier(ringtone, "raw", context.getPackageName());
                notification.sound = Uri.parse("android.resource://"
                        + context.getPackageName() + "/" + ringId);
            }
        }

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notification != null) nManager.notify((int) noteCreating, notification);
    }

    private void loadSettings(Context context) {
        SharedPreferences sPref = context.getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        ringtone = sPref.getString(Constants.PREF_RINGTONE, " ");
        vibro = sPref.getBoolean(Constants.PREF_VIBRO, true);
    }
}
