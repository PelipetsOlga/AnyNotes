package anynotes.olyalya.pelipets.com.anynotes.utils;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by Olga on 27.12.2015.
 */
public class NoteUtils {

    public static void log(String msg) {
        Log.d(Constants.TAG, msg);
    }

    public static void setBrightness(int bright, AppCompatActivity ctx) {
        WindowManager.LayoutParams lparams = ctx.getWindow().getAttributes();
        lparams.screenBrightness = (float) bright / 255;
        ctx.getWindow().setAttributes(lparams);
    }


}
