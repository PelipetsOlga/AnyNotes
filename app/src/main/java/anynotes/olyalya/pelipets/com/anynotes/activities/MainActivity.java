package anynotes.olyalya.pelipets.com.anynotes.activities;

import android.content.Intent;
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
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.application.NotesApplication;
import anynotes.olyalya.pelipets.com.anynotes.models.Note;
import anynotes.olyalya.pelipets.com.anynotes.storage.NotesRepository;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int NOTE_REQUEST_CODE = 100;

    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private NotesRepository repository;
    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        notes = new ArrayList<Note>();
        repository = ((NotesApplication) getApplication()).getDaoSession().getRepository();


        adapter = new NotesAdapter(notes);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra(Constants.EXTRA_ACTION_TYPE, Constants.EXTRA_ACTION_NEW_NOTE);
                startActivityForResult(intent, NOTE_REQUEST_CODE);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        notes.addAll(repository.loadAll());
    }

    @Override
    protected void onPause() {
        super.onPause();
        notes.clear();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

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
                Snackbar.make(fab, "Settings clicked", Snackbar.LENGTH_LONG)
                        .setAction("Settings clicked", null).show();
                return true;
            case R.id.action_estimate:
                Snackbar.make(fab, "Rate us clicked", Snackbar.LENGTH_LONG)
                        .setAction("Rate us clicked", null).show();
                return true;
            case R.id.action_help:
                Snackbar.make(fab, "Help clicked", Snackbar.LENGTH_LONG)
                        .setAction("Help clicked", null).show();
                return true;
            case R.id.action_exit:
                Snackbar.make(fab, "Exit clicked", Snackbar.LENGTH_LONG)
                        .setAction("Exit clicked", null).show();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NOTE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(fab, "Result OK", Snackbar.LENGTH_LONG)
                        .setAction("Result OK", null).show();
            } else {
                Snackbar.make(fab, "Result CANCEL", Snackbar.LENGTH_LONG)
                        .setAction("Result CANCEL", null).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

        private List<Note> items;

        public NotesAdapter(List<Note> items) {
            this.items = items;
        }

        @Override
        public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(),
                    R.layout.item_note_rec_view, null);
            return new NoteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteViewHolder holder, int position) {
            Note note = items.get(position);
            holder.tvTitle.setText(note.getTitle());
            holder.tvText.setText(note.getTitle());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(note.getLastSaving());
            holder.tvLastSaving.setText(DateFormat.format("yyyy-MM-dd hh:mm", new java.util.Date()));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private static class NoteViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvLastSaving;
        private final TextView tvText;

        public NoteViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvLastSaving = (TextView) itemView.findViewById(R.id.tv_lastsaving);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }
}
