package com.example.sashok.testapplication.view.auth;

import com.example.sashok.testapplication.network.model.auth.UserResponse;

public interface AuthListener{
    public void onAuthSuccess(UserResponse data);
    public void onAuthError(String error);
}