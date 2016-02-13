package anynotes.olyalya.pelipets.com.anynotes.activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.application.NotesApplication;
import anynotes.olyalya.pelipets.com.anynotes.fragments.LogInFragment;
import anynotes.olyalya.pelipets.com.anynotes.fragments.SortDialogFragment;
import anynotes.olyalya.pelipets.com.anynotes.interfaces.LoadNotesListener;
import anynotes.olyalya.pelipets.com.anynotes.interfaces.LoginListener;
import anynotes.olyalya.pelipets.com.anynotes.interfaces.RefreshListListener;
import anynotes.olyalya.pelipets.com.anynotes.interfaces.RegistrationListener;
import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.service.NotesService;
import anynotes.olyalya.pelipets.com.anynotes.storage.NotesRepository;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;
import anynotes.olyalya.pelipets.com.anynotes.views.RecyclerViewEmptySupport;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RefreshListListener,
        TextToSpeech.OnInitListener, RegistrationListener, LoginListener {

    private static final int NOTE_REQUEST_CODE = 100;
    private static final int SETTINGS_REQUEST_CODE = 200;

    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private RecyclerViewEmptySupport recyclerView;
    private NotesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static NotesRepository repository;
    private static List<Note> notes;
    private RefreshListListener refreshListener;
    private SharedPreferences mPref;
    private int bright;
    private TextView empty;
    private TextToSpeech mTTS;
    private ImageView ivSignInOut;
    private ImageView ivSync;
    private TextView tvLogin;
    private boolean canSpeech = false;
    private int sortPref = Constants.PREF_SORT_UNSORT;
    private NotesService.NotesWorker worker;
    private boolean isLogined = false;
    private String login;
    private String password;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            worker = (NotesService.NotesWorker) service;

            worker.loadAll(repository, new NotesLoaderListener());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private ProgressDialog wait;

    @Override
    public void register(final String email, final String password) {
        this.login = email;
        this.password = password;
        if (!NoteUtils.isConnected(this)) {
            NoteUtils.showNotNetErrorMessage(this);
            return;
        }

        BackendlessUser user = new BackendlessUser();
        user.setEmail(email);
        user.setPassword(password);
        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser backendlessUser) {
                NoteUtils.log("response success" + backendlessUser);
                saveUserToPreferenceAndUpdateViews(backendlessUser, MainActivity.this.login, MainActivity.this.password);
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                NoteUtils.log("response registration failure" + backendlessFault);
                if (Constants.BAAS_USER_EXIST_ERROR.equals(backendlessFault.getCode())) {
                    logIn(email, password);
                } else {
                    NoteUtils.showErrorMessage(MainActivity.this);
                }
            }
        });
    }

    private void saveUserToPreferenceAndUpdateViews(BackendlessUser backendlessUser, String login, String password) {
        SharedPreferences.Editor ed = mPref.edit();
        ed.putBoolean(Constants.PREF_IS_LOGINED, true);
        ed.putString(Constants.PREF_LOGIN, backendlessUser.getEmail());
        ed.putString(Constants.PREF_LOGIN, login);
        ed.putString(Constants.PREF_PASSWORD, password);
        ed.commit();
        ivSignInOut.setImageResource(R.mipmap.fa_sign_out_0_ffffff_none);
        tvLogin.setText(backendlessUser.getEmail());
        isLogined = true;
    }

    private void removeUserFromPreferenceAndUpdateViews() {
        SharedPreferences.Editor ed = mPref.edit();
        ed.putBoolean(Constants.PREF_IS_LOGINED, false);
        ed.putString(Constants.PREF_LOGIN, "");
        ed.putString(Constants.PREF_PASSWORD, "");
        ivSignInOut.setImageResource(R.mipmap.fa_sign_in_0_ffffff_none);
        tvLogin.setText(getResources().getString(R.string.app_name));
        isLogined = false;
    }

    @Override
    public void logIn(String email, final String password) {
        this.login = email;
        this.password = password;
        if (!NoteUtils.isConnected(this)) {
            NoteUtils.showNotNetErrorMessage(this);
            return;
        }

        Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser user) {
                NoteUtils.log("response success" + user);
                saveUserToPreferenceAndUpdateViews(user, MainActivity.this.login, MainActivity.this.password);
            }

            public void handleFault(BackendlessFault fault) {
                NoteUtils.log("response login failure" + fault);
                NoteUtils.showErrorMessage(MainActivity.this);
            }
        }, true);
    }

    public class NotesLoaderListener implements LoadNotesListener {
        @Override
        public void onLoad(final List<Note> items) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notes.clear();
                    notes.addAll(items);
                    adapter.notifyDataSetChanged();
                    recyclerView.refresh();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTTS = new TextToSpeech(this, this);
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

        refreshListener = this;
        initViews();

        notes = new ArrayList<Note>();
        repository = ((NotesApplication) getApplication()).getDaoSession().getRepository();
        switch (sortPref) {
            case Constants.PREF_SORT_AL_ASC:
                repository.setModeOrdered(Constants.MODE_ORDERED_SORT_ALPHA_ASC);
                break;
            case Constants.PREF_SORT_AL_DESC:
                repository.setModeOrdered(Constants.MODE_ORDERED_SORT_ALPHA_DESC);
                break;
            case Constants.PREF_SORT_NUM_ASC:
                repository.setModeOrdered(Constants.MODE_ORDERED_SORT_DATE_ASC);
                break;
            case Constants.PREF_SORT_NUM_DESC:
                repository.setModeOrdered(Constants.MODE_ORDERED_SORT_DATE_DESC);
                break;
            default:
                repository.setModeOrdered(Constants.MODE_ORDERED_UNSORTED);
                break;
        }

        adapter = new NotesAdapter(refreshListener, mTTS, canSpeech);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int removedPosition = viewHolder.getAdapterPosition();
                        Note note = notes.get(removedPosition);
                        if (note != null) {
                            int status = note.getStatus();
                            if (status == Constants.STATUS_ACTUAL
                                    || status == Constants.STATUS_IMPORTANT) {
                                note.setStatus(Constants.STATUS_DELETED);
                                repository.updateByCreating(note);
                                notes.remove(removedPosition);
                                adapter.notifyItemRemoved(removedPosition);
                                recyclerView.refresh();

                            } else if (status == Constants.STATUS_DRAFT
                                    || status == Constants.STATUS_DELETED) {
                                note.setStatus(Constants.STATUS_DRAFT_DELETED);
                                repository.updateByCreating(note);
                                notes.remove(removedPosition);
                                adapter.notifyItemRemoved(removedPosition);
                                recyclerView.refresh();
                            }
                        }
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra(Constants.EXTRA_ACTION_TYPE, Constants.EXTRA_ACTION_NEW_NOTE);
                startActivityForResult(intent, NOTE_REQUEST_CODE);
            }
        });

        bindService(new Intent(this, NotesService.class), serviceConnection, BIND_AUTO_CREATE);
    }


    public void loadSettings() {
        mPref = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        bright = mPref.getInt(Constants.PREF_BRIGHTNESS, Constants.BRIGHTNESS);
        NoteUtils.setBrightness(bright, this);
        sortPref = mPref.getInt(Constants.PREF_SORT, Constants.PREF_SORT_UNSORT);
        refreshPreferences();
    }

    private void refreshPreferences() {
        mPref = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        isLogined = mPref.getBoolean(Constants.PREF_IS_LOGINED, false);
        login = mPref.getString(Constants.PREF_LOGIN, "");
        password = mPref.getString(Constants.PREF_PASSWORD, "");
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        empty = (TextView) findViewById(R.id.list_empty);
        recyclerView = (RecyclerViewEmptySupport) findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setEmptyView(empty);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        ivSignInOut = (ImageView) headerView.findViewById(R.id.in_out);
        if (isLogined) {
            ivSignInOut.setImageResource(R.mipmap.fa_sign_in_0_ffffff_none);
        } else {
            ivSignInOut.setImageResource(R.mipmap.fa_sign_out_0_ffffff_none);
        }
        ivSignInOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NoteUtils.isConnected(MainActivity.this)) {
                    NoteUtils.showNotNetErrorMessage(MainActivity.this);
                    return;
                }
                if (!isLogined) {
                    signIn();
                } else {
                    signOut();
                }
            }
        });
        ivSync = (ImageView) headerView.findViewById(R.id.sync);
        ivSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NoteUtils.isConnected(MainActivity.this)) {
                    NoteUtils.showNotNetErrorMessage(MainActivity.this);
                    return;
                }
                if (isLogined) {
                    //// TODO: 13.02.2016
                    saveAllDataToServer();
                } else {
                    signIn();
                }
            }
        });

        tvLogin = (TextView) headerView.findViewById(R.id.tv_login);
        if (isLogined) {
            ivSignInOut.setImageResource(R.mipmap.fa_sign_out_0_ffffff_none);
            tvLogin.setText(login);
        } else {
            ivSignInOut.setImageResource(R.mipmap.fa_sign_in_0_ffffff_none);
            tvLogin.setText(R.string.app_name);
        }
    }

    private void saveAllDataToServer() {
        showProgress(true);

        //// TODO: 13.02.2016
        if (!NoteUtils.isConnected(this)) {
            NoteUtils.showNotNetErrorMessage(this);
            showProgress(false);
            return;
        }

        final long lastSynch = readLastSynchDate();
        NoteUtils.log("lastSynch read " + lastSynch);
        final String whereClause = "lastSaving > " + lastSynch;

        Backendless.UserService.login(login, password, new AsyncCallback<BackendlessUser>() {

            public void handleResponse(BackendlessUser user) {
                NoteUtils.log("response success hidden login" + user);

                BackendlessDataQuery dataQuery = new BackendlessDataQuery();
                dataQuery.setWhereClause(whereClause);
                Backendless.Persistence.of(Note.class).find(dataQuery,
                        new AsyncCallback<BackendlessCollection<Note>>() {
                            @Override
                            public void handleResponse(BackendlessCollection<Note> response) {
                                for (Note note : response.getData()) {
                                    repository.insertOrUpdateLoadedNote(note);
                                }
                                saveCurrentSynchDate();
                                List<Note> freshNotes = repository.loadFreshNotes(lastSynch);
                                final boolean[] perfectSaving = {true};
                                try {
                                    for (Note note : freshNotes) {
                                        Backendless.Persistence.save(note, new AsyncCallback<Note>() {
                                            public void handleResponse(Note response) {
                                                repository.writeObjectId(response);
                                            }

                                            public void handleFault(BackendlessFault fault) {
                                                perfectSaving[0] = false;
                                            }
                                        });
                                    }

                                } catch (Exception e) {
                                    NoteUtils.showErrorMessage(MainActivity.this);
                                } finally {
                                    if (perfectSaving[0]) {
                                        saveCurrentSynchDate();
                                        MainActivity.this.refreshList();
                                    }
                                    showProgress(false);
                                }
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                NoteUtils.log("response query failure" + fault);
                                NoteUtils.showErrorMessage(MainActivity.this);
                                showProgress(false);
                            }
                        });
            }

            public void handleFault(BackendlessFault fault) {
                NoteUtils.log("response hidden login failure" + fault);
                NoteUtils.showErrorMessage(MainActivity.this);
                showProgress(false);
            }
        }, true);
    }

    private void saveCurrentSynchDate() {
        SharedPreferences pref = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        long newLastSynch = Calendar.getInstance().getTimeInMillis();
        ed.putLong(Constants.PREF_LAST_SYNCH, newLastSynch);
        ed.commit();
        NoteUtils.log("lastSynch write " + newLastSynch);
    }

    private long readLastSynchDate() {
        SharedPreferences pref = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        return pref.getLong(Constants.PREF_LAST_SYNCH, 0);
    }

    private void signOut() {
        if (!NoteUtils.isConnected(this)) {
            NoteUtils.showNotNetErrorMessage(this);
            return;
        }

        Backendless.UserService.logout(new AsyncCallback<Void>() {
            public void handleResponse(Void response) {
                NoteUtils.log("response logout success");
                removeUserFromPreferenceAndUpdateViews();
            }

            public void handleFault(BackendlessFault fault) {
                NoteUtils.log("response logout failure" + fault);
                NoteUtils.showErrorMessage(MainActivity.this);
            }
        });

    }

    private void signIn() {
        LogInFragment fragment = LogInFragment.newInstance();
        fragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Snackbar.make(fab, "Search clicked", Snackbar.LENGTH_LONG)
                        .setAction("Search clicked", null).show();
                return true;
            case R.id.action_settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivityForResult(settings, SETTINGS_REQUEST_CODE);
                return true;
            case R.id.action_estimate:
                Intent intentEstimate = new Intent(Intent.ACTION_VIEW);
                intentEstimate.setData(Uri
                        .parse("market://details?id=anynotes.olyalya.pelipets.com.anynotes"));
                startActivity(intentEstimate);
                return true;
            case R.id.action_sort:
                SortDialogFragment fragment = new SortDialogFragment();
                fragment.show(getSupportFragmentManager(), null);
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NOTE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                repository.setModeSort(Constants.MODE_SORT_ALL);
                getSupportActionBar().setTitle(R.string.menu_all);
            }
            refreshListener.refreshList();
        } else if (requestCode == SETTINGS_REQUEST_CODE) {
            refreshSettings();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void refreshList() {
        worker.loadAll(repository, new NotesLoaderListener());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.all_notes) {
            getSupportActionBar().setTitle(R.string.menu_all);
            repository.setModeSort(Constants.MODE_SORT_ALL);
        } else if (id == R.id.important_notes) {
            getSupportActionBar().setTitle(R.string.menu_important);
            repository.setModeSort(Constants.MODE_SORT_IMPORTANTS);
        } else if (id == R.id.actual_notes) {
            getSupportActionBar().setTitle(R.string.menu_actual);
            repository.setModeSort(Constants.MODE_SORT_ACTUALS);
        } else if (id == R.id.drafts) {
            getSupportActionBar().setTitle(R.string.menu_drafts);
            repository.setModeSort(Constants.MODE_SORT_DRAFTS);
        } else if (id == R.id.deleted) {
            getSupportActionBar().setTitle(R.string.menu_deleted);
            repository.setModeSort(Constants.MODE_SORT_DELETED);
        } else if (id == R.id.alarms) {
            getSupportActionBar().setTitle(R.string.menu_alarms);
            repository.setModeSort(Constants.MODE_SORT_ALARMS);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        refreshList();
        return true;
    }

    private void refreshSettings() {
        loadSettings();
        adapter = new NotesAdapter(refreshListener, mTTS, canSpeech);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTTS.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                canSpeech = false;
            } else {
                canSpeech = true;
            }

        } else {
            canSpeech = false;
        }
        adapter.setCanSpeech(canSpeech);
        adapter.notifyDataSetChanged();
    }

    private static class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {
        private RefreshListListener listener;
        private TextToSpeech mTTS;
        private boolean canSpeech = false;

        public NotesAdapter(RefreshListListener listener, TextToSpeech mTTS, boolean canSpeech) {
            super();
            this.listener = listener;
            this.mTTS = mTTS;
            this.canSpeech = canSpeech;
        }

        public void setCanSpeech(boolean canSpeech) {
            this.canSpeech = canSpeech;
        }

        @Override
        public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(),
                    R.layout.item_note_rec_view, null);
            return new NoteViewHolder(view, listener, mTTS);
        }

        @Override
        public void onBindViewHolder(NoteViewHolder holder, int position) {
            Note note = notes.get(position);
            holder.tvTitle.setText(note.getTitle());

            int length = note.getText().trim().length();
            if (length <= Constants.LENGTH_TEXT) {
                holder.tvText.setText(note.getText());
            } else {
                holder.tvText.setText(note.getText().substring(0, Constants.LENGTH_TEXT - 1) + "...");
            }

            String alarm = note.getAlarm();
            if (alarm == null || TextUtils.isEmpty(alarm)) {
                holder.ivAlarm.setVisibility(View.INVISIBLE);
            } else {
                holder.ivAlarm.setVisibility(View.VISIBLE);
            }

            if (canSpeech) {
                holder.ivSpeech.setVisibility(View.VISIBLE);
            } else {
                holder.ivSpeech.setVisibility(View.GONE);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(note.getCreating());
            holder.tvCreating.setText(DateFormat.format("yyyy-MM-dd HH:mm", calendar));
            int status = note.getStatus();
            switch (status) {
                case Constants.STATUS_ACTUAL:
                    holder.ivIcon.setImageResource(R.mipmap.pe_7s_star_ffffff_none);
                    break;
                case Constants.STATUS_IMPORTANT:
                    holder.ivIcon.setImageResource(R.mipmap.oi_star_ffffff_none);
                    break;
                case Constants.STATUS_DELETED:
                    holder.ivIcon.setImageResource(R.mipmap.delete_white);
                    break;
                case Constants.STATUS_DRAFT:
                    holder.ivIcon.setImageResource(R.mipmap.draft_white);
                    break;
                case Constants.STATUS_DRAFT_DELETED:
                    holder.ivIcon.setImageResource(R.mipmap.fa_sticky_note);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }

    }

    private static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RefreshListListener listener;
        private final Context context;
        private final TextView tvTitle;
        private final TextView tvCreating;
        private final TextView tvText;
        private final ImageView ivIcon;
        private final ImageView ivSpeech;
        private final ImageView ivAlarm;
        private final LinearLayout llContent;
        private TextToSpeech mTTS;

        public NoteViewHolder(View itemView, RefreshListListener listener, TextToSpeech mTTS) {
            super(itemView);
            this.listener = listener;
            context = itemView.getContext();
            this.mTTS = mTTS;

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvCreating = (TextView) itemView.findViewById(R.id.tv_creating);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            ivSpeech = (ImageView) itemView.findViewById(R.id.iv_speech);
            ivAlarm = (ImageView) itemView.findViewById(R.id.iv_alarm);
            llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            ivIcon.setOnClickListener(this);
            ivSpeech.setOnClickListener(this);
            ivAlarm.setOnClickListener(this);
            llContent.setOnClickListener(this);
            tvText.setOnClickListener(this);
            SharedPreferences sPref = context.getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
            int fontSize = sPref.getInt(Constants.PREF_FONT_SIZE, Constants.SIZE_FONT);
            tvTitle.setTextSize(fontSize);
            tvCreating.setTextSize(fontSize);
            tvText.setTextSize(fontSize);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Note note = notes.get(position);
            switch (v.getId()) {
                case R.id.iv_speech:
                    if (note != null) {
                        String textToSpeech = note.getText();
                        if (textToSpeech != null && !TextUtils.isEmpty(textToSpeech)) {
                            mTTS.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    break;
                case R.id.iv_alarm:
                    displayPopupWindow(v, note);
                    break;
                case R.id.iv_icon:
                    int oldStatus = note.getStatus();
                    switch (oldStatus) {
                        case Constants.STATUS_ACTUAL:
                            note.setStatus(Constants.STATUS_IMPORTANT);
                            repository.updateByCreating(note);
                            listener.refreshList();
                            break;
                        case Constants.STATUS_IMPORTANT:
                        case Constants.STATUS_DELETED:
                            note.setStatus(Constants.STATUS_ACTUAL);
                            repository.updateByCreating(note);
                            listener.refreshList();
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    Intent intent = new Intent(context, NoteActivity.class);
                    intent.putExtra(Constants.EXTRA_ACTION_TYPE, Constants.EXTRA_ACTION_EDIT_NOTE);
                    intent.putExtra(Constants.EXTRA_NOTE, note);
                    ((MainActivity) context).startActivityForResult(intent, NOTE_REQUEST_CODE);
                    break;
            }
        }

        private void displayPopupWindow(View anchorView, Note note) {
            Context ctx = anchorView.getContext();
            PopupWindow popup = new PopupWindow(ctx);
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup, null);
            popup.setContentView(popupView);
            TextView tvPopup = (TextView) popupView.findViewById(R.id.tv_popup);
            tvPopup.setText(note.getAlarm());
            popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setOutsideTouchable(true);
            popup.setFocusable(true);
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.showAsDropDown(anchorView);
        }
    }

    @Override
    public void onDestroy() {
        if (serviceConnection != null) {
            try {
                unbindService(serviceConnection);
            } catch (IllegalArgumentException e) {
                NoteUtils.log(e.toString());
            }
        }

        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (worker != null) refreshList();
    }

    private void showProgress(boolean show) {
        if (show) {
            if (wait == null) {
                wait = new ProgressDialog(this);
                wait.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                wait.setMessage("Processing...");
            }
            try {
                wait.show();
            } catch (Throwable t) {
            }

        } else {
            if (wait != null) {
                try {
                    wait.dismiss();
                } catch (Throwable t) {
                }
                wait = null;
            }
        }
    }
}
