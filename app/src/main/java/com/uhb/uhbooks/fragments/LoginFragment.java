package com.uhb.uhbooks.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.uhb.uhbooks.App;
import com.uhb.uhbooks.R;
import com.uhb.uhbooks.Utils.Utils;
import com.uhb.uhbooks.models.Api;
import com.uhb.uhbooks.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends mainFragment implements View.OnClickListener, TextWatcher {

    private TextInputLayout etUsername, etPassword;
    private MaterialButton btnLogin;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);


        etUsername.getEditText().addTextChangedListener(this);
        etPassword.getEditText().addTextChangedListener(this);
        btnLogin.setOnClickListener(this);

        return view;
    }

    private void postLogin() {
        loading(true);
        String username = etUsername.getEditText().getText().toString().trim();
        String password = etPassword.getEditText().getText().toString().trim();
        mCall = ((App) getActivity().getApplication()).getApiInterface()
                .login(new User(username, password));
        mCall.enqueue(new Callback<Api>() {
            @Override
            public void onResponse(Call<Api> call, Response<Api> response) {
                loading(false);
                if (response.isSuccessful()) {
                    mainListener.onLoginSuccess(
                            response.body().getUser(),
                            response.body().getToken()
                    );
                } else {
                    Utils.showResponseFailedDialog(getActivity(), response);
                }
            }

            @Override
            public void onFailure(Call<Api> call, Throwable t) {
                loading(false);
                if (call.isCanceled()) return;
                Utils.showRetrofitFailureDialog(getActivity(), t);
            }
        });


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // ignore
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String username = etUsername.getEditText().getText().toString().trim();
        String password = etPassword.getEditText().getText().toString().trim();

        btnLogin.setEnabled(username.length() > 0 && password.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {
        // ignore
    }

    @Override
    public void onClick(View v) {
        postLogin();
    }
}
