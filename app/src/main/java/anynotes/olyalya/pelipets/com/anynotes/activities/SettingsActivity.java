package anynotes.olyalya.pelipets.com.anynotes.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sPref;
    private int bright;
    private int size;
    private DiscreteSeekBar seekSize;
    private DiscreteSeekBar seekBright;
    private TextView tvTitle;
    private TextView tvLastsaving;
    private TextView tvText;
    private boolean changes = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        loadSettings();
        initViews();
        refreshFonts();
        setFontSizeConrol();
        setBrightnessControl();

    }

    public void loadSettings() {
        sPref = getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        bright = sPref.getInt(Constants.PREF_BRIGHTNESS, Constants.BRIGHTNESS);
        size = sPref.getInt(Constants.PREF_FONT_SIZE, Constants.SIZE_FONT);
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        tvTitle = (TextView) findViewById(R.id.tv_title_example);
        tvLastsaving = (TextView) findViewById(R.id.tv_lastsaving_example);
        tvText = (TextView) findViewById(R.id.tv_text_example);
        seekSize = (DiscreteSeekBar) findViewById(R.id.seekbar_size_font);
        seekSize.setProgress(size);
        seekBright = (DiscreteSeekBar) findViewById(R.id.seekbar_brightness);
        seekBright.setProgress(bright);
    }

    public void setFontSizeConrol() {
        seekSize.setProgress(size);
        seekSize.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar discreteSeekBar, int i, boolean b) {
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt(Constants.PREF_FONT_SIZE, i);
                size = i;
                ed.commit();
                refreshFonts();
                changes = true;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar discreteSeekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar discreteSeekBar) {
            }
        });
    }

    private void refreshFonts() {
        tvTitle.setTextSize(size);
        tvLastsaving.setTextSize(size);
        tvText.setTextSize(size);
    }

    public void setBrightnessControl() {
        NoteUtils.setBrightness(bright, this);
        seekBright.setProgress(bright);
        seekBright.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar discreteSeekBar, int i, boolean b) {
                SharedPreferences.Editor ed = sPref.edit();
                bright = i;
                ed.putInt(Constants.PREF_BRIGHTNESS, bright);
                ed.commit();
                NoteUtils.setBrightness(bright, SettingsActivity.this);
                changes = true;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar discreteSeekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar discreteSeekBar) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (changes) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
    }
}
