package anynotes.olyalya.pelipets.com.anynotes.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.activities.ColorPickerActivity;
import anynotes.olyalya.pelipets.com.anynotes.activities.MainActivity;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class ColorNewsDialogActivity extends AppCompatActivity {
    private int currentapiVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        NoteUtils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_news);

        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < android.os.Build.VERSION_CODES.LOLLIPOP) {
            startActivity(new Intent(ColorNewsDialogActivity.this, MainActivity.class));
            this.finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        findViewById(R.id.btn_ok).setOnClickListener(okListener);
        findViewById(R.id.btn_cancel).setOnClickListener(cancelListener);

        SharedPreferences mPref = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        boolean firstStartAfterUpdate = mPref.getBoolean(Constants.PREF_FIRST_START_AFTER_UPDATE, true);
        int counter = mPref.getInt(Constants.PREF_FIRST_START_COUNTER, 0);
        if (!firstStartAfterUpdate || counter > 2) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }
    }


    View.OnClickListener okListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            SharedPreferences sPref = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
            int counter = sPref.getInt(Constants.PREF_FIRST_START_COUNTER, 0);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putBoolean(Constants.PREF_FIRST_START_AFTER_UPDATE, false);
            ed.putInt(Constants.PREF_FIRST_START_COUNTER, counter + 1);
            ed.commit();
            Intent intent = new Intent(ColorNewsDialogActivity.this, ColorPickerActivity.class);
            startActivity(intent);
            finish();
        }
    };

    View.OnClickListener cancelListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            doCancel();
        }
    };

    private void doCancel() {
        SharedPreferences sPref = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        int counter = sPref.getInt(Constants.PREF_FIRST_START_COUNTER, 0);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(Constants.PREF_FIRST_START_COUNTER, counter + 1);
        ed.commit();
        Intent intent = new Intent(ColorNewsDialogActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            doCancel();
        }
    }
}
