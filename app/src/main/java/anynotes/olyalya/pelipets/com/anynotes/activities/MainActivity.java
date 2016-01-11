package anynotes.olyalya.pelipets.com.anynotes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.application.NotesApplication;
import anynotes.olyalya.pelipets.com.anynotes.fragments.SortDialogFragment;
import anynotes.olyalya.pelipets.com.anynotes.interfaces.RefreshListListener;
import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.storage.NotesRepository;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;
import anynotes.olyalya.pelipets.com.anynotes.views.RecyclerViewEmptySupport;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RefreshListListener {

    private static final int NOTE_REQUEST_CODE = 100;
    private static final int SETTINGS_REQUEST_CODE = 200;

    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private RecyclerViewEmptySupport recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static NotesRepository repository;
    private static List<Note> notes;
    private RefreshListListener refreshListener;
    private SharedPreferences mPref;
    private int bright;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        adapter = new NotesAdapter(refreshListener);
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
                                repository.update(note);
                                notes.remove(removedPosition);
                                adapter.notifyItemRemoved(removedPosition);
                                recyclerView.refresh();

                            } else if (status == Constants.STATUS_DRAFT
                                    || status == Constants.STATUS_DELETED) {
                                note.setStatus(Constants.STATUS_DRAFT_DELETED);
                                repository.update(note);
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

        refreshList();
    }


    public void loadSettings() {
        mPref = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
        bright = mPref.getInt(Constants.PREF_BRIGHTNESS, Constants.BRIGHTNESS);
        NoteUtils.setBrightness(bright, this);
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

    public void refreshList() {
        notes.clear();
        notes.addAll(repository.loadAll());
        adapter.notifyDataSetChanged();
        recyclerView.refresh();
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
        } //else if (id == R.id.alarms) {
        //}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        refreshList();
        return true;
    }

    private void refreshSettings() {
        loadSettings();
        adapter = new NotesAdapter(refreshListener);
        recyclerView.setAdapter(adapter);
    }

    private static class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {
        private RefreshListListener listener;

        public NotesAdapter(RefreshListListener listener) {
            super();
            this.listener = listener;
        }

        @Override
        public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(),
                    R.layout.item_note_rec_view, null);
            return new NoteViewHolder(view, listener);
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

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(note.getLastSaving());
            holder.tvLastSaving.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar));
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
        private final TextView tvLastSaving;
        private final TextView tvText;
        private final ImageView ivIcon;
        private final ImageView ivAlarm;
        private final LinearLayout llContent;

        public NoteViewHolder(View itemView, RefreshListListener listener) {
            super(itemView);
            this.listener = listener;
            context = itemView.getContext();
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvLastSaving = (TextView) itemView.findViewById(R.id.tv_lastsaving);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            ivAlarm = (ImageView) itemView.findViewById(R.id.iv_alarm);
            llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            ivIcon.setOnClickListener(this);
            ivAlarm.setOnClickListener(this);
            llContent.setOnClickListener(this);
            tvText.setOnClickListener(this);
            SharedPreferences sPref = context.getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
            int fontSize = sPref.getInt(Constants.PREF_FONT_SIZE, Constants.SIZE_FONT);
            tvTitle.setTextSize(fontSize);
            tvLastSaving.setTextSize(fontSize);
            tvText.setTextSize(fontSize);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Note note = notes.get(position);
            switch (v.getId()) {
                case R.id.iv_speech:
                    //// TODO: 10.01.2016
                    break;
                case R.id.iv_alarm:
                    displayPopupWindow(v, note);
                    break;
                case R.id.iv_icon:
                    int oldStatus = note.getStatus();
                    switch (oldStatus) {
                        case Constants.STATUS_ACTUAL:
                            note.setStatus(Constants.STATUS_IMPORTANT);
                            repository.update(note);
                            listener.refreshList();
                            break;
                        case Constants.STATUS_IMPORTANT:
                        case Constants.STATUS_DELETED:
                            note.setStatus(Constants.STATUS_ACTUAL);
                            repository.update(note);
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
            View popupView = inflater.inflate(R.layout.popup_alarm, null);
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
}
