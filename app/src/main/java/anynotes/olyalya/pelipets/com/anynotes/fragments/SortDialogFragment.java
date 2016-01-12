package anynotes.olyalya.pelipets.com.anynotes.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.activities.MainActivity;
import anynotes.olyalya.pelipets.com.anynotes.application.NotesApplication;
import anynotes.olyalya.pelipets.com.anynotes.storage.NotesRepository;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;

/**
 * Created by Olga on 02.01.2016.
 */
public class SortDialogFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    private NotesRepository repository;
    private SharedPreferences sPref;
    private List<CheckBox> checks = new ArrayList<>();

    public static SortDialogFragment newInstance() {
        SortDialogFragment fragment = new SortDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SortDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        repository = ((NotesApplication) getActivity().getApplication()).getDaoSession().getRepository();
        sPref = getActivity().getSharedPreferences(Constants.PREFS_NAME, AppCompatActivity.MODE_PRIVATE);

        View rootView = inflater.inflate(R.layout.sort_dialog_fragment, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

     /*   CheckBox ivUnsort = (CheckBox) rootView.findViewById(R.id.iv_unsort);
        CheckBox ivSortAlphaAsc = (CheckBox) rootView.findViewById(R.id.iv_sort_alpha_asc);
        CheckBox ivSortAlphaDesc = (CheckBox) rootView.findViewById(R.id.iv_sort_alpha_desc);
        CheckBox ivSortDateAsc = (CheckBox) rootView.findViewById(R.id.iv_sort_date_asc);
        CheckBox ivSortDateDesc = (CheckBox) rootView.findViewById(R.id.iv_sort_date_desc);*/
        checks.add((CheckBox) rootView.findViewById(R.id.iv_unsort));
        checks.add((CheckBox) rootView.findViewById(R.id.iv_sort_alpha_asc));
        checks.add((CheckBox) rootView.findViewById(R.id.iv_sort_alpha_desc));
        checks.add((CheckBox) rootView.findViewById(R.id.iv_sort_date_asc));
        checks.add((CheckBox) rootView.findViewById(R.id.iv_sort_date_desc));

        SharedPreferences mPref = getActivity().getSharedPreferences(Constants.PREFS_NAME, getActivity().MODE_PRIVATE);
        int sortPref = mPref.getInt(Constants.PREF_SORT, Constants.PREF_SORT_UNSORT);
        switch (sortPref) {
            case Constants.PREF_SORT_AL_ASC:
                ((CheckBox) rootView.findViewById(R.id.iv_sort_alpha_asc)).setChecked(true);
                break;
            case Constants.PREF_SORT_AL_DESC:
                ((CheckBox) rootView.findViewById(R.id.iv_sort_alpha_desc)).setChecked(true);
                break;
            case Constants.PREF_SORT_NUM_ASC:
                ((CheckBox) rootView.findViewById(R.id.iv_sort_date_asc)).setChecked(true);
                break;
            case Constants.PREF_SORT_NUM_DESC:
                ((CheckBox) rootView.findViewById(R.id.iv_sort_date_desc)).setChecked(true);
                break;
            default:
                ((CheckBox) rootView.findViewById(R.id.iv_unsort)).setChecked(true);
                break;
        }

     /*   ivUnsort.setOnClickListener(this);
        ivSortAlphaAsc.setOnClickListener(this);
        ivSortAlphaDesc.setOnClickListener(this);
        ivSortDateAsc.setOnClickListener(this);
        ivSortDateDesc.setOnClickListener(this);
*/

        for (CheckBox ch : checks) {
            ch.setOnCheckedChangeListener(this);
        }
        return rootView;
    }

    /*  @Override
      public void onClick(View v) {
          SharedPreferences.Editor ed = sPref.edit();
          switch (v.getId()) {
              case R.id.iv_unsort:
                  repository.setModeOrdered(Constants.MODE_ORDERED_UNSORTED);
                  ed.putInt(Constants.PREF_SORT, Constants.PREF_SORT_UNSORT);
                  break;
              case R.id.iv_sort_alpha_asc:
                  repository.setModeOrdered(Constants.MODE_ORDERED_SORT_ALPHA_ASC);
                  ed.putInt(Constants.PREF_SORT, Constants.PREF_SORT_AL_ASC);
                  break;
              case R.id.iv_sort_alpha_desc:
                  repository.setModeOrdered(Constants.MODE_ORDERED_SORT_ALPHA_DESC);
                  ed.putInt(Constants.PREF_SORT, Constants.PREF_SORT_AL_DESC);
                  break;
              case R.id.iv_sort_date_asc:
                  repository.setModeOrdered(Constants.MODE_ORDERED_SORT_DATE_ASC);
                  ed.putInt(Constants.PREF_SORT, Constants.PREF_SORT_NUM_ASC);
                  break;
              case R.id.iv_sort_date_desc:
                  repository.setModeOrdered(Constants.MODE_ORDERED_SORT_DATE_DESC);
                  ed.putInt(Constants.PREF_SORT, Constants.PREF_SORT_NUM_DESC);
                  break;
          }
          ed.commit();
          ((MainActivity)getActivity()).refreshList();
          dismiss();
      }
  */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            SharedPreferences.Editor ed = sPref.edit();
            switch (buttonView.getId()) {
                case R.id.iv_unsort:
                    repository.setModeOrdered(Constants.MODE_ORDERED_UNSORTED);
                    ed.putInt(Constants.PREF_SORT, Constants.PREF_SORT_UNSORT);
                    break;
                case R.id.iv_sort_alpha_asc:
                    repository.setModeOrdered(Constants.MODE_ORDERED_SORT_ALPHA_ASC);
                    ed.putInt(Constants.PREF_SORT, Constants.PREF_SORT_AL_ASC);
                    break;
                case R.id.iv_sort_alpha_desc:
                    repository.setModeOrdered(Constants.MODE_ORDERED_SORT_ALPHA_DESC);
                    ed.putInt(Constants.PREF_SORT, Constants.PREF_SORT_AL_DESC);
                    break;
                case R.id.iv_sort_date_asc:
                    repository.setModeOrdered(Constants.MODE_ORDERED_SORT_DATE_ASC);
                    ed.putInt(Constants.PREF_SORT, Constants.PREF_SORT_NUM_ASC);
                    break;
                case R.id.iv_sort_date_desc:
                    repository.setModeOrdered(Constants.MODE_ORDERED_SORT_DATE_DESC);
                    ed.putInt(Constants.PREF_SORT, Constants.PREF_SORT_NUM_DESC);
                    break;
            }
            ed.commit();
            ((MainActivity) getActivity()).refreshList();
            dismiss();
        }
    }
}
