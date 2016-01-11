package anynotes.olyalya.pelipets.com.anynotes.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.activities.MainActivity;
import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;

public class TimeNotification extends BroadcastReceiver {
    public TimeNotification() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Note note= (Note) intent.getSerializableExtra(Constants.EXTRA_NOTE);
        //builder.setContentTitle(note.getTitle());
        //builder.setContentText(note.getText());
        builder.setContentTitle("test note title");
        builder.setContentText("test note text");
        //builder.setProgress(100, 50, false);
        builder.setAutoCancel(true);

        Intent intentNotification = new Intent(context,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intentNotification, 0);

        builder.setContentIntent(pi);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(100, notification);
    }
}
