package anynotes.olyalya.pelipets.com.anynotes.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private final static int REQUEST_CODE_PICKER = 456;
    private Date alarm;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.FORMAT_ALARM);
    private String alarmNote;
    private long repeatAlarm = 0;
    private int colorPrimary;

    private class RepeatTumbler {
        boolean isChecked;
        long repeatTime;
        String repeatText;

        public RepeatTumbler(boolean isChecked, long repeatTime, String repeatText) {
            this.isChecked = isChecked;
            this.repeatTime = repeatTime;
            this.repeatText = repeatText;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NoteUtils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        colorPrimary = typedValue.data;

        loadSettings();
        initViews();

        Intent inIntent = getIntent();
        alarmNote = inIntent.getStringExtra(Constants.EXTRA_TIME_DATE);
        repeatAlarm = inIntent.getLongExtra(Constants.EXTRA_REPEAT, 0);

        if (alarmNote != null && !TextUtils.isEmpty(alarmNote)) {
            tvClock.setText(alarmNote);
            checkClock.setChecked(true);
            tvClock.setTextColor(colorPrimary);
            if (repeatAlarm!=0){
                checkRepeat.setChecked(true);
                tvRepeat.setTextColor(colorPrimary);
                gridRepeat.setVisibility(View.VISIBLE);
                TextView savedRepeat=search(repeatAlarm);
                if (savedRepeat!=null){
                    savedRepeat.setTextColor(colorPrimary);
                    savedRepeat.setBackgroundDrawable(getResources().getDrawable(R.drawable.big_circle_active));
                    lastRepeat=savedRepeat;
                    tvRepeat.setText(savedRepeat.getText());
                }
            }
        } else {
            String now = dateFormat.format(new Date());
            tvClock.setText(now);
        }

        checkClock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvClock.setTextColor(colorPrimary);
                    Intent pickerIntent = new Intent(ReminderActivity.this, TimeDatePickerActivity.class);
                    if (alarmNote != null) {
                        pickerIntent.putExtra(Constants.EXTRA_TIME_DATE, alarmNote);
                    }
                    startActivityForResult(pickerIntent, REQUEST_CODE_PICKER);
                } else {
                    tvClock.setTextColor(getResources().getColor(R.color.grayInactive));
                }
            }
        });

        checkRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvRepeat.setTextColor(colorPrimary);
                    gridRepeat.setVisibility(View.VISIBLE);
                } else {
                    tvRepeat.setTextColor(getResources().getColor(R.color.grayInactive));
                    tvRepeat.setText(getResources().getString(R.string.repeat_none));
                    gridRepeat.setVisibility(View.GONE);
                    if (lastRepeat != null && listRepeat != null) {
                        lastRepeat.setBackgroundDrawable(ReminderActivity.this.getResources().getDrawable(R.drawable.big_circle_inactive));
                        lastRepeat.setTextColor(ReminderActivity.this.getResources().getColor(R.color.grayInactive));
                        listRepeat.get(lastRepeat).isChecked = false;
                    }
                    lastRepeat = null;
                }
            }
        });
    }

    private TextView search(long repeatAlarm) {
        for (TextView tv: listRepeat.keySet()){
            if (listRepeat.get(tv).repeatTime==repeatAlarm){
                return tv;
            }
        }
        return null;
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
        listRepeat.put((TextView) findViewById(R.id.repeat_1hour), new RepeatTumbler(false, (long) (60 * 60 * 1000),
                getResources().getString(R.string.repeat_1_hour)));
        listRepeat.put((TextView) findViewById(R.id.repeat_3hour), new RepeatTumbler(false, (long) (3 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_3_hour)));
        listRepeat.put((TextView) findViewById(R.id.repeat_6hour), new RepeatTumbler(false, (long) (6 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_6_hour)));
        listRepeat.put((TextView) findViewById(R.id.repeat_12hour), new RepeatTumbler(false, (long) (12 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_12_hour)));
        listRepeat.put((TextView) findViewById(R.id.repeat_1day), new RepeatTumbler(false, (long) (24 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_1_day)));
        listRepeat.put((TextView) findViewById(R.id.repeat_2day), new RepeatTumbler(false, (long) (2 * 24 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_2_day)));
        listRepeat.put((TextView) findViewById(R.id.repeat_3day), new RepeatTumbler(false, (long) (3 * 24 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_3_day)));
        listRepeat.put((TextView) findViewById(R.id.repeat_10day), new RepeatTumbler(false, (long) (10 * 24 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_10_day)));
        listRepeat.put((TextView) findViewById(R.id.repeat_1week), new RepeatTumbler(false, (long) (7 * 24 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_1_week)));
        listRepeat.put((TextView) findViewById(R.id.repeat_2week), new RepeatTumbler(false, (long) (2 * 7 * 24 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_2_week)));
        listRepeat.put((TextView) findViewById(R.id.repeat_3week), new RepeatTumbler(false, (long) (3 * 7 * 24 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_3_week)));
        listRepeat.put((TextView) findViewById(R.id.repeat_4week), new RepeatTumbler(false, (long) (4 * 7 * 24 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_4_week)));
        listRepeat.put((TextView) findViewById(R.id.repeat_1month), new RepeatTumbler(false, (long) (30 * 24 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_1_month)));
        listRepeat.put((TextView) findViewById(R.id.repeat_3month), new RepeatTumbler(false, (long) (3 * 30 * 24 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_3_month)));
        listRepeat.put((TextView) findViewById(R.id.repeat_6month), new RepeatTumbler(false, (long) (6 * 30 * 24 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_6_month)));
        listRepeat.put((TextView) findViewById(R.id.repeat_year), new RepeatTumbler(false, (long) (365 * 24 * 60 * 60 * 1000),
                getResources().getString(R.string.repeat_year)));
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
                tvRepeat.setText(getResources().getString(R.string.repeat_none));
                tvRepeat.setTextColor(getResources().getColor(R.color.grayInactive));
                lastRepeat = tv;
            } else {
                tumbler.isChecked = true;
                tv.setBackgroundDrawable(ReminderActivity.this.getResources().getDrawable(R.drawable.big_circle_active));
                tv.setTextColor(colorPrimary);
                tvRepeat.setText(tumbler.repeatText);
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
                Intent intent = new Intent();
                if (checkClock.isChecked()) {
                    if (alarm!=null){
                        alarmNote=dateFormat.format(alarm);
                    }
                    intent.putExtra(Constants.EXTRA_TIME_DATE, alarmNote);
                    if (checkRepeat.isChecked()) {
                        if (lastRepeat != null) {
                            intent.putExtra(Constants.EXTRA_REPEAT, listRepeat.get(lastRepeat).repeatTime);
                        } else {
                            intent.putExtra(Constants.EXTRA_REPEAT, 0);
                        }
                    } else {
                        intent.putExtra(Constants.EXTRA_REPEAT, 0);
                    }
                }
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String result = data.getStringExtra(Constants.EXTRA_TIME_DATE);
                    if (result != null) {
                        try {
                            alarm = dateFormat.parse(result);
                            tvClock.setText(dateFormat.format(alarm));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
