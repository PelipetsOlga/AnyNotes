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

import java.util.HashMap;

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
    private LinearLayout gridRepeat;
    private int size;
    private HashMap<TextView, RepeatTumbler> listRepeat;
    private TextView lastRepeat;

    private class RepeatTumbler {
        boolean isChecked;
        long repeatTime;

        public RepeatTumbler(boolean isChecked, long repeatTime) {
            this.isChecked = isChecked;
            this.repeatTime = repeatTime;
        }
    }

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

        checkClock = (CheckBox) findViewById(R.id.check_clock);
        checkRepeat = (CheckBox) findViewById(R.id.check_repeat);
        tvClock = (TextView) findViewById(R.id.tv_clock);
        tvRepeat = (TextView) findViewById(R.id.tv_repeat);
        llClock = (LinearLayout) findViewById(R.id.ll_clock);
        llRepeat = (LinearLayout) findViewById(R.id.ll_repeat);
        gridRepeat = (LinearLayout) findViewById(R.id.grid_repeat);
        if (checkRepeat.isChecked()) {
            gridRepeat.setVisibility(View.VISIBLE);
        } else {
            gridRepeat.setVisibility(View.GONE);
        }

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
                if (isChecked) {
                    tvRepeat.setTextColor(getResources().getColor(R.color.colorPrimary));
                    gridRepeat.setVisibility(View.VISIBLE);
                } else {
                    tvRepeat.setTextColor(getResources().getColor(R.color.grayInactive));
                    gridRepeat.setVisibility(View.GONE);
                    if (lastRepeat != null || listRepeat != null) {
                        lastRepeat.setBackgroundDrawable(ReminderActivity.this.getResources().getDrawable(R.drawable.big_circle_inactive));
                        lastRepeat.setTextColor(ReminderActivity.this.getResources().getColor(R.color.grayInactive));
                        listRepeat.get(lastRepeat).isChecked = false;
                    }
                    lastRepeat = null;
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

        listRepeat = new HashMap<TextView, RepeatTumbler>();
        listRepeat.put((TextView) findViewById(R.id.repeat_1hour), new RepeatTumbler(false, (long) (60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_3hour), new RepeatTumbler(false, (long) (3 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_6hour), new RepeatTumbler(false, (long) (6 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_12hour), new RepeatTumbler(false, (long) (12 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_1day), new RepeatTumbler(false, (long) (24 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_2day), new RepeatTumbler(false, (long) (2 * 24 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_3day), new RepeatTumbler(false, (long) (3 * 24 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_10day), new RepeatTumbler(false, (long) (10 * 24 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_1week), new RepeatTumbler(false, (long) (7 * 24 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_2week), new RepeatTumbler(false, (long) (2 * 7 * 24 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_3week), new RepeatTumbler(false, (long) (3 * 7 * 24 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_4week), new RepeatTumbler(false, (long) (4 * 7 * 24 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_1month), new RepeatTumbler(false, (long) (30 * 24 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_3month), new RepeatTumbler(false, (long) (3 * 30 * 24 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_6month), new RepeatTumbler(false, (long) (6 * 30 * 24 * 60 * 60 * 1000)));
        listRepeat.put((TextView) findViewById(R.id.repeat_year), new RepeatTumbler(false, (long) (365 * 24 * 60 * 60 * 1000)));
        for (TextView tv : listRepeat.keySet()) {
            tv.setOnClickListener(new RepeatTumblerListener());
        }
    }

    class RepeatTumblerListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TextView tv = (TextView) v;
            RepeatTumbler tumbler = listRepeat.get(tv);
            if (tumbler.isChecked) {
                tumbler.isChecked = false;
                tv.setBackgroundDrawable(ReminderActivity.this.getResources().getDrawable(R.drawable.big_circle_inactive));
                tv.setTextColor(ReminderActivity.this.getResources().getColor(R.color.grayInactive));
                lastRepeat = tv;
            } else {
                tumbler.isChecked = true;
                tv.setBackgroundDrawable(ReminderActivity.this.getResources().getDrawable(R.drawable.big_circle_active));
                tv.setTextColor(ReminderActivity.this.getResources().getColor(R.color.colorPrimary));
                if (lastRepeat != null) {
                    lastRepeat.setBackgroundDrawable(ReminderActivity.this.getResources().getDrawable(R.drawable.big_circle_inactive));
                    lastRepeat.setTextColor(ReminderActivity.this.getResources().getColor(R.color.grayInactive));
                    listRepeat.get(lastRepeat).isChecked = false;
                }
                lastRepeat = tv;
            }
        }
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
