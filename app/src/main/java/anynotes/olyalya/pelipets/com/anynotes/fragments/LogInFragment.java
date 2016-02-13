package anynotes.olyalya.pelipets.com.anynotes.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.interfaces.LoginListener;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class LogInFragment extends DialogFragment {
    private Resources resources;
    private EditText etLogin;
    private EditText etPassword;
    private EditText etPasswordRepeat;

    public static LogInFragment newInstance() {
        LogInFragment fragment = new LogInFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LogInFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        resources = getActivity().getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(resources.getString(R.string.login_dialog_title));
        builder.setPositiveButton(resources.getString(R.string.registration_btn_ok), null);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.fragment_registration, null, false);
        etLogin = (EditText) rootView.findViewById(R.id.et_login);
        etPassword = (EditText) rootView.findViewById(R.id.et_password);
        etPasswordRepeat = (EditText) rootView.findViewById(R.id.et_password_repeat);
        etPasswordRepeat.setVisibility(View.GONE);
        builder.setView(rootView);

        builder.setNegativeButton(resources.getString(R.string.registration_btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        builder.setNeutralButton(resources.getString(R.string.registration_btn_register), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RegistrationFragment fragment = RegistrationFragment.newInstance().newInstance();
                fragment.show(getActivity().getSupportFragmentManager(), null);
                dismiss();
            }
        });

        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                Button btnOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String login = etLogin.getText().toString().trim();
                        String password = etPassword.getText().toString().trim();
                        if (TextUtils.isEmpty(login)) {
                            NoteUtils.setError(etLogin, getActivity());
                            return;
                        }
                        if (TextUtils.isEmpty(password)) {
                            NoteUtils.setError(etPassword, getActivity());
                            return;
                        }
                        LoginListener listener = (LoginListener) getActivity();
                        listener.logIn(login, password);
                        dismiss();
                    }
                });
            }
        });

        return dialog;
    }


}
