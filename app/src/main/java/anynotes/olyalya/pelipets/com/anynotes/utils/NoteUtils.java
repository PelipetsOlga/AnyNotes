package anynotes.olyalya.pelipets.com.anynotes.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import anynotes.olyalya.pelipets.com.anynotes.R;

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

    public static void setError(EditText editText, Context ctx) {
        PopupWindow popup = new PopupWindow(ctx);
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);
        popup.setContentView(popupView);
        TextView tvPopup = (TextView) popupView.findViewById(R.id.tv_popup);
        tvPopup.setText(ctx.getResources().getString(R.string.error));
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(editText);
    }

    public static void showErrorMessage(Context ctx) {
        Toast.makeText(ctx, ctx.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
    }

    public static boolean isConnected(Context ctx) {
        boolean isMobileConn=false;
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoWiFi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfoMob = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isWifiConn = networkInfoWiFi.isConnected();
        if (networkInfoMob != null) {
            isMobileConn = networkInfoMob.isConnected();
        }
        log("Wifi connected: " + isWifiConn);
        log("Mobile connected: " + isMobileConn);
        return (isWifiConn || isMobileConn);
    }

    public static void showNotNetErrorMessage(Context ctx) {
        Toast.makeText(ctx, ctx.getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
    }

    public static void setTheme(AppCompatActivity activity){
        SharedPreferences mPref = activity.getSharedPreferences(Constants.PREFS_NAME, activity.MODE_PRIVATE);
        String primaryColor = mPref.getString(Constants.PREF_PRIMARY_COLOR, "pink");
        String accentColor = mPref.getString(Constants.PREF_ACCENT_COLOR, "cyan");

        if (primaryColor.equals("red") && accentColor.equals("red")) {
            activity.setTheme(R.style.Theme_Red_Red);
        } else if (primaryColor.equals("red") && accentColor.equals("pink")) {
            activity.setTheme(R.style.Theme_Red_Pink);
        } else if (primaryColor.equals("red") && accentColor.equals("purple")) {
            activity.setTheme(R.style.Theme_Red_Purple);
        } else if (primaryColor.equals("red") && accentColor.equals("deep_purple")) {
            activity.setTheme(R.style.Theme_Red_Deep_Purple);
        } else if (primaryColor.equals("red") && accentColor.equals("indigo")) {
            activity.setTheme(R.style.Theme_Red_Indigo);
        }else if (primaryColor.equals("red") && accentColor.equals("blue")) {
            activity.setTheme(R.style.Theme_Red_Blue);
        }else if (primaryColor.equals("red") && accentColor.equals("light_blue")) {
            activity.setTheme(R.style.Theme_Red_Light_Blue);
        }else if (primaryColor.equals("red") && accentColor.equals("cyan")) {
            activity.setTheme(R.style.Theme_Red_Cyan);
        }else if (primaryColor.equals("red") && accentColor.equals("teal")) {
            activity.setTheme(R.style.Theme_Red_Teal);
        }else if (primaryColor.equals("red") && accentColor.equals("green")) {
            activity.setTheme(R.style.Theme_Red_Green);
        }else if (primaryColor.equals("red") && accentColor.equals("light_green")) {
            activity.setTheme(R.style.Theme_Red_Light_Green);
        }else if (primaryColor.equals("red") && accentColor.equals("lime")) {
            activity.setTheme(R.style.Theme_Red_Lime);
        }else if (primaryColor.equals("red") && accentColor.equals("yellow")) {
            activity.setTheme(R.style.Theme_Red_Yellow);
        }else if (primaryColor.equals("red") && accentColor.equals("amber")) {
            activity.setTheme(R.style.Theme_Red_Amber);
        }else if (primaryColor.equals("red") && accentColor.equals("orange")) {
            activity.setTheme(R.style.Theme_Red_Orange);
        }else if (primaryColor.equals("red") && accentColor.equals("deep_orange")) {
            activity.setTheme(R.style.Theme_Red_Deep_Orange);
        }else if (primaryColor.equals("red") && accentColor.equals("brown")) {
            activity.setTheme(R.style.Theme_Red_Brown);
        }else if (primaryColor.equals("red") && accentColor.equals("grey")) {
            activity.setTheme(R.style.Theme_Red_Grey);
        }else if (primaryColor.equals("red") && accentColor.equals("blue_grey")) {
            activity.setTheme(R.style.Theme_Red_Blue_Grey);
        }
    }

}
