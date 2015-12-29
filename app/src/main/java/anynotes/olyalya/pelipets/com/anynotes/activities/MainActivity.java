package anynotes.olyalya.pelipets.com.anynotes.activities;

import android.content.Context;
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
import android.support.v7.widget.helper.ItemTouchHelper;
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
    private static List<Note> notes;
    private static int mode;

    private static final int MODE_ALL=23;
    private static final int MODE_ACTUAL=2;
    private static final int MODE_IMPORTANT=3;
    private static final int MODE_DRAFT=1;
    private static final int MODE_DELETED=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        notes = new ArrayList<Note>();
        repository = ((NotesApplication) getApplication()).getDaoSession().getRepository();


        adapter = new NotesAdapter();
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
                        notes.remove(removedPosition);
                        adapter.notifyItemRemoved(removedPosition);
                        //TODO change db
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

        notes.addAll(repository.loadAll(mode));

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
                notes.clear();
                notes.addAll(repository.loadAll(mode));
                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

      /*  if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {


        @Override
        public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(),
                    R.layout.item_note_rec_view, null);
            return new NoteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteViewHolder holder, int position) {
            Note note = notes.get(position);
            holder.tvTitle.setText(note.getTitle());
            holder.tvText.setText(note.getTitle());
            holder.tvStatus.setText(note.getStatus() + "");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(note.getLastSaving());
            holder.tvLastSaving.setText(DateFormat.format("yyyy-MM-dd HH:mm", calendar));
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }

    }

    private static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Context context;

        private final TextView tvStatus;
        private final TextView tvTitle;
        private final TextView tvLastSaving;
        private final TextView tvText;

        public NoteViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvStatus = (TextView) itemView.findViewById(R.id.status);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvLastSaving = (TextView) itemView.findViewById(R.id.tv_lastsaving);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Note note = notes.get(position);
            Intent intent = new Intent(context, NoteActivity.class);
            intent.putExtra(Constants.EXTRA_ACTION_TYPE, Constants.EXTRA_ACTION_EDIT_NOTE);
            intent.putExtra(Constants.EXTRA_NOTE, note);
            ((MainActivity) context).startActivityForResult(intent, NOTE_REQUEST_CODE);
        }
    }
}
