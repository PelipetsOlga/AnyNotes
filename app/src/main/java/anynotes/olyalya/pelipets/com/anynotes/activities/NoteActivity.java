package anynotes.olyalya.pelipets.com.anynotes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.application.NotesApplication;
import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.storage.NotesRepository;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class NoteActivity extends AppCompatActivity {
    private static final int TITLE_LENGTH = 100;
    private static final String TAG_CREATING = "creating";

    private int type_operation = Constants.EXTRA_ACTION_NEW_NOTE;

    private EditText etTitle;
    private EditText etText;
    private FloatingActionButton fab;
    private NotesRepository repository;
    private Toolbar toolbar;
    private long creating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent intent = getIntent();
        type_operation = intent.getIntExtra(Constants.EXTRA_ACTION_TYPE, Constants.EXTRA_ACTION_NEW_NOTE);

        repository = ((NotesApplication) getApplication()).getDaoSession().getRepository();

        if (type_operation == Constants.EXTRA_ACTION_NEW_NOTE) {
            creating = Calendar.getInstance().getTimeInMillis();
            NoteUtils.log("creating in OnCreate = " + creating);
        } else {
            //todo read from intent
        }
        initViews();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type_operation == Constants.EXTRA_ACTION_NEW_NOTE) {
                    Note note = readNoteFromTextFields();
                    if (note == null) {
                        setResult(RESULT_CANCELED);
                    } else {
                        saveActual(note, creating);
                        setResult(RESULT_OK);
                    }
                    finish();
                } else {
                    Snackbar.make(view, "Save", Snackbar.LENGTH_LONG)
                            .setAction("Save", null).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        goBack();
        super.onBackPressed();
    }

    private void saveActual(Note note, long cr) {
        note.setCreating(cr);
        note.setLastSaving(Calendar.getInstance().getTimeInMillis());
        note.setStatus(Constants.STATUS_ACTUAL);
        repository.insert(note);
    }

    private void saveDraft(Note note, long cr) {
        note.setCreating(cr);
        note.setLastSaving(Calendar.getInstance().getTimeInMillis());
        note.setStatus(Constants.STATUS_DRAFT);
        repository.insert(note);
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
            if (content.length() <= TITLE_LENGTH) {
                title = content;
            } else {
                title = content.substring(0, TITLE_LENGTH) + "...";
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
                Snackbar.make(fab, "Delete clicked", Snackbar.LENGTH_LONG)
                        .setAction("Delete clicked", null).show();
                return true;
            case R.id.action_share:
                Snackbar.make(fab, "Share clicked", Snackbar.LENGTH_LONG)
                        .setAction("Share clicked", null).show();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private void goBack() {
        Note note = readNoteFromTextFields();
        if (note != null) {
            saveDraft(note, creating);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

}
