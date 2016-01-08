package anynotes.olyalya.pelipets.com.anynotes.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class ReminderActivity extends AppCompatActivity {
    private LinearLayout llClock;
    private LinearLayout llRepeat;
    private CheckBox checkClock;
    private CheckBox checkRepeat;
    private TextView tvClock;
    private TextView tvRepeat;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        checkClock= (CheckBox) findViewById(R.id.check_clock);
        checkRepeat= (CheckBox) findViewById(R.id.check_repeat);
        tvClock= (TextView) findViewById(R.id.tv_clock);
        tvRepeat= (TextView) findViewById(R.id.tv_repeat);
        llClock= (LinearLayout) findViewById(R.id.ll_clock);
        llRepeat= (LinearLayout) findViewById(R.id.ll_repeat);

        tvClock.setTextSize(size);
        tvRepeat.setTextSize(size);

        checkClock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvClock.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tvClock.setTextColor(getResources().getColor(R.color.grayInactive));
                }
            }
        });

        checkRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tvRepeat.setTextColor(getResources().getColor(R.color.colorPrimary));
                }else{
                    tvRepeat.setTextColor(getResources().getColor(R.color.grayInactive));
                }
            }
        });

        llClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkClock.performClick();
            }
        });

        llRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRepeat.performClick();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
