package anynotes.olyalya.pelipets.com.anynotes.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.activities.MainActivity;
import anynotes.olyalya.pelipets.com.anynotes.application.NotesApplication;
import anynotes.olyalya.pelipets.com.anynotes.storage.NotesRepository;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;

/**
 * Created by Olga on 02.01.2016.
 */
public class SortDialogFragment extends DialogFragment implements View.OnClickListener {
    private NotesRepository repository;

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

        View rootView = inflater.inflate(R.layout.sort_dialog_fragment, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ImageView ivUnsort = (ImageView) rootView.findViewById(R.id.iv_unsort);
        ImageView ivSortAlphaAsc = (ImageView) rootView.findViewById(R.id.iv_sort_alpha_asc);
        ImageView ivSortAlphaDesc = (ImageView) rootView.findViewById(R.id.iv_sort_alpha_desc);
        ImageView ivSortDateAsc = (ImageView) rootView.findViewById(R.id.iv_sort_date_asc);
        ImageView ivSortDateDesc = (ImageView) rootView.findViewById(R.id.iv_sort_date_desc);

        ivUnsort.setOnClickListener(this);
        ivSortAlphaAsc.setOnClickListener(this);
        ivSortAlphaDesc.setOnClickListener(this);
        ivSortDateAsc.setOnClickListener(this);
        ivSortDateDesc.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_unsort:
                repository.setModeOrdered(Constants.MODE_ORDERED_UNSORTED);
                break;
            case R.id.iv_sort_alpha_asc:
                repository.setModeOrdered(Constants.MODE_ORDERED_SORT_ALPHA_ASC);
                break;
            case R.id.iv_sort_alpha_desc:
                repository.setModeOrdered(Constants.MODE_ORDERED_SORT_ALPHA_DESC);
                break;
            case R.id.iv_sort_date_asc:
                repository.setModeOrdered(Constants.MODE_ORDERED_SORT_DATE_ASC);
                break;
            case R.id.iv_sort_date_desc:
                repository.setModeOrdered(Constants.MODE_ORDERED_SORT_DATE_DESC);
                break;
        }
        ((MainActivity)getActivity()).refreshList();
        dismiss();
    }
}
