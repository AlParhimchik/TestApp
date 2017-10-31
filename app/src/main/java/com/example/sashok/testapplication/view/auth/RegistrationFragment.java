package com.example.sashok.testapplication.view.auth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.sashok.testapplication.ApiService;
import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.network.ResponseCallBack;
import com.example.sashok.testapplication.network.model.auth.request.RegistrationRequest;
import com.example.sashok.testapplication.network.model.auth.response.RegistrationResponse;

import java.net.ConnectException;

/**
 * Created by sashok on 26.10.17.
 */

public class RegistrationFragment extends Fragment implements View.OnClickListener {
    private TextInputEditText login_edittext;
    private TextInputEditText password_edittext;
    private TextInputEditText confirm_password_edittext;
    private TextInputLayout login_layout;
    private TextInputLayout password_layout;
    private TextInputLayout confirm_password_layout;
    private Button signup_btn;
    private ProgressBar mProgressBar;
    private AuthListener mAuthListener;

    public static RegistrationFragment newInstance() {

        RegistrationFragment fragment = new RegistrationFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        login_edittext = (TextInputEditText) view.findViewById(R.id.login_edittext);
        password_edittext = (TextInputEditText) view.findViewById(R.id.password_edittext);
        confirm_password_edittext = view.findViewById(R.id.confirm_password_edittext);
        login_layout = (TextInputLayout) view.findViewById(R.id.login_layout);
        password_layout = (TextInputLayout) view.findViewById(R.id.password_layout);
        confirm_password_layout = view.findViewById(R.id.password_confirm_layout);
        signup_btn = view.findViewById(R.id.btn_registration);
        mProgressBar = view.findViewById(R.id.progress_bar);
        signup_btn.setOnClickListener(this);
        return view;
    }

    public boolean validate() {
        if (TextUtils.isEmpty(login_edittext.getText())) {
            login_layout.setError(getString(R.string.NotNullError));
            return false;
        }
        if (TextUtils.isEmpty(password_edittext.getText())) {
            password_layout.setError(getString(R.string.NotNullError));
            return false;
        }
        if (TextUtils.isEmpty(confirm_password_edittext.getText())) {
            confirm_password_layout.setError(getString(R.string.NotNullError));
        }
        if (login_edittext.getText().toString().length() < 4 || login_edittext.getText().length() > 50) {
            login_layout.setError(getString(R.string.ErrorLoginLength));
            return false;
        }
        if (password_edittext.getText().toString().length() < 8 || password_edittext.getText().length() > 500) {
            password_layout.setError(getString(R.string.ErrorPasswordLenght));
            return false;
        }
        if (confirm_password_edittext.getText().toString().length() < 8 || confirm_password_edittext.getText().length() > 500) {
            confirm_password_layout.setError(getString(R.string.ErrorPasswordLenght));
            return false;
        }
        if (!TextUtils.equals(confirm_password_edittext.getText(), password_edittext.getText())) {
            confirm_password_layout.setError(getString(R.string.ErrorMatchPassword));
            password_layout.setError(getString(R.string.ErrorMatchPassword));
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if (validate()) {
            startSigning();
            ApiService.getInstance().registration(new RegistrationRequest(login_edittext.getText().toString(), password_edittext.getText().toString()), new ResponseCallBack<RegistrationResponse>() {
                @Override
                public void onResponse(RegistrationResponse registrationResponse) {
                    switch (registrationResponse.status) {
                        case 200:
                            mAuthListener.onAuthSuccess(registrationResponse.getData());
                            break;
                        default:
                            endSigning();
                            mAuthListener.onAuthError(registrationResponse.getErrorMessage());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    endSigning();
                    if (t instanceof ConnectException)
                        mAuthListener.onAuthError("no internet connection");
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAuthListener = (AuthListener) context;
    }

    public void startSigning() {
        signup_btn.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void endSigning() {
        signup_btn.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

}


