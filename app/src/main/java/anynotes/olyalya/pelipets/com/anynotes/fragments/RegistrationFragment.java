package anynotes.olyalya.pelipets.com.anynotes.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.interfaces.RegistrationListener;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class RegistrationFragment extends DialogFragment implements View.OnClickListener {
    private Resources resources;
    private EditText etLogin;
    private EditText etPassword;
    private EditText etPasswordRepeat;
    private Button btnOk;
    private Button btnCancel;
    private TextView btnRegister;
    private TextView btnForgotPassword;
    private TextView btnChangePassword;

    public static RegistrationFragment newInstance() {
        RegistrationFragment fragment = new RegistrationFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RegistrationFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        resources = getActivity().getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(resources.getString(R.string.registration_dialog_title));

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.fragment_registration, null, false);
        etLogin = (EditText) rootView.findViewById(R.id.et_login);
        etPassword = (EditText) rootView.findViewById(R.id.et_password);
        etPasswordRepeat = (EditText) rootView.findViewById(R.id.et_password_repeat);
        btnOk = (Button) rootView.findViewById(R.id.btn_ok);
        btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
        btnRegister = (TextView) rootView.findViewById(R.id.tv_register);
        btnForgotPassword = (TextView) rootView.findViewById(R.id.tv_forgot_password);
        btnChangePassword = (TextView) rootView.findViewById(R.id.tv_change_password);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        btnRegister.setVisibility(View.GONE);
        btnForgotPassword.setVisibility(View.GONE);
        btnChangePassword.setVisibility(View.GONE);
        builder.setView(rootView);

        builder.setCancelable(true);
        return builder.create();
    }
/*
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
                        String passwordRepeat = etPasswordRepeat.getText().toString().trim();
                        if (TextUtils.isEmpty(login)) {
                            NoteUtils.setError(etLogin, getActivity());
                            return;
                        }
                        if (TextUtils.isEmpty(password)) {
                            NoteUtils.setError(etPassword, getActivity());
                            return;
                        }
                        if (TextUtils.isEmpty(passwordRepeat)) {
                            NoteUtils.setError(etPasswordRepeat, getActivity());
                            return;
                        }
                        if (!password.equals(passwordRepeat)) {
                            NoteUtils.setError(etPassword, getActivity());
                            return;
                        }
                        RegistrationListener listener = (RegistrationListener) getActivity();
                        listener.register(login, password);
                        dismiss();
                    }
                });
            }
        });

        return dialog;
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                String login = etLogin.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String passwordRepeat = etPasswordRepeat.getText().toString().trim();
                if (TextUtils.isEmpty(login)) {
                    NoteUtils.setError(etLogin, getActivity());
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    NoteUtils.setError(etPassword, getActivity());
                    return;
                }
                if (TextUtils.isEmpty(passwordRepeat)) {
                    NoteUtils.setError(etPasswordRepeat, getActivity());
                    return;
                }
                if (!password.equals(passwordRepeat)) {
                    NoteUtils.setError(etPassword, getActivity());
                    return;
                }
                RegistrationListener listener = (RegistrationListener) getActivity();
                listener.register(login, password);
                dismiss();
                break;

            case R.id.btn_cancel:
                dismiss();
                break;

            case R.id.tv_register:
                 break;
            case R.id.tv_forgot_password:
                break;
            case R.id.tv_change_password:
                break;
        }
    }
}
