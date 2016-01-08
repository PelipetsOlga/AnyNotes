package anynotes.olyalya.pelipets.com.anynotes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.application.NotesApplication;
import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.storage.NotesRepository;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class NoteActivity extends AppCompatActivity {
    private static final String TAG_CREATING = "creating";

    private int type_operation = Constants.EXTRA_ACTION_NEW_NOTE;

    private EditText etTitle;
    private EditText etText;
    private ImageView ivMicrophone;
    private FloatingActionButton fab;
    private NotesRepository repository;
    private Toolbar toolbar;
    private long creating;
    private Note startNote;
    private int modeFab = 1;
    private static final int MODE_FAB_SAVE = 1;
    private static final int MODE_FAB_EDIT = 2;
    private SharedPreferences sPref;
    private int bright;
    private int size;
    private static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        loadSettings();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Constants.TEST_DEVICE_A)
                .addTestDevice(Constants.TEST_DEVICE_B)
                .addTestDevice(Constants.TEST_DEVICE_C)
                .addTestDevice(Constants.TEST_DEVICE_D)
                .addTestDevice(Constants.TEST_DEVICE_E)
                .addTestDevice(Constants.TEST_DEVICE_G).build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        type_operation = intent.getIntExtra(Constants.EXTRA_ACTION_TYPE, Constants.EXTRA_ACTION_NEW_NOTE);
        if (type_operation == Constants.EXTRA_ACTION_EDIT_NOTE) {
            startNote = (Note) intent.getSerializableExtra(Constants.EXTRA_NOTE);
        }

        repository = ((NotesApplication) getApplication()).getDaoSession().getRepository();

        if (type_operation == Constants.EXTRA_ACTION_NEW_NOTE) {
            creating = Calendar.getInstance().getTimeInMillis();
            NoteUtils.log("creating in OnCreate = " + creating);
        } else {
            creating = startNote.getCreating();
            NoteUtils.log("creating from startNote = " + creating);
        }
        initViews();

        if (setMicrophoneEnabled()) {
            ivMicrophone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startVoiceRecognitionActivity();
                }
            });
        }

        if (type_operation == Constants.EXTRA_ACTION_EDIT_NOTE) {
            etTitle.setText(startNote.getTitle());
            etText.setText(startNote.getText());
        }

        if (type_operation == Constants.EXTRA_ACTION_NEW_NOTE) {
            modeFab = MODE_FAB_SAVE;
            fab.setImageResource(R.mipmap.ic_check_white_24dp);
            etText.setFocusable(true);
            etTitle.setFocusable(true);
            etText.setFocusableInTouchMode(true);
            etText.requestFocus();
            final InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(etText, InputMethodManager.SHOW_IMPLICIT);
        } else if (type_operation == Constants.EXTRA_ACTION_EDIT_NOTE) {
            modeFab = MODE_FAB_EDIT;
            fab.setImageResource(R.mipmap.icomoon_pencil);
            etText.setFocusable(false);
            etTitle.setFocusable(false);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modeFab == MODE_FAB_EDIT) {
                    modeFab = MODE_FAB_SAVE;
                    fab.setImageResource(R.mipmap.ic_check_white_24dp);
                    etTitle.setFocusable(true);
                    etText.setFocusable(true);
                    etText.setFocusableInTouchMode(true);
                    etTitle.setFocusableInTouchMode(true);
                    etText.requestFocus();
                    final InputMethodManager inputMethodManager =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(etText, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    Note note = readNoteFromTextFields();
                    if (note == null) {
                        setResult(RESULT_CANCELED);
                    } else {
                        if (type_operation == Constants.EXTRA_ACTION_NEW_NOTE) {
                            save(Constants.STATUS_ACTUAL, note, creating);
                            setResult(RESULT_OK);
                        } else {
                            if (startNote.getStatus() == Constants.STATUS_IMPORTANT) {
                                update(Constants.STATUS_IMPORTANT, note, creating);
                                setResult(RESULT_OK);
                            } else {
                                update(Constants.STATUS_ACTUAL, note, creating);
                                setResult(RESULT_OK);
                            }
                        }
                    }
                    finish();
                }
            }
        });


    }

    private boolean setMicrophoneEnabled() {
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            ivMicrophone.setVisibility(View.GONE);
            return false;
        } else {
            return true;
        }
    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        String direction = getResources().getString(R.string.voice_direction);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, direction);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() != 0) {
                etText.setText(matches.get(0));
            } else {
                etText.setText("");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void loadSettings() {
        sPref = getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        bright = sPref.getInt(Constants.PREF_BRIGHTNESS, Constants.BRIGHTNESS);
        size = sPref.getInt(Constants.PREF_FONT_SIZE, Constants.SIZE_FONT);
        NoteUtils.setBrightness(bright, this);
    }

    @Override
    public void onBackPressed() {
        goBack();
        super.onBackPressed();
    }

    private void save(int status, Note note, long cr) {
        note.setCreating(cr);
        note.setLastSaving(Calendar.getInstance().getTimeInMillis());
        note.setStatus(status);
        repository.insert(note);
    }

    private void update(int status, Note note, long cr) {
        note.setCreating(cr);
        note.setLastSaving(Calendar.getInstance().getTimeInMillis());
        note.setStatus(status);
        repository.update(note);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(TAG_CREATING, creating);
        NoteUtils.log("creating in onSaveInstanceState = " + creating);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            creating = savedInstanceState.getLong(TAG_CREATING);
            NoteUtils.log("creating in onRestoreInstanceState = " + creating);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private Note readNoteFromTextFields() {
        Note note = new Note();
        String content = etText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return null;
        } else {
            note.setText(content);
        }
        String title = etTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            if (content.length() <= Constants.TITLE_LENGTH) {
                title = content;
            } else {
                title = content.substring(0, Constants.TITLE_LENGTH) + "...";
            }
        }
        note.setTitle(title);
        return note;
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        etTitle = (EditText) findViewById(R.id.et_title);
        etText = (EditText) findViewById(R.id.et_text);
        etTitle.setTextSize(size);
        etText.setTextSize(size);
        ivMicrophone = (ImageView) findViewById(R.id.iv_microphone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
            case R.id.action_alarm:
                Snackbar.make(fab, "Alarm clicked", Snackbar.LENGTH_LONG)
                        .setAction("Alarm clicked", null).show();
                return true;
            case R.id.action_delete:
                if (type_operation == Constants.EXTRA_ACTION_NEW_NOTE) {
                    Note note = readNoteFromTextFields();
                    if (note != null) {
                        save(Constants.STATUS_DRAFT_DELETED, note, creating);
                    }
                } else if (type_operation == Constants.EXTRA_ACTION_EDIT_NOTE) {
                    if (modeFab == MODE_FAB_EDIT) {
                        if (startNote.getStatus() == Constants.STATUS_ACTUAL ||
                                startNote.getStatus() == Constants.STATUS_IMPORTANT) {
                            update(Constants.STATUS_DELETED, startNote, creating);
                        } else if (startNote.getStatus() == Constants.STATUS_DELETED ||
                                startNote.getStatus() == Constants.STATUS_DRAFT) {
                            update(Constants.STATUS_DRAFT_DELETED, startNote, creating);
                        }

                    } else if (modeFab == MODE_FAB_SAVE) {
                        Note note = readNoteFromTextFields();
                        if (note != null) {
                            if (startNote.getStatus() == Constants.STATUS_ACTUAL ||
                                    startNote.getStatus() == Constants.STATUS_IMPORTANT) {
                                update(Constants.STATUS_DELETED, note, creating);
                            } else if (startNote.getStatus() == Constants.STATUS_DELETED ||
                                    startNote.getStatus() == Constants.STATUS_DRAFT) {
                                update(Constants.STATUS_DRAFT_DELETED, note, creating);
                            }
                        }
                    }

                }
                setResult(RESULT_CANCELED);
                finish();
                return true;
            case R.id.action_share:
                Note sharedNote = readNoteFromTextFields();
                if (sharedNote != null) {
                    String sharedText = sharedNote.getTitle() + ":\n" + sharedNote.getText();
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, sharedText);
                    startActivity(Intent.createChooser(share, getResources().getString(R.string.chooser_title_share)));
                }
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private void goBack() {
        if (type_operation == Constants.EXTRA_ACTION_NEW_NOTE) {
            Note note = readNoteFromTextFields();
            if (note != null) {
                save(Constants.STATUS_DRAFT, note, creating);
            }
        }
        setResult(RESULT_CANCELED);
        finish();
    }

}
