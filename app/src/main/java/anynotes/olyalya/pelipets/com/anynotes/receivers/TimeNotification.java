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
import anynotes.olyalya.pelipets.com.anynotes.activities.MainActivity;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;

public class TimeNotification extends BroadcastReceiver {
    public TimeNotification() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sPref = context.getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        String ringtone = sPref.getString(Constants.PREF_RINGTONE, " ");

        long noteCreating = intent.getLongExtra(Constants.EXTRA_CREATING, 0);
        String noteTitle = intent.getStringExtra(Constants.EXTRA_NOTE_TITLE);
        String noteText = intent.getStringExtra(Constants.EXTRA_NOTE_CONTENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(noteTitle);
        builder.setContentText(noteText);
       /* if (TextUtils.isEmpty(ringtone.trim())) {
            //default sound
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
        }*/

    /*
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        builder.setVibrate(pattern);
        builder.setStyle(new NotificationCompat.InboxStyle());*/


        builder.setAutoCancel(true);

        Intent intentNotification = new Intent(context, MainActivity.class);
        intentNotification.putExtra(Constants.EXTRA_CREATING, noteCreating);
        intentNotification.putExtra(Constants.EXTRA_NOTE_TITLE, noteTitle);
        intentNotification.putExtra(Constants.EXTRA_NOTE_CONTENT, noteText);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intentNotification, 0);

        builder.setContentIntent(pi);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }
        if (notification != null) {
            //notification.defaults |= Notification.DEFAULT_VIBRATE;
            long[] vibrate = {500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,500};
            notification.vibrate = vibrate;

            if (TextUtils.isEmpty(ringtone.trim())) {
                notification.defaults |= Notification.DEFAULT_SOUND;
            } else {
                int ringId=context.getResources().getIdentifier(ringtone,"raw",context.getPackageName());
                notification.sound = Uri.parse("android.resource://"
                        + context.getPackageName() + "/" + ringId);
            }


        }

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(100, notification);
    }
}
