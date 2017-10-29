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

import com.example.sashok.testapplication.ApiService;
import com.example.sashok.testapplication.App;
import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.network.model.ResponseConverter;
import com.example.sashok.testapplication.network.model.auth.request.LoginRequest;
import com.example.sashok.testapplication.network.model.auth.response.LoginResponse;

import java.net.ConnectException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sashok on 26.10.17.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private TextInputEditText login_edittext;
    private TextInputEditText password_edittext;
    private TextInputLayout login_layout;
    private TextInputLayout password_layout;
    private Button login_btn;
    private AuthListener mAuthListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        login_edittext = (TextInputEditText) view.findViewById(R.id.login_edittext);
        password_edittext = (TextInputEditText) view.findViewById(R.id.password_edittext);

        login_layout = (TextInputLayout) view.findViewById(R.id.login_layout);
        password_layout = (TextInputLayout) view.findViewById(R.id.password_layout);

        login_btn = (Button) view.findViewById(R.id.btn_login);
        login_btn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAuthListener = (AuthListener) getActivity();
    }

    public static LoginFragment newInstance() {

        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    @Override
    public void onClick(View view) {
        if (validate()) {

            ApiService.getInstance().login(new LoginRequest(login_edittext.getText().toString(), password_edittext.getText().toString())).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse == null) {
                        loginResponse = (LoginResponse) ResponseConverter.convertErrorResponse(response.errorBody(), LoginResponse.class);
                    }
                    switch (loginResponse.status) {
                        case 200:
                            mAuthListener.onAuthSuccess(loginResponse.getData());
                            break;
                        default:
                            mAuthListener.onAuthError(loginResponse.getErrorMessage());
                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    if (t instanceof ConnectException)
                        mAuthListener.onAuthError("no internet connection");
                }
            });

        }
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
        if (login_edittext.getText().toString().length() < 4 || login_edittext.getText().length() > 50) {
            login_layout.setError(getString(R.string.ErrorLoginLength));
            return false;
        }
        if (password_edittext.getText().toString().length() < 8 || password_edittext.getText().length() > 500) {
            password_layout.setError(getString(R.string.ErrorPasswordLenght));
            return false;
        }

        return true;
    }

}
