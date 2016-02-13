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
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import anynotes.olyalya.pelipets.com.anynotes.R;
import anynotes.olyalya.pelipets.com.anynotes.interfaces.LoginListener;
import anynotes.olyalya.pelipets.com.anynotes.utils.NoteUtils;

public class LogInFragment extends DialogFragment implements View.OnClickListener {
    private final String ERROR_NO_SUCH_USER = "3020";
    private Resources resources;
    private EditText etLogin;
    private EditText etPassword;
    private EditText etPasswordRepeat;
    private Button btnOk;
    private Button btnCancel;
    private TextView btnRegister;
    private TextView btnForgotPassword;
    private TextView btnChangePassword;

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
        etPasswordRepeat.setVisibility(View.GONE);
        builder.setView(rootView);

        builder.setCancelable(true);
        return builder.create();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
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
                break;

            case R.id.btn_cancel:
                dismiss();
                break;

            case R.id.tv_register:
                RegistrationFragment fragment = RegistrationFragment.newInstance().newInstance();
                fragment.show(getActivity().getSupportFragmentManager(), null);
                dismiss();
                break;

            case R.id.tv_forgot_password:
                final String email = etLogin.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    NoteUtils.setError(etLogin, getActivity());
                    return;
                }
                Backendless.UserService.restorePassword(email, new AsyncCallback<Void>() {
                    public void handleResponse(Void response) {
                        NoteUtils.log("send new password to " + email);
                        Toast.makeText(getActivity(), getActivity().getResources().
                                getString(R.string.tost_send_password_to_email)+" " + email,
                                Toast.LENGTH_LONG).show();
                    }

                    public void handleFault(BackendlessFault fault) {
                        if (ERROR_NO_SUCH_USER.equals(fault.getCode())) {
                            Toast.makeText(getActivity(), getActivity().getResources().
                                    getString(R.string.tost_error_3020),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            NoteUtils.showErrorMessage(getActivity());
                        }
                    }
                });
                break;
            case R.id.tv_change_password:
                break;
        }
    }
}
