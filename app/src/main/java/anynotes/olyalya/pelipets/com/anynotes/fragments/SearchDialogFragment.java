package anynotes.olyalya.pelipets.com.anynotes.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.interfaces.SearchListener;
import anynotes.olyalya.pelipets.com.anynotes.storage.NotesRepository;
import anynotes.olyalya.pelipets.com.anynotes.utils.Constants;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

/**
 * Created by Olga on 02.01.2016.
 */
public class SearchDialogFragment extends DialogFragment implements View.OnClickListener {
    private NotesRepository repository;
    private SharedPreferences sPref;
    private EditText etSearchKey;
    private Button btnOk;
    private Button btnCancel;
    private Resources resources;

    public static SearchDialogFragment newInstance() {
        SearchDialogFragment fragment = new SearchDialogFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SearchDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        resources = getActivity().getResources();

        SharedPreferences pref = getActivity().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        String oldKeyword = pref.getString(Constants.PREF_SEARCH, "");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(resources.getString(R.string.search_dialog_title));

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.fragment_search, null, false);
        etSearchKey = (EditText) rootView.findViewById(R.id.et_search);
        if (!TextUtils.isEmpty(oldKeyword)) {
            etSearchKey.setText(oldKeyword);
        }
        btnOk = (Button) rootView.findViewById(R.id.btn_ok);
        btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        builder.setView(rootView);

        builder.setCancelable(true);
        return builder.create();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                String keyword = etSearchKey.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    NoteUtils.setError(etSearchKey, getActivity());
                    return;
                }
                SearchListener listener = (SearchListener) getActivity();
                SharedPreferences pref = getActivity().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.PREF_SEARCH, keyword);
                editor.commit();
                listener.search();
                dismiss();
                break;

            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }
}
