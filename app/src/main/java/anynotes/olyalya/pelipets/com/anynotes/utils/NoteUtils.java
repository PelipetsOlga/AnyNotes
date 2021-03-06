package anynotes.olyalya.pelipets.com.anynotes.utils;

import android.content.Context;
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

}
