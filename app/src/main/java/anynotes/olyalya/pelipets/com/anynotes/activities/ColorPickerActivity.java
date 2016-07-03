package anynotes.olyalya.pelipets.com.anynotes.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class ColorPickerActivity extends AppCompatActivity {
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_scheme);

        loadSettings();
        initViews();
    }

    public void loadSettings() {
        SharedPreferences sPref = getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        int bright = sPref.getInt(Constants.PREF_BRIGHTNESS, Constants.BRIGHTNESS);
        size = sPref.getInt(Constants.PREF_FONT_SIZE, Constants.SIZE_FONT);
        NoteUtils.setBrightness(bright, this);
    }

    private void initViews() {
        FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fab_color);
        fab.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_light));

    }
}
