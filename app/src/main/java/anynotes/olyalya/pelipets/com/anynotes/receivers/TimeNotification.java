package anynotes.olyalya.pelipets.com.anynotes.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.activities.MainActivity;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;

public class TimeNotification extends BroadcastReceiver {
    public TimeNotification() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long noteCreating = intent.getLongExtra(Constants.EXTRA_CREATING, 0);
        String noteTitle = intent.getStringExtra(Constants.EXTRA_NOTE_TITLE);
        String noteText = intent.getStringExtra(Constants.EXTRA_NOTE_CONTENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(noteTitle);
        builder.setContentText(noteText);
    /*    builder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        builder.setVibrate(pattern);
        builder.setStyle(new NotificationCompat.InboxStyle());*/

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

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
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(100, notification);
    }
}
