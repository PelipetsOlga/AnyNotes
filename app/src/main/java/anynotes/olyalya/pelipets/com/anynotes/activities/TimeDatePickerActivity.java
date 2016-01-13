package anynotes.olyalya.pelipets.com.anynotes.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class TimeDatePickerActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private DatePicker datePicker;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.FORMAT_ALARM);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_date_picker);
        loadSettings();
        initViews();

        Intent intent = getIntent();
        String alarmNote = intent.getStringExtra(Constants.EXTRA_TIME_DATE);

        if (alarmNote != null) {
            try {
                Calendar currentDate = Calendar.getInstance();
                Date date = dateFormat.parse(alarmNote);
                currentDate.setTime(date);

                int currentDateYear = currentDate.get(Calendar.YEAR);
                int currentDateMonth = currentDate.get(Calendar.MONTH);
                int currentDateDay = currentDate.get(Calendar.DAY_OF_MONTH);
                datePicker.updateDate(currentDateYear, currentDateMonth, currentDateDay);

                timePicker.setCurrentHour(currentDate.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(currentDate.get(Calendar.MINUTE));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        timePicker = (TimePicker) findViewById(R.id.timepicker);
        timePicker.setIs24HourView(true);
        datePicker = (DatePicker) findViewById(R.id.datepicker);
    }

    public void loadSettings() {
        SharedPreferences sPref = getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        int bright = sPref.getInt(Constants.PREF_BRIGHTNESS, Constants.BRIGHTNESS);
        NoteUtils.setBrightness(bright, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                String timeDateExtra = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear()
                        + ", " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_TIME_DATE, timeDateExtra);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
