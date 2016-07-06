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

public class NoteUtils {

    public static final String RED = "red";
    public static final String PINK = "pink";
    public static final String PURPLE = "purple";
    public static final String DEEP_PURPLE = "deep_purple";
    public static final String INDIGO = "indigo";
    public static final String BLUE = "blue";
    public static final String LIGHT_BLUE = "light_blue";
    public static final String CYAN = "cyan";
    public static final String TEAL = "teal";
    public static final String GREEN = "green";
    public static final String LIGHT_GREEN = "light_green";
    public static final String LIME = "lime";
    public static final String YELLOW = "yellow";
    public static final String AMBER = "amber";
    public static final String ORANGE = "orange";
    public static final String DEEP_ORANGE = "deep_orange";
    public static final String BROWN = "brown";
    public static final String GREY = "grey";
    public static final String BLUE_GREY = "blue_grey";

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

        if (primaryColor.equals("red") && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Red_Red);
        } else if (primaryColor.equals("red") && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Red_Pink);
        } else if (primaryColor.equals("red") && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Red_Purple);
        } else if (primaryColor.equals("red") && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Red_Deep_Purple);
        } else if (primaryColor.equals("red") && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Red_Indigo);
        }else if (primaryColor.equals("red") && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Red_Blue);
        }else if (primaryColor.equals("red") && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Red_Light_Blue);
        }else if (primaryColor.equals("red") && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Red_Cyan);
        }else if (primaryColor.equals("red") && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Red_Teal);
        }else if (primaryColor.equals("red") && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Red_Green);
        }else if (primaryColor.equals("red") && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Red_Light_Green);
        }else if (primaryColor.equals("red") && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Red_Lime);
        }else if (primaryColor.equals("red") && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Red_Yellow);
        }else if (primaryColor.equals("red") && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Red_Amber);
        }else if (primaryColor.equals("red") && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Red_Orange);
        }else if (primaryColor.equals("red") && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Red_Deep_Orange);
        }else if (primaryColor.equals("red") && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Red_Brown);
        }else if (primaryColor.equals("red") && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Red_Grey);
        }else if (primaryColor.equals("red") && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Red_Blue_Grey);
        }else if (primaryColor.equals("pink") && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Pink_Red);
        }else if (primaryColor.equals("pink") && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Pink_Pink);
        }else if (primaryColor.equals("pink") && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Pink_Purple);
        }else if (primaryColor.equals("pink") && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Pink_Deep_Purple);
        }else if (primaryColor.equals("pink") && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Pink_Indigo);
        }else if (primaryColor.equals("pink") && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Pink_Blue);
        }else if (primaryColor.equals("pink") && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Pink_Light_Blue);
        }else if (primaryColor.equals("pink") && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Pink_Cyan);
        }else if (primaryColor.equals("pink") && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Pink_Teal);
        }else if (primaryColor.equals("pink") && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Pink_Green);
        }else if (primaryColor.equals("pink") && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Pink_Light_Green);
        }else if (primaryColor.equals("pink") && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Pink_Lime);
        }else if (primaryColor.equals("pink") && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Pink_Yellow);
        }else if (primaryColor.equals("pink") && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Pink_Amber);
        }else if (primaryColor.equals("pink") && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Pink_Orange);
        }else if (primaryColor.equals("pink") && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Pink_Deep_Orange);
        }else if (primaryColor.equals("pink") && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Pink_Brown);
        }else if (primaryColor.equals("pink") && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Pink_Grey);
        }else if (primaryColor.equals("pink") && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Pink_Blue_Grey);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Purple_Red);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Purple_Pink);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Purple_Purple);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Purple_Deep_Purple);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Purple_Indigo);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Purple_Blue);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Purple_Light_Blue);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Purple_Cyan);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Purple_Teal);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Purple_Green);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Purple_Light_Green);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Purple_Lime);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Purple_Yellow);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Purple_Amber);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Purple_Orange);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Purple_Deep_Orange);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Purple_Brown);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Purple_Grey);
        }else if (primaryColor.equals(PURPLE) && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Purple_Blue_Grey);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Red);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Pink);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Purple);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Deep_Purple);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Indigo);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Blue);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Light_Blue);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Cyan);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Teal);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Green);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Light_Green);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Lime);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Yellow);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Amber);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Orange);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Deep_Orange);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Brown);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Grey);
        }else if (primaryColor.equals(DEEP_PURPLE) && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Deep_Purple_Blue_Grey);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Indigo_Red);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Indigo_Pink);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Indigo_Purple);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Indigo_Deep_Purple);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Indigo_Indigo);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Indigo_Blue);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Indigo_Light_Blue);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Indigo_Cyan);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Indigo_Teal);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Indigo_Green);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Indigo_Light_Green);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Indigo_Lime);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Indigo_Yellow);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Indigo_Amber);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Indigo_Orange);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Indigo_Deep_Orange);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Indigo_Brown);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Indigo_Grey);
        }else if (primaryColor.equals(INDIGO) && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Indigo_Blue_Grey);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Blue_Red);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Blue_Pink);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Blue_Purple);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Blue_Deep_Purple);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Blue_Indigo);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Blue_Blue);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Blue_Light_Blue);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Blue_Cyan);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Blue_Teal);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Blue_Green);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Blue_Light_Green);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Blue_Lime);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Blue_Yellow);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Blue_Amber);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Blue_Orange);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Blue_Deep_Orange);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Blue_Brown);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Blue_Grey);
        }else if (primaryColor.equals(BLUE) && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Blue_Blue_Grey);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Light_Blue_Red);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Light_Blue_Pink);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Light_Blue_Purple);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Light_Blue_Deep_Purple);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Light_Blue_Indigo);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Light_Blue_Blue);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Light_Blue_Light_Blue);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Light_Blue_Cyan);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Light_Blue_Teal);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Light_Blue_Green);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Light_Blue_Light_Green);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Light_Blue_Lime);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Light_Blue_Yellow);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Light_Blue_Amber);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Light_Blue_Orange);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Light_Blue_Deep_Orange);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Light_Blue_Brown);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Light_Blue_Grey);
        }else if (primaryColor.equals(LIGHT_BLUE) && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Light_Blue_Blue_Grey);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Cyan_Red);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Cyan_Pink);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Cyan_Purple);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Cyan_Deep_Purple);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Cyan_Indigo);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Cyan_Blue);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Cyan_Light_Blue);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Cyan_Cyan);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Cyan_Teal);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Cyan_Green);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Cyan_Light_Green);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Cyan_Lime);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Cyan_Yellow);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Cyan_Amber);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Cyan_Orange);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Cyan_Deep_Orange);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Cyan_Brown);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Cyan_Grey);
        }else if (primaryColor.equals(CYAN) && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Cyan_Blue_Grey);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Teal_Red);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Teal_Pink);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Teal_Purple);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Teal_Deep_Purple);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Teal_Indigo);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Teal_Blue);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Teal_Light_Blue);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Teal_Cyan);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Teal_Teal);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Teal_Green);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Teal_Light_Green);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Teal_Lime);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Teal_Yellow);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Teal_Amber);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Teal_Orange);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Teal_Deep_Orange);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Teal_Brown);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Teal_Grey);
        }else if (primaryColor.equals(TEAL) && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Teal_Blue_Grey);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Green_Red);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Green_Pink);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Green_Purple);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Green_Deep_Purple);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Green_Indigo);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Green_Blue);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Green_Light_Blue);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Green_Cyan);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Green_Teal);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Green_Green);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Green_Light_Green);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Green_Lime);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Green_Yellow);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Green_Amber);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Green_Orange);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Green_Deep_Orange);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Green_Brown);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Green_Grey);
        }else if (primaryColor.equals(GREEN) && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Green_Blue_Grey);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Light_Green_Red);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Light_Green_Pink);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Light_Green_Purple);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Light_Green_Deep_Purple);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Light_Green_Indigo);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Light_Green_Blue);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Light_Green_Light_Blue);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Light_Green_Cyan);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Light_Green_Teal);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Light_Green_Green);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Light_Green_Light_Green);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Light_Green_Lime);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Light_Green_Yellow);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Light_Green_Amber);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Light_Green_Orange);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Light_Green_Deep_Orange);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Light_Green_Brown);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Light_Green_Grey);
        }else if (primaryColor.equals(LIGHT_GREEN) && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Light_Green_Blue_Grey);
        }else if (primaryColor.equals(LIME) && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Lime_Red);
        }else if (primaryColor.equals(LIME) && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Lime_Pink);
        }else if (primaryColor.equals(LIME) && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Lime_Purple);
        }else if (primaryColor.equals(LIME) && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Lime_Deep_Purple);
        }else if (primaryColor.equals(LIME) && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Lime_Indigo);
        }else if (primaryColor.equals(LIME) && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Lime_Blue);
        }else if (primaryColor.equals(LIME) && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Lime_Light_Blue);
        }else if (primaryColor.equals(LIME) && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Lime_Cyan);
        }else if (primaryColor.equals(LIME) && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Lime_Teal);
        }else if (primaryColor.equals(LIME) && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Lime_Green);
        }else if (primaryColor.equals(LIME) && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Lime_Light_Green);
        }else if (primaryColor.equals(LIME) && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Lime_Yellow);
        }else if (primaryColor.equals(LIME) && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Lime_Amber);
        }else if (primaryColor.equals(LIME) && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Lime_Lime);
        }else if (primaryColor.equals(LIME) && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Lime_Orange);
        }else if (primaryColor.equals(LIME) && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Lime_Deep_Orange);
        }else if (primaryColor.equals(LIME) && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Lime_Brown);
        }else if (primaryColor.equals(LIME) && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Lime_Grey);
        }else if (primaryColor.equals(LIME) && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Lime_Blue_Grey);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(RED)) {
            activity.setTheme(R.style.Theme_Yellow_Red);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(PINK)) {
            activity.setTheme(R.style.Theme_Yellow_Pink);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(PURPLE)) {
            activity.setTheme(R.style.Theme_Yellow_Purple);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(DEEP_PURPLE)) {
            activity.setTheme(R.style.Theme_Yellow_Deep_Purple);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(INDIGO)) {
            activity.setTheme(R.style.Theme_Yellow_Indigo);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(BLUE)) {
            activity.setTheme(R.style.Theme_Yellow_Blue);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(LIGHT_BLUE)) {
            activity.setTheme(R.style.Theme_Yellow_Light_Blue);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(CYAN)) {
            activity.setTheme(R.style.Theme_Yellow_Cyan);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(TEAL)) {
            activity.setTheme(R.style.Theme_Yellow_Teal);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(GREEN)) {
            activity.setTheme(R.style.Theme_Yellow_Green);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(LIGHT_GREEN)) {
            activity.setTheme(R.style.Theme_Yellow_Light_Green);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(LIME)) {
            activity.setTheme(R.style.Theme_Yellow_Lime);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(YELLOW)) {
            activity.setTheme(R.style.Theme_Yellow_Yellow);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(AMBER)) {
            activity.setTheme(R.style.Theme_Yellow_Amber);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(ORANGE)) {
            activity.setTheme(R.style.Theme_Yellow_Orange);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(DEEP_ORANGE)) {
            activity.setTheme(R.style.Theme_Yellow_Deep_Orange);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(BROWN)) {
            activity.setTheme(R.style.Theme_Yellow_Brown);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(GREY)) {
            activity.setTheme(R.style.Theme_Yellow_Grey);
        }else if (primaryColor.equals(YELLOW) && accentColor.equals(BLUE_GREY)) {
            activity.setTheme(R.style.Theme_Yellow_Blue_Grey);
        }
    }

}
