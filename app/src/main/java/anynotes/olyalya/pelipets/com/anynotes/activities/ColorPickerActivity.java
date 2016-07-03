package anynotes.olyalya.pelipets.com.anynotes.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.models.ColorMaterial;
import anynotes.olyalya.pelipets.com.anynotes.models.ColorPallete;
import anynotes.olyalya.pelipets.com.anynotes.models.ThemeMaterial;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class ColorPickerActivity extends AppCompatActivity {
    private int size;
    private ColorPallete pallete;
    private State state = State.NOT_CHANGED;
    private GridView grid;
    private PalleteAdapter adapter;
    private FloatingActionButton fab;
    private ImageView btnHome;
    private ThemeMaterial theme;
    private LinearLayout toolbarColor;

    enum State {NOT_CHANGED, PRIMARY_CHANGED, ACCENT_CHANGED}

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NoteUtils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_scheme);

        pallete = new ColorPallete(this);

        loadSettings();
        initViews();
    }

    public void loadSettings() {
        SharedPreferences sPref = getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        int bright = sPref.getInt(Constants.PREF_BRIGHTNESS, Constants.BRIGHTNESS);
        size = sPref.getInt(Constants.PREF_FONT_SIZE, Constants.SIZE_FONT);
        NoteUtils.setBrightness(bright, this);

        theme = new ThemeMaterial(ColorPickerActivity.this,
                sPref.getString(Constants.PREF_PRIMARY_COLOR, "pink"),
                sPref.getString(Constants.PREF_ACCENT_COLOR, "cyan"));
    }

    private void initViews() {
        fab = (FloatingActionButton) findViewById(R.id.fab_color);
        toolbarColor = (LinearLayout) findViewById(R.id.toolbar_color);
        btnHome = (ImageView) findViewById(R.id.btn_color_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerActivity.this.finish();
            }
        });

        grid = (GridView) findViewById(R.id.pallete);
        adapter = new PalleteAdapter(ColorPickerActivity.this, pallete.getPallete());
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ColorMaterial colorSelected = pallete.getPallete().get(position);
                if (state == State.NOT_CHANGED) {
                    state = State.PRIMARY_CHANGED;
                    theme.setPrimary(colorSelected);
                    toolbarColor.setBackgroundColor(getColor(colorSelected.getTag()));
                } else if (state == State.PRIMARY_CHANGED) {
                    state = State.ACCENT_CHANGED;
                    theme.setAccent(colorSelected);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getColor(colorSelected.getTag())));
                } else if (state == State.ACCENT_CHANGED) {
                    state = State.PRIMARY_CHANGED;
                    theme.setPrimary(colorSelected);
                    toolbarColor.setBackgroundColor(getColor(colorSelected.getTag()));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (state != State.NOT_CHANGED) {
            saveNewTheme();
        }
        super.onDestroy();
    }

    private void saveNewTheme() {
        SharedPreferences sPref = getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Constants.PREF_PRIMARY_COLOR, theme.getPrimary().getTag());
        ed.putString(Constants.PREF_ACCENT_COLOR, theme.getAccent().getTag());
        ed.commit();

        MainActivity.reStart();
    }

    class PalleteAdapter extends ArrayAdapter<ColorMaterial> {
        List<ColorMaterial> items;
        LayoutInflater inflater;

        public PalleteAdapter(Context context, List<ColorMaterial> objects) {
            super(context, R.layout.item_pallete);
            items = objects;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public int getPosition(ColorMaterial item) {
            return items.indexOf(item);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_pallete, null);
            }
            ImageView view = (ImageView) convertView;
            ColorMaterial colorMaterial = items.get(position);
            view.setBackgroundColor(getColor(colorMaterial.getTag()));
            return view;
        }
    }

    private int getColor(String colorTitle) {
        int colorId = getResources().getIdentifier(colorTitle, "color", getPackageName());
        return getResources().getColor(colorId);
    }
}
