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
import anynotes.olyalya.pelipets.com.anynotes.application.NotesApplication;
import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.storage.NotesRepository;
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

        NotesRepository repository = ((NotesApplication) context.getApplicationContext()).getDaoSession().getRepository();
        Note note = (Note) intent.getSerializableExtra(Constants.EXTRA_NOTE);
        if (note.getRepeat() == 0) {
            note.setAlarm("");
            repository.updateByCreating(note);
        } else {
            repository.reSetAlarm(note);
        }

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.anynotes_origin1);
        builder.setContentTitle(note.getTitle());
        builder.setContentText(note.getText());
        builder.setAutoCancel(true);

        Intent intentNotification = null;
        intentNotification = new Intent(context, NoteActivity.class);
        intentNotification.putExtra(Constants.EXTRA_NOTE, note);
        intentNotification.putExtra(Constants.EXTRA_OPEN_CURRENT_NOTE, true);
        intentNotification.putExtra(Constants.EXTRA_ACTION_TYPE, Constants.EXTRA_ACTION_EDIT_NOTE);
        NoteUtils.log(" intentNotification " + intentNotification);
        PendingIntent pi = null;
        pi = PendingIntent.getActivity(context, 0, intentNotification, PendingIntent.FLAG_CANCEL_CURRENT);
        NoteUtils.log(" PendingIntent " + pi);

        builder.setContentIntent(pi);
        Notification notification =  builder.build();

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
        if (notification != null) nManager.notify((int) note.getCreating(), notification);
    }

    private void loadSettings(Context context) {
        SharedPreferences sPref = context.getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        ringtone = sPref.getString(Constants.PREF_RINGTONE, " ");
        vibro = sPref.getBoolean(Constants.PREF_VIBRO, true);
    }
}
